package com.golemcrack;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class SpawnEvent {

    @SubscribeEvent
    public void onVillagerTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityVillager && !event.getEntityLiving().world.isRemote) {
            EntityVillager villager = (EntityVillager) event.getEntityLiving();
            World world = villager.world;

            // Проверяем раз в 2 секунды для оптимизации
            if (world.getTotalWorldTime() % 40 == 0) {
                // Ищем враждебных мобов в радиусе 10 блоков (имитация паники)
                List<EntityMob> enemies = world.getEntitiesWithinAABB(EntityMob.class, villager.getEntityBoundingBox().grow(10.0D));

                if (!enemies.isEmpty()) {
                    // Если нашли врага, пытаемся призвать голема (с шансом, как в 1.14)
                    if (world.rand.nextInt(10) == 0) { // Шанс спавна
                        spawnIronGolemNear(world, villager.getPosition());
                    }
                }
            }
        }
    }
    private void spawnIronGolemNear(World world, BlockPos pos) {
        // Проверяем, нет ли уже големов рядом (радиус 16 блоков)
        List<EntityIronGolem> nearbyGolems = world.getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB(pos).grow(16.0D));

        if (nearbyGolems.isEmpty()) {
            // Делаем до 10 попыток найти подходящее место, чтобы не спавнить в стене
            for (int i = 0; i < 10; i++) {
                // Случайное смещение в радиусе 3 блоков от жителя
                int x = pos.getX() + world.rand.nextInt(7) - 3;
                int z = pos.getZ() + world.rand.nextInt(7) - 3;

                // Находим верхний блок (земля/трава)
                BlockPos topPos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));

                // Проверяем, достаточно ли места в высоту (голему нужно почти 3 блока воздуха)
                if (world.isAirBlock(topPos) && world.isAirBlock(topPos.up()) && world.isAirBlock(topPos.up(2))) {

                    EntityIronGolem golem = new EntityIronGolem(world);

                    // Чтобы давал сдачи при атаке игроком
                    golem.setPlayerCreated(false);

                    // Устанавливаем позицию по центру блока (+0.5), чтобы избежать застревания в краях
                    golem.setLocationAndAngles(topPos.getX() + 0.5D, topPos.getY(), topPos.getZ() + 0.5D, world.rand.nextFloat() * 360.0F, 0.0F);

                    // Финальная проверка Forge на коллизии хитбокса
                    if (golem.getCanSpawnHere()) {
                        world.spawnEntity(golem);

                        // Если успешно заспавнили — выходим из цикла
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onVillageGossip(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.world.isRemote && event.world.getTotalWorldTime() % 600 == 0) {
            // Каждые 30 секунд проверяем скопления жителей
            for (EntityPlayer player : event.world.playerEntities) {
                List<EntityVillager> villagers = event.world.getEntitiesWithinAABB(EntityVillager.class, player.getEntityBoundingBox().grow(32.0D));

                // Если рядом больше 3 жителей (условие 1.14 для "встречи"), пробуем спавн
                if (villagers.size() >= 3) {
                    spawnIronGolemNear(event.world, villagers.get(0).getPosition());
                }
            }
        }
    }
}

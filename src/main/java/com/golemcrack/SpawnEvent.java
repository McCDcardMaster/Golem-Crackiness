package com.golemcrack;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.village.Village;
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

            if (world.getTotalWorldTime() % 40 == 0) {
                List<EntityMob> enemies = world.getEntitiesWithinAABB(EntityMob.class, villager.getEntityBoundingBox().grow(10.0D));

                if (!enemies.isEmpty()) {
                    if (world.rand.nextInt(10) == 0) { 
                        spawnIronGolemInVillage(world, villager.getPosition());
                    }
                }
            }
        }
    }

    private void spawnIronGolemInVillage(World world, BlockPos pos) {
        Village village = world.getVillageCollection().getNearestVillage(pos, 32);
        AxisAlignedBB searchBox;
        BlockPos spawnCenter;

        if (village != null) {
            spawnCenter = village.getCenter();
            searchBox = new AxisAlignedBB(spawnCenter).grow(village.getVillageRadius());
        } else {
            spawnCenter = pos;
            searchBox = new AxisAlignedBB(pos).grow(16.0D);
        }

        List<EntityIronGolem> nearbyGolems = world.getEntitiesWithinAABB(EntityIronGolem.class, searchBox);

        if (nearbyGolems.size() < 2) {
            for (int i = 0; i < 15; i++) {
                int x = pos.getX() + world.rand.nextInt(11) - 5;
                int z = pos.getZ() + world.rand.nextInt(11) - 5;

                BlockPos startPos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
                BlockPos groundPos = null;

                for (int yOffset = 0; yOffset < 20; yOffset++) {
                    BlockPos checkPos = startPos.down(yOffset);

                    if ((world.isAirBlock(checkPos) || world.getBlockState(checkPos).getMaterial().isLiquid()) && 
                        world.getBlockState(checkPos.down()).getMaterial().isSolid() && 
                        !world.getBlockState(checkPos.down()).getBlock().isLeaves(world.getBlockState(checkPos.down()), world, checkPos.down())) {
                        
                        groundPos = checkPos;
                        break;
                    }
                }

                if (groundPos == null) {
                    continue;
                }

                double spawnX = groundPos.getX() + 0.5D;
                double spawnY = groundPos.getY();
                double spawnZ = groundPos.getZ() + 0.5D;

                AxisAlignedBB golemBox = new AxisAlignedBB(
                        spawnX - 0.7D, spawnY, spawnZ - 0.7D,
                        spawnX + 0.7D, spawnY + 2.7D, spawnZ + 0.7D
                );

                if (world.collidesWithAnyBlock(golemBox)) {
                    continue; 
                }

                // Спавн голема
                EntityIronGolem golem = new EntityIronGolem(world);
                golem.setPlayerCreated(false);
                golem.setLocationAndAngles(spawnX, spawnY, spawnZ, world.rand.nextFloat() * 360.0F, 0.0F);
                world.spawnEntity(golem);
                
                break;
            }
        }
    }

    @SubscribeEvent
    public void onVillageGossip(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.world.isRemote && event.world.getTotalWorldTime() % 600 == 0) {
            for (EntityPlayer player : event.world.playerEntities) {
                List<EntityVillager> villagers = event.world.getEntitiesWithinAABB(EntityVillager.class, player.getEntityBoundingBox().grow(32.0D));

                if (villagers.size() >= 3) {
                    spawnIronGolemInVillage(event.world, villagers.get(0).getPosition());
                }
            }
        }
    }
}

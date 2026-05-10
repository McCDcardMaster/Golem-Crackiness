package com.golemcrack;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GolemEventHandler {

    @SubscribeEvent
    public void onGolemInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityIronGolem) {
            EntityIronGolem golem = (EntityIronGolem) event.getTarget();
            EntityPlayer player = event.getEntityPlayer();

            ItemStack stack = player.getHeldItem(event.getHand());

            if (!stack.isEmpty() && stack.getItem() == Items.IRON_INGOT && golem.getHealth() < golem.getMaxHealth()) {
                if (event.getWorld().isRemote) {
                    player.swingArm(event.getHand());
                }

                if (!event.getWorld().isRemote) {
                    // 1. Лечение
                    golem.heal(25.0F);

                    // 2. Звук (null в первом параметре значит "слышат все, включая инициатора")
                    golem.world.playSound(null, golem.getPosition(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 0.32F, 1.0F);
                    golem.world.playSound(null, golem.getPosition(), SoundEvents.ENTITY_IRONGOLEM_HURT, SoundCategory.NEUTRAL, 0.45F, 1.0F);

                    int animationId = (event.getHand() == EnumHand.MAIN_HAND) ? 0 : 3;
                    SPacketAnimation animPacket = new SPacketAnimation(player, animationId);
                    WorldServer worldServer = (WorldServer) golem.world;

                    worldServer.getEntityTracker().sendToTracking(player, animPacket);

                    if (player instanceof EntityPlayerMP) {
                        ((EntityPlayerMP) player).connection.sendPacket(animPacket);
                    }

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                }

                event.setCanceled(true);
                event.setCancellationResult(EnumActionResult.SUCCESS);
            }
        }
    }
}

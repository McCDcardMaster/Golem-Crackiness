package com.golemcrack;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GolemEventHandler {

    @SubscribeEvent
    public void onGolemJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityIronGolem) {
            EntityIronGolem golem = (EntityIronGolem) event.getEntity();
            golem.targetTasks.addTask(3, new EntityAIGolemHateTarget(golem));
        }
    }

    @SubscribeEvent
    public void onGolemHurt(LivingHurtEvent event) {
        if (event.getEntityLiving() instanceof EntityIronGolem && !event.getEntityLiving().world.isRemote) {
            EntityIronGolem golem = (EntityIronGolem) event.getEntityLiving();

            if (event.getSource().getTrueSource() instanceof EntityLivingBase) {
                EntityLivingBase attacker = (EntityLivingBase) event.getSource().getTrueSource();

                if (attacker instanceof EntityPlayer) return;

                if (attacker instanceof EntityCreeper) return;

                String attackerClassName = attacker.getClass().getName();

                NBTTagCompound nbt = golem.getEntityData();
                NBTTagList list;

                if (nbt.hasKey("HatedMobs", Constants.NBT.TAG_LIST)) {
                    list = nbt.getTagList("HatedMobs", Constants.NBT.TAG_STRING);
                } else {
                    list = new NBTTagList();
                }

                boolean alreadyContains = false;
                for (int i = 0; i < list.tagCount(); i++) {
                    if (list.getStringTagAt(i).equals(attackerClassName)) {
                        alreadyContains = true;
                        break;
                    }
                }

                if (!alreadyContains) {
                    list.appendTag(new NBTTagString(attackerClassName));
                    nbt.setTag("HatedMobs", list);
                }

                golem.setRevengeTarget(attacker);
                golem.setAttackTarget(attacker);
            }
        }
    }

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
                    golem.heal(25.0F);
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

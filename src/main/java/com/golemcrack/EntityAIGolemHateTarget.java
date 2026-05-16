package com.golemcrack;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;
import java.util.List;

public class EntityAIGolemHateTarget extends EntityAITarget {
    private final EntityIronGolem golem;
    private EntityLivingBase targetEntity;

    public EntityAIGolemHateTarget(EntityIronGolem golem) {
        super(golem, true, false);
        this.golem = golem;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (golem.getAttackTarget() != null) return false;

        NBTTagCompound nbt = golem.getEntityData();
        if (!nbt.hasKey("HatedMobs", Constants.NBT.TAG_LIST)) return false;

        NBTTagList list = nbt.getTagList("HatedMobs", Constants.NBT.TAG_STRING);
        if (list.hasNoTags()) return false;

        double distance = this.getTargetDistance();
        AxisAlignedBB area = golem.getEntityBoundingBox().grow(distance, 4.0D, distance);
        List<EntityLivingBase> potentialTargets = golem.world.getEntitiesWithinAABB(EntityLivingBase.class, area);

        for (EntityLivingBase entity : potentialTargets) {
            if (entity == golem || !entity.isEntityAlive()) continue;

            if (entity instanceof EntityCreeper) continue;

            String entityClassName = entity.getClass().getName();

            for (int i = 0; i < list.tagCount(); i++) {
                if (list.getStringTagAt(i).equals(entityClassName)) {
                    if (this.isSuitableTarget(entity, false)) {
                        this.targetEntity = entity;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void startExecuting() {
        this.golem.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    @Override
    public void resetTask() {
        this.targetEntity = null;
        super.resetTask();
    }
}

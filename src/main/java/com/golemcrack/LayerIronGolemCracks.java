package com.golemcrack;

import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class LayerIronGolemCracks implements LayerRenderer<EntityIronGolem> {
    private static final ResourceLocation LOW = new ResourceLocation("minecraft", "textures/entity/iron_golem/iron_golem_crackiness_low.png");
    private static final ResourceLocation MEDIUM = new ResourceLocation("minecraft", "textures/entity/iron_golem/iron_golem_crackiness_medium.png");
    private static final ResourceLocation HIGH = new ResourceLocation("minecraft", "textures/entity/iron_golem/iron_golem_crackiness_high.png");

    private final RenderIronGolem renderer;

    public LayerIronGolemCracks(RenderIronGolem rendererIn) {
        this.renderer = rendererIn;
    }

    @Override
    public void doRenderLayer(EntityIronGolem golem, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!golem.isInvisible()) {
            float healthRatio = golem.getHealth() / golem.getMaxHealth();
            ResourceLocation texture = null;

            if (healthRatio <= 0.25F) texture = HIGH;
            else if (healthRatio <= 0.5F) texture = MEDIUM;
            else if (healthRatio <= 0.75F) texture = LOW;

            if (texture != null) {
                this.renderer.bindTexture(texture);
                this.renderer.getMainModel().render(golem, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }

    @Override
    public boolean shouldCombineTextures() { return true; }
}


package com.golemcrack;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.ResourceLocation;

public class LayerIronGolemCracks implements LayerRenderer<EntityIronGolem> {
    
    // Стандартные пути к текстурам трещин
    private static final ResourceLocation LOW = new ResourceLocation("minecraft", "textures/entity/iron_golem/iron_golem_crackiness_low.png");
    private static final ResourceLocation MEDIUM = new ResourceLocation("minecraft", "textures/entity/iron_golem/iron_golem_crackiness_medium.png");
    private static final ResourceLocation HIGH = new ResourceLocation("minecraft", "textures/entity/iron_golem/iron_golem_crackiness_high.png");

    private final RenderIronGolem renderer;

    public LayerIronGolemCracks(RenderIronGolem rendererIn) {
        this.renderer = rendererIn;
    }

    @Override
    public void doRenderLayer(EntityIronGolem golem, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        // Если голем невидим, слой трещин не рендерим
        if (golem.isInvisible()) {
            return;
        }

        // Расчет здоровья для определения стадии разрушения
        float healthRatio = golem.getHealth() / golem.getMaxHealth();
        ResourceLocation texture = null;

        if (healthRatio <= 0.25F) {
            texture = HIGH;
        } else if (healthRatio <= 0.5F) {
            texture = MEDIUM;
        } else if (healthRatio <= 0.75F) {
            texture = LOW;
        }

        // Если голем здоров, выходим из метода
        if (texture == null) {
            return;
        }

        // Запрашиваем актуальную модель (поддерживает OptiFine JEM и кастомные модели паков)
        ModelBase currentModel = this.renderer.getMainModel();
        if (currentModel == null) {
            return;
        }

        // Привязываем текстуру трещин. Метод bindTexture автоматически ищет замену в ресурспаках
        this.renderer.bindTexture(texture);

        // Включаем альфа-тест для корректного отсечения прозрачных пикселей (важно для шейдеров)
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);

        // Настройка блендинга: накладываем трещины поверх основной текстуры
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        
        // Сбрасываем цветовой тон в дефолт, чтобы трещины не красились в цвет биома/света
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        // Защита от Z-файтинга (мерцания): сдвигаем полигоны слоя трещин чуть вперед
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-1.0F, -10.0F);

        // Если у голема эффект свечения, отключаем запись в буфер глубины
        if (golem.isGlowing()) {
            GlStateManager.depthMask(false);
        }

        // Рендерим текущую модель (кастомную или ванильную) со слоем трещин
        currentModel.render(golem, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        // Возвращаем настройки буфера глубины назад
        if (golem.isGlowing()) {
            GlStateManager.depthMask(true);
        }

        // Отключаем смещение полигонов и блендинг, чтобы не сломать рендер других элементов игры
        GlStateManager.disablePolygonOffset();
        GlStateManager.disableBlend();
    }

    @Override
    public boolean shouldCombineTextures() { 
        // Возвращаем true, чтобы текстура смешивалась с базовым скином сущности
        return true; 
    }
}

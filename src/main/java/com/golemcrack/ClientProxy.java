package com.golemcrack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        RenderIronGolem render = (RenderIronGolem) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(EntityIronGolem.class);
        if (render != null) {
            render.addLayer(new LayerIronGolemCracks(render));
        }
    }
}

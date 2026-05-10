package com.golemcrack;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class CommonProxy {

    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register( new GolemEventHandler() );
        MinecraftForge.EVENT_BUS.register( new SpawnEvent() );
        MinecraftForge.EVENT_BUS.register( new ResourcePackHandler() );
    }
}

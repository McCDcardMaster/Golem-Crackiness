package com.golemcrack;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = GolemCracksMod.MOD_ID, name = GolemCracksMod.NAME, version = GolemCracksMod.VERSION, acceptableRemoteVersions = "*")
public class GolemCracksMod
{
    public static final String MOD_ID = "golemcrack";
    public static final String NAME = "Golem Crackiness Mod";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.golemcrack.ClientProxy", serverSide = "com.golemcrack.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void posInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}

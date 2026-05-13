package com.golemcrack;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;
import java.util.Map;

public class ResourcePackHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP)) return;

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        MinecraftServer server = player.getServer();

        if (server != null) {
            // 1. Проверка на хоста (если это одиночная игра или LAN, хосту пак не шлем)
            if (!server.isDedicatedServer() && player.getName().equals(server.getServerOwner())) {
                return;
            }

            // 2. Проверяем наличие мода у зашедшего игрока
            NetworkDispatcher dispatcher = NetworkDispatcher.get(player.connection.netManager);
            boolean hasModOnClient = false;

            if (dispatcher != null) {
                Map<String, String> modList = dispatcher.getModList();
                if (modList != null && modList.containsKey("golemcrack")) {
                    hasModOnClient = true;
                }
            }

            // 3. Если мода нет — отправляем ресурспак
            if (!hasModOnClient) {
                String url = "https://www.curseforge.com/api/v1/mods/1541195/files/8078177/download";

                // Хеш в нижнем регистре. Если пустой "", Minecraft будет качать пак при каждом входе.
                String hash = "d334c795f8b9512c7b8ae8dab981f06220251eac";

                player.loadResourcePack(url, hash);
            }
        }
    }
}

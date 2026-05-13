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
        // Работаем только на стороне сервера и с сетевыми игроками
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
                // Проверьте, что MODID совпадает с указанным в @Mod или mcmod.info
                if (modList != null && modList.containsKey("golemcrack")) {
                    hasModOnClient = true;
                }
            }

            // 3. Если мода нет — отправляем ресурспак
            if (!hasModOnClient) {
                String url = "https://www.curseforge.com/api/v1/mods/1541195/files/8078177/download";

                // Хеш в нижнем регистре. Если пустой "", Minecraft будет качать пак при каждом входе.
                String hash = "1EF83C5033E54BDF6FCB1E72803A78AA349CF137";

                player.loadResourcePack(url, hash);
            }
        }
    }
}

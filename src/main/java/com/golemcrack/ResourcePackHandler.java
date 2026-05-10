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
                String url = "https://drive.google.com/uc?export=download&id=17VHQIgbCu5IUwmosVhnh2hoWROuamUEt";

                // Хеш в нижнем регистре. Если пустой "", Minecraft будет качать пак при каждом входе.
                String hash = "A8A3663615F5898DDC7CFDDFB7216C4DE0EE2E3B";

                player.loadResourcePack(url, hash);
            }
        }
    }
}

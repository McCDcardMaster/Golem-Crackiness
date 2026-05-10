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

        // 1. Проверяем, что это выделенный сервер (не одиночная игра)
        if (server != null && server.isDedicatedServer()) {

            // 2. Получаем список модов, которые клиент отправил серверу при входе
            NetworkDispatcher dispatcher = NetworkDispatcher.get(player.connection.netManager);
            boolean hasModOnClient = false;

            if (dispatcher != null) {
                Map<String, String> modList = dispatcher.getModList();
                // ЗАМЕНИТЕ "golemcrack" на ваш MODID (из файла mcmod.info или @Mod)
                if (modList != null && modList.containsKey("golemcrack")) {
                    hasModOnClient = true;
                }
            }

            // 3. Если мода у клиента нет — отправляем ресурспак
            if (!hasModOnClient) {
                String url = "https://drive.google.com/uc?export=download&id=17VHQIgbCu5IUwmosVhnh2hoWROuamUEt";
                // Пустой хеш заставит скачивать пак каждый раз.
                // Если пак тяжелый, лучше вписать SHA-1 хеш файла.
                String hash = "";

                player.loadResourcePack(url, hash);
            }
        }
    }
}

package com.golemcrack;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class ResourcePackHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;

            // Прямая ссылка на скачивание с Google Drive
            String url = "https://drive.google.com/uc?export=download&id=17VHQIgbCu5IUwmosVhnh2hoWROuamUEt";
            String hash = "";

            // Отправляем пакет клиенту
            player.loadResourcePack(url, hash);
        }
    }
}

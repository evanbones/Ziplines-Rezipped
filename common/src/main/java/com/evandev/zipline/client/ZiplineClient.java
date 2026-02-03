package com.evandev.zipline.client;

import com.evandev.zipline.compat.connectiblechains.ConnectibleChainsCompat;
import com.evandev.zipline.duck.GameRendererDuck;
import com.evandev.zipline.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ZiplineClient {

    public static void init() {
        compat();
    }

    public static void compat() {
        if (Services.PLATFORM.isModLoaded("connectiblechains")) {
            ConnectibleChainsCompat.register();
        }
    }

    public static void ziplineTilt(float yaw) {
        ((GameRendererDuck) Minecraft.getInstance().gameRenderer).zipline$setZiplineTilt(yaw);
    }

    public static void startZiplineSound(Player player) {
        Minecraft.getInstance().getSoundManager().play(new ZiplineSoundInstance(player));
    }
}
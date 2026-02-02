package com.evandev.zipline.client;

import com.evandev.zipline.compat.connectiblechains.ConnectibleChainsCompat;
import com.evandev.zipline.compat.hypha_piracea.HyphaPiraceaCompat;
import com.evandev.zipline.compat.vivatech.VivatechCompat;
import com.evandev.zipline.duck.GameRendererDuck;
import com.evandev.zipline.platform.Services;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;

public class ZiplineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        compat();
    }

    public void compat() {
        if (Services.PLATFORM.isModLoaded("hyphapiracea")) {
            HyphaPiraceaCompat.register();
        }

        if (Services.PLATFORM.isModLoaded("connectiblechains")) {
            ConnectibleChainsCompat.register();
        }

        if (Services.PLATFORM.isModLoaded("vivatech")) {
            VivatechCompat.register();
        }
    }

    public static void ziplineTilt(float yaw) {
        ((GameRendererDuck) Minecraft.getInstance().gameRenderer).zipline$setZiplineTilt(yaw);
    }
}

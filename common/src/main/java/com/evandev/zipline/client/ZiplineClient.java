package com.evandev.zipline.client;

import com.evandev.zipline.compat.connectiblechains.ConnectibleChainsCompat;
import com.evandev.zipline.compat.hypha_piracea.HyphaPiraceaCompat;
import com.evandev.zipline.compat.vivatech.VivatechCompat;
import com.evandev.zipline.duck.GameRendererDuck;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

public class ZiplineClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        compat();

    }

    public void compat() {
        var loader = FabricLoader.getInstance();
        if (loader.isModLoaded("hyphapiracea")) {
            HyphaPiraceaCompat.register();
        }

        if (loader.isModLoaded("connectiblechains")) {
            ConnectibleChainsCompat.register();
        }

        if (loader.isModLoaded("vivatech")) {
            VivatechCompat.register();
        }

        if (loader.isModLoaded("superposition")) {
            SuperpositionCompat.register();
        }

        if (loader.isModLoaded("phonos")) {
            PhonosCompat.register();
        }
    }

    public static void ziplineTilt(float yaw) {
        ((GameRendererDuck) Minecraft.getInstance().gameRenderer).zipline$setZiplineTilt(yaw);
    }
}

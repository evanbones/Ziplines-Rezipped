package com.evandev.zipline.client;

import com.evandev.zipline.config.ClothConfigIntegration;
import com.evandev.zipline.config.ModConfig;
import com.evandev.zipline.platform.Services;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;

public class ClientConfigSetup {
    public static void register() {
        if (Services.PLATFORM.isModLoaded("cloth_config")) {
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ClothConfigIntegration.createScreen(parent))
            );
        }

        MinecraftForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut e) -> ModConfig.restoreLocalConfig());
    }
}
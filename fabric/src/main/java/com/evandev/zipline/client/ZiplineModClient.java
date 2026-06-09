package com.evandev.zipline.client;

import com.evandev.zipline.config.ModConfig;
import com.evandev.zipline.network.ConfigSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ZiplineModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ZiplineClient.init();

        ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.TYPE, (payload, context) -> {
            context.client().execute(() -> {
                if (!context.client().hasSingleplayerServer()) {
                    ModConfig.setServerConfig(payload.toModConfig());
                }
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            client.execute(ModConfig::restoreLocalConfig);
        });
    }
}
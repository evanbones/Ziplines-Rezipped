package com.evandev.zipline.client;

import com.evandev.zipline.Zipline;
import com.evandev.zipline.config.ModConfig;
import com.evandev.zipline.network.ConfigSyncPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class ZiplineModClient implements ClientModInitializer {

    public static final ResourceLocation CONFIG_SYNC_ID = Zipline.id("config_sync");

    @Override
    public void onInitializeClient() {
        ZiplineClient.init();

        ClientPlayNetworking.registerGlobalReceiver(CONFIG_SYNC_ID, (client, handler, buf, responseSender) -> {
            ConfigSyncPayload payload = new ConfigSyncPayload(buf);

            client.execute(() -> {
                if (!client.hasSingleplayerServer()) {
                    ModConfig.setServerConfig(payload.toModConfig());
                }
            });
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            client.execute(ModConfig::restoreLocalConfig);
        });
    }
}
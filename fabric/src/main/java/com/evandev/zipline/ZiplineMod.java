package com.evandev.zipline;

import com.evandev.zipline.config.ModConfig;
import com.evandev.zipline.network.ConfigSyncPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ZiplineMod implements ModInitializer {

    public static final ResourceLocation CONFIG_SYNC_ID = Zipline.id("config_sync");

    @Override
    public void onInitialize() {
        Zipline.init();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            FriendlyByteBuf buf = PacketByteBufs.create();
            ConfigSyncPayload payload = ConfigSyncPayload.fromModConfig(ModConfig.get());
            payload.write(buf);
            sender.sendPacket(CONFIG_SYNC_ID, buf);
        });
    }
}
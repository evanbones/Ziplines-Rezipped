package com.evandev.zipline;

import com.evandev.zipline.client.ClientConfigSetup;
import com.evandev.zipline.client.ZiplineClient;
import com.evandev.zipline.config.ModConfig;
import com.evandev.zipline.network.ConfigSyncPayload;
import com.evandev.zipline.platform.NeoForgeRegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(Zipline.MOD_ID)
public class ZiplineMod {
    public ZiplineMod(IEventBus modEventBus, ModContainer modContainer) {
        NeoForgeRegistryHelper.ITEMS.register(modEventBus);
        NeoForgeRegistryHelper.SOUNDS.register(modEventBus);

        Zipline.init();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::registerPayloads);

        NeoForge.EVENT_BUS.addListener(this::onPlayerJoin);

        if (FMLEnvironment.dist.isClient()) {
            ClientConfigSetup.register(modContainer);
            NeoForge.EVENT_BUS.addListener(this::onPlayerLogout);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ZiplineClient.init();
    }

    private void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0");

        registrar.playToClient(
                ConfigSyncPayload.TYPE,
                ConfigSyncPayload.CODEC,
                (payload, context) -> {
                    context.enqueueWork(() -> {
                        if (!Minecraft.getInstance().hasSingleplayerServer()) {
                            ModConfig.setServerConfig(payload.toModConfig());
                        }
                    });
                }
        );
    }

    private void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, ConfigSyncPayload.fromModConfig(ModConfig.get()));
        }
    }

    private void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ModConfig.restoreLocalConfig();
    }
}
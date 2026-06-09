package com.evandev.zipline;

import com.evandev.zipline.client.ClientConfigSetup;
import com.evandev.zipline.client.ZiplineClient;
import com.evandev.zipline.config.ModConfig;
import com.evandev.zipline.network.ConfigSyncPayload;
import com.evandev.zipline.platform.ForgeRegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

@Mod(Zipline.MOD_ID)
public class ZiplineMod {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            Zipline.id("network"),
            () -> "1",
            v -> true,
            v -> true
    );

    static {
        CHANNEL.registerMessage(
                0, ConfigSyncPayload.class,
                ConfigSyncPayload::write,
                ConfigSyncPayload::new,
                (payload, ctxSupplier) -> {
                    NetworkEvent.Context ctx = ctxSupplier.get();
                    ctx.enqueueWork(() -> {
                        if (!Minecraft.getInstance().hasSingleplayerServer()) {
                            ModConfig.setServerConfig(payload.toModConfig());
                        }
                    });
                    ctx.setPacketHandled(true);
                },
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
    }

    public ZiplineMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgeRegistryHelper.ITEMS.register(modEventBus);
        ForgeRegistryHelper.SOUNDS.register(modEventBus);

        Zipline.init();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        if (FMLLoader.getDist() == Dist.CLIENT) {
            ClientConfigSetup.register();
        }

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerJoin);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ZiplineClient.init();
        ClientConfigSetup.register();
    }

    private void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), ConfigSyncPayload.fromModConfig(ModConfig.get()));
        }
    }
}
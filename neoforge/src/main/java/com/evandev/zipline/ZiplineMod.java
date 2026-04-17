package com.evandev.zipline;

import com.evandev.zipline.client.ClientConfigSetup;
import com.evandev.zipline.client.ZiplineClient;
import com.evandev.zipline.platform.NeoForgeRegistryHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;

@Mod(Zipline.MOD_ID)
public class ZiplineMod {
    public ZiplineMod(IEventBus modEventBus, ModContainer modContainer) {
        NeoForgeRegistryHelper.ITEMS.register(modEventBus);
        NeoForgeRegistryHelper.SOUNDS.register(modEventBus);

        Zipline.init();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        if (FMLLoader.getCurrent().getDist() == Dist.CLIENT) {
            ClientConfigSetup.register(modContainer);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ZiplineClient.init();
    }
}
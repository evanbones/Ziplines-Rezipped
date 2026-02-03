package com.evandev.zipline;

import com.evandev.zipline.client.ClientConfigSetup;
import com.evandev.zipline.client.ZiplineClient;
import com.evandev.zipline.platform.ForgeRegistryHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Zipline.MOD_ID)
public class ZiplineMod {
    public ZiplineMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ForgeRegistryHelper.ITEMS.register(modEventBus);
        ForgeRegistryHelper.SOUNDS.register(modEventBus);

        Zipline.init();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ZiplineClient.init();
        ClientConfigSetup.register();
    }
}
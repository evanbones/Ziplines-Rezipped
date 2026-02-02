package com.evandev.zipline;

import com.evandev.zipline.client.ZiplineClient;
import com.evandev.zipline.platform.NeoForgeRegistryHelper;
import com.evandev.zipline.registry.ZiplineItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Zipline.MOD_ID)
public class ZiplineMod {
    public ZiplineMod(IEventBus modEventBus, ModContainer modContainer) {
        NeoForgeRegistryHelper.ITEMS.register(modEventBus);
        NeoForgeRegistryHelper.SOUNDS.register(modEventBus);

        Zipline.init();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ZiplineClient.init();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ZiplineItems.ZIPLINE.get());
        }
    }
}
package com.evandev.zipline;

import com.evandev.zipline.registry.ZiplineItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;

public class ZiplineMod implements ModInitializer {
    @Override
    public void onInitialize() {
        Zipline.init();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(content -> {
            content.accept(ZiplineItems.ZIPLINE.get());
        });
    }
}
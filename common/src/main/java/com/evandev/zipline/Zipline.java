package com.evandev.zipline;

import com.evandev.zipline.registry.ZiplineCreativeTabs;
import com.evandev.zipline.registry.ZiplineItems;
import com.evandev.zipline.registry.ZiplineSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class Zipline implements ModInitializer {
    @Override
    public void onInitialize() {
        ZiplineItems.register();
        ZiplineCreativeTabs.register();
        ZiplineSoundEvents.register();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("zipline", path);
    }
}

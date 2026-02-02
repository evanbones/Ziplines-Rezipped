package com.evandev.zipline;

import com.evandev.zipline.registry.ZiplineItems;
import com.evandev.zipline.registry.ZiplineSoundEvents;
import net.minecraft.resources.ResourceLocation;

public class Zipline {
    public static final String MOD_ID = "zipline";

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void init() {
        ZiplineItems.register();
        ZiplineSoundEvents.register();
    }
}
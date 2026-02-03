package com.evandev.zipline;

import com.evandev.zipline.compat.connectiblechains.ConnectibleChainsCompat;
import com.evandev.zipline.platform.Services;
import com.evandev.zipline.registry.ZiplineSoundEvents;
import net.minecraft.resources.ResourceLocation;

public class Zipline {
    public static final String MOD_ID = "zipline";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static void init() {
        ZiplineSoundEvents.register();
        if (Services.PLATFORM.isModLoaded("connectiblechains")) {
            ConnectibleChainsCompat.register();
        }
    }
}
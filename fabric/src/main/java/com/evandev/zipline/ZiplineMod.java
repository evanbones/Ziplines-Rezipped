package com.evandev.zipline;

import net.fabricmc.api.ModInitializer;

public class ZiplineMod implements ModInitializer {
    @Override
    public void onInitialize() {
        Zipline.init();
    }
}
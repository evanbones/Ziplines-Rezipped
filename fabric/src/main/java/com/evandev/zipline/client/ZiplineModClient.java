package com.evandev.zipline.client;

import net.fabricmc.api.ClientModInitializer;

public class ZiplineModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ZiplineClient.init();
    }
}
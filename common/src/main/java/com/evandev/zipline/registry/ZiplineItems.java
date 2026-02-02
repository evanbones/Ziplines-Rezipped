package com.evandev.zipline.registry;

import com.evandev.zipline.item.ZiplineItem;
import com.evandev.zipline.platform.Services;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ZiplineItems {
    public static final Supplier<Item> ZIPLINE = Services.REGISTRY.registerItem("zipline", () -> new ZiplineItem(new Item.Properties()));

    public static void register() {
    }
}
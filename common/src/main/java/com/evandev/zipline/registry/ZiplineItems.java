package com.evandev.zipline.registry;

import com.evandev.zipline.Zipline;
import com.evandev.zipline.item.ZiplineItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public class ZiplineItems {
    public static final Item ZIPLINE = register(ZiplineItem::new, new Item.Properties(), "zipline");

    private static Item register(Function<Item.Properties, Item> factory, Item.Properties properties, String path) {
        ResourceLocation itemLocation = Zipline.id(path);
        final var item = factory.apply(properties);

        return Items.registerItem(itemLocation, item);
    }

    public static void register() {
    }
}

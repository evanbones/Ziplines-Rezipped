package com.evandev.zipline.registry;

import com.evandev.zipline.Zipline;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ZiplineTags {
    public static final TagKey<Item> ATTACHMENT = TagKey.create(Registries.ITEM, Zipline.id("attachment"));
}
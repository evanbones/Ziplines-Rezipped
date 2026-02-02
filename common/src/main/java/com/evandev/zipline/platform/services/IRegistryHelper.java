package com.evandev.zipline.platform.services;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import java.util.function.Supplier;

public interface IRegistryHelper {
    <T extends Item> Supplier<T> registerItem(String name, Supplier<T> factory);
    Supplier<SoundEvent> registerSound(String name, Supplier<SoundEvent> factory);
}
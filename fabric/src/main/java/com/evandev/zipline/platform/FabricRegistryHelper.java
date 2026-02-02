package com.evandev.zipline.platform;

import com.evandev.zipline.Zipline;
import com.evandev.zipline.platform.services.IRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> factory) {
        T item = factory.get();
        Registry.register(BuiltInRegistries.ITEM, Zipline.id(name), item);

        return () -> item;
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name, Supplier<SoundEvent> factory) {
        SoundEvent sound = factory.get();
        Registry.register(BuiltInRegistries.SOUND_EVENT, Zipline.id(name), sound);
        return () -> sound;
    }
}
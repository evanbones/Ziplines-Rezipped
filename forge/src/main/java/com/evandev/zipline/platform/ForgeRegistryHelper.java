package com.evandev.zipline.platform;

import com.evandev.zipline.Zipline;
import com.evandev.zipline.platform.services.IRegistryHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ForgeRegistryHelper implements IRegistryHelper {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Zipline.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Zipline.MOD_ID);

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> factory) {
        return ITEMS.register(name, factory);
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name, Supplier<SoundEvent> factory) {
        return SOUNDS.register(name, factory);
    }
}
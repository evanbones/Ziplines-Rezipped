package com.evandev.zipline.platform;

import com.evandev.zipline.Zipline;
import com.evandev.zipline.platform.services.IRegistryHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NeoForgeRegistryHelper implements IRegistryHelper {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Zipline.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Zipline.MOD_ID);

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> factory) {
        return ITEMS.register(name, factory);
    }

    @Override
    public Supplier<SoundEvent> registerSound(String name, Supplier<SoundEvent> factory) {
        return SOUNDS.register(name, factory);
    }
}
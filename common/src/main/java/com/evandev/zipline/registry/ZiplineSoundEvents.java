package com.evandev.zipline.registry;

import com.evandev.zipline.Zipline;
import com.evandev.zipline.platform.Services;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class ZiplineSoundEvents {
    public static final Supplier<SoundEvent> ZIPLINE_ATTACH = register("zipline_attach");
    public static final Supplier<SoundEvent> ZIPLINE_INTERRUPT = register("zipline_interrupt");
    public static final Supplier<SoundEvent> ZIPLINE_USE = register("zipline_use");

    private static Supplier<SoundEvent> register(String path) {
        return Services.REGISTRY.registerSound(path, () -> SoundEvent.createVariableRangeEvent(Zipline.id(path)));
    }

    public static void register() {
    }
}
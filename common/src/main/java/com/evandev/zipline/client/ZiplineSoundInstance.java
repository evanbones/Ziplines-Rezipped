package com.evandev.zipline.client;

import com.evandev.zipline.duck.ZiplinePlayerDuck;
import com.evandev.zipline.registry.ZiplineSoundEvents;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class ZiplineSoundInstance extends AbstractTickableSoundInstance {
    private final Player player;

    public ZiplineSoundInstance(Player player) {
        super(ZiplineSoundEvents.ZIPLINE_USE.get(), SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.player = player;
        this.looping = true;
        this.delay = 0;
        this.volume = 0.1F;
        this.x = player.getX();
        this.y = player.getY();
        this.z = player.getZ();
    }

    // TODO: make new sound for this

    @Override
    public void tick() {
        ZiplinePlayerDuck duck = (ZiplinePlayerDuck) player;

        if (this.player.isRemoved() || !duck.zipline$isActuallyUsing()) {
            this.stop();
            return;
        }

        this.x = this.player.getX();
        this.y = this.player.getY();
        this.z = this.player.getZ();

        double speed = duck.zipline$getSpeed();

        this.pitch = 0.5F + (float) speed * 0.8F;
        this.pitch = Mth.clamp(this.pitch, 0.5F, 2.0F);

        this.volume = Mth.clamp((float) speed, 0.0F, 1.0F);
    }
}
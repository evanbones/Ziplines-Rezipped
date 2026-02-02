package com.evandev.zipline.mixin;

import com.evandev.zipline.Cable;
import com.evandev.zipline.duck.ZiplinePlayerDuck;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements ZiplinePlayerDuck {
    @Unique
    private Cable zipline$cable;
    @Unique
    private double zipline$speed;
    @Unique
    private double zipline$progress;
    @Unique
    private int zipline$directionFactor;
    @Unique
    private Vec3 zipline$lastDir;
    @Unique
    private boolean zipline$actuallyUsing;

    @Override
    public Cable zipline$getCable() {
        return zipline$cable;
    }

    @Override
    public void zipline$setCable(Cable cable) {
        this.zipline$cable = cable;
    }

    @Override
    public double zipline$getSpeed() {
        return zipline$speed;
    }

    @Override
    public void zipline$setSpeed(double speed) {
        this.zipline$speed = speed;
    }

    @Override
    public double zipline$getProgress() {
        return zipline$progress;
    }

    @Override
    public void zipline$setProgress(double progress) {
        this.zipline$progress = progress;
    }

    @Override
    public int zipline$getDirectionFactor() {
        return zipline$directionFactor;
    }

    @Override
    public void zipline$setDirectionFactor(int factor) {
        this.zipline$directionFactor = factor;
    }

    @Override
    public Vec3 zipline$getLastDir() {
        return zipline$lastDir;
    }

    @Override
    public void zipline$setLastDir(Vec3 dir) {
        this.zipline$lastDir = dir;
    }

    @Override
    public boolean zipline$isActuallyUsing() {
        return zipline$actuallyUsing;
    }

    @Override
    public void zipline$setActuallyUsing(boolean using) {
        this.zipline$actuallyUsing = using;
    }
}
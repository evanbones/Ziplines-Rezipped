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
    private Cable cable;
    @Unique
    private double speed;
    @Unique
    private double progress;
    @Unique
    private int directionFactor;
    @Unique
    private Vec3 lastDir;
    @Unique
    private boolean actuallyUsing;

    @Override
    public Cable zipline$getCable() {
        return cable;
    }

    @Override
    public void zipline$setCable(Cable cable) {
        this.cable = cable;
    }

    @Override
    public double zipline$getSpeed() {
        return speed;
    }

    @Override
    public void zipline$setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public double zipline$getProgress() {
        return progress;
    }

    @Override
    public void zipline$setProgress(double progress) {
        this.progress = progress;
    }

    @Override
    public int zipline$getDirectionFactor() {
        return directionFactor;
    }

    @Override
    public void zipline$setDirectionFactor(int factor) {
        this.directionFactor = factor;
    }

    @Override
    public Vec3 zipline$getLastDir() {
        return lastDir;
    }

    @Override
    public void zipline$setLastDir(Vec3 dir) {
        this.lastDir = dir;
    }

    @Override
    public boolean zipline$isActuallyUsing() {
        return actuallyUsing;
    }

    @Override
    public void zipline$setActuallyUsing(boolean using) {
        this.actuallyUsing = using;
    }
}
package com.evandev.zipline.duck;

import com.evandev.zipline.Cable;
import net.minecraft.world.phys.Vec3;

public interface ZiplinePlayerDuck {
    Cable zipline$getCable();
    void zipline$setCable(Cable cable);

    double zipline$getSpeed();
    void zipline$setSpeed(double speed);

    double zipline$getProgress();
    void zipline$setProgress(double progress);

    int zipline$getDirectionFactor();
    void zipline$setDirectionFactor(int factor);

    Vec3 zipline$getLastDir();
    void zipline$setLastDir(Vec3 dir);

    boolean zipline$isActuallyUsing();
    void zipline$setActuallyUsing(boolean using);
}
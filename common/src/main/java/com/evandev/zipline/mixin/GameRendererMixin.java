package com.evandev.zipline.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.evandev.zipline.duck.GameRendererDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererDuck {
    @Shadow
    @Final
    Minecraft minecraft;
    @Unique
    int zipline$ziplineTilt = 0;
    @Unique
    float zipline$ziplineTiltDirection;

    @Inject(method = "bobHurt", at = @At("HEAD"))
    void bobHurt(PoseStack poseStack, float tickDelta, CallbackInfo ci) {
        if (zipline$ziplineTilt < 0) {
            return;
        }

        float g = zipline$ziplineTilt - tickDelta;
        g /= 10;
        g = Mth.sin(g * g * g * g * (float) Math.PI);

        poseStack.mulPose(Axis.YP.rotationDegrees(-zipline$ziplineTiltDirection));

        float tiltStrength = (float) (-g * 14.0 * minecraft.options.damageTiltStrength().get());
        poseStack.mulPose(Axis.ZP.rotationDegrees(tiltStrength));

        poseStack.mulPose(Axis.YP.rotationDegrees(zipline$ziplineTiltDirection));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void tick(CallbackInfo ci) {
        zipline$ziplineTilt--;
    }

    public void zipline$setZiplineTilt(float yaw) {
        this.zipline$ziplineTiltDirection = yaw;
        zipline$ziplineTilt = 10;
    }
}

package com.evandev.zipline.mixin;

import com.evandev.zipline.registry.ZiplineTags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow
    public ServerPlayer player;

    /**
     * Prevent flying kick when using zipline
     */
    @Inject(method = "getMaximumFlyingTicks", at = @At("HEAD"), cancellable = true)
    void getMaximumFlyingTicks(Entity entity, CallbackInfoReturnable<Integer> cir) {
        if (!(entity instanceof Player playerEntity)) {
            return;
        }

        if (zipline$isUsingZipline(playerEntity)) {
            cir.setReturnValue(Integer.MAX_VALUE);
        }
    }

    /**
     * Prevent moving incorrectly kick when using zipline
     */
    @WrapOperation(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isInPostImpulseGraceTime()Z"))
    boolean bypassMovedWrongly(ServerPlayer instance, Operation<Boolean> original) {
        if (zipline$isUsingZipline(instance)) {
            return true;
        }
        return original.call(instance);
    }

    /**
     * Prevent moving too quickly kick when using zipline
     */
    @WrapOperation(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;shouldCheckPlayerMovement(Z)Z"))
    boolean bypassSpeedCheck(ServerGamePacketListenerImpl instance, boolean isFallFlying, Operation<Boolean> original) {
        if (zipline$isUsingZipline(this.player)) {
            return false;
        }
        return original.call(instance, isFallFlying);
    }

    @Unique
    boolean zipline$isUsingZipline(Player p) {
        return p.getUseItem().is(ZiplineTags.ATTACHMENT);
    }
}
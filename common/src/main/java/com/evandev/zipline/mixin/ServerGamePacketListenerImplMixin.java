package com.evandev.zipline.mixin;

import com.evandev.zipline.registry.ZiplineTags;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;
    @Shadow
    private int aboveGroundTickCount;
    @Shadow
    private int aboveGroundVehicleTickCount;

    /**
     * Prevent flying kick when using zipline by resetting the flight timers
     */
    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;aboveGroundTickCount:I", ordinal = 0, opcode = Opcodes.GETFIELD))
    void zipline$resetFlightTicks(CallbackInfo ci) {
        if (zipline$isUsingZipline(this.player)) {
            this.aboveGroundTickCount = 0;
            this.aboveGroundVehicleTickCount = 0;
        }
    }

    /**
     * Prevent moving incorrectly kick when using zipline
     */
    @WrapOperation(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;isSleeping()Z", ordinal = 1))
    boolean handleMovePlayer(ServerPlayer instance, Operation<Boolean> original) {
        if (zipline$isUsingZipline(instance)) {
            return true;
        }

        return original.call(instance);
    }

    /**
     * Prevent moving too quickly kick when using zipline
     */
    @WrapOperation(method = "handleMovePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;isSingleplayerOwner()Z"))
    boolean isSinglePlayerOwner(ServerGamePacketListenerImpl instance, Operation<Boolean> original) {
        if (zipline$isUsingZipline(instance.player)) {
            return true;
        }

        return original.call(instance);
    }

    @Unique
    boolean zipline$isUsingZipline(Player player) {
        return player.getUseItem().is(ZiplineTags.ATTACHMENT);
    }
}
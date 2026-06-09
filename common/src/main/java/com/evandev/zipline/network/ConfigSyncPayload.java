package com.evandev.zipline.network;

import com.evandev.zipline.config.ModConfig;
import net.minecraft.network.FriendlyByteBuf;

public record ConfigSyncPayload(
        double snapRadius,
        double clickReach,
        boolean useAnywhere,
        double maxTurnAngle,
        double hangOffset,
        double speedMultiplier,
        boolean realisticPhysics,
        double exitJumpMultiplier,
        boolean consumeDurability,
        int releaseCooldown
) {

    public ConfigSyncPayload(FriendlyByteBuf buf) {
        this(
                buf.readDouble(), buf.readDouble(), buf.readBoolean(),
                buf.readDouble(), buf.readDouble(), buf.readDouble(),
                buf.readBoolean(), buf.readDouble(), buf.readBoolean(),
                buf.readInt()
        );
    }

    public static ConfigSyncPayload fromModConfig(ModConfig config) {
        return new ConfigSyncPayload(
                config.snapRadius, config.clickReach, config.useAnywhere,
                config.maxTurnAngle, config.hangOffset, config.speedMultiplier,
                config.realisticPhysics, config.exitJumpMultiplier,
                config.consumeDurability, config.releaseCooldown
        );
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeDouble(snapRadius);
        buf.writeDouble(clickReach);
        buf.writeBoolean(useAnywhere);
        buf.writeDouble(maxTurnAngle);
        buf.writeDouble(hangOffset);
        buf.writeDouble(speedMultiplier);
        buf.writeBoolean(realisticPhysics);
        buf.writeDouble(exitJumpMultiplier);
        buf.writeBoolean(consumeDurability);
        buf.writeInt(releaseCooldown);
    }

    public ModConfig toModConfig() {
        ModConfig config = new ModConfig();
        config.snapRadius = this.snapRadius;
        config.clickReach = this.clickReach;
        config.useAnywhere = this.useAnywhere;
        config.maxTurnAngle = this.maxTurnAngle;
        config.hangOffset = this.hangOffset;
        config.speedMultiplier = this.speedMultiplier;
        config.realisticPhysics = this.realisticPhysics;
        config.exitJumpMultiplier = this.exitJumpMultiplier;
        config.consumeDurability = this.consumeDurability;
        config.releaseCooldown = this.releaseCooldown;
        return config;
    }
}
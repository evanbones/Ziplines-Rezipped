package com.evandev.zipline.logic;

import com.evandev.zipline.Cable;
import com.evandev.zipline.Cables;
import com.evandev.zipline.client.ZiplineClient;
import com.evandev.zipline.config.ModConfig;
import com.evandev.zipline.duck.ZiplinePlayerDuck;
import com.evandev.zipline.registry.ZiplineSoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ZiplineLogic {
    private static final double ATTACH_THRESHOLD_PADDING = 1.01;

    public static void inventoryTick(Level level, LivingEntity livingEntity) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }

        ZiplinePlayerDuck duck = (ZiplinePlayerDuck) player;

        if (duck.zipline$isActuallyUsing() && !player.isUsingItem()) {
            interruptUsing(player, duck);
        }
    }

    public static void tick(Level level, LivingEntity livingEntity, ItemStack stack, int i) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }

        if (!level.isClientSide) {
            if (ModConfig.get().consumeDurability && player.tickCount % 40 == 0) {
                Vec3 offsetPlayerPos = player.position().add(0, ModConfig.get().hangOffset, 0);
                Cable cable = Cables.getClosestCable(level, offsetPlayerPos, ModConfig.get().snapRadius);

                if (cable != null) {
                    Vec3 closestPoint = cable.getClosestPoint(offsetPlayerPos);
                    if (closestPoint.distanceToSqr(offsetPlayerPos) < 0.25) {
                        EquipmentSlot slot = player.getOffhandItem() == stack ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
                        stack.hurtAndBreak(1, player, slot);
                    }
                }
            }
            return;
        }

        if (!player.isLocalPlayer()) {
            return;
        }

        ZiplinePlayerDuck duck = (ZiplinePlayerDuck) player;

        if (!duck.zipline$isActuallyUsing()) {
            attemptAttach(player, duck);
        } else {
            ziplineTick(player, duck, stack);
        }
    }

    private static void attemptAttach(Player player, ZiplinePlayerDuck duck) {
        if (player.onGround()) {
            return;
        }

        var playerPos = player.position();
        var offsetPlayerPos = playerPos.add(0, ModConfig.get().hangOffset, 0);

        Cable cable = Cables.getClosestCable(player.level(), offsetPlayerPos, ModConfig.get().snapRadius);

        if (cable == null || !cable.isValid()) {
            return;
        }

        var closestPoint = cable.getClosestPoint(offsetPlayerPos);
        var playerAttachPos = closestPoint.add(0, -ModConfig.get().hangOffset, 0);

        if (closestPoint.y > playerPos.y + ATTACH_THRESHOLD_PADDING * ModConfig.get().hangOffset
                && !isInvalidPosition(player, playerAttachPos.subtract(playerPos))) {
            enable(player, duck, cable, offsetPlayerPos);
        }
    }

    private static void enable(Player player, ZiplinePlayerDuck duck, Cable cable, Vec3 offsetPlayerPos) {
        duck.zipline$setActuallyUsing(true);
        duck.zipline$setCable(cable);

        double initialSpeed = Math.min(player.getDeltaMovement().length(), 0.5);
        duck.zipline$setSpeed(initialSpeed);

        double progress = cable.getProgress(offsetPlayerPos);
        duck.zipline$setProgress(progress);

        int dirFactor = player.getLookAngle().dot(cable.direction(progress)) >= 0 ? 1 : -1;
        duck.zipline$setDirectionFactor(dirFactor);

        var futureT = progress + dirFactor * 0.1 / cable.length();
        var delta = cable.getPoint(futureT).subtract(offsetPlayerPos);

        float rawYaw = (float) (Mth.atan2(delta.z, delta.x) * 57.2957763671875 - player.getYRot());

        float clampedYaw = Mth.clamp(Mth.wrapDegrees(rawYaw), -15.0F, 15.0F) * 0.3F;

        ZiplineClient.ziplineTilt(clampedYaw);

        player.playSound(ZiplineSoundEvents.ZIPLINE_ATTACH.get(), 0.6f, 1);
        ZiplineClient.startZiplineSound(player);
    }

    private static void ziplineTick(Player player, ZiplinePlayerDuck duck, ItemStack stack) {
        if (player.onGround()) {
            interruptUsing(player, duck);
            return;
        }

        if (stack.isEmpty()) {
            interruptUsing(player, duck);
            return;
        }

        Cable cable = duck.zipline$getCable();
        if (cable == null || !cable.isValid()) {
            interruptUsing(player, duck);
            return;
        }

        double oldProgress = duck.zipline$getProgress();

        double currentSpeed = duck.zipline$getSpeed();
        int currentDir = duck.zipline$getDirectionFactor();
        double velocity = currentSpeed * currentDir;

        if (ModConfig.get().realisticPhysics) {
            double deltaT = 0.1 / Math.max(1.0, cable.length());
            double tForward = Math.min(1.0, oldProgress + deltaT);
            double tBackward = Math.max(0.0, oldProgress - deltaT);

            Vec3 pForward = cable.getPoint(tForward);
            Vec3 pBackward = cable.getPoint(tBackward);
            Vec3 tangent = pForward.subtract(pBackward).normalize();

            double gravityAccel = 0.04;
            double acceleration = -gravityAccel * tangent.y;

            velocity += acceleration;
            velocity *= 0.98;

            if (Math.abs(velocity) < 0.01 && Math.abs(tangent.y) < 0.1) {
                velocity = 0;
            }

        } else {
            if (currentSpeed < 1.6) {
                currentSpeed = Mth.lerp(0.03, currentSpeed, 1.6);
            }

            if (currentSpeed < 0.1) {
                int intendedDir = player.getLookAngle().dot(cable.direction(oldProgress)) >= 0 ? 1 : -1;
                if (currentDir != intendedDir) {
                    currentDir = intendedDir;
                }
            }
            velocity = currentSpeed * currentDir;
        }

        duck.zipline$setSpeed(Math.abs(velocity));
        duck.zipline$setDirectionFactor(velocity >= 0 ? 1 : -1);

        double moveDelta = velocity / cable.length();
        double newProgress = oldProgress + moveDelta;
        newProgress = Mth.clamp(newProgress, 0.0, 1.0);

        duck.zipline$setProgress(newProgress);

        Vec3 newPosition = cable.getPoint(newProgress);
        Vec3 newOffsetPosition = new Vec3(newPosition.x, newPosition.y - ModConfig.get().hangOffset, newPosition.z);

        Vec3 oldPosition = cable.getPoint(oldProgress);
        Vec3 lastDir = newPosition.subtract(oldPosition);
        duck.zipline$setLastDir(lastDir);

        if (isInvalidPosition(player, lastDir)) {
            duck.zipline$setSpeed(0);
            duck.zipline$setProgress(oldProgress);
            newProgress = oldProgress;

            newPosition = oldPosition;
            newOffsetPosition = new Vec3(newPosition.x, newPosition.y - ModConfig.get().hangOffset, newPosition.z);
        }

        player.setPos(newOffsetPosition);
        player.setDeltaMovement(0, 0, 0);

        if (newProgress >= 1.0 || newProgress <= 0.0) {
            handleCableSwitch(player, duck, cable, velocity >= 0 ? 1 : -1, lastDir);
        }
    }

    private static void handleCableSwitch(Player player, ZiplinePlayerDuck duck, Cable currentCable, int dirFactor, Vec3 lastDir) {
        var nextCables = currentCable.getNext(dirFactor == 1);
        var playerDir = player.getLookAngle();
        var cableDir = lastDir.normalize();

        double highestDotProduct = -1;
        Cable nextCable = null;

        for (var next : nextCables) {
            if (currentCable.equals(next)) continue;

            var lookDotProduct = next.direction(0).dot(playerDir);
            var cableDotProduct = next.direction(0).dot(cableDir);

            if (lookDotProduct > highestDotProduct && cableDotProduct > ModConfig.get().maxTurnAngle) {
                highestDotProduct = lookDotProduct;
                nextCable = next;
            }
        }

        if (nextCable == null) {
            interruptUsing(player, duck);
            return;
        }

        duck.zipline$setCable(nextCable);
        duck.zipline$setDirectionFactor(1);
        duck.zipline$setProgress(0);
    }

    private static void interruptUsing(Player player, ZiplinePlayerDuck duck) {
        player.stopUsingItem();
        applyExitMomentum(player, duck);
        player.playSound(ZiplineSoundEvents.ZIPLINE_INTERRUPT.get(), 0.5f, 1);
        disable(duck);
    }

    private static void disable(ZiplinePlayerDuck duck) {
        duck.zipline$setCable(null);
        duck.zipline$setActuallyUsing(false);
        duck.zipline$setSpeed(0);
    }

    public static void release(Player player, ItemStack stack, Level level) {
        ZiplinePlayerDuck duck = (ZiplinePlayerDuck) player;

        player.getCooldowns().addCooldown(stack.getItem(), 10);

        if (duck.zipline$isActuallyUsing()) {
            double jumpY = 0.8 * ModConfig.get().exitJumpMultiplier;
            player.addDeltaMovement(new Vec3(0, jumpY, 0));

            applyExitMomentum(player, duck);
            disable(duck);
        }
    }

    private static void applyExitMomentum(LivingEntity livingEntity, ZiplinePlayerDuck duck) {
        Vec3 lastDir = duck.zipline$getLastDir();
        if (lastDir != null) {
            livingEntity.addDeltaMovement(lastDir.scale(.5));
        }
        livingEntity.addDeltaMovement(livingEntity.getLookAngle().with(Direction.Axis.Y, 0).scale(.5));
    }

    private static boolean isInvalidPosition(Player player, Vec3 deltaPos) {
        AABB collisionBox = player.getBoundingBox().move(deltaPos);
        Iterable<VoxelShape> blockCollisions = player.level().getBlockCollisions(player, collisionBox);
        for (var shape : blockCollisions) {
            if (!shape.isEmpty()) return true;
        }
        return false;
    }
}
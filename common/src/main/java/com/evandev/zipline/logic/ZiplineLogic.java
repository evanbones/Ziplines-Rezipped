package com.evandev.zipline.logic;

import com.evandev.zipline.Cable;
import com.evandev.zipline.Cables;
import com.evandev.zipline.client.ZiplineClient;
import com.evandev.zipline.duck.ZiplinePlayerDuck;
import com.evandev.zipline.registry.ZiplineSoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ZiplineLogic {
    private static final double HANG_OFFSET = 2.12;
    private static final double TOP_VERTICAL_SNAP_FACTOR = 0.3;
    private static final double SNAP_RADIUS = 3;
    private static final double MAX_TURN_ANGLE = 0.707;

    public static void tick(Level level, LivingEntity livingEntity, ItemStack itemStack, int i) {
        if (!level.isClientSide || !(livingEntity instanceof Player player) || !player.isLocalPlayer()) {
            return;
        }

        ZiplinePlayerDuck duck = (ZiplinePlayerDuck) player;

        if (!duck.zipline$isActuallyUsing()) {
            attemptAttach(player, duck);
        } else {
            ziplineTick(player, duck, itemStack);
        }
    }

    private static void attemptAttach(Player player, ZiplinePlayerDuck duck) {
        var playerPos = player.position();
        var offsetPlayerPos = playerPos.add(0, HANG_OFFSET, 0);

        Cable cable = Cables.getClosestCable(offsetPlayerPos, SNAP_RADIUS);

        if (cable == null || !cable.isValid()) {
            return;
        }

        var closestPoint = cable.getClosestPoint(offsetPlayerPos);
        var playerAttachPos = closestPoint.add(0, -HANG_OFFSET, 0);

        if (closestPoint.y > playerPos.y + TOP_VERTICAL_SNAP_FACTOR * HANG_OFFSET && !isInvalidPosition(player, playerAttachPos.subtract(playerPos))) {
            enable(player, duck, cable, offsetPlayerPos);
        }
    }

    private static void enable(Player player, ZiplinePlayerDuck duck, Cable cable, Vec3 offsetPlayerPos) {
        duck.zipline$setActuallyUsing(true);
        duck.zipline$setCable(cable);

        duck.zipline$setSpeed(player.getDeltaMovement().length());

        double progress = cable.getProgress(offsetPlayerPos);
        duck.zipline$setProgress(progress);

        int dirFactor = player.getLookAngle().dot(cable.direction(progress)) >= 0 ? 1 : -1;
        duck.zipline$setDirectionFactor(dirFactor);

        var futureT = progress + dirFactor * .1 / cable.length();
        var delta = cable.getPoint(futureT).subtract(offsetPlayerPos);

        float yaw = (float) (Mth.atan2(delta.z, delta.x) * 57.2957763671875 - player.getYRot());
        ZiplineClient.ziplineTilt(yaw);

        player.playSound(ZiplineSoundEvents.ZIPLINE_ATTACH.get(), 0.6f, 1);
    }

    private static void ziplineTick(Player player, ZiplinePlayerDuck duck, ItemStack stack) {
        Cable cable = duck.zipline$getCable();
        if (cable == null || !cable.isValid()) {
            interruptUsing(player, duck);
            return;
        }

        double progress = duck.zipline$getProgress();
        var closestPoint = cable.getPoint(progress);

        double speed = duck.zipline$getSpeed();
        if (speed < 1.6) {
            speed = Mth.lerp(0.03, speed, 1.6);
            duck.zipline$setSpeed(speed);
        }

        int dirFactor = duck.zipline$getDirectionFactor();
        progress += dirFactor * speed / cable.length();
        progress = Mth.clamp(progress, 0.0, 1.0);
        duck.zipline$setProgress(progress);

        Vec3 newPosition = cable.getPoint(progress);
        Vec3 newOffsetPosition = new Vec3(newPosition.x, newPosition.y - HANG_OFFSET, newPosition.z);

        Vec3 lastDir = newPosition.subtract(closestPoint);
        duck.zipline$setLastDir(lastDir);

        if (isInvalidPosition(player, lastDir)) {
            interruptUsing(player, duck);
            return;
        }

        player.setPos(newOffsetPosition);
        player.setDeltaMovement(0, 0, 0);
        player.playSound(ZiplineSoundEvents.ZIPLINE_USE.get(), 1.0F, .3f + (float) (speed));

        if (progress >= 1.0 || progress <= 0.0) {
            handleCableSwitch(player, duck, cable, dirFactor, lastDir);
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

            if (lookDotProduct > highestDotProduct && cableDotProduct > MAX_TURN_ANGLE) {
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

        if (!level.isClientSide) return;

        if (duck.zipline$isActuallyUsing()) {
            player.addDeltaMovement(new Vec3(0, 0.8, 0));
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
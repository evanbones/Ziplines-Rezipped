package com.evandev.zipline.mixin;

import com.evandev.zipline.Cable;
import com.evandev.zipline.Cables;
import com.evandev.zipline.config.ModConfig;
import com.evandev.zipline.logic.ZiplineLogic;
import com.evandev.zipline.registry.ZiplineTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Item.class, priority = 500)
public class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult.Success> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(ZiplineTags.ATTACHMENT)) {
            Vec3 offset = player.position().add(0, ModConfig.get().hangOffset, 0);
            Cable cable = Cables.getClosestCable(level, offset, ModConfig.get().clickReach);

            if (cable != null || ModConfig.get().useAnywhere) {
                player.startUsingItem(hand);
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }
    }

    @Inject(method = "onUseTick", at = @At("HEAD"))
    private void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining, CallbackInfo ci) {
        if (itemStack.is(ZiplineTags.ATTACHMENT)) {
            ZiplineLogic.tick(level, livingEntity, itemStack);
        }
    }

    @Inject(method = "releaseUsing", at = @At("HEAD"))
    private void releaseUsing(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Player player && itemStack.is(ZiplineTags.ATTACHMENT)) {
            ZiplineLogic.release(player, itemStack);
        }
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void getUseDuration(ItemStack itemStack, LivingEntity user, CallbackInfoReturnable<Integer> cir) {
        if (itemStack.is(ZiplineTags.ATTACHMENT)) {
            cir.setReturnValue(72000);
        }
    }

    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    private void getUseAnimation(ItemStack itemStack, CallbackInfoReturnable<ItemUseAnimation> cir) {
        if (itemStack.is(ZiplineTags.ATTACHMENT)) {
            cir.setReturnValue(ItemUseAnimation.NONE);
        }
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, EquipmentSlot slot, CallbackInfo ci) {
        if (itemStack.is(ZiplineTags.ATTACHMENT) && owner instanceof LivingEntity living) {
            ZiplineLogic.inventoryTick(living);
        }
    }
}
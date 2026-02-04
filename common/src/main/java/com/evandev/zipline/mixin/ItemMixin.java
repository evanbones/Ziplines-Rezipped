package com.evandev.zipline.mixin;

import com.evandev.zipline.Cable;
import com.evandev.zipline.Cables;
import com.evandev.zipline.config.ModConfig;
import com.evandev.zipline.logic.ZiplineLogic;
import com.evandev.zipline.registry.ZiplineTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(ZiplineTags.ATTACHMENT)) {
            Vec3 offset = player.position().add(0, ModConfig.get().hangOffset, 0);
            Cable cable = Cables.getClosestCable(level, offset, ModConfig.get().snapRadius);

            if (cable != null) {
                player.startUsingItem(hand);
                cir.setReturnValue(InteractionResultHolder.consume(stack));
            }
        }
    }

    @Inject(method = "onUseTick", at = @At("HEAD"))
    private void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration, CallbackInfo ci) {
        if (stack.is(ZiplineTags.ATTACHMENT)) {
            ZiplineLogic.tick(level, livingEntity, stack, remainingUseDuration);
        }
    }

    @Inject(method = "releaseUsing", at = @At("HEAD"))
    private void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged, CallbackInfo ci) {
        if (livingEntity instanceof Player player && stack.is(ZiplineTags.ATTACHMENT)) {
            ZiplineLogic.release(player, stack);
        }
    }

    @Inject(method = "getUseDuration", at = @At("HEAD"), cancellable = true)
    private void getUseDuration(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (stack.is(ZiplineTags.ATTACHMENT)) {
            cir.setReturnValue(Integer.MAX_VALUE);
        }
    }

    @Inject(method = "getUseAnimation", at = @At("HEAD"), cancellable = true)
    private void getUseAnimation(ItemStack stack, CallbackInfoReturnable<UseAnim> cir) {
        if (stack.is(ZiplineTags.ATTACHMENT)) {
            cir.setReturnValue(UseAnim.NONE);
        }
    }

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected, CallbackInfo ci) {
        if (stack.is(ZiplineTags.ATTACHMENT) && entity instanceof LivingEntity living) {
            ZiplineLogic.inventoryTick(living);
        }
    }
}
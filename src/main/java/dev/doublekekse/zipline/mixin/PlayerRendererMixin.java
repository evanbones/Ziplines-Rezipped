package dev.doublekekse.zipline.mixin;

import dev.doublekekse.zipline.registry.ZiplineItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    /***
     * @see dev.doublekekse.zipline.EarlyRiser
     */
    @Inject(method = "getArmPose(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;"), cancellable = true)
    private static void getArmPos(Player player, ItemStack itemStack, InteractionHand interactionHand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        if(itemStack.is(ZiplineItems.ZIPLINE))  {
            cir.setReturnValue(HumanoidModel.ArmPose.valueOf("ZIPLINE"));
        }
    }
}

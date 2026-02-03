package com.evandev.zipline.mixin;

import com.evandev.zipline.registry.ZiplineTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends LivingEntity> {
    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/geom/ModelPart;copyFrom(Lnet/minecraft/client/model/geom/ModelPart;)V"))
    void poseLeftArm(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (livingEntity instanceof Player player) {
            var useItem = player.getUseItem();
            if (!useItem.is(ZiplineTags.ATTACHMENT) || !player.isUsingItem()) {
                return;
            }

            var hand = player.getUsedItemHand();
            var mainArm = player.getMainArm();

            if (hand == InteractionHand.MAIN_HAND) {
                zipline$positionArm(zipline$getArmModel(mainArm));
            } else {
                zipline$positionArm(zipline$getArmModel(mainArm.getOpposite()));
            }
        }
    }

    @Unique
    ModelPart zipline$getArmModel(HumanoidArm arm) {
        if (arm == HumanoidArm.RIGHT) {
            return rightArm;
        } else {
            return leftArm;
        }
    }

    @Unique
    void zipline$positionArm(ModelPart arm) {
        arm.xRot = (float) -Math.PI;
        arm.zRot = 0;

        arm.y = 5;
        arm.z = 0;
    }
}
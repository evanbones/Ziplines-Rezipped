package com.evandev.zipline.mixin;

import com.evandev.zipline.registry.ZiplineTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {
    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    void poseLeftArm(T state, CallbackInfo ci) {
        if (!state.isUsingItem) {
            return;
        }

        InteractionHand hand = state.useItemHand;
        HumanoidArm mainArm = state.mainArm;
        HumanoidArm usingArm = (hand == InteractionHand.MAIN_HAND) ? mainArm : mainArm.getOpposite();

        ItemStack useItem = state.getUseItemStackForArm(usingArm);

        if (useItem.is(ZiplineTags.ATTACHMENT)) {
            zipline$positionArm(zipline$getArmModel(usingArm));
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
package dev.doublekekse.zipline.mixin;

import dev.doublekekse.zipline.registry.ZiplineItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
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
public class HumanoidModelMixin<T extends HumanoidRenderState> {
    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart body;

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/HumanoidModel;setupAttackAnimation(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;F)V"))
    void poseLeftArm(T state, CallbackInfo ci) {
        // TODO
        var useItem = state.getMainHandItem();
        useItem.
        if (!useItem.is(ZiplineItems.ZIPLINE) || !state.isUsingItem()) {
            return;
        }

        var hand = state.getUsedItemHand();
        var mainArm = state.getMainArm();

        if (hand == InteractionHand.MAIN_HAND) {
            positionArm(getArmModel(mainArm));
        } else {
            positionArm(getArmModel(mainArm.getOpposite()));
        }
    }

    @Unique
    ModelPart getArmModel(HumanoidArm arm) {
        if (arm == HumanoidArm.RIGHT) {
            return rightArm;
        } else {
            return leftArm;
        }
    }

    @Unique
    void positionArm(ModelPart arm) {
        int a = arm == rightArm ? 1 : -1;

        arm.xRot = (float) (-0.9f * Math.PI);
        arm.zRot = .5f * a;
        arm.y = 5;
        arm.z = -2;
    }
}

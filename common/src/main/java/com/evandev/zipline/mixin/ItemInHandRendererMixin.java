package com.evandev.zipline.mixin;

import com.evandev.zipline.registry.ZiplineTags;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Shadow
    public abstract void renderItem(LivingEntity mob, ItemStack itemStack, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords);

    @Shadow
    protected abstract void renderPlayerArm(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float inverseArmHeight, float attackValue, HumanoidArm arm);

    @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
    void renderArmWithItem(AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if (!itemStack.is(ZiplineTags.ATTACHMENT) || !player.isUsingItem()) {
            return;
        }

        poseStack.pushPose();

        boolean bl = hand == InteractionHand.MAIN_HAND;
        HumanoidArm humanoidArm = bl ? player.getMainArm() : player.getMainArm().getOpposite();

        boolean bl2 = humanoidArm == HumanoidArm.RIGHT;
        int q = bl2 ? 1 : -1;

        zipline$shake(itemStack, player, frameInterp, poseStack);

        poseStack.translate(0, -0.4, 0.2);

        poseStack.mulPose(Axis.XP.rotationDegrees(10));
        poseStack.mulPose(Axis.YN.rotationDegrees((float) q * -10.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) q * 70));

        renderPlayerArm(poseStack, submitNodeCollector, lightCoords, 0, 0, humanoidArm);
        poseStack.popPose();

        poseStack.pushPose();

        zipline$shake(itemStack, player, frameInterp, poseStack);

        poseStack.translate(q * 0.4, 0.3, -0.8);
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) q * 15));
        poseStack.mulPose(Axis.XP.rotationDegrees(15));

        this.renderItem(player, itemStack, bl2 ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, poseStack, submitNodeCollector, lightCoords);

        poseStack.popPose();

        ci.cancel();
    }

    @Unique
    void zipline$shake(ItemStack itemStack, AbstractClientPlayer abstractClientPlayer, float frameInterp, PoseStack poseStack) {
        float useFactor = itemStack.getUseDuration(abstractClientPlayer) - (abstractClientPlayer.getUseItemRemainingTicks() - frameInterp + 1.0f);

        float m = Mth.sin((useFactor - 0.1f) * 1.3f);
        float q = Mth.sin((useFactor * .3f - 0.4f) * 1.3f);

        float influence = Mth.clamp((useFactor * .1f) - 0.1f, 0, 1);

        float o = m * influence;
        float l = q * influence;

        poseStack.translate(l * 0.003f, o * 0.001f, o * 0.001f);
    }
}
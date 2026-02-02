package com.evandev.zipline.mixin.compat.connectiblechains;

import com.evandev.zipline.registry.ZiplineTags;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class EntityMixin {
    @WrapMethod(method = "canCollideWith")
    boolean canCollideWith(Entity entity, Operation<Boolean> original) {
        var defaultValue = original.call(entity);

        if (!defaultValue) {
            return false;
        }

        if (!((Object) this instanceof Player player)) {
            return defaultValue;
        }

        if (!entity.getClass().getSimpleName().equals("ChainCollisionEntity")) {
            return defaultValue;
        }

        var mainHand = player.getMainHandItem();
        var offHand = player.getOffhandItem();

        return !mainHand.is(ZiplineTags.ATTACHMENT) && !offHand.is(ZiplineTags.ATTACHMENT);
    }
}
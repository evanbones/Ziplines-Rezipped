package com.evandev.zipline.compat.connectiblechains;

import com.evandev.connectiblechains.CommonClass;
import com.evandev.connectiblechains.entity.ChainKnotEntity;
import com.evandev.zipline.Cable;
import com.evandev.zipline.Cables;
import net.minecraft.world.phys.AABB;

public class ConnectibleChainsCompat {
    public static void register() {
        Cables.registerProvider((level, offsetPlayerPos, squaredRadius) -> {
            var radius = CommonClass.runtimeConfig.getMaxChainRange() + 1;
            var aabb = new AABB(offsetPlayerPos.subtract(radius, radius, radius), offsetPlayerPos.add(radius, radius, radius));

            var knots = level.getEntitiesOfClass(ChainKnotEntity.class, aabb, (a) -> true);

            double nearestDist = squaredRadius;
            Cable nearestCable = null;

            for (var knot : knots) {
                for (var chainData : knot.getChainDataSet()) {
                    var holder = knot.getChainHolder(chainData);

                    if (holder == null) {
                        continue;
                    }

                    var cable = ChainCable.from(knot, holder);
                    var closestPoint = cable.getClosestPoint(offsetPlayerPos);

                    var distance = closestPoint.distanceToSqr(offsetPlayerPos);

                    if (distance < nearestDist) {
                        nearestDist = distance;
                        nearestCable = cable;
                    }
                }

            }

            return nearestCable;
        });
    }
}
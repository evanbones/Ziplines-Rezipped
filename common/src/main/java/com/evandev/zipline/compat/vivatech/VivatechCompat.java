package com.evandev.zipline.compat.vivatech;

import com.evandev.zipline.Cable;
import com.evandev.zipline.Cables;
import falseresync.vivatech.client.VivatechClient;
import net.minecraft.client.Minecraft;

public class VivatechCompat {
    public static void register() {
        Cables.registerProvider((offsetPlayerPos, squaredRadius) -> {
            var level = Minecraft.getInstance().level;

            assert level != null;

            var wires = VivatechClient.getClientWireManager().getWires(level.dimension());

            double nearestDist = squaredRadius;
            Cable nearestCable = null;

            for (var wire : wires) {
                var cable = VivatechWireCable.from(wire);
                var closestPoint = cable.getClosestPoint(offsetPlayerPos);

                var distance = closestPoint.distanceToSqr(offsetPlayerPos);

                if (distance < nearestDist) {
                    nearestDist = distance;
                    nearestCable = cable;
                }

            }

            return nearestCable;
        });
    }
}

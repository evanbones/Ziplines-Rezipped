package com.evandev.zipline.compat.station_decoration;

import com.evandev.zipline.Cable;
import com.evandev.zipline.Cables;
import net.minecraft.world.phys.Vec3;
import org.mtr.core.data.Position;
import top.mcmtr.mod.client.MSDMinecraftClientData;

public class StationDecorationCompat {
    public static void register() {
        Cables.registerProvider((level, offsetPlayerPos, squaredRadius) -> {
            var data = MSDMinecraftClientData.getInstance();
            var catenaries = data.catenaries;

            double nearestDist = squaredRadius;
            Cable nearestCable = null;

            for (var catenary : catenaries) {
                var cable = StationDecorationCable.of(data, catenary.getPosition1(), catenary.getPosition2());
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

    static Vec3 toVec3(Position position) {
        return new Vec3(position.getX() + .5, position.getY(), position.getZ() + .5);
    }
}
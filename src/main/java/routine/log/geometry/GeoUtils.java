package routine.log.geometry;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class GeoUtils {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point toPoint(double latitude, double longitude) {
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }


    public static boolean isWithinRadius(double userLat, double userLng, double centerLat, double centerLng, double radiusMeters) {

        Point userPoint = geometryFactory.createPoint(new Coordinate(userLng, userLat));
        Point centerPoint = geometryFactory.createPoint(new Coordinate(centerLng, centerLat));


        double degreeRadius = radiusMeters / 111_320.0; // 대략적 환산: 1도 ≈ 111.32km

        return centerPoint.buffer(degreeRadius).contains(userPoint);
    }
}

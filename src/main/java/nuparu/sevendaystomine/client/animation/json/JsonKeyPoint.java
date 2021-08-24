package nuparu.sevendaystomine.client.animation.json;

import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.client.animation.KeyPoint;

public class JsonKeyPoint {

    private Vector3d position = new Vector3d(0, 0, 0);
    private Vector3d rotation = new Vector3d(0, 0, 0);

    private Vector3d scale = new Vector3d(1, 1, 1);
    private boolean visible;
    private long time;

    private JsonKeyPoint() {

    }

    public static JsonKeyPoint of(KeyPoint point) {
        JsonKeyPoint jsonKeyPoint = new JsonKeyPoint();

        jsonKeyPoint.position = point.getPosition();
        jsonKeyPoint.rotation = point.getRotation();
        jsonKeyPoint.scale = point.getScale();
        jsonKeyPoint.visible = point.isVisible();
        jsonKeyPoint.time = point.getTime();
        return jsonKeyPoint;
    }

    public KeyPoint toKeyPoint() {
        KeyPoint keyPoint = new KeyPoint();

        keyPoint.setPosition(position);
        keyPoint.setRotation(rotation);
        keyPoint.setScale(scale);
        keyPoint.setTime(time);
        keyPoint.setVisible(visible);

        return keyPoint;
    }
}

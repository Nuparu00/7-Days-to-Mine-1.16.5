package nuparu.sevendaystomine.client.animation.json;

import com.google.gson.JsonObject;
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

    public static JsonKeyPoint from(JsonObject object) {
        JsonKeyPoint jsonKeyPoint = new JsonKeyPoint();

        jsonKeyPoint.position = object.has("position") ? readVector(object.get("position").getAsJsonObject()) : Vector3d.ZERO;
        jsonKeyPoint.rotation = object.has("rotation") ? readVector(object.get("rotation").getAsJsonObject()) : Vector3d.ZERO;
        jsonKeyPoint.scale = object.has("scale") ? readVector(object.get("scale").getAsJsonObject()) : Vector3d.ZERO;
        jsonKeyPoint.visible = object.has("visible") ? object.get("visible").getAsBoolean() : true;
        jsonKeyPoint.time = object.has("time") ? object.get("time").getAsInt() : 0;
        return jsonKeyPoint;
    }

    public static Vector3d readVector(JsonObject object){
        double x = object.has("x") ? object.get("x").getAsDouble() : 0;
        double y = object.has("y") ? object.get("y").getAsDouble() : 0;
        double z = object.has("z") ? object.get("z").getAsDouble() : 0;

        return new Vector3d(x,y,z);
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

    @Override
    public String toString() {
        return this.position.toString() +  " " + this.rotation.toString() + " " + this.scale.toString() + " " + this.scale +  " " + this.visible;
    }
}

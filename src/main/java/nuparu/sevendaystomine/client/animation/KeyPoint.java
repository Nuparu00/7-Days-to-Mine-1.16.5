package nuparu.sevendaystomine.client.animation;

import net.minecraft.util.math.vector.Vector3d;

public class KeyPoint {


    private Vector3d position = new Vector3d(0,0,0);
    private Vector3d rotation= new Vector3d(0,0,0);

    private Vector3d scale = new Vector3d(1,1,1);
    private boolean visible;
    private long time;


    public KeyPoint(){
    }

    public KeyPoint setPosition(Vector3d position){
        this.position = position;
        return this;
    }

    public KeyPoint setPosition(double x, double y, double z){
        return setPosition(new Vector3d(x,y,z));
    }

    public KeyPoint setRotation(Vector3d rotation){
        this.rotation = rotation;
        return this;
    }

    public KeyPoint setScale(Vector3d scale){
        this.scale = scale;
        return this;
    }

    public KeyPoint setVisible(boolean visible){
        this.visible = visible;
        return this;
    }

    public KeyPoint setTime(long time){
        this.time = time;
        return this;
    }

    public Vector3d getPosition() {
        return position;
    }

    public Vector3d getRotation() {
        return rotation;
    }

    public Vector3d getScale() {
        return scale;
    }

    public boolean isVisible() {
        return visible;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return this.position.toString() +  " " + this.rotation.toString() + " " + this.scale.toString() + " " + this.scale +  " " + this.visible;
    }

}

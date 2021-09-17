package nuparu.sevendaystomine.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import nuparu.sevendaystomine.util.MathUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimationModel {

    List<KeyPoint> points = new ArrayList<KeyPoint>();
    List<AnimationModel> children = new ArrayList<>();

    AnimationModelRenderer renderer;

    public AnimationModel(AnimationModelRenderer renderer){
        this.renderer = renderer;
    }

    public AnimationModel(ResourceLocation resourceLocation) {
        if (resourceLocation != null) {
            this.renderer = AnimationModelRenderers.get(resourceLocation);
        }
    }

    public void addKeyPoint(KeyPoint point) {
        points.add(point);
    }

    public void addChild(AnimationModel child) {
        children.add(child);
    }

    public List<KeyPoint> getPoints(){
        return new ArrayList<>(points);
    }

    public List<AnimationModel> getChildren(){
        return new ArrayList<>(children);
    }

    public AnimationModelRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(AnimationModelRenderer renderer) {
        this.renderer = renderer;
    }

    //abstract void renderModel(MatrixStack matrixStack,IRenderTypeBuffer buffer, int light);

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light, long time) {

        Vector3d position = getPositionForTime(time);
        Vector3d rotation = getRotationForTime(time);
        Vector3d scale = getScaleForTime(time);
        boolean visible = isVisible(time);

        matrixStack.pushPose();

        matrixStack.translate(position.x, position.y, position.z);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees((float)rotation.x()));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float)rotation.y()));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float)rotation.z()));
        matrixStack.scale((float)scale.x(), (float)scale.y(), (float)scale.z());

        if(renderer != null && visible){

            renderer.render(matrixStack,buffer,light);
        }
        for(AnimationModel child : children){
            child.render(matrixStack,buffer,light,time);
        }
        matrixStack.popPose();
    }

    public Vector3d getPositionForTime(long time){
        if(points.size() == 0) return Vector3d.ZERO;
        KeyPoint last = points.get(points.size()-1);
        if(time > last.getTime()){
            return last.getPosition();
        }
        if(points.size() == 1) return points.get(0).getPosition();

        KeyPoint prev = null;
        for(int i = 0; i < points.size(); i++){
            KeyPoint point = points.get(i);
            KeyPoint oldPrev = prev;
            prev = point;

            if(time == point.getTime()){
                return point.getPosition();
            }

            if(oldPrev == null) {
                continue;
            }

            if(oldPrev.getTime() < time && point.getTime() > time){
                long timeFromPast = time-oldPrev.getTime();
                long totalTime = point.getTime()-oldPrev.getTime();

                double transition = (double)timeFromPast/totalTime;

                return MathUtils.lerp(oldPrev.getPosition(),point.getPosition(),transition);
            }

        }

        return Vector3d.ZERO;
    }

    Vector3d ZERO = new Vector3d(0,0,0);

    public Vector3d getRotationForTime(long time){
        if(points.size() == 0) return Vector3d.ZERO;
        KeyPoint last = points.get(points.size()-1);
        if(time > last.getTime()){
            return last.getRotation();
        }
        if(points.size() == 1) return points.get(0).getRotation();

        KeyPoint prev = null;
        for(int i = 0; i < points.size(); i++){
            KeyPoint point = points.get(i);
            KeyPoint oldPrev = prev;
            prev = point;

            if(time == point.getTime()){
                return point.getRotation();
            }

            if(oldPrev == null) {
                continue;
            }

            if(oldPrev.getTime() < time && point.getTime() > time){
                long timeFromPast = time-oldPrev.getTime();
                long totalTime = point.getTime()-oldPrev.getTime();

                double transition = (double)timeFromPast/totalTime;

                return MathUtils.lerp(oldPrev.getRotation(),point.getRotation(),transition);
            }

        }

        return new Vector3d(0,90,0);
    }

    public Vector3d getScaleForTime(long time){
        if(points.size() == 0) return Vector3d.ZERO;
        KeyPoint last = points.get(points.size()-1);
        if(time > last.getTime()){
            return last.getScale();
        }
        if(points.size() == 1) return points.get(0).getScale();

        KeyPoint prev = null;
        for(int i = 0; i < points.size(); i++){
            KeyPoint point = points.get(i);
            KeyPoint oldPrev = prev;
            prev = point;

            if(time == point.getTime()){
                return point.getScale();
            }

            if(oldPrev == null) {
                continue;
            }

            if(oldPrev.getTime() < time && point.getTime() > time){
                long timeFromPast = time-oldPrev.getTime();
                long totalTime = point.getTime()-oldPrev.getTime();

                double transition = (double)timeFromPast/totalTime;

                return MathUtils.lerp(oldPrev.getScale(),point.getScale(),transition);
            }

        }

        return ZERO;
    }

    public long getDuration(){
        long duration = 0;
        for(KeyPoint point : points){
            if(point.getTime() > duration){
                duration = point.getTime();
            }
        }
        for(AnimationModel child : children){
            long childDuration = child.getDuration();
            if(childDuration  > duration){
                duration = childDuration;
            }
        }
        return duration;
    }

    public boolean isVisible(long time){
        if(points.size() == 0) return true;
        KeyPoint last = points.get(points.size()-1);
        if(time > last.getTime()){
            return last.isVisible();
        }
        if(points.size() == 1) return points.get(0).isVisible();

        KeyPoint prev = null;
        for(int i = 0; i < points.size(); i++){
            KeyPoint point = points.get(i);
            KeyPoint oldPrev = prev;
            prev = point;

            if(time == point.getTime()){
                return point.isVisible();
            }

            if(oldPrev == null) {
                continue;
            }

            if(oldPrev.getTime() < time && point.getTime() > time){
                long timeFromPast = time-oldPrev.getTime();
                long totalTime = point.getTime()-oldPrev.getTime();

                double transition = (double)timeFromPast/totalTime;

                return oldPrev.isVisible();
            }

        }

        return true;
    }

}

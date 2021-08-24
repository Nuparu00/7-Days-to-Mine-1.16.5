package nuparu.sevendaystomine.client.animation.json;

import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.animation.AnimationModel;
import nuparu.sevendaystomine.client.animation.AnimationModelRenderers;
import nuparu.sevendaystomine.client.animation.KeyPoint;

import java.util.ArrayList;
import java.util.List;

public class JsonAnimationModel {

    List<JsonKeyPoint> points;
    List<JsonAnimationModel> children;
    String renderer;

    private JsonAnimationModel() {

    }

    public static JsonAnimationModel of(AnimationModel model) {
        JsonAnimationModel jsonAnimationModel = new JsonAnimationModel();
        List<JsonKeyPoint> pointList = new ArrayList<>();
        for(KeyPoint point : model.getPoints()){
            pointList.add(JsonKeyPoint.of(point));
        }
        List<JsonAnimationModel> childrenList = new ArrayList<>();
        for(AnimationModel child : model.getChildren()){
            childrenList.add(JsonAnimationModel.of(child));
        }

        JsonAnimationModel jsonAnimationModel1 = new JsonAnimationModel();
        jsonAnimationModel.points = pointList;
        jsonAnimationModel.children = childrenList;
        jsonAnimationModel.renderer = model.getRenderer().registryName.toString();

        return jsonAnimationModel;
    }

    public AnimationModel toAnimationModel(){
        AnimationModel animationModel = new AnimationModel(new ResourceLocation(renderer));
        for(JsonKeyPoint jsonKeyPoint : points){
            animationModel.addKeyPoint(jsonKeyPoint.toKeyPoint());
        }
        for(JsonAnimationModel child : children){
            animationModel.addChild(child.toAnimationModel());
        }
        return animationModel;
    }
}

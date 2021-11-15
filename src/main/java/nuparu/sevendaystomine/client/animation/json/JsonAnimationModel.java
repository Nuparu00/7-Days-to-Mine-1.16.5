package nuparu.sevendaystomine.client.animation.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.client.animation.AnimationModel;
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

    public static JsonAnimationModel from(JsonObject object) {
        JsonAnimationModel jsonAnimationModel = new JsonAnimationModel();
        List<JsonKeyPoint> pointList = new ArrayList<>();
        if(object.has("points")){
            JsonArray pointz = object.getAsJsonArray("points");
            for(JsonElement modelElement : pointz){
                pointList.add(JsonKeyPoint.from(modelElement.getAsJsonObject()));
            }
        }
        List<JsonAnimationModel> childrenList = new ArrayList<>();
        if(object.has("children")){
            JsonArray modelz = object.getAsJsonArray("children");
            for(JsonElement modelElement : modelz){
                childrenList.add(JsonAnimationModel.from(modelElement.getAsJsonObject()));
            }
        }
        JsonAnimationModel jsonAnimationModel1 = new JsonAnimationModel();
        jsonAnimationModel.points = pointList;
        jsonAnimationModel.children = childrenList;
        jsonAnimationModel.renderer = object.has("renderer") ? object.get("renderer").getAsString() : "minecraft:empty";

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

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{");
        s.append(" Renderer: ").append(renderer);
        s.append(" Models: [");
        for(JsonAnimationModel model : children){
            s.append(model.toString()).append(" ");
        }
        s.append("]");
        s.append("Models: [");
        for(JsonKeyPoint point : points){
            s.append(point.toString()).append(" ");
        }
        s.append("]");
        s.append("}");
        return s.toString();
    }
}

package nuparu.sevendaystomine.client.animation.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.client.animation.Animation;
import nuparu.sevendaystomine.client.animation.AnimationModel;

import java.util.ArrayList;
import java.util.List;

public class JsonAnimation {
    public List<JsonAnimationModel> models;
    public String name;
    public double speed;
    public long duration;
    public boolean running;
    public boolean repeat;

    private JsonAnimation() {

    }

    public static JsonAnimation of(Animation animation) {
        JsonAnimation jsonAnimation = new JsonAnimation();
        List<JsonAnimationModel> models = new ArrayList<>();
        for (AnimationModel model : animation.getModels()) {
            models.add(JsonAnimationModel.of(model));
        }
        jsonAnimation.models = models;
        jsonAnimation.name = animation.name.toString();
        jsonAnimation.speed = animation.getSpeed();
        jsonAnimation.duration = animation.getDuration();
        jsonAnimation.running = animation.isRunning();
        jsonAnimation.repeat = animation.isRepeat();

        return jsonAnimation;
    }

    public static JsonAnimation from(JsonObject obj){
        JsonAnimation jsonAnimation = new JsonAnimation();
        List<JsonAnimationModel> models = new ArrayList<>();
        if(obj.has("models")){
            JsonArray modelz = obj.getAsJsonArray("models");
            for(JsonElement modelElement : modelz){
                models.add(JsonAnimationModel.from(modelElement.getAsJsonObject()));
            }
        }
        jsonAnimation.models = models;
        jsonAnimation.name = obj.has("name") ? obj.get("name").getAsString() : "minecraft:empty";
        jsonAnimation.speed = obj.has("speed") ? obj.get("speed").getAsDouble() : 1;
        jsonAnimation.duration = obj.has("duration") ? obj.get("duration").getAsInt() : 1;
        jsonAnimation.running = !obj.has("running") || obj.get("running").getAsBoolean();
        jsonAnimation.repeat = !obj.has("repeat") || obj.get("repeat").getAsBoolean();

        return jsonAnimation;
    }

    public Animation toAnimation() {
        Animation animation = new Animation(new ResourceLocation(name));
        animation.setSpeed(speed);
        animation.setDuration(duration);
        animation.setRunning(running);
        animation.setRepeat(repeat);

        for (JsonAnimationModel jsonAnimationModel : models) {
            animation.addModel(jsonAnimationModel.toAnimationModel());
        }

        return animation;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{");
        s.append(" Name: ").append(name.toString());
        s.append(" Speed: ").append(speed);
        s.append(" Duration: ").append(duration);
        s.append(" Running: ").append(running);
        s.append(" Repeat: ").append(repeat);
        s.append(" Speed: ").append(speed);
        s.append(" Models: [");
        for(JsonAnimationModel model : models){
            s.append(model.toString()).append(" ");
        }
        s.append("]");
        s.append("}");
        return s.toString();
    }

}

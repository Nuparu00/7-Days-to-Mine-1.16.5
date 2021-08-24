package nuparu.sevendaystomine.client.animation.json;

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
        for(AnimationModel model : animation.getModels()){
            models.add(JsonAnimationModel.of(model));
        }
        jsonAnimation.models=models;
        jsonAnimation.name=animation.name.toString();
        jsonAnimation.speed=animation.getSpeed();
        jsonAnimation.duration=animation.getDuration();
        jsonAnimation.running=animation.isRunning();
        jsonAnimation.repeat=animation.isRepeat();

        return jsonAnimation;
    }

    public Animation toAnimation(){
        Animation animation = new Animation(new ResourceLocation(name));
        animation.setSpeed(speed);
        animation.setDuration(duration);
        animation.setRunning(running);
        animation.setRepeat(repeat);

        for(JsonAnimationModel jsonAnimationModel : models){
            animation.addModel(jsonAnimationModel.toAnimationModel());
        }

        return animation;
    }

}

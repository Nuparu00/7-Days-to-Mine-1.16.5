package nuparu.sevendaystomine.client.animation;

import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.client.animation.json.JsonAnimation;
import nuparu.sevendaystomine.entity.human.dialogue.DialogueDataManager;
import nuparu.sevendaystomine.util.dialogue.DialogueParser;
import nuparu.sevendaystomine.util.dialogue.Dialogues;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.Map;

public class Animations extends JsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();
    public static Animations instance = new Animations();
    private HashMap<ResourceLocation, Animation> animations;

    public static Animation currentAnimation = null;
    public static boolean override = false;
    public static Vector3d offset = Vector3d.ZERO;

    public Animations() {
        super(GSON,"animations");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn,
                         IProfiler profilerIn) {
        AnimationModelRenderers.init();

        currentAnimation = null;
        HashMap<ResourceLocation, Animation> animationz = new HashMap<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement je = entry.getValue();
            System.out.println("animations + " + key.toString());
            //JsonAnimation jsonAnimation =  GSON.fromJson(je,JsonAnimation.class);
            JsonAnimation jsonAnimation =  JsonAnimation.from(je.getAsJsonObject());
            System.out.println(key.toString() + jsonAnimation.toString());
            Animation animation = jsonAnimation.toAnimation();
            animationz.put(key, animation);

           /* JsonObject jsonObject = je.getAsJsonObject();
            JsonObject nameObject = jsonObject.getAsJsonObject("name");

            ResourceLocation name = new ResourceLocation(nameObject.get("namespace").getAsString(),nameObject.get("path").getAsString());
            double speed = jsonObject.get("speed").getAsDouble();
            double duration = jsonObject.get("duration").getAsLong();
            boolean running = jsonObject.get("running").getAsBoolean();
            boolean repeat = jsonObject.get("repeat").getAsBoolean();

            JsonArray models = jsonObject.getAsJsonArray("models");
            for (JsonElement modelElement : models) {
                JsonObject modelObject = modelElement.getAsJsonObject();
                JsonArray points = modelObject.getAsJsonArray("points");
                for (JsonElement pointElement : points) {
                    GSON.fromJson(pointElement,)
                }
            }


            currentAnimation = animation;
            animationz.put(key, animation);*/
        }
        animations = animationz;
    }

    public Animation get(ResourceLocation res) {
        return this.animations.get(res);
    }

    public HashMap<ResourceLocation, Animation> getAnimations(){
        return (HashMap<ResourceLocation, Animation>) animations.clone();
    }

    public boolean contains(Animation animation){
        return this.animations.containsValue(animation);
    }



}

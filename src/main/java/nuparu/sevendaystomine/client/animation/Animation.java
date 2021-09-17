package nuparu.sevendaystomine.client.animation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.client.animation.json.JsonAnimation;
import nuparu.sevendaystomine.client.animation.json.JsonAnimationModel;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class Animation {

    private List<AnimationModel> models = new ArrayList<>();
    public ResourceLocation name;

    private long currentTime = 0;
    private long lastRender = 0;
    private long startTime = -1;

    private double speed = 1;


    private long duration = 0;

    private boolean running = false;


    private boolean repeat = false;

    public Animation(ResourceLocation name){
        this.name = name;
    }

    public void addModel(AnimationModel model) {
        models.add(model);
        long modelDuration = model.getDuration();
        if(modelDuration > duration){
            duration = modelDuration;
        }
    }

    public List<AnimationModel> getModels(){
        return new ArrayList<>(models);
    }


    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int light){
        long now = System.currentTimeMillis();


       if(running) {
           if (startTime == -1) {
               startTime = now;
           }
           now -= startTime;

           long deltaTime = (long) ((now - lastRender)*speed);
           currentTime += deltaTime;
           lastRender = now;

           if(currentTime > getDuration()){
               this.reset();
               if(!isRepeat()){
                   this.pause();
               }
           }

       }
        for(AnimationModel model : new ArrayList<>(models)){
            matrixStack.pushPose();
            model.render(matrixStack,buffer,light,currentTime);
            matrixStack.popPose();
        }

    }

    public void reset(){
         currentTime = 0;
         lastRender = 0;
         startTime = -1;
    }

    public void pause(){
        this.running = false;
    }

    public void unpause(){
        lastRender = 0;
        startTime = System.currentTimeMillis();
        this.running = true;
    }

    public void switchPause(){
        if(this.running){
            pause();
        }
        else{
            unpause();
        }


    }

    public boolean isRunning() {
        return running;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void saveAsJson(File file){
        JsonAnimation jsonAnimation = JsonAnimation.of(this);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(jsonAnimation);
        try {
            FileUtils.writeStringToFile(file, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

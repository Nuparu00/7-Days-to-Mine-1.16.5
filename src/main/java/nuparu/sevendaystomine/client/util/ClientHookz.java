package nuparu.sevendaystomine.client.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

//Place to put misc methods called from Mixins. Code in Mixin can not be hotswapped (it seems), so we are doing it here, so I dont have to restart the game
public class ClientHookz {

    public static void skyColorHook(MatrixStack matrixStack, float partialTicks, ClientWorld world) {

        int sunsetStart = 12610;
        int sunsestEnd = 15000;
        int sunsetDarkEnd = 14500;
        int sunsetRedStart = 14000;

            double angle = world.getTimeOfDay(partialTicks);
            long time = world.getDayTime()-24000* (Utils.getDay(world)-1);
            //12610 = sunset start
            if (time > sunsetStart) {
                Vector3d vector3d = world.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getBlockPosition(), partialTicks);
                double mult = MathUtils.clamp(Math.abs(angle - 0.54), 0, 0.30);
                double lightMult = 0;

                //22300 = sunrise start
                if(time > 22300){
                    lightMult = (time-22300)/1700d;
                }

                //System.out.println(lightMult +  " " + time + " " + ((time-12610)/1092d));
                float rNew = (float)( mult * 0.1f);

                float r = rNew;

                if(time > 22300){
                    r = (float) MathUtils.lerp(rNew,vector3d.x,(float)((time-22300)/1700d));
                }
                //13702 = sunset end
                else if(time < sunsestEnd) {
                    double rOld = MathUtils.lerp(vector3d.x,vector3d.x-0.2f,(float) ((MathUtils.clamp(time,0,sunsetDarkEnd) - sunsetStart) / 390));
                    mult*=((time - sunsetStart) / (sunsestEnd-sunsetStart));
                    lightMult=1-((time - sunsetStart) / (sunsestEnd-sunsetStart));
                    rNew = (float)( mult * 0.1f);
                    if (time > sunsetRedStart){
                        r = (float) MathUtils.lerp(rOld, rNew, (float) ((time - 12610) / 1092d));
                    }
                    r= (float) vector3d.x;
                }

                RenderSystem.color3f(r, (float)( lightMult * vector3d.y), (float)( lightMult * vector3d.z));
            }
        }
}

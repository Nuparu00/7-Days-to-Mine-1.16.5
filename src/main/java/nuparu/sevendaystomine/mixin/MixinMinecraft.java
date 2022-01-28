package nuparu.sevendaystomine.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import nuparu.sevendaystomine.client.gui.GuiMainMenuEnhanced;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraft {


    @Inject(method = "Lnet/minecraft/client/Minecraft;getSituationalMusic()Lnet/minecraft/client/audio/BackgroundMusicSelector;", at = @At("RETURN"), cancellable = true, remap = ModConstants.REMAP)
    public void getSituationalMusic(CallbackInfoReturnable<BackgroundMusicSelector> cir) {
        Minecraft thys = ((Minecraft) (Object) this);
        if(ClientConfig.customMenu.get() && cir.getReturnValue() == BackgroundMusicTracks.MENU){
            cir.setReturnValue(GuiMainMenuEnhanced.MENU_DEFAULT);
            cir.cancel();
        }

    }
}



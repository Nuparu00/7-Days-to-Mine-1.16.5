package nuparu.sevendaystomine.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.BackgroundMusicSelector;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import nuparu.sevendaystomine.client.gui.GuiMainMenuEnhanced;
import nuparu.sevendaystomine.config.ClientConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "Lnet/minecraft/client/renderer/GameRenderer;getNightVisionScale(Lnet/minecraft/entity/LivingEntity;F)F", at = @At("HEAD"), cancellable = true, remap = ModConstants.REMAP)
    private static void getSituationalMusic(LivingEntity entity, float partialTicks, CallbackInfoReturnable<Float> cir) {
        System.out.println("DD");
        ItemStack stack = entity.getItemBySlot(EquipmentSlotType.HEAD);
        if(!stack.isEmpty() && stack.getItem() == ModItems.NIGHT_VISION_DEVICE.get()){
            cir.setReturnValue(1f);
            cir.cancel();
        }
    }
}

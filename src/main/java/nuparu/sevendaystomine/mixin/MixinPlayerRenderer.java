package nuparu.sevendaystomine.mixin;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import nuparu.sevendaystomine.item.ItemGun;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class MixinPlayerRenderer {


    @Inject(method = "Lnet/minecraft/client/renderer/entity/PlayerRenderer;getArmPose(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/renderer/entity/model/BipedModel$ArmPose;", at = @At("HEAD"), cancellable = true, remap = ModConstants.REMAP)
    private static void getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedModel.ArmPose> cir) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        if(item instanceof ItemGun ){
            cir.setReturnValue(BipedModel.ArmPose.BOW_AND_ARROW);
        }
    }

}

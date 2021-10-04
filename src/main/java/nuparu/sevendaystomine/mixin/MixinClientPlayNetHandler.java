package nuparu.sevendaystomine.mixin;

import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SRecipeBookPacket;
import net.minecraft.network.play.server.SUpdateRecipesPacket;
import net.minecraft.util.NonNullList;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.util.ModConstants;
import nuparu.sevendaystomine.util.PlayerUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetHandler.class)
public class MixinClientPlayNetHandler {

    @Inject(method = "Lnet/minecraft/client/network/play/ClientPlayNetHandler;handleAddOrRemoveRecipes(Lnet/minecraft/network/play/server/SRecipeBookPacket;)V", at = @At("RETURN"), remap = ModConstants.REMAP)
    public void handleAddOrRemoveRecipes(SRecipeBookPacket p_191980_1_, CallbackInfo ci) {
        BookDataManager.instance.reloadRecipes();
    }

    @Inject(method = "Lnet/minecraft/client/network/play/ClientPlayNetHandler;handleUpdateRecipes(Lnet/minecraft/network/play/server/SUpdateRecipesPacket;)V", at = @At("RETURN"), remap = ModConstants.REMAP)
    public void handleUpdateRecipes(SUpdateRecipesPacket p_199525_1_, CallbackInfo ci) {
        BookDataManager.instance.reloadRecipes();
    }
}

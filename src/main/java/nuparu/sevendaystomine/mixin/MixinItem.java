package nuparu.sevendaystomine.mixin;


import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.item.EnumQuality;
import nuparu.sevendaystomine.item.ItemQuality;
import nuparu.sevendaystomine.util.ModConstants;
import nuparu.sevendaystomine.util.PlayerUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class MixinItem {

    @Shadow public abstract Item asItem();

    @Inject(method = "Lnet/minecraft/item/Item;fillItemCategory(Lnet/minecraft/item/ItemGroup;Lnet/minecraft/util/NonNullList;)V", at = @At("HEAD"), cancellable = true, remap = ModConstants.REMAP)
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> list, CallbackInfo ci) {
        if (PlayerUtils.isVanillaItemSuitableForQuality((Object)this) && allowdedIn(group)) {
            ItemStack stack = new ItemStack(asItem());
            ItemQuality.setQualityForStack(stack, SevenDaysToMine.proxy.getQualityForCurrentPlayer());
            list.add(stack);
            ci.cancel();
        }
    }

    @Inject(method = "Lnet/minecraft/item/Item;onCraftedBy(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"), remap = ModConstants.REMAP)
    public void onCraftedBy(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci) {
        if (PlayerUtils.isVanillaItemSuitableForQuality((Object)this) && ItemQuality.getQualityFromStack(stack) <= 0) {
            ItemQuality.setQualityForStack(stack, (int) Math.min(Math.floor(player.totalExperience / CommonConfig.xpPerQuality.get()),
                            CommonConfig.maxQuality.get()));
        }
    }

    @Inject(method = "Lnet/minecraft/item/Item;getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/util/text/ITextComponent;", at = @At("HEAD"), cancellable = true, remap = ModConstants.REMAP)
    public void getName(ItemStack stack, CallbackInfoReturnable<ITextComponent> cir) {
        if(PlayerUtils.isVanillaQualityItem(stack) && CommonConfig.qualitySystem.get() == EnumQualityState.ALL){
            TranslationTextComponent textComponent = new TranslationTextComponent(getDescriptionId(stack));
            EnumQuality quality = PlayerUtils.getQualityTierFromStack(stack);
            textComponent.setStyle(textComponent.getStyle().withColor(quality.getColor()));
            cir.setReturnValue(textComponent);
        }
    }


    @Shadow
    protected boolean allowdedIn(ItemGroup p_194125_1_) {
        throw new IllegalStateException("Mixin failed to shadow allowdedIn()");
    }

    @Shadow
    public String getDescriptionId(ItemStack p_77667_1_) {
        throw new IllegalStateException("Mixin failed to shadow getDescriptionId()");
    }

}

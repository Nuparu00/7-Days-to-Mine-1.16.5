package nuparu.sevendaystomine.mixin;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SingleJigsawPiece.class)
public class MixinJigsawPiece {

    @Inject(method = "Lnet/minecraft/world/gen/feature/jigsaw/SingleJigsawPiece;getSettings(Lnet/minecraft/util/Rotation;Lnet/minecraft/util/math/MutableBoundingBox;Z)Lnet/minecraft/world/gen/feature/template/PlacementSettings;", at = @At("RETURN"), remap = ModConstants.REMAP)
    private void getSettings(Rotation p_230379_1_, MutableBoundingBox p_230379_2_, boolean p_230379_3_, CallbackInfoReturnable<PlacementSettings> cir) {
        /*System.out.println("gegegeg");
        cir.getReturnValue().popProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);*/
    }
}

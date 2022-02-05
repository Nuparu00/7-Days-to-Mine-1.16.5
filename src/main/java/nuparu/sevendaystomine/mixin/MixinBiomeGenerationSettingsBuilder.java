package nuparu.sevendaystomine.mixin;

import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.gen.feature.StructureFeature;
import nuparu.sevendaystomine.util.ModConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeGenerationSettings.Builder.class)
public class MixinBiomeGenerationSettingsBuilder {
    @Inject(method = "Lnet/minecraft/world/biome/BiomeGenerationSettings$Builder;addStructureStart(Lnet/minecraft/world/gen/feature/StructureFeature;)Lnet/minecraft/world/biome/BiomeGenerationSettings$Builder;", at = @At("HEAD"), cancellable = true, remap = ModConstants.REMAP)
    private  void addStructureStart(StructureFeature<?, ?> p_242516_1_, CallbackInfoReturnable<BiomeGenerationSettings.Builder> cir) {
        Object thys = this;
        String featureName = p_242516_1_.feature.getRegistryName().toString();
        if(featureName.equals("minecraft:village") || featureName.equals("minecraft:pillager_outpost") || featureName.equals("minecraft:mansion")) {
            cir.setReturnValue((BiomeGenerationSettings.Builder) thys);
        }
    }
}

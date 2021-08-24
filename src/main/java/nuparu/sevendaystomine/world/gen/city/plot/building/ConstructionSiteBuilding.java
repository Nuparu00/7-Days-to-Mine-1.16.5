package nuparu.sevendaystomine.world.gen.city.plot.building;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.CityType;

import java.util.Random;

public class ConstructionSiteBuilding extends Building{

    public ConstructionSiteBuilding(ResourceLocation res,int weight, int yOffset) {
        super(res, weight, yOffset);
        setPedestal(ModBlocks.ANDESITE_BRICKS.get().defaultBlockState());
        setAllowedCityTypes(CityType.CITY);
    }


    @Override
    public void generate(ServerWorld world, BlockPos pos, Direction facing, boolean mirror, Random rand) {

        MinecraftServer minecraftserver = world.getServer();
        TemplateManager templatemanager = world.getStructureManager();


        Rotation rot = Utils.facingToRotation(facing.getCounterClockWise());

        PlacementSettings placementsettings = (new PlacementSettings())
                .setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
                .setChunkPos((ChunkPos) null);


        for(int i = 1; i < 3;i++) {
            Template template = templatemanager.get( new ResourceLocation(SevenDaysToMine.MODID,"construction_site_a"+(i)));
            if(template != null) {
                this.generateTemplate(world, pos, mirror, facing, placementsettings, template, true,rand);
            }
            pos = pos.relative(facing.getClockWise(),mirror ? 32 : -32);
        }
        pos = pos.relative(facing, -32).relative(facing.getClockWise(),mirror ? -32 : 32);
        for(int i = 0; i < 2;i++) {
            Template template = templatemanager.get( new ResourceLocation(SevenDaysToMine.MODID,"construction_site_b"+(2-i)));
            if(template != null) {
                this.generateTemplate(world, pos, mirror, facing, placementsettings, template, true,rand);
            }
            pos = pos.relative(facing.getClockWise(),mirror ? -32 : 32);
        }

        pos = pos.relative(facing, 41).relative(facing.getClockWise(),mirror ? 32 : -32);
        Template template = templatemanager.get( new ResourceLocation(SevenDaysToMine.MODID,"construction_site_crane_bottom"));
        if(template != null) {
            this.generateTemplate(world, pos, mirror, facing, placementsettings, template, true,rand);
        }

        pos = pos.above(32);

        template = templatemanager.get( new ResourceLocation(SevenDaysToMine.MODID,"construction_site_crane_cab"));
        if(template != null) {
            this.generateTemplate(world, pos, mirror, facing, placementsettings, template, false,rand);
        }

        pos = pos.relative(facing, -32);
        template = templatemanager.get( new ResourceLocation(SevenDaysToMine.MODID,"construction_site_crane_front"));
        if(template != null) {
            this.generateTemplate(world, pos, mirror, facing, placementsettings, template, false,rand);
        }

        pos = pos.relative(facing, 47);
        template = templatemanager.get( new ResourceLocation(SevenDaysToMine.MODID,"construction_site_crane_end"));
        if(template != null) {
            this.generateTemplate(world, pos, mirror, facing, placementsettings, template, false,rand);
        }

    }

    @Override
    public BlockPos getDimensions(ServerWorld world, Direction facing, boolean mirror) {
        return new BlockPos(70, 10, 70);
    }
}

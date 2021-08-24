package nuparu.sevendaystomine.world.gen.city.plot.building;

import net.minecraft.block.Blocks;
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
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.CityType;

import java.util.Random;

public class HotelBuilding extends Building {

    private ResourceLocation ENTRANCE = new ResourceLocation(SevenDaysToMine.MODID, "hotel_entrance");
    private ResourceLocation ENTRANCE_TOP = new ResourceLocation(SevenDaysToMine.MODID, "hotel_entrance_top");
    private ResourceLocation CAFETERIA = new ResourceLocation(SevenDaysToMine.MODID, "hotel_cafeteria");
    private ResourceLocation CAFETERIA_TOP = new ResourceLocation(SevenDaysToMine.MODID, "hotel_cafeteria_top");
    private ResourceLocation BASEMENT = new ResourceLocation(SevenDaysToMine.MODID, "hotel_entrance_basement");

    public HotelBuilding(int weight) {
        this(weight, 0);
    }

    public HotelBuilding(int weight, int yOffset) {
        super(new ResourceLocation(SevenDaysToMine.MODID, "hotel"), weight, yOffset);
        setAllowedCityTypes(CityType.CITY);
        setPedestal(Blocks.STONE.defaultBlockState());
    }

    @Override
    public void generate(ServerWorld world, BlockPos pos, Direction facing, boolean mirror, Random rand) {

        MinecraftServer minecraftserver = world.getServer();
        TemplateManager templatemanager = world.getStructureManager();

        Template template = templatemanager.get(ENTRANCE);
        if (template == null) {
            return;
        }

        Rotation rot = Utils.facingToRotation(facing.getCounterClockWise());

        pos = pos.above(yOffset);

        PlacementSettings placementsettings = (new PlacementSettings())
                .setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
                .setChunkPos((ChunkPos) null);

        this.generateTemplate(world, pos, mirror, facing, placementsettings, template, true, rand, false);

        template = templatemanager.get(CAFETERIA);
        if (template == null) {
            return;
        }
        pos = pos.relative(facing, -32);
        this.generateTemplate(world, pos, mirror, facing, placementsettings, template, true, rand, false);

        template = templatemanager.get(CAFETERIA_TOP);
        if (template == null) {
            return;
        }
        pos = pos.above(32);
        this.generateTemplate(world, pos, mirror, facing, placementsettings, template, false, rand);

        pos = pos.relative(facing, 32);
        template = templatemanager.get(ENTRANCE_TOP);
        if (template == null) {
            return;
        }
        this.generateTemplate(world, pos, mirror, facing, placementsettings, template, false, rand);
        pos = pos.below(37);
        template = templatemanager.get(BASEMENT);
        if (template == null) {
            return;
        }
        this.generateTemplate(world, pos, mirror, facing, placementsettings, template, false, rand, false);

    }

    @Override
    public BlockPos getDimensions(ServerWorld world, Direction facing, boolean mirror) {
        return new BlockPos(44, 40, 44);
    }
}

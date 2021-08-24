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
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.gen.city.CityType;

import java.util.Random;

public class ChurchBuilding extends Building{


    private ResourceLocation FRONT = new ResourceLocation(SevenDaysToMine.MODID, "church_front");
    private ResourceLocation BACK = new ResourceLocation(SevenDaysToMine.MODID, "church_back");
    private ResourceLocation TOP = new ResourceLocation(SevenDaysToMine.MODID, "church_top");

    public ChurchBuilding(int weight) {
        this(weight, 0);
    }

    public ChurchBuilding(int weight, int yOffset) {
        super(new ResourceLocation(SevenDaysToMine.MODID, "church"), weight, yOffset);
        canBeMirrored = false;
        setAllowedCityTypes(CityType.CITY, CityType.TOWN);
    }

    @Override
    public void generate(ServerWorld world, BlockPos pos, Direction facing, boolean mirror, Random rand) {

        MinecraftServer minecraftserver = world.getServer();
        TemplateManager templatemanager = world.getStructureManager();

        Template template = templatemanager.get(FRONT);
        if (template == null) {
            return;
        }

        Rotation rot = Utils.facingToRotation(facing.getCounterClockWise());

        pos = pos.above(yOffset).relative(facing,-8);

        PlacementSettings placementsettings = (new PlacementSettings())
                .setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
                .setChunkPos((ChunkPos) null);

        generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal, rand);

        BlockPos size = template.getSize();
        template = templatemanager.get(BACK);
        if (template == null) {
            return;
        }
        BlockPos pos2 = pos.relative(facing.getClockWise(), mirror ? 32 : -32);
        generateTemplate(world, pos2, mirror, facing, placementsettings, template, hasPedestal, rand);
        BlockPos pos3 = pos.above(32);
        template = templatemanager.get(TOP);
        if (template == null) {
            return;
        }
        generateTemplate(world, pos3, mirror, facing, placementsettings, template, hasPedestal, rand);
    }

    @Override
    public BlockPos getDimensions(ServerWorld world, Direction facing, boolean mirror) {
        return new BlockPos(35, 22, 35);
    }
}

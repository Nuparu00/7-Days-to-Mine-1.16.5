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

public class ApartmentsBigBuilding extends Building{

    public ResourceLocation top;
    public ApartmentsBigBuilding(ResourceLocation bottom, ResourceLocation top, int weight) {
        this(bottom,top,weight, 0);
    }

    public ApartmentsBigBuilding(ResourceLocation bottom, ResourceLocation top, int weight, int yOffset) {
        super(bottom, weight, yOffset);
        this.top = top;
        canBeMirrored = false;
        setAllowedCityTypes(CityType.CITY, CityType.TOWN);
    }

    @Override
    public void generate(ServerWorld world, BlockPos pos, Direction facing, boolean mirror, Random rand) {

        MinecraftServer minecraftserver = world.getServer();
        TemplateManager templatemanager = world.getStructureManager();

        Template template = templatemanager.get(res);
        if (template == null) {
            return;
        }

        Rotation rot = Utils.facingToRotation(facing.getCounterClockWise());

        pos = pos.above(yOffset);

        PlacementSettings placementsettings = (new PlacementSettings())
                .setMirror(mirror ? Mirror.LEFT_RIGHT : Mirror.NONE).setRotation(rot).setIgnoreEntities(false)
                .setChunkPos((ChunkPos) null);

        generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal, rand);

        pos=pos.above(32);
        template = templatemanager.get(top);
        if (template == null) {
            return;
        }
        generateTemplate(world, pos, mirror, facing, placementsettings, template, false, rand);

    }

    @Override
    public BlockPos getDimensions(ServerWorld world, Direction facing, boolean mirror) {
        return new BlockPos(41, 22, 41);
    }
}

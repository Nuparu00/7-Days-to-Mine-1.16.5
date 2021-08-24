package nuparu.sevendaystomine.world.gen.city.plot.building;

import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.StructureMode;
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
import nuparu.sevendaystomine.world.gen.city.City;
import nuparu.sevendaystomine.world.gen.city.CityType;

import java.util.Random;

public class ApartmentDarkBuilding extends Building{
    private ResourceLocation[] mid_odd = new ResourceLocation[] {
            new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_mid_a"),
            new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_mid_a2") };
    private ResourceLocation[] mid_even = new ResourceLocation[] {
            new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_mid_b"),
            new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_mid_b2") };
    private ResourceLocation[] top_odd = new ResourceLocation[] {
            new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_top") };
    private ResourceLocation[] top_even = new ResourceLocation[] {
            new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_top2") };
    private ResourceLocation[] roof = new ResourceLocation[] {
            new ResourceLocation(SevenDaysToMine.MODID, "apartment_dark_roof") };

    public ApartmentDarkBuilding(ResourceLocation res, int weight) {
        this(res, weight,0);
    }
    public ApartmentDarkBuilding(ResourceLocation res, int weight, int yOffset) {
        super(res, weight,yOffset);
        setAllowedCityTypes(CityType.CITY);
        this.setPedestal(Blocks.STONE.defaultBlockState());
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
        int segments = rand.nextInt(7) + 2;
        for (int i = 0; i < segments; i++) {
            pos = pos.above(5);
            ResourceLocation res2 = null;
            if (i >= segments - 1) {
                res2 = roof[rand.nextInt(roof.length)];
                System.out.println("FFFFFFFFFFFFFFFFF");
            } else if (i >= segments - 2) {
                if (i % 2 == 0) {
                    res2 = top_odd[rand.nextInt(top_odd.length)];
                } else {
                    res2 = top_even[rand.nextInt(top_even.length)];
                }
            } else if (i % 2 == 0) {
                res2 = mid_odd[rand.nextInt(mid_odd.length)];
            } else {
                res2 = mid_even[rand.nextInt(mid_even.length)];
            }

            template = templatemanager.get(res2);
            if (template == null) {
                System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF " + res2.toString());
                return;
            }
            if(template.getSize().getY()+pos.getY() > 255) {
                i = segments-2;
                continue;
            }
            generateTemplate(world, pos, mirror, facing, placementsettings, template, hasPedestal, rand);
        }
    }

}

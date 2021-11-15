package nuparu.sevendaystomine.world.prefab.json;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.world.prefab.EnumPrefabType;
import nuparu.sevendaystomine.world.prefab.EnumStructureBiomeType;
import nuparu.sevendaystomine.world.prefab.Prefab;
import nuparu.sevendaystomine.world.prefab.buffered.BufferedBlock;
import nuparu.sevendaystomine.world.prefab.buffered.BufferedEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonPrefab {

    public HashMap<RemapEntry, RemapResult> remapMap = new HashMap<>();

    public void init() {
        remapMap = new HashMap<>();
        remapMap.put(new RemapEntry("sevendaystomine:stone_brick_stairs_mossy",-1),new RemapResult("minecraft:mossy_stone_brick_stairs"));
        remapMap.put(new RemapEntry("sevendaystomine:andesitebricks",-1),new RemapResult("sevendaystomine:andesite_bricks"));
        remapMap.put(new RemapEntry("sevendaystomine:medicalcabinet",-1),new RemapResult("sevendaystomine:medical_cabinet"));
        remapMap.put(new RemapEntry("sevendaystomine:marblebricks",-1),new RemapResult("sevendaystomine:marble_bricks"));
        remapMap.put(new RemapEntry("sevendaystomine:granitebricks",-1),new RemapResult("sevendaystomine:granite_bricks"));
        remapMap.put(new RemapEntry("sevendaystomine:dioritebricks",-1),new RemapResult("sevendaystomine:diorite_bricks"));
        remapMap.put(new RemapEntry("minecraft:fence",-1),new RemapResult("minecraft:oak_fence"));
        remapMap.put(new RemapEntry("sevendaystomine:razor_wire",0),new RemapResult("sevendaystomine:razor_wire","{Properties:{facing:\"south\"},waterlogged:\"false\",Name:\"sevendaystomine:razor_wire\"}"));
        remapMap.put(new RemapEntry("sevendaystomine:razor_wire",1),new RemapResult("sevendaystomine:razor_wire","{Properties:{facing:\"west\"},waterlogged:\"false\",Name:\"sevendaystomine:razor_wire\"}"));
        remapMap.put(new RemapEntry("sevendaystomine:razor_wire",2),new RemapResult("sevendaystomine:razor_wire","{Properties:{facing:\"north\"},waterlogged:\"false\",Name:\"sevendaystomine:razor_wire\"}"));
        remapMap.put(new RemapEntry("sevendaystomine:razor_wire",3),new RemapResult("sevendaystomine:razor_wire","{Properties:{facing:\"east\"},waterlogged:\"false\",Name:\"sevendaystomine:razor_wire\"}"));

        remapMap.put(new RemapEntry("minecraft:dirt",1),new RemapResult("minecraft:coarse_dirt"));
        remapMap.put(new RemapEntry("minecraft:dirt",2),new RemapResult("minecraft:podzol"));

        remapMap.put(new RemapEntry("minecraft:wool",11),new RemapResult("minecraft:blue_wool"));
        remapMap.put(new RemapEntry("minecraft:carpet",14),new RemapResult("minecraft:red_carpet"));
        remapMap.put(new RemapEntry("minecraft:carpet",12),new RemapResult("minecraft:brown_carpet"));
        remapMap.put(new RemapEntry("minecraft:tallgrass",-1),new RemapResult("minecraft:grass"));

        remapMap.put(new RemapEntry("sevendaystomine:armchairblack",-1),new RemapResult("sevendaystomine:armchair_black"));
        remapMap.put(new RemapEntry("minecraft:wool",0),new RemapResult("minecraft:white_wool"));

        remapMap.put(new RemapEntry("minecraft:stained_hardened_clay",4),new RemapResult("minecraft:yellow_terracotta"));
        remapMap.put(new RemapEntry("minecraft:stained_hardened_clay",3),new RemapResult("minecraft:light_blue_terracotta"));
        remapMap.put(new RemapEntry("minecraft:stained_hardened_clay",5),new RemapResult("minecraft:lime_terracotta"));
        remapMap.put(new RemapEntry("minecraft:stained_hardened_clay",9),new RemapResult("minecraft:cyan_terracotta"));
        remapMap.put(new RemapEntry("minecraft:stained_hardened_clay",11),new RemapResult("minecraft:blue_terracotta"));
        remapMap.put(new RemapEntry("minecraft:stained_hardened_clay",13),new RemapResult("minecraft:green_terracotta"));
        remapMap.put(new RemapEntry("minecraft:stained_hardened_clay",14),new RemapResult("minecraft:red_terracotta"));

        remapMap.put(new RemapEntry("minecraft:concrete",0),new RemapResult("minecraft:white_concrete"));
        remapMap.put(new RemapEntry("minecraft:concrete",14),new RemapResult("minecraft:red_concrete"));
        remapMap.put(new RemapEntry("minecraft:concrete",4),new RemapResult("minecraft:yellow_concrete"));
        remapMap.put(new RemapEntry("minecraft:concrete",8),new RemapResult("minecraft:light_gray_concrete"));

        remapMap.put(new RemapEntry("minecraft:stained_glass_pane",8),new RemapResult("minecraft:light_gray_stained_glass_pane"));

        remapMap.put(new RemapEntry("minecraft:stone",1),new RemapResult("minecraft:granite"));
        remapMap.put(new RemapEntry("minecraft:stone",2),new RemapResult("minecraft:polished_granite"));
        remapMap.put(new RemapEntry("minecraft:stone",3),new RemapResult("minecraft:diorite"));
        remapMap.put(new RemapEntry("minecraft:stone",4),new RemapResult("minecraft:polished_diorite"));
        remapMap.put(new RemapEntry("minecraft:stone",5),new RemapResult("minecraft:andesite"));
        remapMap.put(new RemapEntry("minecraft:stone",6),new RemapResult("minecraft:polished_andesite"));

        remapMap.put(new RemapEntry("sevendaystomine:structure_stone",0),new RemapResult("minecraft:stone"));
        remapMap.put(new RemapEntry("sevendaystomine:structure_stone",1),new RemapResult("minecraft:granite"));
        remapMap.put(new RemapEntry("sevendaystomine:structure_stone",2),new RemapResult("minecraft:polished_granite"));
        remapMap.put(new RemapEntry("sevendaystomine:structure_stone",3),new RemapResult("minecraft:diorite"));
        remapMap.put(new RemapEntry("sevendaystomine:structure_stone",4),new RemapResult("minecraft:polished_diorite"));
        remapMap.put(new RemapEntry("sevendaystomine:structure_stone",5),new RemapResult("minecraft:andesite"));
        remapMap.put(new RemapEntry("sevendaystomine:structure_stone",6),new RemapResult("minecraft:polished_andesite"));

        remapMap.put(new RemapEntry("minecraft:stonebrick",0),new RemapResult("minecraft:stone_bricks"));
        remapMap.put(new RemapEntry("minecraft:stonebrick",1),new RemapResult("minecraft:mossy_stone_bricks"));
        remapMap.put(new RemapEntry("minecraft:stonebrick",2),new RemapResult("minecraft:cracked_stone_bricks"));

        remapMap.put(new RemapEntry("sevendaystomine:catwalk",0),new RemapResult("sevendaystomine:catwalk","{Properties:{waterlogged:\"false\",type:\"top\"},Name:\"sevendaystomine:catwalk\"}"));

        remapMap.put(new RemapEntry("minecraft:brick_block",0),new RemapResult("minecraft:bricks"));

        remapMap.put(new RemapEntry("minecraft:planks",0),new RemapResult("minecraft:oak_planks"));
        remapMap.put(new RemapEntry("minecraft:planks",1),new RemapResult("minecraft:spruce_planks"));
        remapMap.put(new RemapEntry("minecraft:planks",2),new RemapResult("minecraft:birch_planks"));
        remapMap.put(new RemapEntry("minecraft:planks",3),new RemapResult("minecraft:jungle_planks"));
        remapMap.put(new RemapEntry("minecraft:planks",4),new RemapResult("minecraft:acacia_planks"));
        remapMap.put(new RemapEntry("minecraft:planks",5),new RemapResult("minecraft:dark_oak_planks"));

        remapMap.put(new RemapEntry("sevendaystomine:reinforcedconcrete",-1),new RemapResult("sevendaystomine:reinforced_concrete"));


        remapMap.put(new RemapEntry("sevendaystomine:oakplanksframe",-1),new RemapResult("sevendaystomine:oak_planks_frame"));
        remapMap.put(new RemapEntry("sevendaystomine:birchplanksframe",-1),new RemapResult("sevendaystomine:birch_planks_frame"));
        remapMap.put(new RemapEntry("sevendaystomine:spruceplanksframe",-1),new RemapResult("sevendaystomine:spruce_planks_frame"));
        remapMap.put(new RemapEntry("sevendaystomine:jungleplanksframe",-1),new RemapResult("sevendaystomine:jungle_planks_frame"));
        remapMap.put(new RemapEntry("sevendaystomine:acaciaplanksframe",-1),new RemapResult("sevendaystomine:acacia_planks_frame"));
        remapMap.put(new RemapEntry("sevendaystomine:darkoakplanksframe",-1),new RemapResult("sevendaystomine:dark_oak_planks_frame"));

        remapMap.put(new RemapEntry("minecraft:log",0),new RemapResult("minecraft:oak_log","{Properties:{axis:\"y\"},Name:\"minecraft:oak_log\"}"));
        remapMap.put(new RemapEntry("minecraft:log",4),new RemapResult("minecraft:oak_log","{Properties:{axis:\"y\"},Name:\"minecraft:oak_log\"}"));
        remapMap.put(new RemapEntry("minecraft:log",8),new RemapResult("minecraft:oak_log","{Properties:{axis:\"y\"},Name:\"minecraft:oak_log\"}"));
        remapMap.put(new RemapEntry("minecraft:log",12),new RemapResult("minecraft:oak_log","{Properties:{axis:\"y\"},Name:\"minecraft:oak_log\"}"));

        remapMap.put(new RemapEntry("minecraft:log",1),new RemapResult("minecraft:spruce_log","{Properties:{axis:\"y\"},Name:\"minecraft:spruce_log\"}"));
        remapMap.put(new RemapEntry("minecraft:log",5),new RemapResult("minecraft:spruce_log","{Properties:{axis:\"x\"},Name:\"minecraft:spruce_log\"}"));
        remapMap.put(new RemapEntry("minecraft:log",9),new RemapResult("minecraft:spruce_log","{Properties:{axis:\"z\"},Name:\"minecraft:spruce_log\"}"));
        remapMap.put(new RemapEntry("minecraft:log",13),new RemapResult("minecraft:spruce_log","{Properties:{axis:\"none\"},Name:\"minecraft:spruce_log\"}"));

        remapMap.put(new RemapEntry("minecraft:grass",-1),new RemapResult("minecraft:grass_block"));

        remapMap.put(new RemapEntry("minecraft:leaves",12),new RemapResult("minecraft:oak_leaves"));
        remapMap.put(new RemapEntry("minecraft:leaves",0),new RemapResult("minecraft:oak_leaves"));
        remapMap.put(new RemapEntry("minecraft:leaves",4),new RemapResult("minecraft:oak_leaves"));


        remapMap.put(new RemapEntry("sevendaystomine:writingtable",-1),new RemapResult("sevendaystomine:oak_writing_table"));
        remapMap.put(new RemapEntry("sevendaystomine:torchunlit",-1),new RemapResult("sevendaystomine:torch_unlit"));
        remapMap.put(new RemapEntry("sevendaystomine:bookshelf",-1),new RemapResult("sevendaystomine:oak_bookshelf"));
        remapMap.put(new RemapEntry("sevendaystomine:stonebrick_wall",-1),new RemapResult("minecraft:stone_brick_wall"));

        remapMap.put(new RemapEntry("minecraft:stone_slab",5),new RemapResult("minecraft:stone_brick_slab"));
        remapMap.put(new RemapEntry("minecraft:stone_slab",0),new RemapResult("minecraft:smooth_stone_slab"));
        remapMap.put(new RemapEntry("minecraft:stone_slab",3),new RemapResult("minecraft:cobblestone_slab"));
        remapMap.put(new RemapEntry("minecraft:stone_slab",4),new RemapResult("minecraft:brick_slab"));
        remapMap.put(new RemapEntry("minecraft:stone_slab",12),new RemapResult("minecraft:brick_slab","{Properties:{waterlogged:\"false\",type:\"top\"},Name:\"minecraft:brick_slab\"}"));
        remapMap.put(new RemapEntry("minecraft:stone_slab",13),new RemapResult("minecraft:stone_brick_slab","{Properties:{waterlogged:\"false\",type:\"top\"},Name:\"minecraft:stone_brick_slab\"}"));
        remapMap.put(new RemapEntry("minecraft:stone_slab",8),new RemapResult("minecraft:smooth_stone_slab","{Properties:{waterlogged:\"false\",type:\"top\"},Name:\"minecraft:smooth_stone_slab\"}"));
        remapMap.put(new RemapEntry("minecraft:double_stone_slab",0),new RemapResult("minecraft:smooth_stone_slab","{Properties:{waterlogged:\"false\",type:\"double\"},Name:\"minecraft:smooth_stone_slab\"}"));
        remapMap.put(new RemapEntry("minecraft:double_stone_slab",8),new RemapResult("minecraft:smooth_stone"));


        remapMap.put(new RemapEntry("minecraft:vine",0),new RemapResult("minecraft:vine","{Properties:{east:\"false\",south:\"false\",north:\"false\",west:\"false\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",1),new RemapResult("minecraft:vine","{Properties:{east:\"false\",south:\"true\",north:\"false\",west:\"false\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",2),new RemapResult("minecraft:vine","{Properties:{east:\"false\",south:\"false\",north:\"false\",west:\"true\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",3),new RemapResult("minecraft:vine","{Properties:{east:\"false\",south:\"true\",north:\"false\",west:\"true\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",4),new RemapResult("minecraft:vine","{Properties:{east:\"false\",south:\"false\",north:\"true\",west:\"false\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",5),new RemapResult("minecraft:vine","{Properties:{east:\"false\",south:\"true\",north:\"true\",west:\"false\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",6),new RemapResult("minecraft:vine","{Properties:{east:\"false\",south:\"false\",north:\"true\",west:\"true\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",7),new RemapResult("minecraft:vine","{Properties:{east:\"false\",south:\"true\",north:\"true\",west:\"true\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",8),new RemapResult("minecraft:vine","{Properties:{east:\"true\",south:\"false\",north:\"false\",west:\"false\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",9),new RemapResult("minecraft:vine","{Properties:{east:\"true\",south:\"true\",north:\"false\",west:\"false\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",10),new RemapResult("minecraft:vine","{Properties:{east:\"true\",south:\"false\",north:\"false\",west:\"true\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",11),new RemapResult("minecraft:vine","{Properties:{east:\"true\",south:\"true\",north:\"false\",west:\"true\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",12),new RemapResult("minecraft:vine","{Properties:{east:\"true\",south:\"false\",north:\"true\",west:\"false\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",13),new RemapResult("minecraft:vine","{Properties:{east:\"true\",south:\"true\",north:\"true\",west:\"false\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",14),new RemapResult("minecraft:vine","{Properties:{east:\"true\",south:\"false\",north:\"true\",west:\"true\",up:\"false\"},Name:\"minecraft:vine\"}"));
        remapMap.put(new RemapEntry("minecraft:vine",15),new RemapResult("minecraft:vine","{Properties:{east:\"false\",south:\"true\",north:\"true\",west:\"true\",up:\"false\"},Name:\"minecraft:vine\"}"));
    }

    String name;
    int width;
    int height;
    int length;
    int offsetX;
    int offsetY;
    int offsetZ;
    int pedestalLayer = 255;
    boolean underground = false;
    double requiredRoom;
    double requiredFlatness;
    int weight;
    EnumPrefabType type = EnumPrefabType.NONE;
    List<EnumStructureBiomeType> biomeTypes;
    List<JsonBlock> blocks;
    List<JsonEntity> entities;
    public JsonPrefab(String name, int width, int height, int length, int offsetX, int offsetY, int offsetZ,
                      boolean underground, double requiredRoom, double requiredFlatness, int weight, EnumPrefabType type,
                      List<EnumStructureBiomeType> biomeTypes, List<JsonBlock> blocks, List<JsonEntity> entities) {
        init();
        this.name = name;
        this.width = width;
        this.height = height;
        this.length = length;

        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;

        this.underground = underground;
        this.requiredRoom = requiredRoom;
        this.requiredFlatness = requiredFlatness;
        this.weight = weight;

        this.type = type;
        this.biomeTypes = biomeTypes;

        this.blocks = blocks;
        this.entities = entities;
    }

    public Prefab toPrefab() {
        init();
        return new Prefab(name, width, height, length, offsetX, offsetY, offsetZ, underground, requiredRoom,
                requiredFlatness, weight, type, biomeTypes, getBlocks(), getEntities(), pedestalLayer);
    }

    public List<BufferedBlock> getBlocks() {
        List<BufferedBlock> list = new ArrayList<BufferedBlock>();
        for (JsonBlock block : blocks) {
            Block bl = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block.block));
            if (block.y < pedestalLayer) {
                pedestalLayer = block.y;
            }

            String state = "";

            RemapResult remap = findRemap(block.block, block.meta);
            if (remap != null) {
                bl = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(remap.name));
                state = remap.state;
            }

            if (!ForgeRegistries.BLOCKS.containsKey(new ResourceLocation(block.block)) && remap == null) {

                    Utils.getLogger()
                            .info("Couldn't find a tile{" + block.block + "} for file " + name + ". Replacing by Air.");
            }

            if(bl instanceof StairsBlock){
                switch(block.meta){
                    case 0 : state = "{Properties:{half:\"bottom\",waterlogged:\"false\",shape:\"straight\",facing:\"east\"},Name:\""+bl.getRegistryName().toString()+"\"}";break;
                    case 1 : state = "{Properties:{half:\"bottom\",waterlogged:\"false\",shape:\"straight\",facing:\"west\"},Name:\""+bl.getRegistryName().toString()+"\"}";break;
                    case 2 : state = "{Properties:{half:\"bottom\",waterlogged:\"false\",shape:\"straight\",facing:\"south\"},Name:\""+bl.getRegistryName().toString()+"\"}";break;
                    case 3 : state = "{Properties:{half:\"bottom\",waterlogged:\"false\",shape:\"straight\",facing:\"north\"},Name:\""+bl.getRegistryName().toString()+"\"}";break;
                    case 4 : state = "{Properties:{half:\"top\",waterlogged:\"false\",shape:\"straight\",facing:\"east\"},Name:\""+bl.getRegistryName().toString()+"\"}";break;
                    case 5 : state = "{Properties:{half:\"top\",waterlogged:\"false\",shape:\"straight\",facing:\"west\"},Name:\""+bl.getRegistryName().toString()+"\"}";break;
                    case 6 : state = "{Properties:{half:\"top\",waterlogged:\"false\",shape:\"straight\",facing:\"south\"},Name:\""+bl.getRegistryName().toString()+"\"}";break;
                    case 7 : state = "{Properties:{half:\"top\",waterlogged:\"false\",shape:\"straight\",facing:\"north\"},Name:\""+bl.getRegistryName().toString()+"\"}";break;
                }
            }

            CompoundNBT nbt = null;
            if (!block.nbt.isEmpty()) {
                try {
                    nbt = JsonToNBT.parseTag(block.nbt);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }
            }
            BufferedBlock block2 = new BufferedBlock(block.x, block.y, block.z, bl, state, nbt, block.lootTable);
            list.add(block2);
        }
        return list;
    }

    public RemapResult findRemap(String name, int meta){
        for(Map.Entry<RemapEntry,RemapResult> entry : remapMap.entrySet()){
            if(name.equals(entry.getKey().name) && (meta == entry.getKey().meta || entry.getKey().meta == -1)){
                return entry.getValue();
            }
        }
        return null;
    }

    public List<BufferedEntity> getEntities() {
        List<BufferedEntity> list = new ArrayList<BufferedEntity>();
        /*for (JsonEntity entity : entities) {
            CompoundNBT nbt = null;
            if (!entity.nbt.isEmpty()) {
                try {
                    nbt = JsonToNBT.getTagFromJson(entity.nbt);
                } catch (NBTException e) {
                    e.printStackTrace();
                }
            }
            BufferedEntity entity2 = new BufferedEntity(entity.name, entity.x, entity.y, entity.z, entity.yaw,
                    entity.pitch, nbt);
            list.add(entity2);
        }*/
        return list;
    }

    public static class RemapEntry {
        String name;
        int meta;

        public RemapEntry(String name, int meta) {
            this.name = name;
            this.meta = meta;
        }
        public RemapEntry(String name) {
            this.name = name;
            this.meta = -1;
        }
    }

    public static class RemapResult {
        String name;
        String state = "";

        public RemapResult(String name, String state) {
            this.name = name;
            this.state = state;
        }
        public RemapResult(String name) {
            this.name = name;
        }
    }
}

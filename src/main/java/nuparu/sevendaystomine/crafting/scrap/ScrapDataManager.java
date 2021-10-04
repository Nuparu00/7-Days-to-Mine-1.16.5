package nuparu.sevendaystomine.crafting.scrap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.crafting.forge.MaterialStack;
import nuparu.sevendaystomine.entity.human.dialogue.DialogueDataManager;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.util.dialogue.Dialogues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrapDataManager extends JsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();

    public static ScrapDataManager instance = new ScrapDataManager();

    private List<ScrapEntry> scraps = new ArrayList<ScrapEntry>();
    private HashMap<EnumMaterial,ScrapEntry> scrapBitsMap = new HashMap<EnumMaterial, ScrapEntry>();

    public ScrapDataManager() {
        super(GSON, "scraps");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn,
                         IProfiler profilerIn) {
        scraps.clear();
        scrapBitsMap.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement je = entry.getValue();
            JsonObject jo = je.getAsJsonObject();

            try {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(jo.get("item").getAsString()));
                EnumMaterial material = EnumMaterial.byName(jo.get("material").getAsString());
                if(material == null){
                    throw new NullPointerException("Invalid or null material: " + jo.get("material"));
                }
                int weight = jo.get("weight").getAsInt();
                boolean canBeScrapped = jo.get("canBeScrapped").getAsBoolean();
                boolean isScrapBit = jo.get("isScrapBit").getAsBoolean();
                boolean excludeFromMin = false;
                if(jo.has("excludeFromMin")) {
                    excludeFromMin = jo.get("excludeFromMin").getAsBoolean();
                }
                ScrapEntry scrapEntry = new ScrapEntry(key,item,material,weight,canBeScrapped,isScrapBit,excludeFromMin);
                scraps.add(scrapEntry);
                if(isScrapBit){
                    scrapBitsMap.put(material,scrapEntry);
                }
            }
            catch (NullPointerException e){
                SevenDaysToMine.LOGGER.error("An error occurred while trying to load scrap (" + key.toString() + ") :" + e.getMessage());
            }
        }

    }

    public boolean hasEntry(Item item){
        for(ScrapEntry entry : scraps){
            if(entry.item == item){
                return true;
            }
        }
        return false;
    }

    public ScrapEntry getEntry(Item item){
        for(ScrapEntry entry : scraps){
            if(entry.item == item){
                return entry;
            }
        }
        return null;
    }

    public boolean hasEntry(ItemStack stack){
        return hasEntry(stack.getItem());
    }

    public ScrapEntry getEntry(ItemStack stack){
        return getEntry(stack.getItem());
    }

    public boolean hasScrapResult(EnumMaterial material){
        return scrapBitsMap.containsKey(material);
    }

    public ScrapEntry getScrapResult(EnumMaterial material){
        if(hasScrapResult(material)){
            return scrapBitsMap.get(material);
        }
        return null;
    }

    /*
    Returns the item of the given material with the smallest weight
     */
    public ScrapEntry getSmallestItem(EnumMaterial material){
        ScrapEntry min = null;
        for(ScrapEntry entry : scraps){
            if(!entry.excludeFromMin && (entry.material == material && (min == null || entry.weight < min.weight))){
                min = entry;
            }
        }
        return min;
    }

    public List<ScrapEntry> getScraps(){
        return new ArrayList<>(this.scraps);
    }

    public CompoundNBT save(CompoundNBT nbt){
        ListNBT list = new ListNBT();
        for(ScrapEntry entry : scraps){
            list.add(entry.save(new CompoundNBT()));
        }
        nbt.put("list",list);
        return nbt;
    }

    public void load(CompoundNBT nbt){
        if(nbt.contains("list",Constants.NBT.TAG_LIST)){
            scraps.clear();
            scrapBitsMap.clear();
            ListNBT list = nbt.getList("list",Constants.NBT.TAG_COMPOUND);
            for(INBT inbt : list){
                CompoundNBT compoundNBT = (CompoundNBT)inbt;
                ScrapEntry scrapEntry = ScrapEntry.of(compoundNBT);
                scraps.add(scrapEntry);
                if(scrapEntry.isScrapBit){
                    scrapBitsMap.put(scrapEntry.material,scrapEntry);
                }
            }
        }
    }

    public static class ScrapEntry {
        public ResourceLocation name;
        public Item item;
        public EnumMaterial material;
        public int weight;
        public boolean canBeScrapped;
        //Scrap bit is the smallest unit of the material and the result of scrapping of other items
        public boolean isScrapBit;
        public boolean excludeFromMin;

        public ScrapEntry(ResourceLocation name, Item item, EnumMaterial material, int weight, boolean canBeScrapped, boolean isScrapBit, boolean excludeFromMin){
            this.name = name;
            this.item = item;
            this.material = material;
            this.weight = weight;
            this.canBeScrapped = canBeScrapped;
            this.isScrapBit = isScrapBit;
            this.excludeFromMin = excludeFromMin;
        }

        public CompoundNBT save(CompoundNBT nbt){
            nbt.putString("name",name.toString());
            nbt.putString("item",item.getRegistryName().toString());
            nbt.putString("material", material.name());
            nbt.putInt("weight", weight);
            nbt.putBoolean("canBeScrapped", canBeScrapped);
            nbt.putBoolean("isScrapBit", isScrapBit);
            nbt.putBoolean("excludeFromMin", excludeFromMin);
            return nbt;
        }

        public static ScrapEntry of(CompoundNBT nbt){
            if(!nbt.contains("name", Constants.NBT.TAG_STRING)) return null;
            if(!nbt.contains("item", Constants.NBT.TAG_STRING)) return null;
            if(!nbt.contains("material", Constants.NBT.TAG_STRING)) return null;
            if(!nbt.contains("weight", Constants.NBT.TAG_INT)) return null;
            if(!nbt.contains("canBeScrapped", Constants.NBT.TAG_BYTE)) return null;
            if(!nbt.contains("isScrapBit", Constants.NBT.TAG_BYTE)) return null;
            if(!nbt.contains("excludeFromMin", Constants.NBT.TAG_BYTE)) return null;

            ResourceLocation name = new ResourceLocation(nbt.getString("name"));
            Item item =  ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("item")));
            EnumMaterial material =  EnumMaterial.byName(nbt.getString("material"));
            int weight = nbt.getInt("weight");
            boolean canBeScrapped = nbt.getBoolean("canBeScrapped");
            boolean isScrapBit = nbt.getBoolean("isScrapBit");
            boolean excludeFromMin = nbt.getBoolean("excludeFromMin");

            return new ScrapEntry(name,item,material,weight,canBeScrapped,isScrapBit,excludeFromMin);
        }

        public MaterialStack toMaterialStack(){
            return new MaterialStack(material,weight);
        }
    }

}

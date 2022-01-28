package nuparu.sevendaystomine.block.repair;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.sevendaystomine.SevenDaysToMine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepairDataManager extends JsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).create();

    public static RepairDataManager instance = new RepairDataManager();

    private List<RepairEntry> repairs = new ArrayList<RepairEntry>();

    public RepairDataManager() {
        super(GSON, "repairs");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn,
                         IProfiler profilerIn) {
        repairs.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation key = entry.getKey();
            JsonElement je = entry.getValue();
            JsonObject jo = je.getAsJsonObject();

            try {
                ResourceLocation block = new ResourceLocation(jo.get("block").getAsString());
                JsonArray stacksList = jo.get("items").getAsJsonArray();
                NonNullList<ItemStack> stacks = NonNullList.create();
                for (JsonElement stackElement : stacksList) {
                    ResourceLocation name = new ResourceLocation(stackElement.getAsJsonObject().get("item").getAsString());
                    Item item = ForgeRegistries.ITEMS.getValue(name);
                    if (item == null) continue;

                    int count = stackElement.getAsJsonObject().get("count").getAsInt();
                    CompoundNBT nbt = null;
                    if (stackElement.getAsJsonObject().has("tag")) {
                        nbt = JsonToNBT.parseTag(stackElement.getAsJsonObject().get("tag").getAsString());
                    }
                    ItemStack stack = new ItemStack(item, count);
                    stack.setTag(nbt);
                    stacks.add(stack);
                }
                double repairAmount = 0.1;
                double repairLimit = 1;

                if (jo.has("repairAmount")) {
                    repairAmount = jo.get("repairAmount").getAsDouble();
                }

                if (jo.has("repairLimit")) {
                    repairAmount = jo.get("repairLimit").getAsDouble();
                }

                RepairEntry scrapEntry = new RepairEntry(key, block, stacks, repairAmount, repairLimit);
                repairs.add(scrapEntry);
            } catch (NullPointerException | CommandSyntaxException e) {
                SevenDaysToMine.LOGGER.error("An error occurred while trying to load repair (" + key.toString() + ") :" + e.getMessage());
            }
        }

    }

    public boolean hasEntry(ResourceLocation resourceLocation) {
        for (RepairEntry entry : repairs) {
            if (entry.block.equals(resourceLocation)) {
                return true;
            }
        }
        return false;
    }

    public RepairEntry getEntry(ResourceLocation resourceLocation) {
        for (RepairEntry entry : repairs) {
            if (entry.block.equals(resourceLocation)) {
                return entry;
            }
        }
        return null;
    }

    public NonNullList<RepairEntry> getEntries(ResourceLocation resourceLocation) {
        NonNullList<RepairEntry> entries = NonNullList.create();
        for (RepairEntry entry : repairs) {
            if (entry.block.equals(resourceLocation)) {
                entries.add(entry);
            }
        }
        return entries;
    }

    public boolean hasEntry(Block block) {
        return hasEntry(block.getRegistryName());
    }

    public RepairEntry getEntry(Block block) {
        return getEntry(block.getRegistryName());
    }

    public NonNullList<RepairEntry> getEntries(Block block) {
        return getEntries(block.getRegistryName());
    }

    public boolean hasEntry(BlockState state) {
        return hasEntry(state.getBlock());
    }

    public RepairEntry getEntry(BlockState state) {
        return getEntry(state.getBlock());
    }

    public NonNullList<RepairEntry> getEntries(BlockState state) {
        return getEntries(state.getBlock());
    }

    public List<RepairEntry> getRepairs() {
        return new ArrayList<>(this.repairs);
    }

    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        for (RepairEntry entry : repairs) {
            list.add(entry.save(new CompoundNBT()));
        }
        nbt.put("list", list);
        return nbt;
    }

    public void load(CompoundNBT nbt) {
        if (nbt.contains("list", Constants.NBT.TAG_LIST)) {
            repairs.clear();
            ListNBT list = nbt.getList("list", Constants.NBT.TAG_COMPOUND);
            for (INBT inbt : list) {
                CompoundNBT compoundNBT = (CompoundNBT) inbt;
                RepairEntry scrapEntry = RepairEntry.of(compoundNBT);
                repairs.add(scrapEntry);
            }
        }
    }
}

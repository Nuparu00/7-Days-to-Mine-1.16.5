package nuparu.sevendaystomine.block.repair;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

public class RepairEntry {
    public final ResourceLocation name;
    public final ResourceLocation block;
    protected final NonNullList<ItemStack> items;
    protected final double repairAmount;
    protected final double repairLimit;

    public RepairEntry(ResourceLocation name, ResourceLocation block, NonNullList<ItemStack> items) {
        this(name, block, items, 0.1, 1);
    }

    public RepairEntry(ResourceLocation name, ResourceLocation block, NonNullList<ItemStack> items, double repairAmount, double repairLimit) {
        this.name = name;
        this.block = block;
        this.items = items;
        this.repairAmount = repairAmount;
        this.repairLimit = repairLimit;
    }

    public static RepairEntry of(CompoundNBT nbt) {
        if (!nbt.contains("name", Constants.NBT.TAG_STRING)) return null;
        if (!nbt.contains("block", Constants.NBT.TAG_STRING)) return null;
        if (!nbt.contains("items", Constants.NBT.TAG_LIST)) return null;
        if (!nbt.contains("repairAmount", Constants.NBT.TAG_DOUBLE)) return null;
        if (!nbt.contains("repairLimit", Constants.NBT.TAG_DOUBLE)) return null;


        ListNBT list = nbt.getList("items", Constants.NBT.TAG_COMPOUND);
        NonNullList<ItemStack> stacks = NonNullList.create();

        for (INBT inbt : list) {
            if (inbt instanceof CompoundNBT) {
                CompoundNBT tag = (CompoundNBT) inbt;
                ItemStack stack = ItemStack.of(tag);
                if (stack != null) {
                    stacks.add(stack);
                }
            }
        }

        return new RepairEntry(new ResourceLocation(nbt.getString("name")), new ResourceLocation(nbt.getString("block")), stacks, nbt.getDouble("repairAmount"), nbt.getDouble("repairLimit"));
    }

    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putString("name", name.toString());
        nbt.putString("block", block.toString());
        nbt.putDouble("repairAmount", repairAmount);
        nbt.putDouble("repairLimit", repairLimit);
        ListNBT list = new ListNBT();
        for (ItemStack stack : items) {
            list.add(stack.save(new CompoundNBT()));
        }
        nbt.put("items", list);
        return nbt;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public double getRepairAmount() {
        return repairAmount;
    }

    public double getRepairLimit() {
        return repairLimit;
    }

    public static JsonElement serialize(RepairEntry entry) {
        JsonObject root = new JsonObject();
        root.addProperty("block",entry.name.toString());
        root.addProperty("repairAmount",entry.repairAmount);
        root.addProperty("repairLimit",entry.repairLimit);
        JsonArray items = new JsonArray();
        for(ItemStack stack : entry.getItems()){
            JsonObject stackObject = new JsonObject();
            stackObject.addProperty("item",stack.getItem().getRegistryName().toString());
            stackObject.addProperty("count",stack.getCount());
            CompoundNBT nbt = stack.getTag();
            if(nbt != null) {
                stackObject.addProperty("tag", nbt.toString());
            }
            items.add(stackObject);
        }
        root.add("items",items);

        return root;
    }

    public static class Builder{
        protected NonNullList<ItemStack> items = NonNullList.create();
        protected double repairAmount = 0.1;
        protected double repairLimit = 1;

        public Builder setRepairAmount(double repairAmount){
            this.repairAmount = repairAmount;
            return this;
        }

        public Builder setRepairLimit(double repairLimit){
            this.repairLimit = repairLimit;
            return this;
        }

        public Builder setRepairItems(NonNullList<ItemStack> items){
            this.items = items;
            return this;
        }

        public Builder addRepairItem(ItemStack stack){
            if(items == null){
                items = NonNullList.create();
            }

            if(stack != null){
                items.add(stack);
            }

            return this;
        }

        public RepairEntry build(ResourceLocation block){
            return new RepairEntry(block,block,items,repairAmount,repairLimit);
        }

        public RepairEntry build(Block block){
            return build(block.getRegistryName());
        }
    }
}

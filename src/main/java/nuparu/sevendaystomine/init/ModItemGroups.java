package nuparu.sevendaystomine.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ModItemGroups {
    // CREATIVE TABS
    public static ItemGroup TAB_MATERIALS = new ItemGroup(SevenDaysToMine.MODID + ".materials") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.IRON_SCRAP.get());
        }
    };
    public static ItemGroup TAB_BUILDING = new ItemGroup(SevenDaysToMine.MODID + ".building") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.OAK_FRAME.get());
        }
    };
    public static ItemGroup TAB_MEDICINE = new ItemGroup(SevenDaysToMine.MODID + ".medicine") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BANDAGE_ADVANCED.get());
        }
    };
    public static ItemGroup TAB_FORGING = new ItemGroup(SevenDaysToMine.MODID + ".forging") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.FORGE.get());
        }
    };
    public static ItemGroup TAB_ELECTRICITY = new ItemGroup(SevenDaysToMine.MODID + ".electricity") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.WIRE.get());
        }
    };
    public static ItemGroup TAB_CLOTHING = new ItemGroup(SevenDaysToMine.MODID + ".clothing") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SHORTS.get());
        }
    };
    public static ItemGroup TAB_BOOKS = (new ItemGroup(SevenDaysToMine.MODID + ".books") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.BOOK_FORGING.get());
        }
    }).setEnchantmentCategories(ModEnchantments.GUNS);
}

package nuparu.sevendaystomine.item;

import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMap.Builder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class ItemClawhammer extends ItemUpgrader {

	public ItemClawhammer(IItemTier p_i48530_1_, float p_i48530_2_, float p_i48530_3_, Item.Properties p_i48530_4_) {
		super(p_i48530_2_, p_i48530_3_, p_i48530_1_, new HashSet<Block>(), p_i48530_4_);
		effect = 0.5f;
		length = EnumLength.SHORT;
	}
}

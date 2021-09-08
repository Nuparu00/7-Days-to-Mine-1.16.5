package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityPhoto;

public class ItemPhoto extends Item {

	public ItemPhoto() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt != null && nbt.contains("path", Constants.NBT.TAG_STRING)) {
			StringTextComponent text = new StringTextComponent(nbt.getString("path"));
			text.setStyle(text.getStyle().withItalic(true));
			tooltip.add(text);
		}
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (!playerIn.isCrouching()) {
			CompoundNBT nbt = stack.getOrCreateTag();
			if (nbt != null && nbt.contains("path", Constants.NBT.TAG_STRING)) {
				SevenDaysToMine.proxy.openClientOnlyGui(1, stack);
				return ActionResult.success(stack);
			}
		}
		return ActionResult.fail(stack);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {

		Direction facing = context.getClickedFace();
		PlayerEntity player = context.getPlayer();
		World worldIn = context.getLevel();
		Hand hand = context.getHand();
		BlockPos pos = context.getClickedPos();
		if (facing == Direction.UP) {
			return ActionResultType.PASS;
		} else {
			ItemStack stack = player.getItemInHand(hand);

			BlockState state = worldIn.getBlockState(pos);
			Block block = state.getBlock();

			BlockState stateToPlace = ModBlocks.PHOTO.get().defaultBlockState().setValue(BlockHorizontalBase.FACING, facing);

			if (!state.isFaceSturdy(worldIn, pos, facing)) {
				return ActionResultType.PASS;
			} else {
				worldIn.setBlockAndUpdate(pos.relative(facing), stateToPlace);
				TileEntity tileEntity = worldIn.getBlockEntity(pos.relative(facing));
				if (tileEntity != null && tileEntity instanceof TileEntityPhoto) {
					((TileEntityPhoto) tileEntity).setPath(stack.getOrCreateTag().getString("path"));
				}
				stack.shrink(1);
				return ActionResultType.SUCCESS;

			}
		}
	}

}

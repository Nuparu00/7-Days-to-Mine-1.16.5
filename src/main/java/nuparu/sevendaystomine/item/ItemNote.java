package nuparu.sevendaystomine.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
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
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.tileentity.TileEntityNote;
import nuparu.sevendaystomine.tileentity.TileEntityPhoto;
import nuparu.sevendaystomine.util.book.BookData;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNote extends Item {

	public ItemNote(Properties properties) {
		super(properties);
	}
	
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		SevenDaysToMine.proxy.openClientOnlyGui(4, stack);
		return ActionResult.success(stack);
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

			BlockState stateToPlace = ModBlocks.NOTE.get().defaultBlockState().setValue(BlockHorizontalBase.FACING, facing);

			if (!state.isFaceSturdy(worldIn, pos, facing)) {
				return ActionResultType.PASS;
			} else {
				worldIn.setBlockAndUpdate(pos.relative(facing), stateToPlace);
				TileEntity tileEntity = worldIn.getBlockEntity(pos.relative(facing));
				if (tileEntity != null && tileEntity instanceof TileEntityNote) {
					((TileEntityNote) tileEntity).setData(getData(stack));
				}
				stack.shrink(1);
				return ActionResultType.SUCCESS;

			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		ResourceLocation resourceLocation = this.getData(stack);


		String title = "Missing Data";
		String desc = "Missing Data";

		if(!resourceLocation.toString().equals("minecraft:empty")){
			BookData data = BookDataManager.instance.get(resourceLocation);
			if(data != null){
				if(data.getTitle() != null){
					title =data.getTitle();
				}
				if(data.getDesc() != null){
					desc =data.getDesc();
				}
			}
		}

		tooltip.add(new StringTextComponent(title));
		tooltip.add(new StringTextComponent(desc));
	}

	public static ItemStack setData(ItemStack stack, ResourceLocation data) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putString("data", data.toString());
		return stack;
	}

	public ResourceLocation getData(ItemStack stack){
		CompoundNBT nbt = stack.getOrCreateTag();
		if(!nbt.contains("data", Constants.NBT.TAG_STRING)){
			return new ResourceLocation("minecraft:empty");
		}
		return new ResourceLocation(nbt.getString("data"));
	}
}

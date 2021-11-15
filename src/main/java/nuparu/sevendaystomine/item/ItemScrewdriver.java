package nuparu.sevendaystomine.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.block.BlockDoorLocked;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.tileentity.TileEntityItemHandler;
import nuparu.sevendaystomine.util.MathUtils;

public class ItemScrewdriver extends ItemQuality {

	public ItemScrewdriver() {
		super(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_TOOLS));
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		World worldIn = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = worldIn.getBlockState(pos);

		ItemStack itemstack = player.getItemInHand(hand);
		if (player.isCrouching()) {
			TileEntity TE = worldIn.getBlockEntity(pos);
			if (TE != null) {
				if (TE instanceof TileEntityComputer) {
					if (player instanceof ServerPlayerEntity) {
						itemstack.hurt(1, random, (ServerPlayerEntity) player);
					}
					INamedContainerProvider namedContainerProvider = ModBlocks.COMPUTER.get().getMenuProvider(state, worldIn, pos);
					if (namedContainerProvider != null) {
						TileEntityItemHandler tileEntity = (TileEntityItemHandler)namedContainerProvider;
						tileEntity.unpackLootTable(player);
						if (!(player instanceof ServerPlayerEntity))
							return ActionResultType.FAIL;
						ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
						NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeBlockPos(pos));
					}
				}
			} else {
				if (!worldIn.isClientSide() && state.getBlock() instanceof BlockDoorLocked) {
					if (worldIn.random.nextInt(16) == 0) {
						((BlockDoorLocked) state.getBlock()).unlock(worldIn, pos, state);
						worldIn.playSound(null, pos, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS,
								0.75f + MathUtils.getFloatInRange(0, 0.25f), 0.8f + MathUtils.getFloatInRange(0, 0.2f));
					} else {
						worldIn.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundCategory.BLOCKS,
								0.75f + MathUtils.getFloatInRange(0, 0.25f), 0.8f + MathUtils.getFloatInRange(0, 0.2f));
					}
					if (player instanceof ServerPlayerEntity) {
						itemstack.hurt(1, random, (ServerPlayerEntity) player);
					}
				}
			}

		}
		return ActionResultType.PASS;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		int i = 0;
		if (stack.getOrCreateTag() != null) {
			i = getQuality(stack);
		}
		return super.getMaxDamage(stack) + i;
	}
}

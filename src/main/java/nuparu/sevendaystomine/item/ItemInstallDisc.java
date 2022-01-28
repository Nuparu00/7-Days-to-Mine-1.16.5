package nuparu.sevendaystomine.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import nuparu.sevendaystomine.block.BlockComputer;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;
import nuparu.sevendaystomine.computer.EnumSystem;

public class ItemInstallDisc extends Item {

	public final EnumSystem system;

	public ItemInstallDisc(EnumSystem system) {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY));
		this.system = system;
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity playerIn = context.getPlayer();
		World worldIn = context.getLevel();
		Hand hand = context.getHand();
		BlockPos pos = context.getClickedPos();

		ItemStack stack = playerIn.getItemInHand(hand);
		if (!worldIn.isClientSide()) {
			BlockState state = worldIn.getBlockState(pos);
			if (state.getBlock() instanceof BlockComputer) {
				if (playerIn.isCrouching()) {
					if (!stack.isEmpty()) {
						Item item = stack.getItem();
						if (item instanceof ItemInstallDisc) {
							TileEntity TE = worldIn.getBlockEntity(pos);
							if (TE != null && TE instanceof TileEntityComputer) {
								TileEntityComputer computerTE = (TileEntityComputer) TE;
								if (computerTE.getSystem() == EnumSystem.NONE && computerTE.isCompleted()) {
									EnumSystem system = ((ItemInstallDisc) item).system;
									computerTE.installSystem(system);
									TranslationTextComponent text = new TranslationTextComponent("computer.install",
											system.getReadeable());
									text.getStyle().withColor(TextFormatting.GREEN);
									playerIn.sendMessage(text, Util.NIL_UUID);
									return ActionResultType.SUCCESS;
								}
							}
						}
					}
				}
			}
		}

		return ActionResultType.FAIL;
	}
}

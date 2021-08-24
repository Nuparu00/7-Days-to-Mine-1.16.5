package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemVoltmeter extends Item {

	public ItemVoltmeter() {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY));
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		World worldIn = context.getLevel();
		BlockPos pos = context.getClickedPos();

		if (!player.level.isClientSide()) {
			TileEntity te = worldIn.getBlockEntity(pos);
			if (te != null && te instanceof IVoltage) {
				player.sendMessage(
						new TranslationTextComponent("voltmeter.message", ((IVoltage) te).getVoltageStored() + ""),
						Util.NIL_UUID);
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
}

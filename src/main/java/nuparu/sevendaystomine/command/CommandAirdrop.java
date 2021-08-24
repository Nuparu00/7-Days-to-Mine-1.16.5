package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.Utils;

public class CommandAirdrop {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> mbesayCommand = Commands.literal("airdrop")
				.requires((commandSource) -> commandSource.hasPermission(2)).executes((p_198700_0_) -> {
					return airdrop(p_198700_0_, Utils.getAirdropPos(p_198700_0_.getSource().getLevel()));
				}).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((p_198700_0_) -> {
					return airdrop(p_198700_0_, BlockPosArgument.getOrLoadBlockPos(p_198700_0_, "pos"));
				}));

		dispatcher.register(mbesayCommand);
	}

	static int airdrop(CommandContext<CommandSource> commandContext, BlockPos pos) {
		ServerWorld world = commandContext.getSource().getLevel();
		if (world.isClientSide())
			return 0;
		/*EntityAirdrop e = new EntityAirdrop(world, world.getSharedSpawnPos().above(255));
		world.addFreshEntity(e);
		e.setPos(pos.getX(), pos.getY(), pos.getZ());

		commandContext.getSource().sendSuccess(new TranslationTextComponent("airdrop.message",
				pos.getX() + MathUtils.getIntInRange(world.random, 32, 128) * (world.random.nextBoolean() ? 1 : -1),
				pos.getZ() + MathUtils.getIntInRange(world.random, 32, 128) * (world.random.nextBoolean() ? 1 : -1)),
				true);
*/
		return 1;
	}

}
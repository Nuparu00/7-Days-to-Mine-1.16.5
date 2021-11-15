package nuparu.sevendaystomine.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.item.ItemQuality;

public class CommandSetQuality {

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> mbesayCommand = Commands.literal("setquality")
				.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.argument("targets", EntityArgument.players())
				.then(Commands.argument("quality", MessageArgument.message()).executes((p_198539_0_) -> airdrop(p_198539_0_, EntityArgument.getPlayers(p_198539_0_, "targets")))));

		dispatcher.register(mbesayCommand);
	}

	static int airdrop(CommandContext<CommandSource> commandContext, Collection<ServerPlayerEntity> collection) {
		ServerWorld world = commandContext.getSource().getLevel();
		if (world.isClientSide())
			return 0;
		for (ServerPlayerEntity player : collection) {
			int quality;
			try {
				quality = Integer.parseInt(MessageArgument.getMessage(commandContext, "quality").getString());
				ItemStack stack = player.getMainHandItem();
				if (!stack.isEmpty()) {
					ItemQuality.setQualityForStack(stack, quality);
				}
			} catch (NumberFormatException | CommandSyntaxException e) {
				e.printStackTrace();
			}
		}
		return 1;
	}
}
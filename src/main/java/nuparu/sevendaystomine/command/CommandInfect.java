package nuparu.sevendaystomine.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.util.Utils;

public class CommandInfect{
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> mbesayCommand = Commands.literal("infect")
				.requires((commandSource) -> commandSource.hasPermission(2)).then(Commands.argument("targets", EntityArgument.players()).executes((p_198539_0_) -> {
					return cure(p_198539_0_, EntityArgument.getPlayers(p_198539_0_, "targets"));
				}));

		dispatcher.register(mbesayCommand);
	}

	static int cure(CommandContext<CommandSource> commandContext, Collection<ServerPlayerEntity> collection) {
		ServerWorld world = commandContext.getSource().getLevel();
		if (world.isClientSide())
			return 0;
		for (ServerPlayerEntity player : collection) {
			Utils.infectPlayer(player, 0);
		}
		return 1;
	}
}
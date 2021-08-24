package nuparu.sevendaystomine.command;

import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IChunkData;
import nuparu.sevendaystomine.util.Utils;

public class CommandSetBreakData {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> mbesayCommand = Commands.literal("setbreakdata")
				.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
				.then(Commands.argument("data", MessageArgument.message()).executes((p_198539_0_) -> {
					return breakData(p_198539_0_, BlockPosArgument.getOrLoadBlockPos(p_198539_0_, "pos"));
				})));

		dispatcher.register(mbesayCommand);
	}

	static int breakData(CommandContext<CommandSource> commandContext, BlockPos from) {
		System.out.println("DDDD");
		ServerWorld world = commandContext.getSource().getLevel();
		if (world.isClientSide())
			return 0;
		CommandSource sender = commandContext.getSource();
		System.out.println("DDDD");

		try {
			System.out.println("DDDD");
			float state = Float.parseFloat(MessageArgument.getMessage(commandContext, "data").getString());
			Chunk chunk = world.getChunkAt(from);
			if(chunk != null) {
				System.out.println("DDDD");
				IChunkData data = CapabilityHelper.getChunkData(chunk);
				data.setBreaakData(from, state);
				System.out.println("DATAX " + data.getBreakData(from).toString() + " " + data.getData().size());
			}
			
		} catch (NumberFormatException | CommandSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 1;
	}
}
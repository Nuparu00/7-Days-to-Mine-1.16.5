package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.world.prefab.Prefab;
import nuparu.sevendaystomine.world.prefab.PrefabParser;

import java.io.FileNotFoundException;

public class CommandPlacePrefab {
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> mbesayCommand = Commands.literal("placeprefab")
				.requires((commandSource) -> commandSource.hasPermission(2))
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
				.then(Commands.argument("name", MessageArgument.message()).executes((p_198539_0_) -> breakData(p_198539_0_, BlockPosArgument.getOrLoadBlockPos(p_198539_0_, "pos")))));

		dispatcher.register(mbesayCommand);
	}

	static int breakData(CommandContext<CommandSource> commandContext, BlockPos pos) {
		System.out.println("root");
		ServerWorld world = commandContext.getSource().getLevel();
		if (world.isClientSide())
			return 0;
		CommandSource sender = commandContext.getSource();


		System.out.println("if");
		try {
			String name = MessageArgument.getMessage(commandContext, "name").getString();
			Prefab prefab = null;
			long startTime = 0L;
			long endTime = 0L;
			long duration = 0L;

			System.out.println("try");
			try {
				prefab = PrefabParser.INSTANCE.getPrefabFromFile(name);
				System.out.println("DDDDDDDDD");
				if(prefab != null) {
					System.out.println("NOT NULL");
					startTime = System.nanoTime();
					prefab.generate(world, pos, Direction.SOUTH, false);
					endTime = System.nanoTime();
					duration = (endTime - startTime);
					sender.sendSuccess(new StringTextComponent("Prefab has been placed at " + pos.toShortString() + " within " + (duration / 1000000) + "ms."),true);
				}
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		} catch (CommandSyntaxException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return 1;
	}
}
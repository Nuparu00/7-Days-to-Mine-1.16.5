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
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.util.Utils;

public class CommandLocateModded {
public static void register(CommandDispatcher<CommandSource> dispatcher) {
	LiteralArgumentBuilder<CommandSource> mbesayCommand = Commands.literal("locatemodded")
			.requires((commandSource) -> commandSource.hasPermission(2)).then(Commands.argument("pos", BlockPosArgument.blockPos())).then(Commands.argument("maxdistance", MessageArgument.message()).executes((p_198539_0_) -> {
				return cure(p_198539_0_, BlockPosArgument.getOrLoadBlockPos(p_198539_0_, "pos"));
			}));

	dispatcher.register(mbesayCommand);
}

static int cure(CommandContext<CommandSource> commandContext, BlockPos from) {
	ServerWorld world = commandContext.getSource().getLevel();
	if (world.isClientSide())
		return 0;
	// BlockPos to = parseBlockPos(sender, args, 3, true);

	int chunkX = from.getX() >> 4;
	int chunkZ = from.getZ() >> 4;
	
	CommandSource sender = commandContext.getSource();

	try {
		int maxDst = Integer.parseInt(MessageArgument.getMessage(commandContext, "maxdistance").getString());
		
		new Thread() {
			@Override
			public void run() {
				List<ChunkPos> poses = Utils.getClosestCities(world, chunkX, chunkZ, maxDst);
				if (poses.isEmpty()) {
					sender.sendFailure(new StringTextComponent("No city located"));

				} else {
					for (ChunkPos pos : poses) {
						int x = (pos.x * 16);
						int z = (pos.z * 16);
						StringTextComponent component = new StringTextComponent("City is located at " + x + " " + z
								+ " ("
								+ Math.round(Math.sqrt(Math.pow(pos.x - chunkX, 2) + Math.pow(pos.z - chunkZ, 2)))
								+ " chunks)");
						
						Style style = component.getStyle().withClickEvent(
								new ClickEvent(Action.RUN_COMMAND, "/tp " + x + " " + 120 + " " + z) {
									@Override
									public Action getAction() {
										return Action.RUN_COMMAND;
									}
								});
						if (!world.getChunk(pos.x, pos.z).isEmpty()) {
							style.withColor(TextFormatting.GREEN);
						}
						component.setStyle(style);
						sender.sendSuccess(component,true);
					}
				}
			}

		}.start();
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return 1;
}
}
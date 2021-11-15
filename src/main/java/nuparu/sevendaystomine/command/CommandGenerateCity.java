package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.world.gen.city.CityType;

public class CommandGenerateCity {
public static void register(CommandDispatcher<CommandSource> dispatcher) {
	LiteralArgumentBuilder<CommandSource> mbesayCommand = Commands.literal("generatecity")
			.requires((commandSource) -> commandSource.hasPermission(2)).then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((p_198700_0_) -> city(p_198700_0_, BlockPosArgument.getOrLoadBlockPos(p_198700_0_, "pos"))));

	dispatcher.register(mbesayCommand);
}

static int city(CommandContext<CommandSource> commandContext, BlockPos from) {
	ServerWorld world = commandContext.getSource().getLevel();
	if (world.isClientSide())
		return 0;
	// BlockPos to = parseBlockPos(sender, args, 3, true);
	try {
		CityType cityType = CityType.CITY;
		if (cityType != null) {
			/*City city = new City(world, from, cityType, new Random());
			city.startCityGen();*/
		}
	}
	catch (Exception e){
		e.printStackTrace();
	}
	return 1;
}
}
package nuparu.sevendaystomine.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemNote;

import java.util.Collection;

public class CommandGiveNote {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> mbesayCommand = Commands.literal("givenote")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("path", MessageArgument.message()).executes((p_198539_0_) -> {
                            return giveNote(p_198539_0_, EntityArgument.getPlayers(p_198539_0_, "targets"));
                        })));

        dispatcher.register(mbesayCommand);
    }

    static int giveNote(CommandContext<CommandSource> commandContext, Collection<ServerPlayerEntity> collection) {
        ServerWorld world = commandContext.getSource().getLevel();
        if (world.isClientSide())
            return 0;
        for (ServerPlayerEntity player : collection) {
            try {
                String path = MessageArgument.getMessage(commandContext, "path").getString();
                ResourceLocation resourceLocation = new ResourceLocation(path);
                ItemStack stack = new ItemStack(ModItems.NOTE.get());
                ItemNote.setData(stack, resourceLocation);

                if (!player.inventory.add(stack)) {
                    ItemEntity itementity = player.drop(stack, false);
                    if (itementity != null) {
                        itementity.setNoPickUpDelay();
                        itementity.setOwner(player.getUUID());
                    }
                }

            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }
}
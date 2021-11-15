package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.tileentity.ILootTableProvider;

public class ItemRealityWand extends Item {
    public ItemRealityWand() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        World worldIn = context.getLevel();
        if(worldIn.isClientSide()) return ActionResultType.PASS;
        BlockPos pos = context.getClickedPos();
        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        ItemStack stack = context.getItemInHand();
        TranslationTextComponent textComponent;
            if (tileEntity != null) {
                if(stack.hasCustomHoverName()) {
                    CompoundNBT nbt = tileEntity.save(new CompoundNBT());
                    //if (nbt.contains("LootTable", Constants.NBT.TAG_STRING)) {
                    String lootTable = "sevendaystomine:containers/" + stack.getHoverName().getContents();
                    if(tileEntity instanceof ILootTableProvider){
                        ILootTableProvider lootTableProvider = (ILootTableProvider)tileEntity;
                        lootTableProvider.setLootTable(new ResourceLocation(lootTable),worldIn.random.nextLong());

                        textComponent = new TranslationTextComponent("debug.loottable.set", pos.toShortString(), lootTable);
                        textComponent.setStyle(textComponent.getStyle().withColor(TextFormatting.GREEN));
                    }
                    else{
                        nbt.putString("LootTable",lootTable);
                        tileEntity.load(worldIn.getBlockState(pos),nbt);
                        nbt = tileEntity.save(new CompoundNBT());
                        if (!nbt.contains("LootTable", Constants.NBT.TAG_STRING)) {
                            textComponent = new TranslationTextComponent("debug.loottable.support", pos.toShortString());
                            textComponent.setStyle(textComponent.getStyle().withColor(TextFormatting.YELLOW));
                        }
                        else {
                            textComponent = new TranslationTextComponent("debug.loottable.set", pos.toShortString(), lootTable);
                            textComponent.setStyle(textComponent.getStyle().withColor(TextFormatting.GREEN));
                        }
                    }
                }
                else {
                    CompoundNBT nbt = tileEntity.save(new CompoundNBT());
                    if (nbt.contains("LootTable", Constants.NBT.TAG_STRING)) {
                        String lootTable = nbt.getString("LootTable");
                        textComponent = new TranslationTextComponent("debug.loottable", pos.toShortString(), lootTable);
                        textComponent.setStyle(textComponent.getStyle().withColor(TextFormatting.GREEN));
                    } else {
                        textComponent = new TranslationTextComponent("debug.noloottable", pos.toShortString());
                        textComponent.setStyle(textComponent.getStyle().withColor(TextFormatting.YELLOW));
                    }
                }
            } else {
                textComponent = new TranslationTextComponent("debug.notileentity", pos.toShortString());
                textComponent.setStyle(textComponent.getStyle().withColor(TextFormatting.RED));
        }
        player.sendMessage(textComponent, Util.NIL_UUID);

        return ActionResultType.PASS;
    }


}

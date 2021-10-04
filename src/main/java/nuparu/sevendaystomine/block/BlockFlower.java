package nuparu.sevendaystomine.block;

import net.minecraft.block.FlowerBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;

public class BlockFlower extends FlowerBlock implements IBlockBase {

    public BlockFlower(Effect p_i49984_1_, int p_i49984_2_, Properties p_i49984_3_) {
        super(p_i49984_1_, p_i49984_2_, p_i49984_3_);
    }

    @Override
    public BlockItem createBlockItem() {
        final Item.Properties properties = new Item.Properties().tab(getItemGroup());
        return new BlockItem(this, properties);
    }
}

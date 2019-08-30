package dev.vatuu.tesseract.block;

import dev.vatuu.tesseract.block.entities.TesseractGatewayBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class TesseractGatewayBlock extends BlockWithEntity {

    public TesseractGatewayBlock(Settings s){
        super(s);
    }

    public BlockEntity createBlockEntity(BlockView v) {
        return new TesseractGatewayBlockEntity();
    }

    public ItemStack getPickStack(BlockView v, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }


}

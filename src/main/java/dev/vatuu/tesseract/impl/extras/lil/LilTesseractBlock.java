package dev.vatuu.tesseract.impl.extras.lil;

import dev.vatuu.tesseract.impl.Tesseract;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LilTesseractBlock extends Block implements BlockEntityProvider {

    private static LilTesseractBlock BLOCK_TYPE;

    public static LilTesseractBlock getBlockType(){
        if(BLOCK_TYPE == null) {
            BLOCK_TYPE = Registry.register(
                    Registry.BLOCK,
                    new Identifier(Tesseract.MOD_ID, "lil_tesseract"),
                    new LilTesseractBlock(Block.Settings.of(Material.METAL))
            );
            Registry.register(Registry.ITEM, new Identifier(Tesseract.MOD_ID, "lil_tesseract"), new BlockItem(BLOCK_TYPE, new Item.Settings().group(ItemGroup.MISC)));
        }
        return BLOCK_TYPE;
    }

    public LilTesseractBlock(Settings settings) {
        super(settings);
    }

    @Override
    public LilTesseractBlockEntity createBlockEntity(BlockView view) {
        return new LilTesseractBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ctx){
        return VoxelShapes.cuboid(0.1875f, 0.1875f, 0.1875f, 0.8125f, 0.8125f, 0.8125f);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        MinecraftClient.getInstance().openScreen(new LilConfigScreen(((LilTesseractBlockEntity)world.getBlockEntity(pos)).settings));
        return ActionResult.SUCCESS;
    }
}

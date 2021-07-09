package dev.teamtesseract.fractal.lil;

import dev.teamtesseract.fractal.client.rendering.LilTesseractRenderer;
import dev.teamtesseract.fractal.Fractal;
import dev.teamtesseract.fractal.client.ui.LilConfigScreen;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
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

public class LilTesseractBlockEntity extends BlockEntity {

    private static BlockEntityType<LilTesseractBlockEntity> BLOCK_ENTITY_TYPE;

    public LilTesseractSettings settings;

    public static BlockEntityType<LilTesseractBlockEntity> getBlockEntityType() {
        if(BLOCK_ENTITY_TYPE == null) {
            BLOCK_ENTITY_TYPE = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(Fractal.MOD_ID, "lil_tesseract"),
                    FabricBlockEntityTypeBuilder.create(LilTesseractBlockEntity::new, LilTesseractBlock.getBlockType()).build()
            );
        }
        return BLOCK_ENTITY_TYPE;
    }

    public static void registerRenderer() {
        LilTesseractBlock.registerRenderer();
        BlockEntityRendererRegistry.INSTANCE.register(BLOCK_ENTITY_TYPE, ctx -> new LilTesseractRenderer());
    }

    public LilTesseractBlockEntity(BlockPos position, BlockState state) {
        super(getBlockEntityType(), position, state);
        this.settings = new LilTesseractSettings();
    }

    public static class LilTesseractBlock extends Block implements BlockEntityProvider {

        private static LilTesseractBlock BLOCK_TYPE;

        public static LilTesseractBlock getBlockType(){
            if(BLOCK_TYPE == null) {
                BLOCK_TYPE = Registry.register(
                        Registry.BLOCK,
                        new Identifier(Fractal.MOD_ID, "lil_tesseract"),
                        new LilTesseractBlock(Block.Settings.of(Material.METAL))
                );
            }
            return BLOCK_TYPE;
        }

        public static void registerRenderer() {
            Registry.register(Registry.ITEM, new Identifier(Fractal.MOD_ID, "lil_tesseract"), new BlockItem(BLOCK_TYPE, new Item.Settings().group(ItemGroup.MISC)));
        }

        public LilTesseractBlock(Settings settings) {
            super(settings);
        }

        @Override
        public BlockRenderType getRenderType(BlockState state) {
            return BlockRenderType.INVISIBLE;
        }

        @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
            return VoxelShapes.cuboid(1F / 16 * 4, 1F / 16 * 4, 1F / 16 * 4, 1F / 16 * 12, 1F / 16 * 12, 1F / 16 * 12);
        }

        @Override
        public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
            if (world.isClient) {
                MinecraftClient.getInstance().openScreen(new LilConfigScreen(((LilTesseractBlockEntity) world.getBlockEntity(pos)).settings));
            }
            return ActionResult.SUCCESS;
        }

        @Override
        public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new LilTesseractBlockEntity(pos, state);
        }
    }
}

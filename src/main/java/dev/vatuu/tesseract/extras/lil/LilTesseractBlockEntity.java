package dev.vatuu.tesseract.extras.lil;

import dev.vatuu.tesseract.Tesseract;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class LilTesseractBlockEntity extends BlockEntity {

    private static BlockEntityType<LilTesseractBlockEntity> BLOCK_ENTITY_TYPE;

    public TesseractSettings settings;

    public static BlockEntityType<LilTesseractBlockEntity> getBlockEntityType() {
        if(BLOCK_ENTITY_TYPE == null) {
            BLOCK_ENTITY_TYPE = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(Tesseract.MOD_ID, "lil_tesseract"),
                    FabricBlockEntityTypeBuilder.create(LilTesseractBlockEntity::new, LilTesseractBlock.getBlockType()).build()
            );
            BlockEntityRendererRegistry.INSTANCE.register(BLOCK_ENTITY_TYPE, ctx -> new LilTesseractRenderer());
        }
        return BLOCK_ENTITY_TYPE;
    }

    public LilTesseractBlockEntity(BlockPos position, BlockState state) {
        super(getBlockEntityType(), position, state);
        this.settings = new TesseractSettings();
    }
}

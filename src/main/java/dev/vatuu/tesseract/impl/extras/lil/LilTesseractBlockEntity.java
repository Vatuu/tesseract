package dev.vatuu.tesseract.impl.extras.lil;

import dev.vatuu.tesseract.impl.Tesseract;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class LilTesseractBlockEntity extends BlockEntity {

    private static BlockEntityType<LilTesseractBlockEntity> BLOCK_ENTITY_TYPE;

    public TesseractSettings settings;

    public static BlockEntityType<LilTesseractBlockEntity> getBlockEntityType() {
        if(BLOCK_ENTITY_TYPE == null) {
            BLOCK_ENTITY_TYPE = Registry.register(
                    Registry.BLOCK_ENTITY_TYPE,
                    new Identifier(Tesseract.MOD_ID, "lil_tesseract"),
                    BlockEntityType.Builder.create(LilTesseractBlockEntity::new, LilTesseractBlock.getBlockType()).build(null)
            );
            BlockEntityRendererRegistry.INSTANCE.register(BLOCK_ENTITY_TYPE, LilTesseractRenderer::new);
        }
        return BLOCK_ENTITY_TYPE;
    }

    public LilTesseractBlockEntity() {
        super(getBlockEntityType());
        this.settings = new TesseractSettings();
    }
}

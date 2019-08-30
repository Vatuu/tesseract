package dev.vatuu.tesseract;

import dev.vatuu.tesseract.block.TesseractGatewayBlock;
import dev.vatuu.tesseract.block.entities.TesseractGatewayBlockEntity;
import dev.vatuu.tesseract.cmd.WorldResetCommand;
import dev.vatuu.tesseract.world.DimensionTypeRegistry;
import dev.vatuu.tesseract.world.TesseractDimension;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tesseract implements ModInitializer {

    public static String MOD_ID = "tesseract";

    private DimensionTypeRegistry registry;

    public DimensionType cube;

    public static Block GATEWAY_BLOCK = Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "gateway"), new TesseractGatewayBlock(Block.Settings.copy(Blocks.END_GATEWAY)));
    public static BlockEntityType GATEWAY_BE = Registry.register(Registry.BLOCK_ENTITY, new Identifier(MOD_ID, "gateway"), BlockEntityType.Builder.create(TesseractGatewayBlockEntity::new, GATEWAY_BLOCK).build(null));

    @Override
    public void onInitialize() {
        registry = new DimensionTypeRegistry();
        cube = registry.registerDimensionType("cube", true, (w, t) -> new TesseractDimension(w, t, new BlockPos(0, 64, 0)));

        ServerStartCallback.EVENT.register((ci) -> {
            WorldResetCommand.register(ci.getCommandManager().getDispatcher());
        });
    }

    public static Logger getLogger(){
        return LogManager.getLogger(MOD_ID);
    }
}

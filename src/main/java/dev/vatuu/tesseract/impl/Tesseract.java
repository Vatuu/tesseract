package dev.vatuu.tesseract.impl;

import dev.vatuu.tesseract.api.DimensionBuilder;
import dev.vatuu.tesseract.impl.cmd.WorldResetCommand;
import dev.vatuu.tesseract.impl.cmd.WorldTeleportCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensionType;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

public class Tesseract implements ModInitializer {

    @Override
    public void onInitialize() {
        DimensionType cube = FabricDimensionType.builder()
                .skyLight(true)
                .defaultPlacer((entity, serverWorld, direction, v, v1) -> new BlockPattern.TeleportTarget(new Vec3d(0, 100, 0), new Vec3d(0, 0, 0), 0))
                .factory(((world, type) -> DimensionBuilder.create().build(world, type)))
                .buildAndRegister(new Identifier("tesseract", "cube"));


        ServerStartCallback.EVENT.register((ci) -> {
            WorldResetCommand.register(ci.getCommandManager().getDispatcher());
            WorldTeleportCommand.register(ci.getCommandManager().getDispatcher());
        });
    }

    public static Vec3d getRgbColour(int red, int green, int blue) {
        return new Vec3d(red / 255d, green / 255d, blue / 255d);
    }
}

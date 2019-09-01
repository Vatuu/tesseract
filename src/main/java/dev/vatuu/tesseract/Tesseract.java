package dev.vatuu.tesseract;

import dev.vatuu.tesseract.world.DimensionBuilder;
import dev.vatuu.tesseract.cmd.WorldResetCommand;
import dev.vatuu.tesseract.world.DimensionTypeRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

public class Tesseract implements ModInitializer {

    public static DimensionTypeRegistry dimensionRegistry;

    @Override
    public void onInitialize() {
        dimensionRegistry = new DimensionTypeRegistry();
        /*DimensionType cube = dimensionRegistry.registerDimensionType(
                new Identifier("tesseract", "cube"),
                true,
                (w, t) -> new DimensionBuilder()
                    .bedsExplode(true)
                    .cloudHeight(70f)
                    .forcedSpawnPoint(new BlockPos(0, 64, 0))
                    .renderSkybox(true)
                    .renderFog(false)
                .build(w, t)
        );*/

        ServerStartCallback.EVENT.register((ci) -> {
            WorldResetCommand.register(ci.getCommandManager().getDispatcher());
        });
    }

    public static Vec3d getRgbColour(int red, int green, int blue) {
        return new Vec3d(red / 255d, green / 255d, blue / 255d);
    }
}

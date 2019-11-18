package dev.vatuu.tesseract.impl;

import dev.vatuu.tesseract.impl.cmd.WorldResetCommand;
import dev.vatuu.tesseract.impl.world.DimensionBuilderImpl;
import dev.vatuu.tesseract.impl.world.DimensionRegistryImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.BiomeAccess;
import net.minecraft.world.biome.HorizontalVoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

public class Tesseract implements ModInitializer {

    @Override
    public void onInitialize() {
        DimensionType cube = DimensionRegistryImpl.getInstance().registerDimensionType(
                new Identifier("tesseract", "cube"),
                true,
                (w, t) -> new DimensionBuilderImpl()
                    .bedsExplode(true)
                    .cloudHeight(70f)
                    .forcedSpawnPoint(new BlockPos(0, 64, 0))
                    .renderSkybox(true)
                    .renderFog(false)
                .build(w, t),
                HorizontalVoronoiBiomeAccessType.INSTANCE
        );

        ServerStartCallback.EVENT.register((ci) -> {
            WorldResetCommand.register(ci.getCommandManager().getDispatcher());
        });
    }

    public static Vec3d getRgbColour(int red, int green, int blue) {
        return new Vec3d(red / 255d, green / 255d, blue / 255d);
    }
}

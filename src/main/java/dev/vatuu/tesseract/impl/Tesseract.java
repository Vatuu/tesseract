package dev.vatuu.tesseract.impl;

import dev.vatuu.tesseract.api.DimensionBuilder;
import dev.vatuu.tesseract.api.DimensionRegistry;
import dev.vatuu.tesseract.impl.cmd.ChangeDimensionCommand;
import dev.vatuu.tesseract.impl.cmd.RegisterTestCommand;
import dev.vatuu.tesseract.impl.cmd.WorldResetCommand;
import dev.vatuu.tesseract.impl.extras.lil.LilTesseractBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.VoidBiome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.OverworldDimension;

public class Tesseract implements ModInitializer {

    public static String MOD_ID = "tesseract";

    @Override
    public void onInitialize() {

        DimensionRegistry.getInstance().registerDimensionType(
                new Identifier(MOD_ID, "telesis"),
                true,
                (w, t) -> DimensionBuilder.create().bedsExplode(true).cloudHeight(40).forcedSpawnPoint(new BlockPos(0, 64, 0)).visibleSky(true).build(w, t),
                VoronoiBiomeAccessType.INSTANCE
        );

        CommandRegistry.INSTANCE.register(false, ci -> {
            ChangeDimensionCommand.register(ci);
            RegisterTestCommand.register(ci);
            WorldResetCommand.register(ci);
        });
    }

    public static Vec3d getRgbColour(int red, int green, int blue) {
        return new Vec3d(red / 255d, green / 255d, blue / 255d);
    }
}

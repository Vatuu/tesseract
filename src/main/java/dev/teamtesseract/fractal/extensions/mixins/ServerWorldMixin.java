package dev.teamtesseract.fractal.extensions.mixins;

import dev.teamtesseract.fractal.extensions.ServerWorldExt;
import dev.teamtesseract.fractal.world.DimensionState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements ServerWorldExt {

    @Unique private DimensionState saveState;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
    }

    @Override
    public DimensionState getSaveState() {
        return saveState;
    }

    @Override
    public void setSaveState(DimensionState state) {
        this.saveState = state;
    }
}

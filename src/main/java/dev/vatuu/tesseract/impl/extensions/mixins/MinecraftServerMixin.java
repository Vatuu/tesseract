package dev.vatuu.tesseract.impl.extensions.mixins;

import dev.vatuu.tesseract.api.DimensionState;
import dev.vatuu.tesseract.impl.world.DimensionRegistryImpl;
import dev.vatuu.tesseract.impl.world.TesseractDimension;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.SecondaryServerWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.DisableableProfiler;
import net.minecraft.world.SessionLockException;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Shadow @Final private Map<DimensionType, ServerWorld> worlds;

    @Shadow @Final private DisableableProfiler profiler;
    @Mutable private WorldSaveHandler saveHandler;
    @Mutable private WorldGenerationProgressListener generationProgress;

    @Inject(method = "createWorlds", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/dimension/DimensionType;getAll()Ljava/lang/Iterable;"), cancellable = true)
    private void createWorld(WorldSaveHandler save, LevelProperties props, LevelInfo info, WorldGenerationProgressListener listener, CallbackInfo ci){
        this.saveHandler = save;
        this.generationProgress = listener;

        ci.cancel();
    }

    @Inject(method = "getWorld", at = @At("RETURN"), cancellable = true)
    private void getWorld(DimensionType dimensionType, CallbackInfoReturnable<ServerWorld> cir) {
        Validate.notNull(dimensionType, "Dimension type must not be NULL");

        if (dimensionType != DimensionType.OVERWORLD && cir.getReturnValue() == null) {
            ServerWorld overworld = worlds.get(DimensionType.OVERWORLD);
            Validate.notNull(overworld, "Overworld not loaded!");

            ServerWorld world = new SecondaryServerWorld(overworld, (MinecraftServer) (Object) this, ((MinecraftServer) (Object) this).getWorkerExecutor(), this.saveHandler, dimensionType, this.profiler, generationProgress);
            worlds.put(dimensionType, world);

            cir.setReturnValue(world);
        }
    }

    private static final ThreadLocal<Iterator<ServerWorld>> worldIteratorThreadSafe = new ThreadLocal<>();

    @Inject(method = "save(ZZZ)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;save(Lnet/minecraft/util/ProgressListener;ZZ)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void save(boolean suppressLog, boolean flushToDisk, boolean b3, CallbackInfoReturnable<Boolean> cir, boolean b4, Iterator<ServerWorld> iterator){
        worldIteratorThreadSafe.set(iterator);
    }

    @Redirect(method = "save(ZZZ)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;save(Lnet/minecraft/util/ProgressListener;ZZ)V"))
    private void save(ServerWorld world, ProgressListener listener, boolean b1, boolean b2) throws SessionLockException {
        Iterator<ServerWorld> it = worldIteratorThreadSafe.get();
        worldIteratorThreadSafe.remove();
        if (world.getDimension() instanceof TesseractDimension && it != null) {
            DimensionState state = ((TesseractDimension) world.getDimension()).getSaveState();

            if (state.shouldUnload()) unloadWorld(world, it);
            if (state.shouldReset()) resetWorld(world);
            else world.save(listener, b1, b2);
            
            if (state.shouldUnregister()) unregister(world.getDimension().getType());
        } else {
            world.save(listener, b1, b2);
        }
    }

    @Unique
    private void unloadWorld(ServerWorld world, Iterator<ServerWorld> it){
        if(!world.getPlayers().isEmpty()) {
            BlockPos entryPoint = ((TesseractDimension) world.dimension).entryPoint;

            world.getPlayers().forEach(player -> player.teleport(
                    world.getServer().getWorld(DimensionType.OVERWORLD),
                    entryPoint.getX(),
                    entryPoint.getY(),
                    entryPoint.getZ(),
                    player.pitch, player.yaw
            ));
        }

        try {
            world.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        it.remove();
    }

    @Unique
    private void resetWorld(ServerWorld world){
        world.savingDisabled = true;

        try {
            File worldSaveDirectory = world.dimension.getType().getSaveDirectory(world.getSaveHandler().getWorldDir());
            FileUtils.deleteDirectory(worldSaveDirectory);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Unique
    private void unregister(DimensionType type){
        DimensionRegistryImpl.getInstance().unregister(type);
    }
}

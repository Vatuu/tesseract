package dev.vatuu.tesseract.extensions.mixins;

import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.world.DimensionTypeRegistry;
import dev.vatuu.tesseract.world.TesseractDimension;
import dev.vatuu.tesseract.world.TesseractDimensionType;
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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    private WorldSaveHandler saveHandler;
    private WorldGenerationProgressListener generationProgress;

    @Inject(method = "createWorlds", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/world/dimension/DimensionType;getAll()Ljava/lang/Iterable;"), cancellable = true)
    private void createWorld(WorldSaveHandler save, LevelProperties props, LevelInfo info, WorldGenerationProgressListener listener, CallbackInfo ci){
        this.saveHandler = save;
        this.generationProgress = listener;
        ci.cancel();
    }

    @Inject(method = "getWorld", at = @At("RETURN"), cancellable = true)
    private void getWorld(DimensionType dimensionType, CallbackInfoReturnable<ServerWorld> cir) {
        System.out.println(dimensionType);
        Validate.notNull(dimensionType, "Dimension type must not be NULL");
        if(dimensionType != DimensionType.OVERWORLD && cir.getReturnValue() == null) {
            ServerWorld overWorld = worlds.get(DimensionType.OVERWORLD);
            Validate.notNull(overWorld, "Overworld not loaded!");
            Tesseract.getLogger().debug("Loading dimension {}", DimensionType.getId(dimensionType));
            ServerWorld world = new SecondaryServerWorld(overWorld, (MinecraftServer)(Object)this, ((MinecraftServer)(Object)this).getWorkerExecutor(), this.saveHandler, dimensionType, this.profiler, generationProgress);
            worlds.put(dimensionType, world);
            cir.setReturnValue(world);
        }
    }

    private static final ThreadLocal<Iterator<ServerWorld>> apcraftvolvic = new ThreadLocal<>();

    @Inject(method = "save(ZZZ)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;save(Lnet/minecraft/util/ProgressListener;ZZ)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void save(boolean suppressLog, boolean flushToDisk, boolean b3, CallbackInfoReturnable<Boolean> cir, boolean b4, Iterator<ServerWorld> iterator){
        apcraftvolvic.set(iterator);
    }

    @Redirect(method = "save(ZZZ)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;save(Lnet/minecraft/util/ProgressListener;ZZ)V"))
    private void save(ServerWorld w, ProgressListener listener, boolean b1, boolean b2) throws SessionLockException{
        Iterator<ServerWorld> it = apcraftvolvic.get();
        if(w.getDimension().getType() instanceof TesseractDimensionType && it != null){
            if(!((TesseractDimension)w.dimension).shouldSave())
                unloadWorld(w, ((TesseractDimension)w.dimension).shouldUnregister(), it);
        }else{
            w.save(listener, b1, b2);
        }
    }

    private void unloadWorld(ServerWorld w, boolean unregister, Iterator<ServerWorld> it){
        if(!w.getPlayers().isEmpty()){
            BlockPos entryPoint = ((TesseractDimension)w.dimension).entryPoint;
            w.getPlayers().forEach((p) ->  p.teleport(
                    w.getServer().getWorld(DimensionType.OVERWORLD),
                    entryPoint.getX(),
                    entryPoint.getY(),
                    entryPoint.getZ(),
                    p.pitch, p.yaw));
        }
        w.savingDisabled = true;

        try{
            w.close();
            File f = w.dimension.getType().getFile(w.getSaveHandler().getWorldDir());
            FileUtils.deleteDirectory(f);
        }catch(IOException e){
            e.printStackTrace();
        }

        it.remove();
        if(unregister)
            DimensionTypeRegistry.unregister(w.dimension.getType());
    }
}

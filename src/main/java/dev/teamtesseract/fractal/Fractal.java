package dev.teamtesseract.fractal;

import dev.teamtesseract.fractal.cmd.CreateTestWorldCommand;
import dev.teamtesseract.fractal.cmd.RegenerateWorldCommand;
import dev.teamtesseract.fractal.lil.LilTesseractBlockEntity;
import dev.teamtesseract.fractal.network.NetworkHandler;
import dev.teamtesseract.fractal.registry.FractalManagement;
import dev.teamtesseract.fractal.registry.FractalRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

public class Fractal implements ModInitializer {

    public static final String MOD_ID = "fractal";

    public static Fractal INSTANCE;

    public static BlockEntityType<LilTesseractBlockEntity> LIL_TESSERACT_BE;

    private NetworkHandler networkHandler;

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;

        if(FabricLoader.getInstance().isDevelopmentEnvironment())
            CommandRegistrationCallback.EVENT.register((dis, b) ->  {
                CreateTestWorldCommand.register(dis);
                RegenerateWorldCommand.register(dis);
            });

        ServerLifecycleEvents.SERVER_STARTED.register(t -> {
            FractalRegistry.setInstance(t.getNetworkIo().getServer());
            FractalManagement.setInstance(t.getNetworkIo().getServer());
        });

        this.networkHandler = new NetworkHandler();

        LIL_TESSERACT_BE = LilTesseractBlockEntity.getBlockEntityType();
    }

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }
}

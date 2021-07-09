package dev.teamtesseract.fractal.client;

import dev.teamtesseract.fractal.client.network.ClientNetworkHandler;
import dev.teamtesseract.fractal.lil.LilTesseractBlockEntity;
import dev.teamtesseract.fractal.Fractal;
import dev.teamtesseract.fractal.client.skybox.EndSkyboxRenderer;
import dev.teamtesseract.fractal.client.world.SkyPropertiesBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.Vec3d;

public class FractalClient implements ClientModInitializer {

    public static FractalClient INSTANCE;

    private ClientNetworkHandler networkHandler;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        this.networkHandler = new ClientNetworkHandler();

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ClientLifecycleEvents.CLIENT_STARTED.register(t ->
                    SkyPropertiesBuilder.create()
                    .setFogColorFunction((a, b) -> new Vec3d(0, 0, 0))
                    .hasAlternateSkyColour(false)
                    .hasBrightLightning(true)
                    .setCloudHeight(120)
                    .setFogThickFunction((a, b) -> false)
                    .isDarkened(false)
                    .setSkybox(new EndSkyboxRenderer())
                    .register(Fractal.id("test")));
        }

        LilTesseractBlockEntity.registerRenderer();
    }

    public ClientNetworkHandler getNetworkHandler() {
        return networkHandler;
    }
}

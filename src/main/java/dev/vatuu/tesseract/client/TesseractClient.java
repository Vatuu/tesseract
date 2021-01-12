package dev.vatuu.tesseract.client;

import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.client.network.ClientNetworkHandler;
import dev.vatuu.tesseract.client.skybox.EndSkyboxRenderer;
import dev.vatuu.tesseract.client.world.SkyPropertiesBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class TesseractClient implements ClientModInitializer {

    public static TesseractClient INSTANCE;

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
                    .register(Tesseract.id("test")));
        }
    }

    public ClientNetworkHandler getNetworkHandler() {
        return networkHandler;
    }
}

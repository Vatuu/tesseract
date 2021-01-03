package dev.vatuu.tesseract.client;

import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.client.network.ClientNetworkHandler;
import dev.vatuu.tesseract.client.skybox.EndSkyboxRenderer;
import dev.vatuu.tesseract.client.skybox.MissingSkyboxRenderer;
import dev.vatuu.tesseract.client.skybox.OverworldSkyboxRenderer;
import dev.vatuu.tesseract.client.world.SkyPropertiesBuilder;
import dev.vatuu.tesseract.client.world.TesseractSkyProperties;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;

public class TesseractClient implements ClientModInitializer {

    public static TesseractClient INSTANCE;

    private ClientNetworkHandler networkHandler;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        this.networkHandler = new ClientNetworkHandler();

         RegistryKey<TesseractSkyProperties> skyProperties = SkyPropertiesBuilder.create()
                .hasAlternateSkyColour(true)
                .hasBrightLightning(false)
                .isDarkened(false)
                .setCloudHeight(Float.NaN)
                .setSkybox(new MissingSkyboxRenderer())
                .setFogColorFunction((c, s) -> new Vec3d(0, 0, 0))
                .register(Tesseract.id("telesis_sky"));
    }

    public ClientNetworkHandler getNetworkHandler() {
        return networkHandler;
    }
}

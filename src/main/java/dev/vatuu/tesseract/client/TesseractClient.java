package dev.vatuu.tesseract.client;

import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.client.network.ClientNetworkHandler;
import dev.vatuu.tesseract.client.skybox.EndSkyboxRenderer;
import dev.vatuu.tesseract.client.world.SkyPropertiesBuilder;
import dev.vatuu.tesseract.client.world.TesseractSkyProperties;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.registry.RegistryKey;

public class TesseractClient implements ClientModInitializer {

    public static TesseractClient INSTANCE;

    private ClientNetworkHandler networkHandler;

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        this.networkHandler = new ClientNetworkHandler();

         RegistryKey<TesseractSkyProperties> skyProperties = SkyPropertiesBuilder.create()
                .hasAlternateSkyColour(false)
                .hasBrightLightning(true)
                .isDarkened(false)
                .setCloudHeight(Float.NaN)
                .setSkybox(new EndSkyboxRenderer())
                .register(Tesseract.id("telesis_sky"));
    }

    public ClientNetworkHandler getNetworkHandler() {
        return networkHandler;
    }
}

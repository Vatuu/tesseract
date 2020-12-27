package dev.vatuu.tesseract.client.extensions.mixins;

import dev.vatuu.tesseract.client.SkyPropertiesRegistry;
import dev.vatuu.tesseract.client.extensions.ClientWorldExtension;
import dev.vatuu.tesseract.client.world.TesseractSkyProperties;
import net.minecraft.client.render.SkyProperties;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;

import java.util.Objects;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin implements ClientWorldExtension {

    @Mutable @Shadow @Final private SkyProperties skyProperties;

    @Unique
    public void setSkybox(Identifier id) {
        if(id != null) {
            TesseractSkyProperties props = SkyPropertiesRegistry.REGISTRY.get(id);
            if(props != null)
                this.skyProperties = props;
            else
                this.skyProperties = TesseractSkyProperties.THE_END;
        }
    }
}

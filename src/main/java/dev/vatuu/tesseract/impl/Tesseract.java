package dev.vatuu.tesseract.impl;

import dev.vatuu.tesseract.impl.cmd.ChangeDimensionCommand;
import dev.vatuu.tesseract.impl.cmd.RegisterTestCommand;
import dev.vatuu.tesseract.impl.cmd.WorldResetCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.util.math.Vec3d;

public class Tesseract implements ModInitializer {

    public static String MOD_ID = "tesseract";

    @Override
    public void onInitialize() {
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

package dev.teamtesseract.fractal.lil;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.teamtesseract.fractal.client.rendering.Vec4f;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;

public final class LilTesseractSettings {

    public Vec4f rotations;
    public boolean isWireframe;

    public LilTesseractSettings() {
        this(Vec4f.ZERO, false);
    }

    public LilTesseractSettings(Vec4f rotations, boolean wireframe) {
        this.rotations = rotations;
        this.isWireframe = wireframe;
    }

    public NbtElement toNbt() {
        return NbtOps.INSTANCE.withEncoder(CODEC).apply(this).get().left().orElse(new NbtCompound());
    }

    private static final Codec<LilTesseractSettings> CODEC = RecordCodecBuilder.create(i -> i.group(
                Vec4f.CODEC.fieldOf("rotations").forGetter((LilTesseractSettings s) -> s.rotations),
                Codec.BOOL.fieldOf("wireframe").forGetter((LilTesseractSettings s) -> s.isWireframe))
            .apply(i, LilTesseractSettings::new));
}

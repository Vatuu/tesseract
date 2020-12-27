package dev.vatuu.tesseract.world;

import dev.vatuu.tesseract.Tesseract;
import dev.vatuu.tesseract.registry.TesseractRegistry;
import dev.vatuu.tesseract.registry.TesseractRegistryException;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.source.BiomeAccessType;
import net.minecraft.world.biome.source.VoronoiBiomeAccessType;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

public class DimensionTypeBuilder {

    private final Identifier name;
    private final BiomeAccessType biomeAccessType;

    private boolean ultrawarm = false, natural = true, skylight = true, ceiling = false, piglinSafe = false, bedsExplode = false, respawnAnchorsExplode = false, beesExplode = false, raids = false;
    private int logicalHeight = 256, minY = 0, height = 256;
    private OptionalLong fixedTime = OptionalLong.empty();
    private float coordinateScale = 1, ambientLight = 0;
    private Identifier skyPropertiesKey = Tesseract.id("none");


    private DimensionTypeBuilder(Identifier id, BiomeAccessType biomeAccess) {
        this.name = id;
        this.biomeAccessType = biomeAccess;
    }

    public static DimensionTypeBuilder create(Identifier id) {
        return create(id, VoronoiBiomeAccessType.INSTANCE);
    }

    public static DimensionTypeBuilder create(Identifier id, BiomeAccessType accessType) {
        return new DimensionTypeBuilder(id, accessType);
    }

    public DimensionTypeBuilder isUltrawarm(boolean is) {
        this.ultrawarm = is;
        return this;
    }

    public DimensionTypeBuilder isNatural(boolean is) {
        this.natural = is;
        return this;
    }

    public DimensionTypeBuilder hasSkylight(boolean has) {
        this.skylight = has;
        return this;
    }

    public DimensionTypeBuilder hasCeiling(boolean has) {
        this.ceiling = has;
        return this;
    }

    public DimensionTypeBuilder isPiglinSafe(boolean is) {
        this.piglinSafe = is;
        return this;
    }

    public DimensionTypeBuilder doBedsExplode(boolean is) {
        this.bedsExplode = is;
        return this;
    }

    public DimensionTypeBuilder doBeesExplode(boolean is) {
        this.beesExplode = is;
        return this;
    }

    public DimensionTypeBuilder doRespawnAnchorsExplode(boolean is) {
        this.respawnAnchorsExplode = is;
        return this;
    }

    public DimensionTypeBuilder hasRaids(boolean has) {
        this.raids = has;
        return this;
    }

    public DimensionTypeBuilder setLogicalHeight(int height) {
        this.logicalHeight = height;
        return this;
    }

    public DimensionTypeBuilder setMinimumY(int y) {
        this.minY = y;
        return this;
    }

    public DimensionTypeBuilder setBlockHeight(int height) {
        this.height = height;
        return this;
    }

    public DimensionTypeBuilder setFixedTime(long time) {
        this.fixedTime = OptionalLong.of(time);
        return this;
    }

    public DimensionTypeBuilder setCoordinateScaling(float factor) {
        this.coordinateScale = factor;
        return this;
    }

    public DimensionTypeBuilder setAmbientLightLevel(float light) {
        this.ambientLight = light;
        return this;
    }

    public DimensionTypeBuilder setSkyProperties(Identifier id) {
        this.skyPropertiesKey = id;
        return this;
    }

    public DimensionType build() {
        return DimensionType.method_32922(
                fixedTime, skylight, ceiling, ultrawarm, natural,
                coordinateScale, false, piglinSafe, !bedsExplode, respawnAnchorsExplode,
                raids,  minY, height, logicalHeight, biomeAccessType, Tesseract.id("none"), DimensionType.OVERWORLD_ID, ambientLight);
    }

    public RegistryKey<DimensionType> register() throws TesseractRegistryException {
        return TesseractRegistry.getInstance().registerDimension(build(), name, skyPropertiesKey);
    }
}

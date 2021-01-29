package dev.vatuu.tesseract.extensions;

import net.minecraft.util.registry.RegistryKey;

public interface SimpleRegistryExt<T> {

    void remove(RegistryKey<T> key);
}

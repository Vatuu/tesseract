package dev.teamtesseract.fractal.extensions.mixins;

import com.google.common.collect.BiMap;
import com.mojang.serialization.Lifecycle;
import dev.teamtesseract.fractal.extensions.SimpleRegistryExt;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleRegistry.class)
public abstract class SimpleRegistryMixin<T> extends MutableRegistry<T> implements SimpleRegistryExt<T> {

    @Shadow @Final private BiMap<RegistryKey<T>, T> keyToEntry;
    @Shadow @Final private Object2IntMap<T> entryToRawId;
    @Shadow @Final private ObjectList<T> rawIdToEntry;
    @Shadow private int nextId;

    @Shadow @Final private BiMap<Identifier, T> idToEntry;

    @Shadow protected Object[] randomEntries;

    public SimpleRegistryMixin(RegistryKey<? extends Registry<T>> registryKey, Lifecycle lifecycle) { super(registryKey, lifecycle); }

    @Override
    public void remove(RegistryKey<T> key) {
        try {
            T object = keyToEntry.get(key);
            if (object == null)
                return;

            int rawId = entryToRawId.getInt(object);
            this.nextId--;

            T last = rawIdToEntry.remove(this.nextId);
            entryToRawId.removeInt(object);

            if(rawId != this.nextId) {
                rawIdToEntry.set(rawId, last);
                rawIdToEntry.size(rawIdToEntry.size());
                entryToRawId.replace(last, rawId);
            }

            keyToEntry.remove(key);
            idToEntry.remove(key.getValue());

            randomEntries = null;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

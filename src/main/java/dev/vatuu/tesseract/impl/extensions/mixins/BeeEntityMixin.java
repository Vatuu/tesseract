package dev.vatuu.tesseract.impl.extensions.mixins;

import dev.vatuu.tesseract.impl.world.TesseractDimension;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeEntity.class)
public abstract class BeeEntityMixin extends AnimalEntity {

    public BeeEntityMixin(EntityType<? extends AnimalEntity> type, World world){
        super(type, world);
    }

    //BeeGoBoom
    @Inject(method = "damage", at = @At(value = "RETURN"))
    public void damage(DamageSource src, float amount, CallbackInfoReturnable<Boolean> info){
        if(amount > 0 && src instanceof EntityDamageSource)
            if(((TesseractDimension)this.getEntityWorld().dimension).getSettings().isBeesExplode())
                this.kill();
                this.getEntityWorld().createExplosion(this, this.getPos().x, this.getPos().y, this.getPos().z, 2.3F, true, Explosion.DestructionType.BREAK);
    }
}

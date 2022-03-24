package com.infinityraider.ninjagear.potion;

import com.infinityraider.infinitylib.modules.synchronizedeffects.ISynchronizedEffect;
import com.infinityraider.infinitylib.potion.PotionEffectBase;
import com.infinityraider.ninjagear.reference.Names;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class PotionEffectNinjaHidden extends PotionEffectBase implements ISynchronizedEffect {
    public PotionEffectNinjaHidden() {
        super(Names.Effects.NINJA_HIDDEN, MobEffectCategory.BENEFICIAL, new Color(0, 0, 0).getRGB());
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplification) {
        entity.setInvisible(true);
    }

    //TODO
    /*
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderHUD(EffectInstance effect) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRender(EffectInstance effect) {
        return true;
    }
    */
}

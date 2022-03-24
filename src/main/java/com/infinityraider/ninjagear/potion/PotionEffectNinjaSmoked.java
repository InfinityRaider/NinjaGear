package com.infinityraider.ninjagear.potion;

import com.infinityraider.infinitylib.modules.synchronizedeffects.ISynchronizedEffect;
import com.infinityraider.infinitylib.potion.PotionEffectBase;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class PotionEffectNinjaSmoked extends PotionEffectBase implements ISynchronizedEffect {
    public PotionEffectNinjaSmoked()  {
        super(Names.Effects.NINJA_SMOKED, MobEffectCategory.NEUTRAL, new Color(0, 0, 0).getRGB());
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplification) {
        if(entity.hasEffect(EffectRegistry.effectNinjaRevealed)) {
            entity.removeEffect(EffectRegistry.effectNinjaRevealed);
        }
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

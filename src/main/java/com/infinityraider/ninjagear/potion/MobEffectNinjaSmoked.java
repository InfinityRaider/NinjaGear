package com.infinityraider.ninjagear.potion;

import com.infinityraider.infinitylib.modules.synchronizedeffects.ISynchronizedEffect;
import com.infinityraider.infinitylib.potion.MobEffectBase;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import java.awt.*;

public class MobEffectNinjaSmoked extends MobEffectBase implements ISynchronizedEffect {
    public MobEffectNinjaSmoked()  {
        super(Names.Effects.NINJA_SMOKED, MobEffectCategory.NEUTRAL, new Color(0, 0, 0).getRGB());
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplification) {
        if(entity.hasEffect(EffectRegistry.getInstance().getNinjaRevealedEffect())) {
            entity.removeEffect(EffectRegistry.getInstance().getNinjaRevealedEffect());
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

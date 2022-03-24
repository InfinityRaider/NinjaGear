package com.infinityraider.ninjagear.registry;

import com.infinityraider.ninjagear.potion.*;
import net.minecraft.world.effect.MobEffect;

public class EffectRegistry {
    private static final EffectRegistry INSTANCE = new EffectRegistry();

    public static EffectRegistry getInstance() {
        return INSTANCE;
    }

    public MobEffect effectNinjaHidden = new PotionEffectNinjaHidden();
    public MobEffect effectNinjaRevealed = new PotionEffectNinjaRevealed();
    public MobEffect effectNinjaSmoked = new PotionEffectNinjaSmoked();

    private EffectRegistry() {}
}

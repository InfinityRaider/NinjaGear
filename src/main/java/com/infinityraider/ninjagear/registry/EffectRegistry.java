package com.infinityraider.ninjagear.registry;

import com.infinityraider.ninjagear.potion.*;
import net.minecraft.world.effect.MobEffect;

public final class EffectRegistry {
    public static final MobEffect effectNinjaHidden = new PotionEffectNinjaHidden();
    public static final MobEffect effectNinjaRevealed = new PotionEffectNinjaRevealed();
    public static final MobEffect effectNinjaSmoked = new PotionEffectNinjaSmoked();
}

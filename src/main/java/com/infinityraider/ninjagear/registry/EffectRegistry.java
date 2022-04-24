package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.utility.registration.ModContentRegistry;
import com.infinityraider.infinitylib.utility.registration.RegistryInitializer;
import com.infinityraider.ninjagear.potion.*;

public final class EffectRegistry extends ModContentRegistry {
    private static final EffectRegistry INSTANCE = new EffectRegistry();

    public static EffectRegistry getInstance() {
        return INSTANCE;
    }

    private final RegistryInitializer<PotionEffectNinjaHidden> ninjaHidden;
    private final RegistryInitializer<PotionEffectNinjaRevealed> ninjaRevealed;
    private final RegistryInitializer<PotionEffectNinjaSmoked> ninjaSmoked;

    private EffectRegistry() {
        this.ninjaHidden = this.mobEffect(PotionEffectNinjaHidden::new);
        this.ninjaRevealed = this.mobEffect(PotionEffectNinjaRevealed::new);
        this.ninjaSmoked = this.mobEffect(PotionEffectNinjaSmoked::new);
    }

    public PotionEffectNinjaHidden getNinjaHiddenEffect() {
        return this.ninjaHidden.get();
    }

    public PotionEffectNinjaRevealed getNinjaRevealedEffect() {
        return this.ninjaRevealed.get();
    }

    public PotionEffectNinjaSmoked getNinjaSmokedEffect() {
        return this.ninjaSmoked.get();
    }
}

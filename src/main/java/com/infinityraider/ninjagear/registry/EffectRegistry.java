package com.infinityraider.ninjagear.registry;

import com.infinityraider.infinitylib.utility.registration.ModContentRegistry;
import com.infinityraider.infinitylib.utility.registration.RegistryInitializer;
import com.infinityraider.ninjagear.potion.*;

public final class EffectRegistry extends ModContentRegistry {
    private static final EffectRegistry INSTANCE = new EffectRegistry();

    public static EffectRegistry getInstance() {
        return INSTANCE;
    }

    private final RegistryInitializer<MobEffectNinjaHidden> ninjaHidden;
    private final RegistryInitializer<MobEffectNinjaRevealed> ninjaRevealed;
    private final RegistryInitializer<MobEffectNinjaSmoked> ninjaSmoked;

    private EffectRegistry() {
        this.ninjaHidden = this.mobEffect(MobEffectNinjaHidden::new);
        this.ninjaRevealed = this.mobEffect(MobEffectNinjaRevealed::new);
        this.ninjaSmoked = this.mobEffect(MobEffectNinjaSmoked::new);
    }

    public MobEffectNinjaHidden getNinjaHiddenEffect() {
        return this.ninjaHidden.get();
    }

    public MobEffectNinjaRevealed getNinjaRevealedEffect() {
        return this.ninjaRevealed.get();
    }

    public MobEffectNinjaSmoked getNinjaSmokedEffect() {
        return this.ninjaSmoked.get();
    }
}

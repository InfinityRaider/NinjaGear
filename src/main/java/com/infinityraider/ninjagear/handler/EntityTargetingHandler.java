package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.api.v1.IEntityTrueSight;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityTargetingHandler {
    private static final EntityTargetingHandler INSTANCE = new EntityTargetingHandler();

    public static EntityTargetingHandler getInstance() {
        return INSTANCE;
    }

    private EntityTargetingHandler() {}

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onEntityTargetingEvent(LivingSetAttackTargetEvent event) {
        LivingEntity target = event.getTarget();
        LivingEntity attacker = event.getEntityLiving();
        if(target == null || attacker == null || !(target instanceof Player)) {
            return;
        }
        if(target.hasEffect(EffectRegistry.effectNinjaHidden)) {
            if(attacker instanceof IEntityTrueSight && ((IEntityTrueSight) attacker).canSeeTarget((Player) target)) {
                return;
            }
            if (attacker instanceof Mob) {
                Mob mob = (Mob) attacker;
                mob.setAggressive(false);
                mob.setTarget(null);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onSummonAidEvent(ZombieEvent.SummonAidEvent event) {
        LivingEntity attacker = event.getAttacker();
        if(attacker instanceof Player) {
            Player player = (Player) attacker;
            if(player.hasEffect(EffectRegistry.effectNinjaHidden)) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}

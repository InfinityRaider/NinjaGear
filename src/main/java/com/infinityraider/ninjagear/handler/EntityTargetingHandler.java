package com.infinityraider.ninjagear.handler;

import com.infinityraider.ninjagear.api.v1.IEntityTrueSight;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
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
        if(target == null || attacker == null || !(target instanceof PlayerEntity)) {
            return;
        }
        if(target.isPotionActive(EffectRegistry.getInstance().effectNinjaHidden)) {
            if(attacker instanceof IEntityTrueSight && ((IEntityTrueSight) attacker).canSeeTarget((PlayerEntity) target)) {
                return;
            }
            if (attacker instanceof MobEntity) {
                MobEntity mob = (MobEntity) attacker;
                mob.setAggroed(false);
                mob.setAttackTarget(null);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SuppressWarnings("unused")
    public void onSummonAidEvent(ZombieEvent.SummonAidEvent event) {
        LivingEntity attacker = event.getAttacker();
        if(attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;
            if(player.isPotionActive(EffectRegistry.getInstance().effectNinjaHidden)) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}

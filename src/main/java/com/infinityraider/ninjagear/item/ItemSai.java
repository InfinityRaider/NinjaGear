package com.infinityraider.ninjagear.item;

import com.google.common.collect.ImmutableMultimap;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.api.v1.IHiddenItem;
import com.infinityraider.ninjagear.handler.NinjaAuraHandler;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import com.infinityraider.ninjagear.registry.EffectRegistry;
import com.google.common.collect.Multimap;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemSai extends ItemBase implements IHiddenItem {
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final Multimap<Attribute, AttributeModifier> attributeModifiersCrit;
    private final Multimap<Attribute, AttributeModifier> attributeModifiersIneffective;

    public ItemSai() {
        super("sai", new Properties()
                .durability(1000)
                .tab(ItemRegistry.CREATIVE_TAB));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.attributeModifiers = builder
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Sai Attack Damage Modifier",
                        3.0 + NinjaGear.instance.getConfig().getSaiDamage(), AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Sai Attack Speed Modifier",
                        1, AttributeModifier.Operation.ADDITION))
                .build();
        builder = ImmutableMultimap.builder();
        this.attributeModifiersCrit = builder
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Sai Attack Damage Modifier",
                        3.0 +  NinjaGear.instance.getConfig().getCitMultiplier()*NinjaGear.instance.getConfig().getSaiDamage(),
                        AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Sai Attack Speed Modifier",
                        1, AttributeModifier.Operation.ADDITION))
                .build();
        builder = ImmutableMultimap.builder();
        this.attributeModifiersIneffective = builder
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Sai Attack Damage Modifier",
                        3.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Sai Attack Speed Modifier",
                        -2, AttributeModifier.Operation.ADDITION))
                .build();
    }

    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            return material != Material.PLANT
                    && material != Material.REPLACEABLE_PLANT
                    && !state.is(BlockTags.LEAVES)
                    && material != Material.VEGETABLE
                    ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(attacker instanceof Player) {
            NinjaAuraHandler.getInstance().revealEntity((Player) attacker, NinjaGear.instance.getConfig().getHidingCooldown(), true);
        }
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        ItemStack offhand = attacker.getItemBySlot(EquipmentSlot.OFFHAND);
        if(offhand != null && offhand.getItem() instanceof ItemSai) {
            offhand.hurtAndBreak(1, attacker, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entity) {
        if ((double)state.getDestroySpeed(world, pos) != 0.0D) {
            stack.hurtAndBreak(1, entity, (e) -> {
                e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
            ItemStack offhand = entity.getItemBySlot(EquipmentSlot.OFFHAND);
            if(offhand != null && offhand.getItem() instanceof ItemSai) {
                offhand.hurtAndBreak(1, entity, (e) -> {
                    e.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });
            }
        }
        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockIn) {
        return blockIn.getBlock() == Blocks.COBWEB;
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return getRepairItem().test(repair);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        ItemStack offHand = player.getItemBySlot(EquipmentSlot.OFFHAND);
        if(offHand.getItem() == this) {
            if(player.hasEffect(EffectRegistry.getInstance().effectNinjaHidden)) {
                player.getAttributes().addTransientAttributeModifiers(this.attributeModifiersCrit);
            } else {
                player.getAttributes().addTransientAttributeModifiers(this.attributeModifiers);
            }
        } else {
            player.getAttributes().addTransientAttributeModifiers(this.attributeModifiersIneffective);
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            return this.attributeModifiers;
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L1"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L2"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L3"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:" + this.getInternalName() + "_L4"));
    }

    @Override
    public boolean shouldRevealPlayerWhenEquipped(Player entity, ItemStack stack) {
        return false;
    }

    private static Ingredient repairItem;

    public static Ingredient getRepairItem() {
        if(repairItem == null) {
            repairItem = ItemKatana.getRepairItem();
        }
        return repairItem;
    }
}

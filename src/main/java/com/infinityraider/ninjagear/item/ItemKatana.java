package com.infinityraider.ninjagear.item;

import com.google.common.collect.ImmutableMultimap;
import com.infinityraider.infinitylib.crafting.fallback.FallbackIngredient;
import com.infinityraider.ninjagear.NinjaGear;
import com.infinityraider.ninjagear.reference.Names;
import com.infinityraider.ninjagear.reference.Reference;
import com.infinityraider.infinitylib.item.ItemBase;
import com.google.common.collect.Multimap;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemKatana extends ItemBase {
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final Multimap<Attribute, AttributeModifier> attributeModifiersIneffective;

    public ItemKatana() {
        super(Names.Items.KATANA, new Properties()
                .durability(1000)
                .tab(ItemRegistry.CREATIVE_TAB)
        );
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.attributeModifiers = builder
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Katana Attack Damage Modifier",
                        3.0 + NinjaGear.instance.getConfig().getKatanaDamage(), AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Katana Attack Speed Modifier",
                        1, AttributeModifier.Operation.ADDITION)).build();
        builder = ImmutableMultimap.builder();
        this.attributeModifiersIneffective = builder
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Katana Attack Damage modifier",
                        3.0, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Katana Attack Speed Modifier",
                        -2, AttributeModifier.Operation.ADDITION))
                .build();
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
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity entityLiving) {
            if (state.getDestroySpeed(world, pos) != 0.0F) {
                stack.hurtAndBreak(2, entityLiving, (entity) -> {
                    entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
                });
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
        ItemStack offhand = player.getItemBySlot(EquipmentSlot.OFFHAND);
        if(offhand.isEmpty()) {
            player.getAttributes().addTransientAttributeModifiers(this.attributeModifiers);
        } else {
            player.getAttributes().addTransientAttributeModifiers(this.attributeModifiersIneffective);
        }
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        } else if(slot == EquipmentSlot.OFFHAND) {
            return this.attributeModifiersIneffective;
        } else {
            return super.getAttributeModifiers(slot, stack);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L1"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L2"));
        tooltip.add(new TranslatableComponent(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L3"));
    }

    private static Ingredient repairItem;

    public static Ingredient getRepairItem() {
        if(repairItem == null) {
            repairItem = new FallbackIngredient(
                    ForgeRegistries.ITEMS.tags().getTag(ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge", "ingots/steel"))),
                    Ingredient.of(ForgeRegistries.ITEMS.tags().createTagKey(new ResourceLocation("forge", "ingots/iron")))
            );
        }
        return repairItem;
    }
}

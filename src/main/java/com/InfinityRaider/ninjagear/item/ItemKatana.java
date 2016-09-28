package com.infinityraider.ninjagear.item;

import com.infinityraider.infinitylib.item.IItemWithModel;
import com.infinityraider.infinitylib.item.IItemWithRecipe;
import com.infinityraider.infinitylib.item.ItemBase;
import com.infinityraider.ninjagear.handler.ConfigurationHandler;
import com.infinityraider.ninjagear.reference.Reference;
import com.google.common.collect.Multimap;
import com.infinityraider.ninjagear.registry.ItemRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@MethodsReturnNonnullByDefault
public class ItemKatana extends ItemBase implements IItemWithRecipe, IItemWithModel {
    private ItemStack repairItem;

    public ItemKatana() {
        super("katana");
        this.setMaxDamage(1000);
        this.setMaxStackSize(1);
        this.setCreativeTab(ItemRegistry.CREATIVE_TAB);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.WEB) {
            return 15.0F;
        } else {
            Material material = state.getMaterial();
            return material != Material.PLANTS
                    && material != Material.VINE
                    && material != Material.CORAL
                    && material != Material.LEAVES
                    && material != Material.GOURD
                    ? 1.0F : 1.5F;
        }
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {
        if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(2, entityLiving);
        }
        return true;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return blockIn.getBlock() == Blocks.WEB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 15;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = this.getRepairItemStack();
        return mat != null
                && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)
                || super.getIsRepairable(toRepair, repair);
    }

    public ItemStack getRepairItemStack() {
        if(this.repairItem == null) {
            List<ItemStack> steel = OreDictionary.getOres("ingotSteel");
            if (steel.size() > 0) {
                this.repairItem = steel.get(0);
            } else {
                this.repairItem = new ItemStack(Items.IRON_INGOT);
            }
        }
        return this.repairItem;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if(player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND) != null) {
            player.getAttributeMap().applyAttributeModifiers(getAttributeModifiers(EntityEquipmentSlot.MAINHAND, stack, false));
        } else {
            player.getAttributeMap().applyAttributeModifiers(getAttributeModifiers(EntityEquipmentSlot.MAINHAND, stack, true));
        }
        return false;
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack, boolean effective) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(),
                    new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier",  3.0 + (effective ? ConfigurationHandler.getInstance().katanaDamage : 0), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName(),
                    new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (effective ? 1 : -2), 0));
        }
        return multimap;
    }

    @Override
    @ParametersAreNonnullByDefault
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return  getAttributeModifiers(slot, stack, true);
    }

    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L1"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L2"));
        tooltip.add(I18n.translateToLocal(Reference.MOD_ID + ".tooltip:"+this.getInternalName() + "_L3"));
    }

    @Override
    public List<IRecipe> getRecipes() {
        List<IRecipe> list = new ArrayList<>();
        String steel = OreDictionary.doesOreNameExist("ingotSteel") ? "ingotSteel" : "ingotIron";
        list.add(new ShapedOreRecipe(this," i ", "gsg", "wiw",
                'i', steel,
                'g', "ingotGold",
                's', Items.IRON_SWORD,
                'w', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)));
        return list;
    }

    @Override
    public List<String> getOreTags() {
        return Collections.emptyList();
    }

    @Override
    public List<Tuple<Integer, ModelResourceLocation>> getModelDefinitions() {
        List<Tuple<Integer, ModelResourceLocation>> list = new ArrayList<>();
        list.add(new Tuple<>(0, new ModelResourceLocation(Reference.MOD_ID.toLowerCase() + ":" + this.getInternalName(), "inventory")));
        return list;
    }
}

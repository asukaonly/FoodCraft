package net.infstudio.foodcraftreloaded.item.food;

import net.infstudio.foodcraftreloaded.FoodCraftReloaded;
import net.infstudio.foodcraftreloaded.init.FCRCreativeTabs;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

public class ItemFruits extends FCRItemFood {
    public ItemFruits() {
        super(1, 1.0f, false);
        setHasSubtypes(true);
        setAlwaysEdible(true);
        setMaxDamage(0);
        setRegistryName(FoodCraftReloaded.MODID, "fruit");
        setCreativeTab(FCRCreativeTabs.INGREDIENTS);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return I18n.format("item.fruit" + StringUtils.capitalize(EnumFruitType.values()[stack.getMetadata()].toString()));
    }

    @Override
    public void getSubItems(@Nonnull Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (EnumFruitType fruitType : EnumFruitType.values())
            subItems.add(new ItemStack(itemIn, 1, fruitType.ordinal()));
    }
}

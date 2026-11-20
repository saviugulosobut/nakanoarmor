package com.nakano;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ReceitaUpgradeTerra extends SpecialCraftingRecipe {

    public static SpecialRecipeSerializer<ReceitaUpgradeTerra> SERIALIZER;

    private static final TagKey<Item> ARMADURA_DIAMANTE =
        TagKey.of(RegistryKeys.ITEM, Identifier.of(NakanoArmor.MOD_ID, "diamond_armor"));
    private static final RegistryKey<Enchantment> PROTECAO_X =
        RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(NakanoArmor.MOD_ID, "protecao_x"));

    public ReceitaUpgradeTerra(CraftingRecipeCategory categoria) {
        super(categoria);
    }

    public static void registrar() {
        SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER,
            Identifier.of(NakanoArmor.MOD_ID, "upgrade_terra"),
            new SpecialRecipeSerializer<>(ReceitaUpgradeTerra::new));
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack peca = ItemStack.EMPTY;
        int terra = 0;
        int pecas = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack s = input.getStackInSlot(i);
            if (s.isEmpty()) continue;
            if (s.isOf(Items.DIRT)) {
                terra++;
            } else if (s.isIn(ARMADURA_DIAMANTE)) {
                pecas++;
                peca = s;
            } else {
                return false;
            }
        }
        if (terra != 1 || pecas != 1) return false;

        final ItemStack alvo = peca;
        int nivel = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT)
            .getEntry(PROTECAO_X)
            .map(e -> EnchantmentHelper.getLevel(e, alvo))
            .orElse(0);
        return nivel >= 1 && nivel < NakanoArmor.NIVEL_NECESSARIO;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack resultado = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack s = input.getStackInSlot(i);
            if (s.isIn(ARMADURA_DIAMANTE)) resultado = s.copy();
        }
        resultado.setCount(1);

        var ench = lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(PROTECAO_X);
        int nivel = EnchantmentHelper.getLevel(ench, resultado);

        ItemEnchantmentsComponent.Builder b =
            new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(resultado));
        b.set(ench, nivel + 1);
        resultado.set(DataComponentTypes.ENCHANTMENTS, b.build());
        return resultado;
    }

    @Override
    public boolean fits(int largura, int altura) {
        return largura * altura >= 2;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return SERIALIZER;
    }
}
package com.nakano;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ReceitaUpgradeTerra extends SpecialCraftingRecipe {

    public static SpecialRecipeSerializer<ReceitaUpgradeTerra> SERIALIZER;

    private static final TagKey<Item> ARMADURA_DIAMANTE =
        TagKey.of(RegistryKeys.ITEM, Identifier.of(NakanoArmor.MOD_ID, "diamond_armor"));
    private static final RegistryKey<Enchantment> PROTECAO_X =
        RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(NakanoArmor.MOD_ID, "protecao_x"));

    public ReceitaUpgradeTerra(CraftingRecipeCategory categoria) {
        super(categoria);
    }

    public static void registrar() {
        SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER,
            Identifier.of(NakanoArmor.MOD_ID, "upgrade_terra"),
            new SpecialRecipeSerializer<>(ReceitaUpgradeTerra::new));
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        ItemStack peca = ItemStack.EMPTY;
        int terra = 0;
        int pecas = 0;
        for (int i = 0; i < input.size(); i++) {
            ItemStack s = input.getStackInSlot(i);
            if (s.isEmpty()) continue;
            if (s.isOf(Items.DIRT)) {
                terra++;
            } else if (s.isIn(ARMADURA_DIAMANTE)) {
                pecas++;
                peca = s;
            } else {
                return false;
            }
        }
        if (terra != 1 || pecas != 1) return false;

        final ItemStack alvo = peca;
        int nivel = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT)
            .getEntry(PROTECAO_X)
            .map(e -> EnchantmentHelper.getLevel(e, alvo))
            .orElse(0);
        return nivel >= 1 && nivel < NakanoArmor.NIVEL_NECESSARIO;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack resultado = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            ItemStack s = input.getStackInSlot(i);
            if (s.isIn(ARMADURA_DIAMANTE)) resultado = s.copy();
        }
        resultado.setCount(1);

        var ench = lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(PROTECAO_X);
        int nivel = EnchantmentHelper.getLevel(ench, resultado);

        ItemEnchantmentsComponent.Builder b =
            new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(resultado));
        b.set(ench, nivel + 1);
        resultado.set(DataComponentTypes.ENCHANTMENTS, b.build());
        return resultado;
    }

    @Override
    public boolean fits(int largura, int altura) {
        return largura * altura >= 2;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return SERIALIZER;
    }
}

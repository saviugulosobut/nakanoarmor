package com.nakano;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ArmorItem;
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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ReceitaArmaduraSuprema extends SpecialCraftingRecipe {

    public static SpecialRecipeSerializer<ReceitaArmaduraSuprema> SERIALIZER;

    private static final TagKey<Item> ARMADURA_DIAMANTE =
        TagKey.of(RegistryKeys.ITEM, Identifier.of(NakanoArmor.MOD_ID, "diamond_armor"));
    private static final RegistryKey<Enchantment> PROTECAO_X =
        RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(NakanoArmor.MOD_ID, "protecao_x"));

    public ReceitaArmaduraSuprema(CraftingRecipeCategory categoria) {
        super(categoria);
    }

    public static void registrar() {
        SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER,
            Identifier.of(NakanoArmor.MOD_ID, "armadura_suprema"),
            new SpecialRecipeSerializer<>(ReceitaArmaduraSuprema::new));
    }

    @Override
    public boolean matches(CraftingRecipeInput input, World world) {
        if (input.getWidth() != 3 || input.getHeight() != 3) return false;
        for (int i = 0; i < 9; i++) {
            ItemStack s = input.getStackInSlot(i);
            if (i == 4) {
                if (!armaduraValida(s, world)) return false;
            } else if (i == 1 || i == 3 || i == 5 || i == 7) {
                if (!s.isOf(Items.MILK_BUCKET)) return false;
            } else if (!s.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private static boolean armaduraValida(ItemStack s, World world) {
        if (!s.isIn(ARMADURA_DIAMANTE)) return false;
        RegistryEntry<Enchantment> ench = world.getRegistryManager()
            .get(RegistryKeys.ENCHANTMENT).getEntry(PROTECAO_X).orElse(null);
        return ench != null && EnchantmentHelper.getLevel(ench, s) >= NakanoArmor.NIVEL_NECESSARIO;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        ItemStack resultado = input.getStackInSlot(4).copy();
        resultado.setCount(1);

        var registro = lookup.getWrapperOrThrow(RegistryKeys.ENCHANTMENT);
        ItemEnchantmentsComponent.Builder b =
            new ItemEnchantmentsComponent.Builder(EnchantmentHelper.getEnchantments(resultado));

        // para todas as peças
        b.set(registro.getOrThrow(Enchantments.MENDING), 1);
        b.set(registro.getOrThrow(Enchantments.UNBREAKING), 3);
        b.set(registro.getOrThrow(Enchantments.FIRE_PROTECTION), 4);
        b.set(registro.getOrThrow(Enchantments.BLAST_PROTECTION), 4);
        b.set(registro.getOrThrow(Enchantments.PROJECTILE_PROTECTION), 4);

        // só para algumas peças
        if (resultado.getItem() instanceof ArmorItem armadura) {
            switch (armadura.getType()) {
                case HELMET -> {
                    b.set(registro.getOrThrow(Enchantments.RESPIRATION), 3);
                    b.set(registro.getOrThrow(Enchantments.AQUA_AFFINITY), 1);
                }
                case LEGGINGS -> b.set(registro.getOrThrow(Enchantments.SWIFT_SNEAK), 3);
                case BOOTS -> {
                    b.set(registro.getOrThrow(Enchantments.FEATHER_FALLING), 4);
                    b.set(registro.getOrThrow(Enchantments.DEPTH_STRIDER), 3);
                    b.set(registro.getOrThrow(Enchantments.SOUL_SPEED), 3);
                }
                default -> { }
            }
        }

        resultado.set(DataComponentTypes.ENCHANTMENTS, b.build());
        resultado.set(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true));
        return resultado;
    }

    @Override
    public boolean fits(int largura, int altura) {
        return largura >= 3 && altura >= 3;
    }

    @Override
    public RecipeSerializer<? extends SpecialCraftingRecipe> getSerializer() {
        return SERIALIZER;
    }
}

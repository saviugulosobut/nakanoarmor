package com.nakano;

import java.util.EnumMap;
import java.util.List;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModArmor {
    public static final RegistryEntry<ArmorMaterial> NAKANO = material();
    public static Item CAPACETE, PEITORAL, CALCAS, BOTAS;

    private static RegistryEntry<ArmorMaterial> material() {
        EnumMap<ArmorItem.Type, Integer> defesa = new EnumMap<>(ArmorItem.Type.class);
        defesa.put(ArmorItem.Type.BOOTS, 5);
        defesa.put(ArmorItem.Type.LEGGINGS, 9);
        defesa.put(ArmorItem.Type.CHESTPLATE, 12);
        defesa.put(ArmorItem.Type.HELMET, 5);
        defesa.put(ArmorItem.Type.BODY, 12);

        Identifier id = Identifier.of(NakanoArmor.MOD_ID, "nakano");
        ArmorMaterial m = new ArmorMaterial(
            defesa, 25, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE,
            () -> Ingredient.ofItems(Items.DIAMOND),
            List.of(new ArmorMaterial.Layer(id)),
            5.0f, 0.2f);
        return Registry.registerReference(Registries.ARMOR_MATERIAL, id, m);
    }

    private static Item peca(String nome, ArmorItem.Type tipo) {
        Item.Settings s = new Item.Settings()
            .maxDamage(tipo.getMaxDamage(45)).fireproof();
        return Registry.register(Registries.ITEM,
            Identifier.of(NakanoArmor.MOD_ID, nome),
            new NakanoArmorItem(NAKANO, tipo, s));
    }

    public static void registrar() {
        CAPACETE = peca("nakano_capacete_t1", ArmorItem.Type.HELMET);
        PEITORAL = peca("nakano_peitoral_t1", ArmorItem.Type.CHESTPLATE);
        CALCAS = peca("nakano_calcas_t1", ArmorItem.Type.LEGGINGS);
        BOTAS = peca("nakano_botas_t1", ArmorItem.Type.BOOTS);
    }
}

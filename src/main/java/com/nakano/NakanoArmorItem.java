package com.nakano;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class NakanoArmorItem extends ArmorItem {

    public NakanoArmorItem(RegistryEntry<ArmorMaterial> material, Type type, Settings settings) {
        super(material, type, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient || !(entity instanceof PlayerEntity jogador)) return;

        boolean equipada = jogador.getInventory().armor.contains(stack);
        boolean dono = jogador.getGameProfile().getName().equals("NakanoBot");

        if (equipada && !dono) {
            jogador.dropItem(stack.copy(), false);
            stack.setCount(0);
        }
    }
}

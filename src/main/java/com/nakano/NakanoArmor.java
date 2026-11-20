package com.nakano;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NakanoArmor implements ModInitializer {
    public static final String MOD_ID = "nakano";
    public static final String DONO = "NakanoBot";

    // nível do encantamento exigido em cada peça
    public static final int NIVEL_NECESSARIO = 10;
    // 0 = Resistência I, 1 = II, 2 = III, 3 = IV, 4 = V
    public static final int RESISTENCIA = 3;

    private static final RegistryKey<Enchantment> PROTECAO_X =
        RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "protecao_x"));

    private static final EquipmentSlot[] ARMADURA = {
        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    @Override
    public void onInitialize() {
        ReceitaArmaduraSuprema.registrar();

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            int tick = server.getTicks();
            for (ServerPlayerEntity jogador : server.getPlayerManager().getPlayerList()) {
                if (tick % 5 == 0) travaDono(jogador);
                if (tick % 20 == 0 && conjuntoCompleto(jogador)) {
                    jogador.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.RESISTANCE, 40, RESISTENCIA, true, false, false));
                }
            }
        });
    }

    private static RegistryEntry<Enchantment> encantamento(ServerPlayerEntity jogador) {
        return jogador.getWorld().getRegistryManager()
            .get(RegistryKeys.ENCHANTMENT).getEntry(PROTECAO_X).orElse(null);
    }

    // tira a armadura de quem não é o dono
    private static void travaDono(ServerPlayerEntity jogador) {
        if (jogador.getGameProfile().getName().equalsIgnoreCase(DONO)) return;
        RegistryEntry<Enchantment> ench = encantamento(jogador);
        if (ench == null) return;

        for (EquipmentSlot slot : ARMADURA) {
            ItemStack peca = jogador.getEquippedStack(slot);
            if (EnchantmentHelper.getLevel(ench, peca) > 0) {
                jogador.equipStack(slot, ItemStack.EMPTY);
                jogador.getInventory().offerOrDrop(peca);
            }
        }
    }

    private static boolean conjuntoCompleto(ServerPlayerEntity jogador) {
        RegistryEntry<Enchantment> ench = encantamento(jogador);
        if (ench == null) return false;

        int pecas = 0;
        for (ItemStack peca : jogador.getArmorItems()) {
            if (EnchantmentHelper.getLevel(ench, peca) >= NIVEL_NECESSARIO) pecas++;
        }
        return pecas == 4;
    }
}

package com.nakano;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
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

    // nível do encantamento exigido em cada peça
    public static final int NIVEL_NECESSARIO = 10;
    // 0 = Resistência I, 1 = II, 2 = III, 3 = IV, 4 = V
    public static final int RESISTENCIA = 3;

    private static final RegistryKey<Enchantment> PROTECAO_X =
        RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "protecao_x"));

    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.getTicks() % 20 != 0) return;
            for (ServerPlayerEntity jogador : server.getPlayerManager().getPlayerList()) {
                if (conjuntoCompleto(jogador)) {
                    jogador.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.RESISTANCE, 40, RESISTENCIA, true, false, false));
                }
            }
        });
    }

    private static boolean conjuntoCompleto(ServerPlayerEntity jogador) {
        var registro = jogador.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT);
        RegistryEntry<Enchantment> ench = registro.getEntry(PROTECAO_X).orElse(null);
        if (ench == null) return false;

        int pecas = 0;
        for (ItemStack peca : jogador.getArmorItems()) {
            if (EnchantmentHelper.getLevel(ench, peca) >= NIVEL_NECESSARIO) pecas++;
        }
        return pecas == 4;
    }
}

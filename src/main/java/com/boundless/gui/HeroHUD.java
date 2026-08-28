package com.boundless.gui;

import com.boundless.ability.AbilityEntry;
import com.boundless.ability.TechniqueAbility;
import com.boundless.loadouts.TechniqueLoadout;
import com.boundless.registry.ShaderRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.registry.TechniqueAbilityRegistry;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import com.boundless.util.ShaderAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HeroHUD {
    public static void render(DrawContext context, RenderTickCounter renderTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || !HeroUtils.isHero(client.player)) return;

        // Todo: make this a mixin
        if (client.player.hasStatusEffect(StatusEffectRegistry.IMPACT_FRAME_EFFECT)) {
            //((ShaderAccessor) client.gameRenderer).boundless$loadShader(ShaderRegistry.CLEAVE);
            ShaderRegistry.CLEAVE_RED.render(renderTickCounter.getTickDelta(true));
        } else if (client.player.hasStatusEffect(StatusEffectRegistry.CLAP_IMPACT_FRAME_EFFECT)) {
            ShaderRegistry.CLEAVE_WHITE.render(renderTickCounter.getTickDelta(true));
        } else if (client.player.hasStatusEffect(StatusEffectRegistry.GRAYSCALE)) {
            ShaderRegistry.GRAYSCALE.render(renderTickCounter.getTickDelta(true));
        } else if (client.player.hasStatusEffect(StatusEffectRegistry.SHRINE_EFFECT)) {
            ShaderRegistry.SHRINE_OVERLAY.render(renderTickCounter.getTickDelta(true));
        }
        else {
            if (client.gameRenderer.getPostProcessor() != null) {
                ((ShaderAccessor) client.gameRenderer).boundless$disablePostProcessor();
            }
        }

        if (client.player.hasStatusEffect(StatusEffectRegistry.CINEMATIC_BARS)) {
            drawCinematicBars(context);
        } else {
            renderKeybindAbilities(client, context);
        }
    }

    public static void drawCinematicBars(DrawContext context) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(0, 0, 10000);

        int barThickness = 40;
        context.fill(0, 0, context.getScaledWindowWidth(), barThickness, 0xff000000);
        context.fill(0, context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight() - barThickness, 0xff000000);
        matrices.pop();
    }

    public static void renderKeybindAbilities(MinecraftClient client, DrawContext context) {
        if (client.player == null) return;

        TechniqueLoadout loadout = HeroUtils.getTechniqueLoadout(client.player);
        int offset = 1;

        for (AbilityEntry entry: loadout.getAbilities()) {
            Identifier abilityId = entry.getAbilityId(client.player);
            TechniqueAbility ability = TechniqueAbilityRegistry.getAbilityFromID(abilityId);

            if (ability == null) continue;

            Text displayText = ability.getDisplayText(client.player);

            if (displayText == null) continue;

            String boundKey = "";

            if (entry.key() != null) {
                boundKey = KeybindingUtils.getKeyBindingFromTranslation(entry.key().getTranslationKey()).getBoundKeyLocalizedText().getString();
            }

            renderKeybindAbility(client, context, offset, boundKey, displayText, 0);
            offset++;
        }
        /*
        LinkedHashMap<String, Identifier> abilityLoadout = new LinkedHashMap<>(HeroUtils.getHeroStack(client.player).getOrDefault(DataComponentRegistry.ABILITY_LOADOUT, new LinkedHashMap<>()));
        LinkedHashMap<Identifier, Long> abilityCooldowns = new LinkedHashMap<>(HeroUtils.getHeroStack(client.player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, new LinkedHashMap<>()));

        int offset = 1;
        for (Map.Entry<String, Identifier> entry : abilityLoadout.entrySet()) {
            Ability ability = AbilityRegistry.getAbilityFromID(entry.getValue());
            if (ability == null || ability.isHide() || ability.getDisplayText() == null) continue;
            String boundKey = KeybindingUtils.getKeyBindingFromTranslation(entry.getKey()).getBoundKeyLocalizedText().getString();

            long endTick = abilityCooldowns.getOrDefault(ability.getAbilityID(), 0L);
            int cooldown = Math.toIntExact(endTick - client.player.getWorld().getTime());

            renderKeybindAbility(client, context, offset, boundKey, ability.getDisplayText(), cooldown);
            offset += 1;
        }

         */
    }

    public static void renderKeybindAbility(MinecraftClient client, DrawContext context, int yOffset, String boundKey, Text abilityText, int cooldown) {
        int padX = 2;
        int padY = 2;

        MutableText displayText = Text.literal("");

        if (!boundKey.isEmpty()) {
            displayText = displayText.append(Text.literal(boundKey).formatted(Formatting.AQUA));
            displayText = displayText.append(Text.literal(" - ").formatted(Formatting.GRAY));
        }

        displayText = displayText.append(abilityText);

        if (cooldown > 0) {
            displayText = displayText.append(Text.literal(" (" + cooldownToSeconds(cooldown) + ")").formatted(Formatting.GOLD));
        }

        int x = client.textRenderer.getWidth(displayText);
        int y = (10 + padY) * yOffset - padY;

        context.fill(10, y, 10 + (padX * 2) + x, y + 12, client.options.getTextBackgroundColor(0.4F));
        context.drawText(client.textRenderer, displayText, 12, y + padY, 0xffffff, false);
    }

    public static String cooldownToSeconds(int cooldown) {
        BigDecimal seconds = BigDecimal.valueOf(cooldown).divide(BigDecimal.valueOf(20), 10, RoundingMode.DOWN);
        BigDecimal rounded = seconds.setScale(1, RoundingMode.DOWN);
        return rounded + "s";
    }
}

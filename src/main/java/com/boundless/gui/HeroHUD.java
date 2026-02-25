package com.boundless.gui;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.registry.ShaderRegistry;
import com.boundless.registry.StatusEffectRegistry;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import com.boundless.util.ShaderAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

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
            drawCinematicBars(client, context);
        } else {
            renderKeybindAbilities(client, context);
        }
    }

    public static void drawCinematicBars(MinecraftClient client, DrawContext context) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(0, 0, 10000);

        int barThickness = 40;
        context.fill(0, 0, context.getScaledWindowWidth(), barThickness, 0xff000000);
        context.fill(0, context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight() - barThickness, 0xff000000);
        matrices.pop();
    }

    // Todo: revisit
    /*
    public static void handleCamera(MinecraftClient client) {
        if (client.player == null || client.player.getWorld() == null) return;
        Integer boundCameraID = HeroUtils.getHeroStack(client.player).get(DataComponentRegistry.BOUND_CAMERA_ID);

        if (boundCameraID != null) {
            Entity camera = client.player.getWorld().getEntityById(boundCameraID);

            // Todo: rework this into interfaces for setting / removing camera
            if (camera != null) {
                client.setCameraEntity(camera);
                client.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
            } else {
                client.setCameraEntity(client.player);
            }
        }
    }

     */

    public static void renderKeybindAbilities(MinecraftClient client, DrawContext context) {
        if (client.player == null) return;
        LinkedHashMap<String, Identifier> abilityLoadout = new LinkedHashMap<>(HeroUtils.getHeroStack(client.player).getOrDefault(DataComponentRegistry.ABILITY_LOADOUT, new LinkedHashMap<>()));
        LinkedHashMap<Identifier, Long> abilityCooldowns = new LinkedHashMap<>(HeroUtils.getHeroStack(client.player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, new LinkedHashMap<>()));

        int offset = 1;
        for (Map.Entry<String, Identifier> entry : abilityLoadout.entrySet()) {
            Ability ability = AbilityRegistry.getAbilityFromID(entry.getValue());
            if (ability == null || ability.isHide() || ability.getDisplayString() == null) continue;
            String boundKey = KeybindingUtils.getKeyBindingFromTranslation(entry.getKey()).getBoundKeyLocalizedText().getString();

            long endTick = abilityCooldowns.getOrDefault(ability.getAbilityID(), 0L);
            int cooldown = Math.toIntExact(endTick - client.player.getWorld().getTime());

            renderKeybindAbility(client, context, offset, boundKey, ability.getDisplayString(), cooldown);
            offset += 1;
        }
    }

    public static void renderKeybindAbility(MinecraftClient client, DrawContext context, int yOffset, String boundKey, String abilityString, int cooldown) {
        int padX = 2;
        int padY = 2;

        int x = client.textRenderer.getWidth(formattedAbilityString(boundKey, abilityString, cooldown));
        int y = (10 + padY) * yOffset - padY;

        context.fill(10, y, 10 + (padX * 2) + x, y + 12, client.options.getTextBackgroundColor(0.4F));
        context.drawText(client.textRenderer, formattedAbilityString(boundKey, abilityString, cooldown), 12, y + padY, 0xffffffff, false);
        context.drawText(client.textRenderer, boundKey + " - ", 12, y + padY, 0xffbebebe, false);

        if (cooldown > 0) {
            context.drawText(client.textRenderer, boundKey + " - " + abilityString + " (" + cooldownToSeconds(cooldown) + ")", 12, y + padY, 0xffF5B027, false);
        }
        context.drawText(client.textRenderer, boundKey, 12, y + padY, 0xff00fcff, false);
    }

    public static String formattedAbilityString(String boundKey, String abilityString, int cooldown) {
        if (cooldown > 0) return boundKey + " - " + abilityString + " (" + cooldownToSeconds(cooldown) + ")";
        return boundKey + " - " + abilityString;
    }

    public static String cooldownToSeconds(int cooldown) {
        BigDecimal seconds = BigDecimal.valueOf(cooldown).divide(BigDecimal.valueOf(20), 10, RoundingMode.DOWN);
        BigDecimal rounded = seconds.setScale(1, RoundingMode.DOWN);
        return rounded + "s";
    }
}

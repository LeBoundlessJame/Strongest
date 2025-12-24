package com.boundless.hero;

import com.boundless.BoundlessAPI;
import com.boundless.ability.Ability;
import com.boundless.registry.AbilityRegistry;
import com.boundless.registry.DataComponentRegistry;
import com.boundless.util.GUIUtils;
import com.boundless.util.HeroUtils;
import com.boundless.util.KeybindingUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Math;

import java.util.*;

public class HeroHUD {
    public static Identifier ABILITY_FRAME =  BoundlessAPI.hudPNG("ability_frame_dark");
    public static void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.player == null || !HeroUtils.isHero(minecraftClient.player)) return;

        LinkedHashMap<String, Identifier> abilityLoadout = new LinkedHashMap<>(HeroUtils.getHeroStack(minecraftClient.player).getOrDefault(DataComponentRegistry.ABILITY_LOADOUT, new LinkedHashMap<>()));
        LinkedHashMap<Identifier, Long> abilityCooldowns = new LinkedHashMap<>(HeroUtils.getHeroStack(minecraftClient.player).getOrDefault(DataComponentRegistry.COOLDOWN_DATA, new LinkedHashMap<>()));
        List<Map.Entry<String, Identifier>> entryList = new LinkedList<>(abilityLoadout.entrySet());

        // Todo: make a more efficient solution
        Collections.reverse(entryList);

        for (int i = 0; i < abilityLoadout.size(); i++) {
            Ability ability = AbilityRegistry.getAbilityFromID(entryList.get(i).getValue());
            if (ability == null || ability.isHide()) continue;

            Text boundKeyText = KeybindingUtils.getKeyBindingFromTranslation(entryList.get(i).getKey()).getBoundKeyLocalizedText();
            drawContext.drawTexture(ABILITY_FRAME, 15, 20 + (i * 21), 0, 0, 22, 22, 22, 22);

            if (ability.getAbilityIcon() != null) {
                drawContext.drawTexture(ability.getAbilityIcon(), 15, 20 + (i * 21), 0, 0, ability.getIconWidth(), ability.getIconHeight(), ability.getIconWidth(), ability.getIconHeight());
            }

            float scale = 0.8f;
            MatrixStack matrixStack = drawContext.getMatrices();
            matrixStack.push();
            matrixStack.scale(scale, scale, scale);

            ArrayList<Float> colors = GUIUtils.hexToUnitColor("bcced4");
            GUIUtils.drawOutlinedText(drawContext, minecraftClient, boundKeyText.getString(), (int) (45 / scale), (int) ((20 + (i * 21) + 8) / scale), colors);
            matrixStack.pop();

            long endTick = abilityCooldowns.getOrDefault(ability.getAbilityID(), 0L);
            long currentTick = minecraftClient.player.getWorld().getTime();
            int abilityCooldown = ability.getCooldown();

            float f = (float) (endTick - currentTick);
            float g = (float) abilityCooldown;

            if (g > 0) {
                float cooldownProgress = MathHelper.clamp(f / g, 0, 1);
                int displayProgress = (int) Math.lerp(0, 22, cooldownProgress);
                drawContext.fill(15, 20 + (i * 21) + displayProgress, 37, 20 + (i * 21), 0, 0X64FFFFFF);
            }
        }
    }
}

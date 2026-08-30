package com.boundless.combat;

import com.boundless.util.CombatUtils;
import com.boundless.util.SoundUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class HitEffects {
    @Getter @Setter
    private List<Identifier> visuals;

    @Getter @Setter
    private List<SoundEvent> sounds;

    public HitEffects() {
        this.visuals = new ArrayList<>();
        this.sounds = new ArrayList<>();
    }

    public HitEffects(List<Identifier> visuals, List<SoundEvent> sounds) {
        this.visuals = new ArrayList<>(visuals);
        this.sounds = new ArrayList<>(sounds);
    }

    public HitEffects(Identifier visual, SoundEvent sound) {
        this.visuals = new ArrayList<>(List.of(visual));
        this.sounds = new ArrayList<>(List.of(sound));
    }

    public void playEffects(PlayerEntity player, LivingEntity target) {
        for (Identifier visual: visuals) {
            CombatUtils.playImpactVisual(player, target, visual);
        }

        for (SoundEvent sound: sounds) {
            SoundUtils.playSound(target, sound);
        }
    }

    public void addVisual(Identifier visual) {
        this.visuals.add(visual);
    }

    public void addSound(SoundEvent sound) {
        this.sounds.add(sound);
    }
}

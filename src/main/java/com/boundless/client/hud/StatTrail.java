package com.boundless.client.hud;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class StatTrail {
    private boolean hasPrevStat = false;
    private float prevStat;
    private float trailingStat;
    private float trailingStatStart;
    private int lastChangedTimestamp;

    private int trailDelayTicks;
    private int trailTransitionTicks;

    public StatTrail(int trailDelayTicks, int trailTransitionTicks) {
        this.trailDelayTicks = trailDelayTicks;
        this.trailTransitionTicks = trailTransitionTicks;
    }

    public float getUpdatedDisplay(float value, PlayerEntity player) {
        if (!hasPrevStat) {
            prevStat = value;
            trailingStat = value;
            hasPrevStat = true;
        }

        if (value < prevStat) {
            trailingStat = prevStat;
            trailingStatStart = prevStat;
            lastChangedTimestamp = player.age;
        }

        prevStat = value;

        if (lastChangedTimestamp >= 0) {
            int ticksSinceLastChange = player.age - lastChangedTimestamp;

            if (ticksSinceLastChange > this.trailDelayTicks && trailingStat > value) {
                float progress = MathHelper.clamp((ticksSinceLastChange - this.trailDelayTicks) / (float) this.trailTransitionTicks, 0.0f, 1.0f);
                trailingStat = MathHelper.lerp(progress, trailingStatStart, value);
            }
        }

        return trailingStat;
    }
}

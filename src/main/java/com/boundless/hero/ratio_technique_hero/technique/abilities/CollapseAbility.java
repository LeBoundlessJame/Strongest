package com.boundless.hero.ratio_technique_hero.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.TechniqueAbility;
import com.boundless.ability.generic.AOEAbility;
import com.boundless.ability.generic.PunchAbility;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.util.AOE;
import com.boundless.util.AOEUtils;
import com.boundless.util.CombatUtils;
import com.boundless.util.EffekUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;

import java.util.List;

@Getter @Setter @Accessors(chain = true)
public class CollapseAbility extends AOEAbility implements AOE {
    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("collapse");
    }

    public void activateAOE(PlayerEntity player, HeroActionEntity action) {
        super.activateAOE(player, action);
        EffekUtils.playEffect(BoundlessAPI.id("collapse_impact"), player, player.getPos(), new Vec3d(0.5f, 0.5f, 0.5f));
        player.getWorld().syncWorldEvent(WorldEvents.SMASH_ATTACK, player.getSteppingPos(), 750);
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 10;
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Collapse");
    }
}

package com.boundless.hero.nanami.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.generic.AOEAbility;
import com.boundless.entity.hero_action.HeroActionEntity;
import com.boundless.util.AOE;
import com.boundless.util.EffekUtils;
import com.boundless.util.PlayerAnimationUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;

@Getter @Setter @Accessors(chain = true)
public class CollapseAbility extends AOEAbility implements AOE {
    @Override
    public Identifier getAbilityId() {
        return BoundlessAPI.id("collapse");
    }

    // Todo: I should also make some sort of 'AttackAbility' interface or something that requires an attack duration and starts timer
    // Todo: I hate the unclearness of 'activate' vs 'activateAOE'. Definitely gonna clear this up soon frfr
    @Override
    public void activate(PlayerEntity player) {
        super.activate(player);
        PlayerAnimationUtils.playSyncedAnimation(player, BoundlessAPI.id("collapse"));
    }

    public void activateAOE(PlayerEntity player, HeroActionEntity action) {
        super.activateAOE(player, action);
        EffekUtils.playEffect(BoundlessAPI.id("collapse_impact"), player, player.getPos(), new Vec3d(0.5f, 0.5f, 0.5f));
        player.getWorld().syncWorldEvent(WorldEvents.SMASH_ATTACK, player.getSteppingPos(), 750);
    }

    @Override
    public long getCooldown(PlayerEntity player) {
        return 300;
    }

    @Override
    public Text getDisplayText(PlayerEntity player) {
        return Text.of("Collapse");
    }
}

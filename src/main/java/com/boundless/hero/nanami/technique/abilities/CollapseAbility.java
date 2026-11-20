package com.boundless.hero.nanami.technique.abilities;

import com.boundless.BoundlessAPI;
import com.boundless.ability.generic.AOEAbility;
import com.boundless.util.AOE;
import com.boundless.util.EffekUtils;
import com.boundless.util.AnimationManager;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;

@Getter @Setter @Accessors(chain = true)
public class CollapseAbility extends AOEAbility implements AOE {
    @Override
    public Identifier getId() {
        return BoundlessAPI.id("collapse");
    }

    // Todo: I should also make some sort of 'AttackAbility' interface or something that requires an attack duration and starts timer
    // Todo: I hate the unclearness of 'activate' vs 'activateAOE'. Definitely gonna clear this up soon frfr
    @Override
    public void activate(LivingEntity user) {
        super.activate(user);
        AnimationManager.playSyncedAnimation(user, BoundlessAPI.id("collapse"));
    }

    public void activateAOE(LivingEntity user) {
        super.activateAOE(user);
        EffekUtils.playEffect(BoundlessAPI.id("collapse_impact"), user, user.getPos(), new Vec3d(0.5f, 0.5f, 0.5f));
        user.getWorld().syncWorldEvent(WorldEvents.SMASH_ATTACK, user.getSteppingPos(), 750);
    }

    @Override
    public long getCooldown(LivingEntity user) {
        return 300;
    }

    @Override
    public Text getDisplayText(LivingEntity user) {
        return Text.of("Collapse");
    }
}

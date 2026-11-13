package com.boundless.entity.nail;

import com.boundless.BoundlessAPI;
import com.boundless.hero.nobara.technique.components.CursedNailData;
import com.boundless.hero.nobara.technique.components.StrawdollComponents;
import com.boundless.registry.EntityRegistry;
import com.boundless.registry.GameRulesRegistry;
import com.boundless.registry.SoundRegistry;
import com.boundless.util.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class NailEntity extends PersistentProjectileEntity {
    private int MAX_AGE = 1200;
    private Vec3d explosionRadius = new Vec3d(3, 3, 3);

    public NailEntity(EntityType<NailEntity> entityType, World world) {
        super(entityType, world);
        this.setStack(Items.ARROW.getDefaultStack());
    }

    public NailEntity(LivingEntity livingEntity, World world) {
        super(EntityRegistry.NAIL, world);
        this.setOwner(livingEntity);
        this.setStack(Items.ARROW.getDefaultStack());
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        age++;

        if (this.age >= MAX_AGE) {
            this.discard();
        }
    }

    public void detonate() {
        if (this.getOwner() == null) return;
        PlayerEntity owner = (PlayerEntity) this.getOwner();

        List<LivingEntity> targets = AOEUtils.getTargetsInRadius(owner, this.getWorld(), this.getPos(), explosionRadius, entity -> true);
        targets.forEach(target -> target.damage(target.getDamageSources().playerAttack(owner), (float) this.getDamage()));

        SoundUtils.playSound((LivingEntity) this.getOwner(), SoundRegistry.RATIO_IMPACT_1, 10, 11);
        EffekUtils.playRandomRotatedEffect(BoundlessAPI.id("hairpin_impact"), this, this.getPos().add(0, this.getHeight() / 2, 0), new Vec3d(0.35, 0.35, 0.35));

        this.discard();
    }

    @Override
    public double getDamage() {
        return 20;
    }

    @Override
    public void onBlockHit(BlockHitResult result) {
        super.onBlockHit(result);
        if (this.getOwner() == null) return;
        EffekUtils.playEffect(BoundlessAPI.id("energy_spark"), this, this.getPos().add(0f, 0.1f, 0f), 0.5f);
    }

    @Override
    public void onEntityHit(EntityHitResult result) {
        super.onEntityHit(result);
        if (result.getEntity() == null || this.getOwner() == null) return;
        EffekUtils.playEffect(BoundlessAPI.id("energy_spark"), this, this.getPos().add(0f, 0.1f, 0f), 0.5f);

        if (this.getOwner() instanceof PlayerEntity player) {
            Map<UUID, List<CursedNailData>> targets = ComponentUtils.getOr(player, StrawdollComponents.HAIRPIN_TARGETS, new HashMap()<>);
            List<CursedNailData> nails = targets.getOrDefault(result.getEntity(), new ArrayList<>());
            nails.add(new CursedNailData(this.getWorld().getTime()));

            ComponentUtils.updateMap(HeroUtils.getHeroStack(player), StrawdollComponents.HAIRPIN_TARGETS, result.getEntity().getUuid(), nails);
        }
    }

    protected SoundEvent getHitSound() {
        return SoundRegistry.RATIO_IMPACT_1;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return Items.ARROW.getDefaultStack();
    }
}
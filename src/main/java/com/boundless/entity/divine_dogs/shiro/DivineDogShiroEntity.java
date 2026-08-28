package com.boundless.entity.divine_dogs.shiro;

import com.boundless.entity.divine_dogs.goals.ReturnFoundItemGoal;
import com.boundless.entity.divine_dogs.goals.SearchForItemGoal;
import com.boundless.entity.divine_dogs.kuro.DivineDogDispatcher;
import com.boundless.entity.divine_dogs.kuro.DivineDogKuroEntity;
import com.boundless.registry.EntityRegistry;
import com.boundless.util.TenShadowsShikigami;
import lombok.Getter;
import lombok.Setter;
import mod.azure.azurelib.common.util.MoveAnalysis;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Objects;

public class DivineDogShiroEntity extends DivineDogKuroEntity implements TenShadowsShikigami, InventoryOwner {
    public final DivineDogDispatcher dispatcher;
    public final MoveAnalysis moveAnalysis;
    private final SimpleInventory inventory = new SimpleInventory(1);
    @Setter @Getter
    public boolean hasItemToReturn = false;

    public DivineDogShiroEntity(EntityType<? extends WolfEntity> entityType, World world) {
        super(entityType, world);
        this.dispatcher = new DivineDogDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
    }

    public DivineDogShiroEntity(World world, PlayerEntity owner) {
        super(EntityRegistry.DIVINE_DOG_SHIRO, world);
        this.dispatcher = new DivineDogDispatcher(this);
        this.moveAnalysis = new MoveAnalysis(this);
        this.setOwner(owner);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(6, new ReturnFoundItemGoal(this));
        this.goalSelector.add(7, new SearchForItemGoal(this));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack playerStack = player.getStackInHand(hand);
        ItemStack shiroHeldStack = this.getStackInHand(Hand.MAIN_HAND);

        if (shiroHeldStack.isEmpty() && !playerStack.isEmpty()) {
            ItemStack stack = playerStack.copyWithCount(1);
            this.setStackInHand(Hand.MAIN_HAND, stack);
            this.decrementStackUnlessInCreative(player, playerStack);
            this.getWorld().playSoundFromEntity(player, this, SoundEvents.ENTITY_ALLAY_ITEM_GIVEN, SoundCategory.NEUTRAL, 2.0F, 1.0F);
            return ActionResult.SUCCESS;
        } else if (!shiroHeldStack.isEmpty() && hand == Hand.MAIN_HAND && playerStack.isEmpty()) {
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.getWorld().playSoundFromEntity(player, this, SoundEvents.ENTITY_ALLAY_ITEM_TAKEN, SoundCategory.NEUTRAL, 2.0F, 1.0F);
            this.swingHand(Hand.MAIN_HAND);

            for (ItemStack itemStack4 : this.getInventory().clearToList()) {
                LookTargetUtil.give(this, itemStack4, this.getPos());
            }

            player.giveItemStack(shiroHeldStack);
            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        moveAnalysis.update();
        this.animationTick();
    }

    @Override
    public void animationTick() {
        super.animationTick();
    }

    @Override
    public boolean canPickUpLoot() {
        return this.isHoldingItem();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        this.writeInventory(nbt, this.getRegistryManager());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.readInventory(nbt, this.getRegistryManager());
        this.hasItemToReturn = !this.getInventory().isEmpty();
    }

    public boolean isHoldingItem() {
        return !this.getStackInHand(Hand.MAIN_HAND).isEmpty();
    }

    @Override
    public SimpleInventory getInventory() {
        return this.inventory;
    }

    @Override
    protected void loot(ItemEntity item) {
        InventoryOwner.pickUpItem(this, this, item);

        if (!this.getInventory().isEmpty()) {
            this.setHasItemToReturn(true);
        }
    }

    @Override
    public boolean canGather(ItemStack stack) {
        ItemStack itemStack = this.getStackInHand(Hand.MAIN_HAND);
        return !itemStack.isEmpty()
                && this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)
                && this.inventory.canInsert(stack)
                && this.areItemsEqual(itemStack, stack);
    }

    private boolean areItemsEqual(ItemStack stack, ItemStack stack2) {
        return ItemStack.areItemsEqual(stack, stack2) && !this.areDifferentPotions(stack, stack2);
    }

    private boolean areDifferentPotions(ItemStack stack, ItemStack stack2) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        PotionContentsComponent potionContentsComponent2 = stack2.get(DataComponentTypes.POTION_CONTENTS);
        return !Objects.equals(potionContentsComponent, potionContentsComponent2);
    }

    private void decrementStackUnlessInCreative(PlayerEntity player, ItemStack stack) {
        stack.decrementUnlessCreative(1, player);
    }

    public static DefaultAttributeContainer.Builder createWolfAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 435.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0)
                .add(EntityAttributes.GENERIC_SCALE, 1.5)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64)
                .add(EntityAttributes.GENERIC_SAFE_FALL_DISTANCE, 64);
    }
}

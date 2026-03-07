package com.boundless.combat;

public class CombatSystem {
    /*
     * hashmap of target combo and the biconsumer connected to it
     * evaluate each combo in the hashmap upon each attack
     * if matches target combo? do combo and reset
     * else if correct progression? update progress!
     * else? reset progress on combo
     * example: for combo component in hashmap
     * HashMap<Combo string, BiConsumer<PlayerEntity, HeroAction>
     * lllmll_progress = "l"
     * store as components on player, which are generated from hashmap
     */
}

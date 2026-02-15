# Boundless

<img width="500" height="500" alt="Boundless(2)" src="https://github.com/user-attachments/assets/6d0faffa-990e-4cd6-bf36-acb760e7a159" />

# Description
An extensive API with an emphasis on abilities, effects and combat. (Fabric)

This API aims to streamline the process of creating new characters and abilities by providing a simplified yet powerful interface for the user to interact with in a modding environment context.

* The Action system. An Action is a series of tasks which the user specifies in order to acomplish a goal. For example, a 'melee singleAttack Action' could be comprised of an initial wind-up animation, followed by a sound effect, and finally a damage impact frame. The Action system works by having the user feed in a linked list comprised of an integer key and a biconsumer value. The key dictates the 'tick' upon which a given task is executed, and the biconsumer is the task itself. For example, for a delay of 5 ticks, the user can set the key for an entry to 5.

* HeroActionEntity: An entity which helps leverage the capabilities of the action system, managing and working with Actions in order to produce the desired results. Allows for hitbox collision and rendering.
* Ability system: Easily bind abilities to heroes in order to use them in-game!
* Utils + Helper methods: Utils + helpers for commonly used methods in the mod. Allows ease of interactivity and more readable code.
* Automatic Hero registration: Integrates methods for easily adding new heroes to the game! This includes itemgroup registration + automatic datagen for models and translations.
* Custom attributes + effects: 'Damage resistance' attribute + 'Invulnerability' effects which are very useful in everyday modding.
* Many other things!

# Getting Started
To use this API, simply add the curse-maven snippet found in the dropdown menu of the files to your build.gradle like so:
<img width="1180" height="540" alt="image" src="https://github.com/user-attachments/assets/109e9cc5-ba39-4d42-b147-1ac57073bb37" />

```build.gradle
dependencies {
    modImplementation "curse.maven:boundless-api-1310831:6792951" // example
}

repositories {
        // NOTE: This line especially is important, otherwise it won't be able to find the API!
        maven { url "https://cursemaven.com" }
}
```

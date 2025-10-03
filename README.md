# Limited Mace

## A fork of [Limited Mace](https://github.com/Adriancodessometimes/LimitedMace) that allows for more than one mace

A simple Fabric mod that allows a custom amount of maces to be crafted per world.  
Once a player crafts the final Mace, all further crafting attempts (including crafters) are blocked.

## Features
- Prevents mass-crafting with shift-click.
- Compatible with automated crafters.
- `/limitedmace reset` command to allow crafting a new Mace.
- `/limitedmace state` command to check if the first Mace has been crafted.

## Usage
1. Install Fabric API for Minecraft 1.21.8.
2. Place the mod `.jar` file into your `mods/` folder.
4. The first Mace crafted in the world will be allowed — all others are blocked until reset with command.

## Requirements
- Minecraft 1.21.8
- Fabric Loader `>=0.17.2`
- Fabric API `>=0.130.0+1.21.8`

## Credits
I did not invent the Mace concept — it is an official Minecraft item by Mojang. 
This mod simply enforces a "one per world" rule, like other plugins do, using the Fabric modding API.  
Special thanks to the FabricMC community for mixin documentation, and any code snippets referenced from open discussions, examples, and Fabric Wiki resources.

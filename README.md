# Limited Mace

A simple Fabric mod that allows **only one Mace** to be crafted per world.  
Once a player crafts the first Mace, all further crafting attempts (including shift-click and automated crafters) are blocked.

## Features
- Works in both the **player inventory crafting grid** and **crafting table**.
- Prevents mass-crafting with shift-click.
- Compatible with automated crafters.
- `/limitedmace reset` command to allow crafting a new Mace.
- `/limitedmace state` command to check if the first Mace has been crafted.

## Usage
1. Install Fabric Loader and Fabric API for Minecraft 1.21.8.
2. Place the mod `.jar` file into your `mods/` folder.
3. Start your server or singleplayer world.
4. The first Mace crafted in the world will be allowed — all others are blocked until reset with command.

## Requirements
- Minecraft 1.21.8
- Fabric Loader `>=0.17.2`
- Fabric API `>=0.130.0+1.21.8`

## Credits
I did not invent the Mace concept — it is an official Minecraft item by Mojang.  
This mod simply enforces a "one per world" rule using the Fabric modding API.  
Special thanks to the FabricMC community for mixin documentation, and any code snippets referenced from open discussions, examples, and Fabric Wiki resources.

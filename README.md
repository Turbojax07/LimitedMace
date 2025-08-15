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
4. The first Mace crafted in the world will be allowed — all others are blocked until reset with:

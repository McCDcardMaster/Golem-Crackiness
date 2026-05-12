# Golem Crackiness — Golem Rework for Minecraft 1.12.2

[![Minecraft 1.12.2](https://img.shields.io/badge/Minecraft-1.12.2-brightgreen)](https://minecraft.net)
[![Forge 14.23.5.2860+](https://img.shields.io/badge/Forge-14.23.5.2860+-orange)](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.12.2.html)
[![Server Side](https://img.shields.io/badge/Server--Side-✓-blue)](https://github.com/McCDcardMaster/Golem-Crackiness)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/McCDcardMaster/Golem-Crackiness)

This mod completely overhauls the iron golem mechanics, bringing them closer to modern game versions (1.14–1.15).  
Now golem spawning works as in 1.14, and when they take damage, they display cracking textures as in 1.15.

**Key feature** — the mod works entirely server-side. Even if the client doesn't have the mod installed, all new mechanics (including spawning, behavior, and textures) will work correctly.

## Features

### 🏠 New spawning system (as in 1.14)
Iron golems now spawn not only from the classic structure (iron block + pumpkin), but also through villager gossip and panic.

<p align="center">
  <img src="https://github.com/user-attachments/assets/7faed3de-3b52-4c9b-a2c0-c229dbbe219c" alt="Heavily damaged golem" width="800">
  <br><em>A golem spawns right among villagers after a panic event</em>
</p>

### 💥 Visual damage (as in 1.15)
When damaged, golems display cracks on their texture. The degree of damage depends on remaining health.

<p align="center">
  <img src="https://github.com/user-attachments/assets/430b39b7-1815-4c1c-8faa-4dfd53e263a0" alt="Damaged golem" width="800">
  <br><em>Cracks on the body — the golem is seriously battered</em>
  <br><em>At low health, cracks cover most of the model</em>
</p>

### 💊 Golem repair (as in 1.14+)
A damaged golem can be healed — use an **iron ingot** on it by right-clicking. Each ingot restores **25%** of maximum health (10 ❤️). The crack texture will gradually diminish as it heals.

<p align="center">
  <img src="https://github.com/user-attachments/assets/592c5056-c9f5-4c9c-9ed1-e04d90538144" alt="Golem spawning in a village" width="800">
  <br><em>Right-click the golem with an iron ingot — the cracks will begin to close</em>
</p>

### 🔄 Full server-side compatibility
The mod only needs to be installed on the server. Vanilla clients can join and see all changes (spawning, behavior, damage synchronization).  
*For correct display of crack textures on the server, clients are recommended to install OptiFine.*

## Requirements

- Minecraft **1.12.2**
- Minecraft Forge **14.23.5.2860** or higher

## Installation

### For the server
1. Install Forge on the server.
2. Copy the mod `.jar` file into the `mods/` folder.
3. Restart the server.

### For the client (optional)
1. Install Forge on the client.
2. Copy the mod `.jar` file into the `.minecraft/mods/` folder.
3. Install `Optifine` for playing on the server (if the mod is present on the server).

If you are playing on a server with the mod, client-side installation is not required.

## Compatibility

- Works with any mods that do not completely replace the `EntityIronGolem` classes.
- No conflicts with OptiFine.
- Works correctly on servers with SpongeForge, Thermos, and other forks.

## Known Issues

- Without the mod installed on the client, crack textures are not visible without a resource pack (but damage mechanics remain).
- Golem spawning from gossip may be slightly less predictable in very large villages.

## License

The mod is distributed under the **MIT** license. You are free to use, copy, modify, and distribute the code.

## Links

- [GitHub repository](https://github.com/McCDcardMaster/Golem-Crackiness)
- [Download latest release](link_to_release)

---

*Made for those who appreciate modern mechanics but stay on classic 1.12.2.*

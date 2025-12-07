# easyPathForgeForge-47.3.11
A Minecraft Forge mod for version 1.20.1 (Forge 47.3.11) that provides a "Path Shovel" tool.

## Features

The **Path Shovel** is a special item that helps players quickly build paths:

- **Custom Item**: Uses a shovel texture and appears in its own creative tab called "Path Tools"
- **Path Creation**: Right-click while holding the item to create a 4-block wide, 10-block long path
- **Smart Replacement**: Only replaces grass blocks with cobblestone, leaving other blocks untouched
- **Direction Aware**: The path extends in the direction the player is facing
- **Durability**: Has 500 uses and wears down based on blocks replaced

## Building the Mod

1. Ensure you have Java 17 installed
2. Clone this repository
3. Run `./gradlew build` (or `gradlew.bat build` on Windows)
4. The compiled mod JAR will be in `build/libs/`

## Installation

1. Install Minecraft 1.20.1
2. Install Forge 47.3.11 or higher
3. Place the mod JAR file in your `mods` folder
4. Launch Minecraft

## Usage

1. Open the creative inventory
2. Navigate to the "Path Tools" tab
3. Get the "Path Shovel" item
4. Stand where you want to start the path
5. Face the direction you want the path to go
6. Right-click to create a 4x10 path of cobblestone (replacing grass only)

## Technical Details

- **Mod ID**: `easypathforge`
- **Minecraft Version**: 1.20.1
- **Forge Version**: 47.3.11+
- **Java Version**: 17

## License

All Rights Reserved


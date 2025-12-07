# easyPathForgeForge-47.3.11
A Minecraft Forge mod for version 1.20.1 (Forge 47.3.11) that provides a "Path Shovel" tool.

## Features

The **Path Shovel** is a special item that helps players quickly build paths:

- **Custom Item**: Uses a shovel texture and appears in its own creative tab called "Path Tools"
- **Path Creation**: Right-click while holding the item to create a 4-block wide, 10-block long path
- **Smart Replacement**: Only replaces grass blocks with cobblestone, leaving other blocks untouched
- **Direction Aware**: The path extends in the direction the player is facing
- **Durability**: Has 500 uses and wears down based on blocks replaced

## Download

Download the latest release from the [Releases](../../releases) page.

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

## Creating a Release

To create a new release with an automated build, you have two options:

### Option 1: Using Git Tags (Recommended for versioned releases)

1. Ensure all changes are committed and pushed to the `main` branch
2. Create and push a version tag:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
3. The release pipeline will automatically:
   - Build the mod JAR with the version from the tag
   - Create a GitHub release with the JAR file attached
   - Include installation instructions in the release notes

### Option 2: Manual Workflow Run

1. Go to the [Actions](../../actions/workflows/release.yml) tab in the repository
2. Select the "Release Pipeline" workflow
3. Click "Run workflow"
4. Select the branch (usually `main`)
5. Enter the version number (e.g., `1.0.0`)
6. Click "Run workflow"
7. The pipeline will automatically:
   - Build the mod JAR with the specified version
   - Create a GitHub release with tag `v[VERSION]`
   - Include installation instructions in the release notes

Version tags should follow the format `v[MAJOR].[MINOR].[PATCH]` (e.g., `v1.0.0`, `v1.2.3`)

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


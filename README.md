# Smart Loadout Auditor

A RuneLite external plugin that checks whether the player's current loadout satisfies flexible activity rules before an activity starts.

The plugin is advisory only. It does not click, withdraw, equip, modify menu entries, send chat, or automate gameplay.

## MVP checks

- Required items and item groups
- Minimum item quantities
- Required spellbook
- Rune pouch contents
- Ammunition or ranged weapon presence
- Attack style category
- Teleport category and Wilderness teleport limits
- Duplicate supply warnings

## Usage

1. Select an activity template in the side panel.
2. Click **Run audit** or close the bank with **Audit when bank closes** enabled.
3. Review failed and warning checks before starting the activity.
4. Use **Export JSON** and **Import JSON** to share templates through the clipboard.

## Starter templates

- Wilderness clue
- Generic boss trip
- Raid pre-check

## Development

This project is intended for RuneLite Plugin Hub standard builds.

```powershell
.\gradlew.bat test
.\gradlew.bat run
```

`gradlew run` launches RuneLite in developer mode with the plugin loaded. The Plugin Hub build remains `build=standard` and does not require third-party runtime dependencies.

## Safety

Smart Loadout Auditor only reads local client state and displays reminders. It never performs gameplay actions for the player, never modifies menu entries, and does not use external services.
# Smart Loadout Auditor

A RuneLite external plugin that checks whether the player's current loadout satisfies flexible activity rules.

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

## Safety

Smart Loadout Auditor only reads local client state and displays reminders. It never performs gameplay actions for the player.

## Current verification note

Automated tests and developer-mode launch require Java and Gradle to be available on PATH. In this workspace, `java`, `gradle test`, and `gradle run` currently fail before execution because those commands are not installed or not on PATH.
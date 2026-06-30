# RuneLite Plugin Hub Submission Checklist

This repository is prepared for a standard RuneLite Plugin Hub submission.

## Repository checks

- [ ] Repository is public on GitHub.
- [ ] Default branch contains the plugin source.
- [ ] `runelite-plugin.properties` has the correct plugin class.
- [ ] `build=standard` is set.
- [ ] No third-party runtime dependencies are required; template JSON import/export is implemented with project code.
- [ ] `./gradlew test` passes with Java 11 available.
- [ ] `./gradlew run` launches RuneLite in developer mode.
- [ ] README explains usage and passive safety posture.
- [ ] LICENSE is present.

## Plugin Hub marker file template

Create a new file in the RuneLite `plugin-hub` repository under `plugins/`, usually named after the plugin, and point it at the public GitHub repository and commit hash.

```properties
repository=https://github.com/<github-user>/OSRS-Smart-Loadout-Auditor-Runelite
commit=<full-commit-sha>
```

Use the exact commit hash that has been tested locally.
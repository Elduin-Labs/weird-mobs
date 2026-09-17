# Weird Mobs

A mod full of weird mobs. The first one is the OKIE — a robot that teaches you
how to spell.

This file is read automatically whenever Claude Code is opened in this folder.
Everything below is specific to this one mod. The general rules about how to
work with Elduin live in `~/.claude/CLAUDE.md`.

## Facts about this mod

    mod id            weird_mobs            (underscores — never change this)
    slug              weird-mobs            (repo name and Modrinth slug)
    package           com.elduin.weird_mobs
    loader            fabric                (only fabric — see below)
    minecraft         1.21.8, 1.21.4
    primary version   1.21.8                (the one he plays)
    java              21 for both — Gradle picks this per version

Elduin chose 1.21.8 and 1.21.4 himself. They are **not** the org's
`DEFAULT_VERSIONS`, so don't "correct" them to 1.21.11 / 26.2.

The mod id is baked into save files. Once a world has been played with this mod,
**changing the mod id breaks that world.** Rename the display name freely;
never rename the mod id.

## The mobs

### OKIE

Elduin modelled this one himself in Blockbench. It is a tall flat panel — a
light blue face with two pink eyes and a red zigzag across it.

What he asked for, in his words:

- It **moves by low legs that you can't see.** So: no leg geometry, and it
  glides along the ground rather than doing a walk cycle.
- It **blinks.** There is an eyelid box hinged at its top edge that swings down
  over both eyes and back up.
- It **only teaches you how to spell.** He explicitly changed his mind about it
  attacking — the OKIE is friendly and must never be hostile.
- It **spawns in the Deep Dark**, and has a **spawn egg** in the creative
  Spawn Eggs tab.
- Right-clicking it with a **debug stick** shrinks it, stepping 1.0 -> 0.7 ->
  0.5 -> 0.35 -> 0.25 and wrapping back to full size, via the `SCALE` attribute
  so the hitbox shrinks too. He described this as a glitch he had seen; it was
  not one, but he liked the idea so it is a real feature now. The wrap-around
  matters - an OKIE must never be left stuck tiny.
- It has a **boss bar** across the top of the screen saying OKIE, filled to its
  health, shown to players within 24 blocks. He asked for "a bar that says
  OKIE... with all the health. It has 10 Hearts" - 10 hearts is `MAX_HEALTH`
  20.0, which it already had. He was offered a floating name tag instead and
  picked the bar, so don't swap it back.

The Deep Dark is pitch black and vanilla spawns nothing there, so the spawn
placement predicate deliberately skips the usual light-level check. It only asks
for solid ground. Category is `CREATURE`, so they arrive with new chunks rather
than trickling in — that keeps them friendly-feeling and off the monster cap.

Model numbers from his Blockbench file, for keeping the code and the model in
step:

    body     position (-18, -12, -11)  size (19, 35, 46)
    eyelid   position (-11,  12, -12)  size ( 8,  5,  1)
             bone pivot (-7, 17, -11)  — hinged at the eyelid's top edge

## Layout

Multi-version is handled by [Stonecutter](https://plugins.gradle.org/plugin/dev.kikugie.stonecutter):
one source tree, version-conditional comments, many outputs.

    src/main/java/com/elduin/weird_mobs/    the mod
    src/main/resources/                     assets, textures, mixins, lang
    versions/<mcversion>-fabric/build/libs/ built jars land here
    stonecutter.properties.toml             mod id, name, version, dependencies
    settings.gradle.kts                     the Minecraft version list
    .github/workflows/release.yml           builds and publishes on a version tag

There is **no `fabric.mod.json` file** — it is generated at build time from
`stonecutter.properties.toml` by the code in `build-logic/`. Editing mod
metadata means editing the `.toml`, not a json file. Same for `mod.version`:
there is no `mod_version` in `gradle.properties`.

Stonecutter subprojects are named `<mcversion>-fabric`, so the 1.21.8 jar is in
`versions/1.21.8-fabric/build/libs/`. That `-fabric` suffix is easy to forget.

Do **not** add a branch or a repo for a new Minecraft version. Add it to the
list in `settings.gradle.kts`, add a matching `[fabric."<version>"]` block in
`stonecutter.properties.toml`, add it to the matrix in
`.github/workflows/release.yml`, and fix whatever stops compiling.

### Mappings across versions

`build.fabric.gradle.kts` rewrites `ResourceLocation` to `Identifier` (and
`location()` to `identifier()`) for 1.21.11 and up. Both versions in this mod
are below that, so **write `ResourceLocation` in the shared sources.** If the
tree ever looks like it uses `Identifier`, the sources are sitting in a newer
version's form and want switching back.

## Fabric only

This template builds Fabric and nothing else. NeoForge and Forge were removed on
purpose: each extra loader is another full copy of Minecraft to decompile, and
this is an 8 GB machine. Do not add them back.

For the same reason `gradle.properties` sets `org.gradle.parallel=false`.
Leave it off. Turning it on with more than one Minecraft version in the list
will exhaust memory and take the whole machine down.

## Commands

    ./gradlew "Set active project to 1.21.8-fabric"   switch versions first
    ./gradlew "1.21.8-fabric:build"                   build just that version
    ./gradlew build                                   build every version
    ./gradlew runActiveClient                         launch a dev client

Switching rewrites the shared source tree into that version's form. It is **not**
required before building — each version subproject regenerates its own sources,
so the jars are correct either way. Switch to keep the working tree in the
version you're reading, not because the build needs it.

Never hand-edit `.sc_active_version`. Stonecutter records what form the shared
sources are currently in, and editing that file behind its back desyncs the
bookkeeping — you get `cannot find symbol` errors on classes that plainly exist.
Use the task above and nothing else.

**If the active version is one you just deleted from `settings.gradle.kts`,**
Gradle refuses to configure at all — "Version 'x' is not registered" — so you
cannot run the switch task to get out of it. Put the old version back in
`settings.gradle.kts` and `stonecutter.properties.toml` temporarily, run the
switch task, then take it out again. Still don't edit `.sc_active_version`.

## This machine's Xcode license

`git`, `python3` and `curl` on this Mac are Xcode shims, and as of 2026-09-17
they refuse to run with "You have not agreed to the Xcode license agreements."
Prefix commands with `DEVELOPER_DIR=/Library/Developer/CommandLineTools` to use
the standalone command line tools instead. The real fix needs a grown-up to run
`sudo xcodebuild -license` once.

## Access wideners

Optional and absent by default. If you need one, create
`src/main/resources/aw/<mcversion>.accesswidener` and Loom picks it up
automatically; without the file the step is skipped entirely. An *empty*
placeholder file does not work.

## Conventions for this repo

- Textures are 16x16 unless there's a reason. Keep the pixel-art style consistent
  with the rest of the mod.
- Every new block, item and mob needs an entry in the language file
  (`assets/weird_mobs/lang/en_us.json`) or it shows up in-game as a raw id, which
  reads to him as "broken".
- Anything a player can tune goes in the config, not hardcoded.
- Keep it dependency-free. Fabric API only.

## Releasing

Handled by the **share-it** skill. Short version: bump `mod.version` in
`stonecutter.properties.toml`, update `CHANGELOG.md` in plain words, push a
`v<version>` tag, and the workflow publishes to Modrinth using the org's
`MODRINTH_TOKEN`.

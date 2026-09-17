# Weird Mobs

A mod full of weird mobs.

The first one is the **OKIE**. It is a robot — a tall flat panel with a light
blue face, two pink eyes and a red zigzag across it. It glides along the ground
on little legs you cannot see, it blinks, and if you talk to it, it teaches you
how to spell.

More weird mobs will go in here later.

## Minecraft versions

| version | Java |
|---|---|
| 1.21.8 | 21 |
| 1.21.4 | 21 |

Fabric only, and it needs Fabric API. Gradle downloads the JDK it needs, so only
one has to be installed.

## Building

```bash
./gradlew "Set active project to 1.21.8-fabric"
./gradlew "1.21.8-fabric:build"
```

Jars land in `versions/<version>-fabric/build/libs/`.

To build both versions:

```bash
./gradlew build
```

## Licence

MIT.

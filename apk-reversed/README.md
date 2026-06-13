# theme.apk - Reverse Engineering Results

**APK Package:** `com.huawei.livewallpaper.cosmos.superwallpaper`
**App Name:** Cosmos Live Wallpaper
**Version:** 1.0_2024052101 (build 1)
**Min SDK:** 28 (Android 9), **Target SDK:** 31 (Android 12)

## Overview

This is a **Huawei Cosmos Super Wallpaper** APK repackaged for **64Gram Desktop** (a Telegram client). It provides an animated live wallpaper with AOD (Always-On Display), lock screen, and launcher (home screen) states.

## Architecture

### APK Structure

```
theme.apk/
├── AndroidManifest.xml       # Declares wallpaper service
├── classes.dex                # Dalvik bytecode (decompiled to Java)
├── resources.arsc             # Compiled Android resources
├── assets/
│   ├── Cosmos/
│   │   ├── astc/frame/        # 120 ASTC texture frames (animation)
│   │   ├── data/              # JSON configs for scene/states
│   │   └── lock.jpg           # Lock screen static image
│   ├── fragment.frag          # GLSL fragment shader
│   ├── fragColor.frag         # Solid color fragment shader
│   ├── fragMask.frag          # Mask fragment shader
│   ├── image_list.frag/.vert  # Image list shaders (with HSV animation)
│   ├── mask.astc              # Mask texture
│   └── vertex.vert            # Vertex shader
├── res/                       # Android resources (icons, layouts, notifications)
└── META-INF/                  # Signatures & metadata
```

### Key Components (decompiled Java)

| Class | Path | Purpose |
|-------|------|---------|
| `CosmosWallpaper` | `com.huawei.livewallpaper.paradise.cosmos` | Main wallpaper service entry point |
| `CosmosManager` | `com.huawei.livewallpaper.paradise.cosmos` | State manager for AOD/Lock/Launcher |
| `Scene` | `com.huawei.superwallpaper.engine.scene` | Scene graph parser (JSON-based) |
| `ImageList` | `com.huawei.superwallpaper.engine` | ASTC/Png frame animation with OpenGL ES 3.2 |
| `BaseWallpaperManager` | `com.huawei.superwallpaper.engine.wallpaper` | State machine (AOD/Lock/Launcher) + animation |
| `BaseSuperWallpaperService` | `com.huawei.superwallpaper.engine.wallpaper` | File provider + command handling |

### Scene Graph (from `scene.json`)

```
RootNode
├── ClearColor ("clearColor")
└── ImageList ("main") → Cosmos/astc/frame/
```

### State Machine

Three states with transitions:
- **AOD** (Always-On Display) → frame index 0.0
- **LOCK** (Lock screen) → frame index 0.35
- **LAUNCHER** (Home screen) → frame index 1.0

Transitions: AOD↔LOCK (358ms), AOD↔LAUNCHER (1333ms), LOCK↔LAUNCHER (855ms)

### Render Engine

- **OpenGL ES 3.2** with GLSL shaders
- ASTC texture compression for animation frames
- HSV interpolation shader (`image_list.frag`) for smooth brightness transitions
- Camera system with 3D perspective transforms

### Output structure

```
~/theme_apk_reversed/
├── docs/           # Analysis documentation
├── src/            # Decompiled Java source (jadx)
│   ├── resources/  # Decoded Android resources
│   └── sources/    # Java source code
├── README.md       # This file
├── AndroidManifest.xml
├── assets/         # Raw assets
└── res/            # Raw resources
```

## Usage

This APK is a **Huawei-style super wallpaper** designed for Android systems that support the `huawei.super.wallpaper` metadata tag. The 120 ASTC frames create a parallax/animated effect on the home screen and lock screen.

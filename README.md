# OptiCam

A realistic CCTV surveillance system for Paper Minecraft servers. Buy cameras from a shop, place them in the world, wire them to redstone power, and view a live, motorized feed from a "computer" block — complete with pan/tilt limits and per-camera access codes.

## Features

- **Camera shop** (`/camshop`) — buy cameras with Vault economy.
- **Placeable cameras** with a base facing direction, saved to disk (`cameras.yml`).
- **Live viewing**: while watching a feed you're vanished, frozen, and see through the camera. Moving your mouse pans the camera like a real motorized mount, clamped to a realistic pan (±90°) and tilt (±30°) range with a smooth sweep.
- **Redstone power**: cameras only work while powered; cutting power drops the feed.
- **Access control**: per-camera codes/passwords entered in chat.
- **Admin tools**: `/opticam reload`.

## Requirements

- **Paper** 1.21.8+
- **Java 21**
- **Vault** plus an economy provider (e.g. EssentialsX). The plugin uses Vault for the camera shop and will not enable without it.

## Building

```bash
mvn clean package
```

Output: `target/OptiCam.jar` → drop into `plugins/` and restart.

## Commands

| Command | Permission | Description |
|---|---|---|
| `/camshop` | `opticam.use` | Open the camera shop |
| `/cam` | `opticam.use` | Camera control (e.g. exit a feed) |
| `/opticam reload` | `opticam.admin` | Reload configuration |

## Permissions

| Permission | Default | Description |
|---|---|---|
| `opticam.use` | everyone | Use cameras and the shop |
| `opticam.admin` | op | Administrative access |

## How it works

Cameras are backed by armor-stand/entity state and persisted in `cameras.yml`. The motor loop runs each tick while a player is viewing, easing the camera toward the player's look direction within the configured limits. Economy transactions are verified via Vault's `transactionSuccess()` so purchases only complete when the player is actually charged.

## License

[MIT](LICENSE) © 2026 Scappi

# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.5.2] - 2026-09-06

### Fixed

- Fixed conventional wrench tags (@meeprdib).

## [1.5.1] - 2026-09-04

### Fixed

- Fixed possible crash while ziplining.

## [1.5.0] - 2026-07-27

### Changed

- Added a small buffer after beginning to zipline so pressing space won't jump off the zipline.

### Fixed

- Ziplines now properly preserve momentum when dismounting.

## [1.4.0] - 2026-07-11

### Changed

- Adjusted default dismount behavior to not jump off Ziplines unless pressing space.
    - Can be reverted with the new config option.

## [1.3.0] - 2026-06-16

### Added

- Hoes can now also be used for ziplining by default.

### Changed

- Adjusted default hang offset.

### Fixed

- Fixed zipline items activating with a shield in the offhand.

## [1.2.2] - 2026-06-08

### Fixed

- Configs are now server-authoritative.
- Fixed ziplining desync.

## [1.2.1] - 2026-05-04

### Fixed

- Actually fixed fall damage accumulation.

## [1.2.0] - 2026-04-19

### Added

- Added a config option to control the cooldown after dismounting zipline.

### Fixed

- Fixed fall damage accumulation when dismounting zipline.

## [1.1.1] - 2026-04-17

### Fixed

- Fixed missing Cloth Config screen on Forge.

## [1.1.0] - 2026-04-13

### Added

- Added config for ziplining speed multiplier.

### Changed

- Holding shift when disconnecting from a Zipline will now drop you to the ground without jumping.

### Fixed

- Fixed incompatibility with Spice of Life: Valheim.

## [1.0.3] - 2026-02-27

### Fixed

* Fixed edge-case bug when reconnecting to ziplines from the ground.

## [1.0.2] - 2026-02-17

### Fixed

* Support per-chain slack adjustments with Reconnectible Chains for zipline visuals.

## [1.0.1] - 2026-02-17

### Fixed

* Updated Reconnectible Chains compat.

## [1.0.0] - 2026-02-10

### Changed

* Rewrote the mod and ported to multiloader Forge/Fabric.
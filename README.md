# Zipline: Rezipped!

<a href='https://files.minecraftforge.net'><img alt="forge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/forge_vector.svg"></a>
<a href='https://fabricmc.net'><img alt="fabric" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg"></a>
<a href='https://neoforged.net/'><img alt="neoforge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/neoforge_vector.svg"></a>

Ziplines inspired by the hit zipline game [Satisfactory](https://www.satisfactorygame.com/)!

![zoop](./assets/zoop.webp)

## Features

* **Momentum-Based Physics**: Gain speed going down, lose speed going up. Includes a toggle to disable realistic physics
  for those who prefer a more arcade-y feel.
* **Seamless Switching**: Look toward a connecting cable at a junction to automatically switch lines without stopping.
* **Durability**: Usage wears down your zipline tool (configurable).

## Usage

### How to Zip

1. **Equip a Zipline tool** (see *Compatibility* or *Customization* below).
2. **Aim** at a compatible cable, wire, or chain.
3. **Hold Right-Click** (Use Item) to attach and start zipping.
4. **Look** in the direction you want to travel to influence your momentum.
5. **Jump** to detach and launch yourself off the line.

### Controls

* **Attach**: Hold Use (right-click by default) near a cable.
* **Switch Lines**: While zipping, look toward the cable you want to merge onto.
* **Detach**: Release the Use button or jump.

## Configuration

This mod uses **Cloth Config** (optional but recommended) to allow customization of the mechanics. You can access the
config screen via Mod Menu or by editing `config/zipline.json`.

## Compatibility

This mod **does not add any cables on its own**. Instead, it turns existing cables from other mods into ziplines.

Supported mods include:

- [Reconnectible Chains](https://modrinth.com/mod/reconnectible-chains)
- [Create Crafts & Additions](https://modrinth.com/mod/createaddition)
- [Station Decoration (Minecraft Transit Railway addon)](https://modrinth.com/mod/station-decoration)

## Customization

### Adding Zipline Tools

This mod defines the specific tag `zipline:attachment`. Any item added to this tag will function as a zipline tool.
By default, this includes Pickaxes and items tagged as Wrenches.

You can add items to this tag via a Data Pack:
`data/zipline/tags/item/attachment.json`

```json
{
  "replace": false,
  "values": [
    "minecraft:stick",
    "create:red_rose_quartz_pickaxe"
  ]
}
```

### API

Developers can add support for their own cables by implementing the `Cable` interface and registering a `CableProvider`
via `Cables.registerProvider`.

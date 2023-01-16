# Full Block Chests

Turn chests into full blocks and make them openable even with a block above, like before Beta 1.8.

## Configuration

A config file is available at `config/fullblockchests.properties` after first launch. It contains two options:

- `reskinModdedChests`: If `false`, will only turn vanilla chests, trapped chests and ender chests into full blocks.
  Otherwise, all modded chests derived from the vanilla chest entity will be affected.
- `disableBlockedCheck`: If `true` then chests can always be opened, regardless of any solid block or cat standing on top.

## Q&A
### Nostalgic Tweaks does the same thing!
This mod has significant overlap with (and in fact is based on) Nostalgic Tweaks's chest "Eye Candy" tweaks,
but NT limits itself to vanilla chests (see [BlockEntityRenderDispatcherMixin](https://github.com/Adrenix/Nostalgic-Tweaks/blob/1.18.2/common/src/main/java/mod/adrenix/nostalgic/mixin/client/renderer/BlockEntityRenderDispatcherMixin.java)
& [ChestBlockMixin](https://github.com/Adrenix/Nostalgic-Tweaks/blob/1.18.2/common/src/main/java/mod/adrenix/nostalgic/mixin/common/world/level/block/ChestBlockMixin.java))
and does not change the behavior of chests covered by a solid block or a cat.

### Christmas chest skins don't work anymore!
Yes, I know. Feel free to fork this mod and find a clean way of implementing this with regular block model predicates.

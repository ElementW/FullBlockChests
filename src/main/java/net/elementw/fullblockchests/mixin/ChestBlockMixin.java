package net.elementw.fullblockchests.mixin;

import net.elementw.fullblockchests.ModConfig;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockMixin {
    @Contract(pure = true)
    private boolean isVanillaChest(BlockState state) {
        Class<?> clazz = state.getBlock().getClass();
        return clazz == ChestBlock.class || clazz == TrappedChestBlock.class;
    }

    @Inject(
            method = "getRenderType",
            at = @At("HEAD"),
            cancellable = true
    )
    private void FBC$getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> callback) {
        if (ModConfig.INSTANCE.reskinModdedChests || isVanillaChest(state)) {
            callback.setReturnValue(BlockRenderType.MODEL);
        }
    }

    @Inject(
            method = "getOutlineShape",
            at = @At("HEAD"),
            cancellable = true
    )
    private void FBC$getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> callback) {
        if (ModConfig.INSTANCE.reskinModdedChests || isVanillaChest(state)) {
            callback.setReturnValue(VoxelShapes.fullCube());
        }
    }

    @Inject(
            method = "isChestBlocked",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void FBC$isChestBlocked(WorldAccess world, BlockPos pos, CallbackInfoReturnable<Boolean> callback) {
        if (ModConfig.INSTANCE.disableBlockedCheck) {
            callback.setReturnValue(false);
        }
    }
}

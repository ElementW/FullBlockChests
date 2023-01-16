package net.elementw.fullblockchests.mixin;

import net.elementw.fullblockchests.ModConfig;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin {
    @Contract(pure = true)
    private boolean isVanillaEnderChest(BlockState state) {
        return state.getBlock().getClass() == EnderChestBlock.class;
    }

    @Inject(
            method = "getRenderType",
            at = @At("HEAD"),
            cancellable = true
    )
    private void FBC$getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> callback) {
        if (ModConfig.INSTANCE.reskinModdedChests || isVanillaEnderChest(state)) {
            callback.setReturnValue(BlockRenderType.MODEL);
        }
    }

    @Inject(
            method = "getOutlineShape",
            at = @At("HEAD"),
            cancellable = true
    )
    private void FBC$getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> callback) {
        if (ModConfig.INSTANCE.reskinModdedChests || isVanillaEnderChest(state)) {
            callback.setReturnValue(VoxelShapes.fullCube());
        }
    }

    @Redirect(
            method = "onUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"
            )
    )
    private BlockState FBC$onUse$getBlockState(World instance, BlockPos pos) {
        if (ModConfig.INSTANCE.disableBlockedCheck) {
            return Blocks.AIR.getDefaultState();
        }
        return instance.getBlockState(pos);
    }
}

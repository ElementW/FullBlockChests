package net.elementw.fullblockchests.mixin;

import net.elementw.fullblockchests.ModConfig;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.block.ChestAnimationProgress;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @Inject(
        method = "get(Lnet/minecraft/block/entity/BlockEntity;)Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;",
        at = @At("HEAD"),
        cancellable = true
    )
    private <E extends BlockEntity> void FBC$get(E blockEntity, CallbackInfoReturnable<@Nullable BlockEntityRenderer<E>> callback) {
        if (ModConfig.INSTANCE.reskinModdedChests) {
            if (blockEntity instanceof ChestAnimationProgress) {
                callback.setReturnValue(null);
            }
        } else {
            BlockEntityType<?> type = blockEntity.getType();
            if (type == BlockEntityType.CHEST || type == BlockEntityType.TRAPPED_CHEST || type == BlockEntityType.ENDER_CHEST) {
                callback.setReturnValue(null);
            }
        }
    }
}

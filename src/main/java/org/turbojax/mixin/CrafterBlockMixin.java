package org.turbojax.mixin;

import org.turbojax.LimitedMaceMod;
import org.turbojax.LimitedMaceState;
import net.minecraft.block.BlockState;
import net.minecraft.block.CrafterBlock;
import net.minecraft.block.entity.CrafterBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CrafterBlock.class)
public abstract class CrafterBlockMixin {

    @Inject(method = "transferOrSpawnStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/CrafterBlockEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;Lnet/minecraft/recipe/RecipeEntry;)V", at = @At("HEAD"), cancellable = true)
    private void limitedMace$blockCrafterMace(ServerWorld world, net.minecraft.util.math.BlockPos pos, CrafterBlockEntity be, ItemStack stack, BlockState state, RecipeEntry<?> recipe, CallbackInfo ci) {
        if (!stack.isOf(Items.MACE)) return;

        LimitedMaceState lm = LimitedMaceState.get(world.getServer());
        if (lm.getCrafted() == LimitedMaceMod.getMaxMaces()) {
            ci.cancel();
        } else {
            lm.setCrafted(lm.getCrafted() + 1);
            lm.markDirty();
        }
    }
}
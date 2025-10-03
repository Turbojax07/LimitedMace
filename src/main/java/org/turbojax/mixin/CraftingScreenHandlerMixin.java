package org.turbojax.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;              // <-- only RecipeEntry is needed
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.turbojax.LimitedMaceMod;

/**
 * After vanilla recomputes the crafting result (2x2 & 3x3),
 * if the world is locked and result is a mace, clear it.
 */
@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin {

    // Your environment expects the signature WITH a final RecipeEntry parameter:
    // updateResult(ScreenHandler, ServerWorld, PlayerEntity, RecipeInputInventory, CraftingResultInventory, @Nullable RecipeEntry)
    @Inject(
            method = "updateResult(Lnet/minecraft/screen/ScreenHandler;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/inventory/CraftingResultInventory;Lnet/minecraft/recipe/RecipeEntry;)V",
            at = @At("TAIL")
    )
    private static void limitedMace$hideMaceAfterFirst(
            ScreenHandler handler,
            World world,
            PlayerEntity player,
            RecipeInputInventory craftingInventory,
            CraftingResultInventory resultInventory,
            @Nullable RecipeEntry<?> recipe,         //  optional
            CallbackInfo ci
    ) {
        ItemStack out = resultInventory.getStack(0);
        boolean lockWorld = org.turbojax.LimitedMaceState.get(world.getServer()).getCrafted() == LimitedMaceMod.getMaxMaces();
        boolean guardTick = (player instanceof net.minecraft.server.network.ServerPlayerEntity spe)
                && org.turbojax.ClickGuard.isThisTick(spe, world.getServer().getOverworld());

        if (!out.isEmpty()
                && out.isOf(net.minecraft.item.Items.MACE)
                && (lockWorld || guardTick)) {
            resultInventory.setStack(0, net.minecraft.item.ItemStack.EMPTY);
        }

    }
}

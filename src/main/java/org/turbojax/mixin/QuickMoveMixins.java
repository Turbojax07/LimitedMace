package org.turbojax.mixin;

import org.turbojax.LimitedMaceMod;
import org.turbojax.LimitedMaceState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * One .java file that contains two small mixins:
 * - CraftingScreenHandlerQuickMove (3x3 crafting table)
 * - PlayerScreenHandlerQuickMove   (2x2 inventory crafting)
 *
 * Both intercept quickMove(...) (shift-click) on the result slot.
 * If it's the first ever mace: set the world flag and allow it.
 * Otherwise: block the move and show the message.
 */

// ======================= 3x3 Crafting Table =======================
@Mixin(CraftingScreenHandler.class)
class CraftingScreenHandlerQuickMove {

    // quickMove(PlayerEntity, int) -> ItemStack
    @Inject(
            method = "quickMove(Lnet/minecraft/entity/player/PlayerEntity;I)Lnet/minecraft/item/ItemStack;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void limitedMace$quickMove(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        CraftingScreenHandler self = (CraftingScreenHandler) (Object) this;

        // On the crafting table, index 0 is the result slot
        if (index < 0 || index >= self.slots.size()) return;
        Slot slot = self.getSlot(index);
        if (!(slot instanceof CraftingResultSlot)) return;

        ItemStack out = slot.getStack();
        if (!out.isOf(Items.MACE)) return;

        LimitedMaceState state = LimitedMaceState.get(serverPlayer.getServer());

        if (state.getCrafted() == LimitedMaceMod.getMaxMaces()) {
            String insert = LimitedMaceMod.getMaxMaces() + "mace" + (LimitedMaceMod.getMaxMaces() == 1 ? "" : "s");
            serverPlayer.sendMessage(Text.literal("Only " + insert + " can ever be crafted on this world."), false);
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        state.setCrafted(state.getCrafted() + 1);
        state.markDirty();
    }
}

// ======================= 2x2 Inventory =======================
@Mixin(PlayerScreenHandler.class)
class PlayerScreenHandlerQuickMove {

    // quickMove(PlayerEntity, int) -> ItemStack
    @Inject(
            method = "quickMove(Lnet/minecraft/entity/player/PlayerEntity;I)Lnet/minecraft/item/ItemStack;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void limitedMace$quickMove(PlayerEntity player, int index, CallbackInfoReturnable<ItemStack> cir) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        PlayerScreenHandler self = (PlayerScreenHandler) (Object) this;

        if (index < 0 || index >= self.slots.size()) return;
        Slot slot = self.getSlot(index);
        if (!(slot instanceof CraftingResultSlot)) return;

        ItemStack out = slot.getStack();
        if (!out.isOf(Items.MACE)) return;

        LimitedMaceState state = LimitedMaceState.get(serverPlayer.getServer());

        if (state.getCrafted() == LimitedMaceMod.getMaxMaces()) {
            String insert = LimitedMaceMod.getMaxMaces() + "mace" + (LimitedMaceMod.getMaxMaces() == 1 ? "" : "s");
            serverPlayer.sendMessage(Text.literal("Only " + insert + " can ever be crafted on this world."), false);
            cir.setReturnValue(ItemStack.EMPTY);
            return;
        }

        state.setCrafted(state.getCrafted() + 1);
        state.markDirty();
    }
}

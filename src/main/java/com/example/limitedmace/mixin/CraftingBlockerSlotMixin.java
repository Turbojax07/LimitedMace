package com.example.limitedmace.mixin;

import com.example.limitedmace.LimitedMaceState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class CraftingBlockerSlotMixin {

    @Inject(method = "canTakeItems", at = @At("HEAD"), cancellable = true)
    private void limitedMace$guard(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        if (!(((Object) this) instanceof CraftingResultSlot resultSlot)) return;

        ItemStack out = resultSlot.getStack();
        if (!out.isOf(Items.MACE)) return;

        ServerWorld world = serverPlayer.getWorld();
        if (LimitedMaceState.get(world).crafted) {
            serverPlayer.sendMessage(Text.literal("Only one mace can ever be crafted on this world."), false);
            cir.setReturnValue(false);
        }
    }
}

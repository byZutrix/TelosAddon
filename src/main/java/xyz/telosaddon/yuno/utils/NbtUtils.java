package xyz.telosaddon.yuno.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

public class NbtUtils {
    public static Optional<String> getMythicType(ItemStack stack) {
        NbtComponent nbtComponent = stack.getComponents().get(DataComponentTypes.CUSTOM_DATA);
        if(nbtComponent == null) return Optional.empty();

        NbtCompound nbtData = nbtComponent.copyNbt();
        if(!nbtData.contains("MYTHIC_TYPE")) return Optional.empty();

        String itemType = nbtData.getString("MYTHIC_TYPE");
        return Optional.of(itemType);
    }
}

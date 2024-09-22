package xyz.telosaddon.yuno.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import xyz.telosaddon.yuno.renderer.CircleRenderer;
import xyz.telosaddon.yuno.utils.Config;

import java.util.function.Function;
import java.util.function.Supplier;

public class ShowRangeFeature extends AbstractFeature {
	private ItemStack previousItem = null;
	private float radius = Float.NaN;
	private final CircleRenderer circleRenderer;
	private final Function<PlayerInventory, ItemStack> itemGetter;

	private int color = 0x99FF0000;

	public ShowRangeFeature(Config config, Function<PlayerInventory, ItemStack> itemGetter, String itemSlotName) {
		super(config, "Show" + itemSlotName + "RangeFeature");
		this.circleRenderer = new CircleRenderer();
		this.itemGetter = itemGetter;
	}

	private float parseRadius(ItemStack itemStack) {
		var loreComponent = itemStack.getComponents().get(DataComponentTypes.LORE);
		if (loreComponent == null) return Float.NaN;
		for (var line : loreComponent.lines()) {
			if (!line.getString().contains("Range:")) continue;
			try {
				String num = line.getString().split("Range:")[1].trim();
				return Float.parseFloat(num);
			} catch (ArrayIndexOutOfBoundsException | NumberFormatException ignored) {
			}
		}
		return Float.NaN;
	}

	private void checkMainHand(MinecraftClient client) {
		assert client.player != null;
		var inventory = client.player.getInventory();
		if (inventory == null) return;
		ItemStack itemToCheck = this.itemGetter.apply(inventory);
		if (!itemToCheck.equals(this.previousItem)) {
			previousItem = itemToCheck;
			float radius = parseRadius(itemToCheck);
			this.radius = radius;
			if (Float.isNaN(radius)) {
				this.circleRenderer.clearAngles();
			} else {
				this.circleRenderer.setRadius(radius);
			}
		}
	}

	public void tick() {
		if (!this.isEnabled()) return;
		var client = MinecraftClient.getInstance();
		if (client.player == null) return;
		checkMainHand(client);
	}

	public void draw(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ClientPlayerEntity player) {
		if (!this.isEnabled()) return;
		this.circleRenderer.drawCircle(matrices,
				vertexConsumers,
				player,
				this.getConfig().getInteger(this.getFeatureName() + "Color"),
				this.getConfig().getInteger(this.getFeatureName() + "Height"));

	}
}

package xyz.telosaddon.yuno.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import xyz.telosaddon.yuno.renderer.CircleRenderer;
import xyz.telosaddon.yuno.renderer.IRenderer;
import xyz.telosaddon.yuno.renderer.LineRenderer;
import xyz.telosaddon.yuno.utils.config.Config;

import java.util.function.Function;

public class ShowRangeFeature extends AbstractFeature {
	private ItemStack previousItem = null;
	private final IRenderer circleRenderer;
	private final Function<PlayerInventory, ItemStack> itemGetter;


	public ShowRangeFeature(Config config, Function<PlayerInventory, ItemStack> itemGetter, String itemSlotName) {
		super(config, "Show" + itemSlotName + "RangeFeature");
		this.circleRenderer = new LineRenderer();
		this.itemGetter = itemGetter;
	}

	private float parseRadius(ItemStack itemStack) {
		var loreComponent = itemStack.getComponents().get(DataComponentTypes.LORE);
		if (loreComponent == null) return Float.NaN;
		for (var line : loreComponent.lines()) {
			if (!line.getString().contains("Range:")) continue;
			try {
				String numWithPossibleExt = line.getString().split("Range:")[1].trim();
				// Handle <num>(+3) case for EX
				String num = numWithPossibleExt.split("\\(")[0].trim();
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
			this.circleRenderer.setRadius(radius);
		}
	}

	public void tick() {
		if (!this.isEnabled()) return;
		var client = MinecraftClient.getInstance();
		if (client.player == null) return;
		checkMainHand(client);
	}

	public void draw(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, ClientPlayerEntity player) {
		if (!this.isEnabled()) return;
		this.circleRenderer.draw(tickDelta,
				matrices,
				vertexConsumers,
				player,
				this.getConfig().getInteger(this.getFeatureName() + "Color"),
				this.getConfig().getDouble(this.getFeatureName() + "Height").floatValue());

	}
}

package xyz.telosaddon.yuno.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.telosaddon.yuno.TelosAddon;
import xyz.telosaddon.yuno.renderer.CircleRenderer;
import xyz.telosaddon.yuno.renderer.IRenderer;
import xyz.telosaddon.yuno.renderer.LineRenderer;
import xyz.telosaddon.yuno.utils.ItemType;
import xyz.telosaddon.yuno.utils.config.Config;

import java.util.List;
import java.util.function.Function;

public class ShowRangeFeature extends AbstractFeature {
	private ItemStack previousItem = null;
	private List<IRenderer> renderers;
	private final Function<PlayerInventory, ItemStack> itemGetter;
	private float radius = Float.NaN;


	public ShowRangeFeature(Config config, Function<PlayerInventory, ItemStack> itemGetter, String itemSlotName) {
		super(config, "Show" + itemSlotName + "RangeFeature");
		this.itemGetter = itemGetter;
		this.setRangeType(RangeViewType.valueOf(config.getString("Show" + itemSlotName + "RangeFeatureViewType")));
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

	private void checkItem(@NotNull ClientPlayerEntity player) {
		var inventory = player.getInventory();
		if (inventory == null) return;
		ItemStack itemToCheck = this.itemGetter.apply(inventory);
		if (!itemToCheck.equals(this.previousItem)) {
			ItemType itemType = ItemType.fromItemStack(itemToCheck);
			previousItem = itemToCheck;
			float offset = 0;
			if (itemType == null) radius = parseRadius(itemToCheck);
			else {
				// Hacks for specific items
				switch (itemType) {
					case UT_HERALD_ESSENCE, EX_HERALD_ESSENCE -> {
						radius = 6;
						offset = 3;
					}
					case EX_AYAHUASCA_FLASK, UT_AYAHUASCA_FLASK -> radius = 8;
					default -> radius = parseRadius(itemToCheck);

				}
			}
			float finalOffset = offset;
			this.renderers.forEach(r -> r.setRadius(radius));
			this.renderers.forEach(r -> r.setOffset(finalOffset));
		}
	}

	public void tick() {
		if (!this.isEnabled()) return;
		var client = MinecraftClient.getInstance();
		if (client.player == null) return;
		checkItem(client.player);
	}

	public float getRadius() {
		return this.radius;
	}

	public void draw(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, ClientPlayerEntity player, float dy) {
		if (!this.isEnabled()) return;
		this.renderers.forEach(r -> r.draw(tickDelta,
				matrices,
				vertexConsumers,
				player,
				this.getConfig().getInteger(this.getFeatureName() + "Color"),
				this.getConfig().getDouble(this.getFeatureName() + "Height").floatValue() + dy));

	}

	public void setRangeType(RangeViewType type) {
		this.renderers = switch (type) {
			case CIRCLE -> List.of(new CircleRenderer());
			case LINE -> List.of(new LineRenderer());
			case BOTH -> List.of(new CircleRenderer(), new LineRenderer());
		};
		var player = MinecraftClient.getInstance().player;
		if(player == null) {
			TelosAddon.LOGGER.warning("Got client.player == null even though the guy did so through a GUI?");
			return;
		}
		this.previousItem = null;
		checkItem(player);
	}

	public enum RangeViewType {
		CIRCLE,
		LINE,
		BOTH;
	}
}

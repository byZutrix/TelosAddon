package xyz.telosaddon.yuno.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import xyz.telosaddon.yuno.TelosAddon;

public class RangeRenderer {
	private static final Identifier RENDER_IDENTIFIER = Identifier.of("showtelosrange", "range_draw");

	public static void init(){
		WorldRenderEvents.AFTER_TRANSLUCENT.addPhaseOrdering(Event.DEFAULT_PHASE, RENDER_IDENTIFIER);
		WorldRenderEvents.AFTER_TRANSLUCENT.register(RENDER_IDENTIFIER, RangeRenderer::draw);
	}

	private static void draw(WorldRenderContext context){
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		assert player != null;
		var matrixStack = context.matrixStack();
		assert matrixStack != null;



		float tickDelta = context.tickCounter().getTickDelta(false);
		double px = MathHelper.lerp(tickDelta, player.lastRenderX, player.getX());
		double py = MathHelper.lerp(tickDelta, player.lastRenderY, player.getY());
		double pz = MathHelper.lerp(tickDelta, player.lastRenderZ, player.getZ());
		Vec3d playerPos = new Vec3d(px, py, pz);

		Vec3d cameraPos = context.camera().getPos();
		playerPos = playerPos.subtract(cameraPos);

		matrixStack.push();
		matrixStack.translate(playerPos.x, playerPos.y, playerPos.z);

		VertexConsumerProvider vertexConsumers = context.consumers();
		TelosAddon.getInstance().getShowMainRangeFeature().draw(tickDelta, context.matrixStack(), vertexConsumers, player);
		TelosAddon.getInstance().getShowOffHandFeature().draw(tickDelta, context.matrixStack(), vertexConsumers, player);

		matrixStack.pop();
	}
}


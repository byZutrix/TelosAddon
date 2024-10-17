package xyz.telosaddon.yuno.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import xyz.telosaddon.yuno.TelosAddon;

public class RangeRenderer {
	private static final Identifier RENDER_IDENTIFIER = Identifier.of("showtelosrange", "range_draw");

	public static void init() {
		WorldRenderEvents.AFTER_TRANSLUCENT.addPhaseOrdering(Event.DEFAULT_PHASE, RENDER_IDENTIFIER);
		WorldRenderEvents.AFTER_TRANSLUCENT.register(RENDER_IDENTIFIER, RangeRenderer::draw);
	}

	private static void draw(WorldRenderContext context) {
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
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-player.getYaw(tickDelta)));

		VertexConsumerProvider vertexConsumers = context.consumers();
		var mainRangeFeature = TelosAddon.getInstance().getShowMainRangeFeature();
		var offHandRangeFeature = TelosAddon.getInstance().getShowOffHandFeature();

		/*
		Holy this is dirty. Basically, opengl doesn't like when you have 2 objects at the EXACT same distance to camera.
		With fp arithmetic (i'm guessing), this leads to inconsistencies, where they kind of uglily bend together.
		So, I just add a smidge, like a VERY tiny smidge of height (so it's not noticeable by the player) to the one I
		want in front, so the depth test will always give priority to the one I want on front. At least, this allows
		the code to kinda be render-order independent. Still, very dirty, very demure.
		 */
		var frontDy = 0.0001f;
		// By semantic, NaN > x is ALWAYS false
		var isMainRangeGreater = mainRangeFeature.getRadius() > offHandRangeFeature.getRadius();
		mainRangeFeature.draw(tickDelta, context.matrixStack(), vertexConsumers, player, isMainRangeGreater ? 0 : frontDy);
		offHandRangeFeature.draw(tickDelta, context.matrixStack(), vertexConsumers, player, isMainRangeGreater ? frontDy : 0);

		matrixStack.pop();
	}
}


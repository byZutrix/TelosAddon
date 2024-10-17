package xyz.telosaddon.yuno.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.Locale;
import java.util.OptionalDouble;

public class LineRenderer implements IRenderer{

	@Override
	public void draw(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, int color, float height) {
		float dy = (entity.isInSneakingPose() ? 0.125f : 0) + height;

		RenderLayer layer = LineRendererPhases.LINE_LAYER;

		VertexConsumer vertices = vertexConsumers.getBuffer(layer);

		matrices.push();
		drawLine(tickDelta, matrices, vertices, dy, color);
		matrices.pop();
	}

	private void drawLine(float tickDelta, MatrixStack matrices, VertexConsumer vertexConsumer, float dy, int argb){
		MatrixStack.Entry entry = matrices.peek();
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		assert player != null;

		double pYaw = player.getYaw(tickDelta);
		double angleRad = -Math.toRadians(pYaw);
		Vector3f unit = new Vector3f((float)Math.sin(angleRad), 0, (float)Math.cos(angleRad));
		unit.mul(10f);

		vertexConsumer.vertex(entry, new Vector3f(0f, 0f, 0f)).color(argb).normal(matrices.peek(), -unit.x, 0.0f, -unit.z);
		vertexConsumer.vertex(entry, unit).color(argb).normal(matrices.peek(), -unit.x, 0.0f, -unit.z);
	}

	@Override
	public void setRadius(float radius) {

	}

	private static class LineRendererPhases extends RenderPhase {
		static final RenderLayer.MultiPhase LINE_LAYER = makeLayer();

		private static RenderLayer.MultiPhase makeLayer() {
			String name = "showtelosrange_line_" + VertexFormat.DrawMode.QUADS.name().toLowerCase(Locale.ROOT);

			return RenderLayer.of(
					name,
					VertexFormats.LINES,
					VertexFormat.DrawMode.LINES,
					1536,
					RenderLayer.MultiPhaseParameters.builder()
							.program(LINES_PROGRAM)
							.lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(10f)))
							.layering(VIEW_OFFSET_Z_LAYERING)
							.transparency(TRANSLUCENT_TRANSPARENCY)
							.target(ITEM_ENTITY_TARGET)
							.writeMaskState(ALL_MASK)
							.cull(DISABLE_CULLING)
							.build(false));
		}

		// required for COLOR_PROGRAM, etc.
		// see #makeLayer
		private LineRendererPhases(String name, Runnable beginAction, Runnable endAction) {
			super(name, beginAction, endAction);
		}
	}
}

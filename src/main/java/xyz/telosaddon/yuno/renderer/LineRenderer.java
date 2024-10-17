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

	private float radius = Float.NaN;

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
		if(Float.isNaN(radius))
			return;
		MatrixStack.Entry entry = matrices.peek();
		float width = 0.2f;

		vertexConsumer.vertex(entry, width/2, dy, 0f).color(argb).normal(entry, 0.0f, 0.0f, 0);
		vertexConsumer.vertex(entry, width/2, dy, this.radius).color(argb).normal(entry, 0.0f, 0.0f, 0);
		vertexConsumer.vertex(entry, -width/2, dy, this.radius).color(argb).normal(entry, 0.0f, 0.0f, 0);
		vertexConsumer.vertex(entry, -width/2, dy, 0f).color(argb).normal(entry, 0.0f, 0.0f, 0);


	}

	@Override
	public void setRadius(float radius) {
		this.radius = radius;
	}

	private static class LineRendererPhases extends RenderPhase {
		static final RenderLayer.MultiPhase LINE_LAYER = makeLayer();

		private static RenderLayer.MultiPhase makeLayer() {
			String name = "showtelosrange_line_" + VertexFormat.DrawMode.LINES.name().toLowerCase(Locale.ROOT);

			return RenderLayer.of(
					name,
					VertexFormats.POSITION_COLOR,
					VertexFormat.DrawMode.QUADS,
					1536,
					false,
					true,
					RenderLayer.MultiPhaseParameters.builder()
							.program(RenderPhase.COLOR_PROGRAM)
							.lineWidth(new RenderPhase.LineWidth(OptionalDouble.of(10f)))
							.layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
							.transparency(RenderPhase.NO_TRANSPARENCY)
							.target(ITEM_ENTITY_TARGET)
							.writeMaskState(ALL_MASK)
							.cull(DISABLE_CULLING)
							.depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
							.build(false));
		}

		// required for COLOR_PROGRAM, etc.
		// see #makeLayer
		private LineRendererPhases(String name, Runnable beginAction, Runnable endAction) {
			super(name, beginAction, endAction);
		}
	}
}

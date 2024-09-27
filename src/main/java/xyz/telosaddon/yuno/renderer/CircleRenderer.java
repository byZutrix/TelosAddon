package xyz.telosaddon.yuno.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.minecraft.client.render.RenderPhase.*;

public class CircleRenderer {
	private final List<Angle> angles = new ArrayList<>();

	public void drawCircle(MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, int color, float height) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;

		float dy = (entity.isInSneakingPose() ? 0.125f : 0) + height;

		RenderLayer layer = CircleRendererPhases.DEBUG_QUADS;;

		VertexConsumer vertices = vertexConsumers.getBuffer(layer);

		matrices.push();
		drawCircleQuad(matrices, vertices, dy, color);
		matrices.pop();
	}

	private void drawCircleLineStrip(MatrixStack matrices, VertexConsumer vertices, float dy, int argb) {
		Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

		for (Angle angle : angles) {
			vertices.vertex(positionMatrix, angle.dx, dy, angle.dz).color(argb).normal(matrices.peek(), 0.0f, 0.0f, 0.0f);
		}

		Angle first = angles.getFirst(); // closes the circle
		vertices.vertex(positionMatrix, first.dx, dy, first.dz).color(argb).normal(matrices.peek(), 0.0f, 0.0f, 0.0f);
	}

	private void drawCircleQuad(MatrixStack matrices, VertexConsumer vertices, float dy, int argb) {
		Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

		for (int i = 1; i < angles.size() + 1; i++) {
			Angle angle = angles.get(i % angles.size());
			Angle prevAngle = angles.get(i - 1);

			vertices.vertex(positionMatrix, prevAngle.dx, dy, prevAngle.dz).color(argb).normal(matrices.peek(), 0.0f, 0.0f, 0.0f);
			vertices.vertex(positionMatrix, prevAngle.farDx, dy, prevAngle.farDz).color(argb).normal(matrices.peek(), 0.0f, 0.0f, 0.0f);
			vertices.vertex(positionMatrix, angle.farDx, dy, angle.farDz).color(argb).normal(matrices.peek(), 0.0f, 0.0f, 0.0f);
			vertices.vertex(positionMatrix, angle.dx, dy, angle.dz).color(argb).normal(matrices.peek(), 0.0f, 0.0f, 0.0f);
		}
	}

	private void drawCircleTriangleFan(MatrixStack matrices, VertexConsumer vertices, float dy, int argb) {
		Matrix4f positionMatrix = matrices.peek().getPositionMatrix();

		vertices.vertex(positionMatrix, 0, dy, 0).color(argb).normal(matrices.peek(), 0.0f, 0.0f, 0.0f);
		drawCircleLineStrip(matrices, vertices, dy, argb);
	}

	public void setRadius(float radius){
		computeAngles(radius);
	}

	public void clearAngles(){
		angles.clear();
	}

	public void computeAngles(float radius) {
		angles.clear();
		int segments = 100;
		float thickness = 0.2f;
		for (int i = 0; i < segments; i++) {
			float angle = 2.0f * MathHelper.PI * ((float) i / segments);
			float dst = radius - (thickness / 2);

			float dx = dst * MathHelper.sin(angle);
			float dz = dst * MathHelper.cos(angle);

			float farDx = (dst + thickness) * MathHelper.sin(angle);
			float farDz = (dst + thickness) * MathHelper.cos(angle);

			angles.add(new Angle(dx, dz, farDx, farDz));
		}
	}

	private record Angle(float dx, float dz, float farDx, float farDz) {
		public Angle(float dx, float dz) {
			this(dx, dz, 0, 0);
		}
	}

	private static class CircleRendererPhases extends RenderPhase{
		static final RenderLayer.MultiPhase DEBUG_LINE_STRIP = makeLayer(VertexFormat.DrawMode.DEBUG_LINE_STRIP);
		static final RenderLayer.MultiPhase DEBUG_QUADS = makeLayer(VertexFormat.DrawMode.QUADS);
		static final RenderLayer.MultiPhase TRIANGLE_FAN = makeLayer(VertexFormat.DrawMode.TRIANGLE_FAN);

		private static RenderLayer.MultiPhase makeLayer(VertexFormat.DrawMode mode) {
			String name = "showtelosrange_" + mode.name().toLowerCase(Locale.ROOT);

			return RenderLayer.of(name, VertexFormats.POSITION_COLOR, mode, 1536, false, true,
					RenderLayer.MultiPhaseParameters.builder()
							.program(COLOR_PROGRAM)
							.transparency(TRANSLUCENT_TRANSPARENCY)
							.cull(ENABLE_CULLING)
							.lightmap(ENABLE_LIGHTMAP)
							.overlay(ENABLE_OVERLAY_COLOR)
							.writeMaskState(COLOR_MASK)
							.depthTest(LEQUAL_DEPTH_TEST)
							.layering(VIEW_OFFSET_Z_LAYERING)
							.build(false)
			);
		}

		// required for COLOR_PROGRAM, etc.
		// see #makeLayer
		private CircleRendererPhases(String name, Runnable beginAction, Runnable endAction) {
			super(name, beginAction, endAction);
		}
	}
}
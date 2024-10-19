package xyz.telosaddon.yuno.renderer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import xyz.telosaddon.yuno.TelosAddon;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CircleRenderer implements IRenderer{


	private final List<Angle> angles = new ArrayList<>();
	private float offset = 0;

	@Override
	public void draw(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, int color, float height) {
		float dy = (entity.isInSneakingPose() ? 0.125f : 0) + height;

		RenderLayer layer = CircleRendererPhases.DEBUG_QUADS;

		VertexConsumer vertices = vertexConsumers.getBuffer(layer);

		matrices.push();
		matrices.translate(0, 0, this.offset);
		drawCircleQuad(matrices, vertices, dy, color);
		matrices.pop();
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

	@Override
	public void setRadius(float radius){
		if(Float.isNaN(radius))
			clearAngles();
		else
			computeAngles(radius);
	}

	@Override
	public void setOffset(float offset) {
		this.offset = offset;
	}

	private void clearAngles(){
		angles.clear();
	}

	private void computeAngles(float radius) {
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
		static final RenderLayer.MultiPhase DEBUG_QUADS = makeLayer();

		private static RenderLayer.MultiPhase makeLayer() {
			String name = "showtelosrange_circle_" + VertexFormat.DrawMode.QUADS.name().toLowerCase(Locale.ROOT);

			return RenderLayer.of(name,
					VertexFormats.POSITION_COLOR,
					VertexFormat.DrawMode.QUADS,
					1536,
					false,
					true,
					RenderLayer.MultiPhaseParameters.builder()
							.program(COLOR_PROGRAM)
							.layering(VIEW_OFFSET_Z_LAYERING)
							.transparency(NO_TRANSPARENCY)
							.target(ITEM_ENTITY_TARGET)
							.writeMaskState(ALL_MASK)
							.cull(DISABLE_CULLING)
//							.lightmap(ENABLE_LIGHTMAP)
//							.overlay(ENABLE_OVERLAY_COLOR)
//							.depthTest(LEQUAL_DEPTH_TEST)
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
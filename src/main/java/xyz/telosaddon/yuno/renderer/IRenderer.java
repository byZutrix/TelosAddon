package xyz.telosaddon.yuno.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public interface IRenderer {
	void draw(float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity entity, int color, float height);

	/**
	 * Sets the radius/length of the specific shape renderer.
	 * Putting NaN into this function will make it so it will not render anymore
	 * @param radius
	 */
	void setRadius(float radius);

}

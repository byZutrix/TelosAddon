package xyz.telosaddon.yuno.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.telosaddon.yuno.TelosAddon;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
	@Shadow @Final private BufferBuilderStorage bufferBuilders;

	@Inject(method = "render", at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/render/WorldRenderer;checkEmpty(Lnet/minecraft/client/util/math/MatrixStack;)V"))
	public void renderFirstPersonCircle(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local(ordinal = 0) MatrixStack matrices) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		assert player != null;

		float tickDelta = tickCounter.getTickDelta(false);
		double px = MathHelper.lerp(tickDelta, player.lastRenderX, player.getX());
		double py = MathHelper.lerp(tickDelta, player.lastRenderY, player.getY());
		double pz = MathHelper.lerp(tickDelta, player.lastRenderZ, player.getZ());
		Vec3d playerPos = new Vec3d(px, py, pz);

		Vec3d cameraPos = camera.getPos();
		playerPos = playerPos.subtract(cameraPos);

		matrices.push();
		matrices.translate(playerPos.x, playerPos.y, playerPos.z);

		VertexConsumerProvider vertexConsumers = this.bufferBuilders.getEntityVertexConsumers();
		TelosAddon.getInstance().getShowMainRangeFeature().draw(matrices, vertexConsumers, player);
		TelosAddon.getInstance().getShowOffHandFeature().draw(matrices, vertexConsumers, player);

		matrices.pop();
	}
}

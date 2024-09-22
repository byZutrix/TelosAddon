package xyz.telosaddon.yuno.renderer;


import net.minecraft.client.render.*;

import java.util.Locale;

public class CircleRendererPhases extends RenderPhase {
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


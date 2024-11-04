package xyz.telosaddon.yuno.ui.tabs;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import xyz.telosaddon.yuno.TelosAddon;

public class TeleportMenuScreen extends BaseOwoScreen<FlowLayout> {
    final String[] serverNames = {"Hub-1", "Hub-2", "Hub-3", "Hub-4", "Hub-5", "Hub-6", "Hub-7",
            "Galla", "Twilight", "Epenon", "Spire", "Eternia", "Terra", "Develyn",
            "Xeros", "Wahr", "Walms", "Yollixar", "Valoria", "Nymeris", "Kyndra",
            "Wynnthera", "Runedar", "Quinthor", "Pyrelia", "Ashburn", "Xelia", "Skyblock"};


    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER)
                .verticalAlignment(VerticalAlignment.CENTER)
                .padding(Insets.of(5));



        rootComponent.child(
                Containers.verticalFlow(Sizing.content(), Sizing.content())
                        .child(Components.label(Text.literal("Teleport Menu")))
                        .child(
                                Containers.grid(Sizing.content(), Sizing.content(),4,7)

                                        .<GridLayout>configure(layout ->{
                                            for(int i = 0; i < 7; i++){
                                                for(int j = 0; j < 4; j++) {
                                                    int finalI = i;
                                                    int finalJ = j;
                                                    layout.child(Components.button(Text.literal(serverNames[finalJ*7+finalI]), button -> {
                                                        if (client == null || client.player == null) return;
                                                        client.player.networkHandler.sendChatCommand("joinq " + serverNames[finalJ*7+finalI]);
                                                    }).renderer(ButtonComponent.Renderer.flat(
                                                            new java.awt.Color(0, 0, 0, 150).getRGB(),
                                                            TelosAddon.getInstance().getConfig().getInteger("FillColor"),
                                                            new java.awt.Color(0, 0, 0, 50).getRGB()))
                                                            .sizing(Sizing.fixed(55), Sizing.fixed(20)).margins(Insets.of(5)), j, i
                                                    );
                                                }
                                            }
                                        })
                                        .surface(Surface.DARK_PANEL)
                                        .verticalAlignment(VerticalAlignment.CENTER)
                                        .horizontalAlignment(HorizontalAlignment.CENTER)
                                        .margins(Insets.of(5))
                        )
                        .margins(Insets.of(30))
                        .verticalAlignment(VerticalAlignment.CENTER)
                        .horizontalAlignment(HorizontalAlignment.CENTER)



        );
    }
}

package xyz.telosaddon.yuno.utils;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FontHelper {

    public static Text toCustomFont(String text, String fontName) {
        if(fontName.isEmpty() || !isCustomFont(fontName) )
            return Text.literal(text);
        return Text.literal(text).setStyle(Style.EMPTY.withFont(Identifier.of("telosaddon:" + fontName)));
    }

    private static boolean isCustomFont(String fontName) {
        return fontName.equalsIgnoreCase("roboto")
                || fontName.equalsIgnoreCase("nokiacf")
                || fontName.equalsIgnoreCase("arial")
                || fontName.equalsIgnoreCase("comic_sans")
                || fontName.equalsIgnoreCase("silkscreen");
    }

}

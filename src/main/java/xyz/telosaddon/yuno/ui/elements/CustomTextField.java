package xyz.telosaddon.yuno.ui.elements;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import xyz.telosaddon.yuno.ui.AbstractCustomElement;
import xyz.telosaddon.yuno.utils.FontHelper;

import java.awt.*;
import java.util.function.Predicate;

public class CustomTextField extends AbstractCustomElement {

    private int x;
    private int y;
    private int width;
    private int height;
    private String text = "";
    private String placeHolderText;
    private boolean isFocused = false;
    private int maxLength = 28;
    private int cursorPos = 0;
    private Predicate<Character> inputFilter = character -> true;
    private long lastBlinkTime = 0;
    private boolean isCursorVisible = false;
    private int selectionStart = -1;
    private int selectionEnd = -1;

    private long lastClickTime = 0;
    private boolean isSelecting = false;

    public CustomTextField(int x, int y, int width, int height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.placeHolderText = text;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer tr = getMinecraftClient().textRenderer;

        context.fill(this.x, this.y, this.x + this.width, this.y + this.height, getConfig().getInteger("FillColor"));
        context.drawBorder(this.x, this.y, this.width, this.height, getConfig().getInteger("BorderColor"));

        String displayText = tr.trimToWidth(this.text, this.width - 8);
        Text textFieldText = FontHelper.toCustomFont(displayText, getConfig().getString("Font"));

        if(this.text.isEmpty() && !this.isFocused) {
            Text placeHolderText = FontHelper.toCustomFont(this.placeHolderText, getConfig().getString("Font"));
            context.drawText(tr, placeHolderText, this.x + 4, this.y + (this.height - 8) / 2, new Color(150, 150, 150).getRGB(), true);
        } else {
            context.drawText(tr, textFieldText, this.x + 4, this.y + (this.height - 8) / 2, 0xFFFFFF, true);
        }

        if(this.isFocused) {

            if(selectionStart != -1 && selectionEnd != -1) {
                int start = Math.min(this.selectionStart, this.selectionEnd);
                int end = Math.max(this.selectionStart, this.selectionEnd);
                Text startText = FontHelper.toCustomFont(displayText.substring(0, start), getConfig().getString("Font"));
                Text endText = FontHelper.toCustomFont(displayText.substring(0, end), getConfig().getString("Font"));
                int startX = this.x + 4 + tr.getWidth(startText);
                int endX = this.x + 4 + tr.getWidth(endText);
                context.fill(startX, this.y + 5, endX, this.y + this.height - 5, new Color(255, 255, 255, 75).getRGB());
            }

            updateCursorVisibility();
            if(this.isCursorVisible) {
                Text cursorText = FontHelper.toCustomFont(displayText.substring(0, this.cursorPos), getConfig().getString("Font"));
                int cursorX = this.x + 4 + tr.getWidth(cursorText);
                context.fill(cursorX, this.y + 5, cursorX + 1, this.y + this.height - 5, new Color(255, 255, 255, 200).getRGB());
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0 && isHovered((int) mouseX, (int) mouseY)) {

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastClickTime < 250) {
                handleDoubleClick(mouseX);
            } else {
                this.isFocused = true;
                this.unselect();
                setCursorPos(mouseX);
            }

            lastClickTime = currentTime;

            getSoundManager().playSound("button_click");

            return true;
        } else {
            this.isFocused = false;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(button == 0 && this.isFocused) {
            if(!this.isSelecting) {
                this.isSelecting = true;
                this.selectionStart = this.cursorPos;
            }
            setCursorPos(mouseX);
            this.selectionEnd = this.cursorPos;
            return true;
        }
        return false;
    }

    @Override
    public void keyPressed(int keyCode, int scanCode, int modifiers) {
        if(!isFocused) return;

        switch (keyCode) {

            case GLFW.GLFW_KEY_BACKSPACE -> this.deleteText();
            case GLFW.GLFW_KEY_DELETE -> {
                if(this.cursorPos < this.text.length())
                    this.text = this.text.substring(0, this.cursorPos) + this.text.substring(this.cursorPos + 1);
            }
            case GLFW.GLFW_KEY_LEFT -> this.moveCursorLeft(Screen.hasShiftDown(), Screen.hasControlDown());
            case GLFW.GLFW_KEY_RIGHT -> this.moveCursorRight(Screen.hasShiftDown(), Screen.hasControlDown());
            case GLFW.GLFW_KEY_END -> this.cursorPos = this.text.length();
            case GLFW.GLFW_KEY_HOME -> this.cursorPos = 0;
            case GLFW.GLFW_KEY_ENTER -> this.isFocused = false;
            case GLFW.GLFW_KEY_V -> {
                if(Screen.hasControlDown())
                    this.pasteFromClipboard();
            }
            case GLFW.GLFW_KEY_C -> {
                if(Screen.hasControlDown() && !this.getSelectedText().isEmpty())
                    getMinecraftClient().keyboard.setClipboard(this.getSelectedText());
            }
            case GLFW.GLFW_KEY_X -> {
                if(Screen.hasControlDown() && !this.getSelectedText().isEmpty()) {
                    getMinecraftClient().keyboard.setClipboard(this.getSelectedText());
                    this.deleteSelectedText();
                }
            }
        }
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if(!this.isFocused || !this.inputFilter.test(chr) || this.text.length() >= this.maxLength) return;

        if(!this.getSelectedText().isEmpty())
            this.deleteSelectedText();

        this.text = this.text.substring(0, this.cursorPos) + chr + this.text.substring(this.cursorPos);
        this.cursorPos++;
    }





    private void updateCursorVisibility() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - this.lastBlinkTime >= 350L) { //350L = 0.35s
            this.isCursorVisible = !this.isCursorVisible;
            this.lastBlinkTime = currentTime;
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.x
                && mouseY >= this.y
                && mouseX < this.x + this.width
                && mouseY < this.y + this.height;
    }

    private String getClipboardText() {
        String clipboardText = getMinecraftClient().keyboard.getClipboard();
        if(clipboardText == null) return "";

        clipboardText = clipboardText.replaceAll("[^\\x20-\\x7E]", ""); // Remove non-printable characters
        int pasteLength = Math.min(clipboardText.length(), this.maxLength - text.length());
        clipboardText = clipboardText.substring(0, pasteLength);
        return clipboardText;
    }

    private void pasteFromClipboard() {
        if(this.getSelectedText().isEmpty()) {
            text = text.substring(0, this.cursorPos) + this.getClipboardText() + text.substring(this.cursorPos);
        } else {
            this.text = this.text.replace(this.getSelectedText(), this.getClipboardText());
            this.unselect();
        }
        this.cursorPos += this.getClipboardText().length();
    }

    public String getText() { return this.text; }

    public boolean isFocused() { return this.isFocused; }
    private String getSelectedText() {
        if(selectionStart != -1 && selectionEnd != -1 && selectionStart != selectionEnd) {
            int start = Math.min(this.selectionStart, this.selectionEnd);
            int end = Math.max(this.selectionStart, this.selectionEnd);
            return this.text.substring(start, end);
        }
        return "";
    }

    private void unselect() {
        this.selectionStart = -1;
        this.selectionEnd = -1;
        this.isSelecting = false;
    }

    private void deleteText() {
        if(this.selectionStart != -1 && this.selectionEnd != -1) {
            this.deleteSelectedText();
        } else {
            if(this.cursorPos > 0 && !this.text.isEmpty()) {
                this.text = this.text.substring(0, this.cursorPos - 1) + this.text.substring(this.cursorPos);
                this.cursorPos--;
            }
        }
    }

    private void deleteSelectedText() {
        int start = Math.min(this.selectionStart, this.selectionEnd);
        int end = Math.max(this.selectionStart, this.selectionEnd);
        this.text = this.text.substring(0, start) + this.text.substring(end);
        this.cursorPos = start;
        this.unselect();
    }

    private void moveCursorLeft(boolean select, boolean ctrl) {
        if (select) {

            if (this.selectionStart == -1)
                this.selectionStart = this.cursorPos;

            if (ctrl) {
                this.cursorPos = moveToPreviousWord(this.cursorPos);

            } else {

                if (this.cursorPos > 0)
                    this.cursorPos--;

            }
            this.selectionEnd = this.cursorPos;

        } else {
            if (ctrl) {
                this.cursorPos = moveToPreviousWord(this.cursorPos);

            } else {

                if (this.cursorPos > 0)
                    this.cursorPos--;

            }

            this.unselect();
        }
    }

    private void moveCursorRight(boolean select, boolean ctrl) {
        if (select) {

            if (this.selectionStart == -1)
                this.selectionStart = this.cursorPos;

            if (ctrl) {
                this.cursorPos = moveToNextWord(this.cursorPos);

            } else {

                if (this.cursorPos < this.text.length())
                    this.cursorPos++;

            }
            this.selectionEnd = this.cursorPos;

        } else {
            if (ctrl) {
                this.cursorPos = moveToNextWord(this.cursorPos);

            } else {

                if (this.cursorPos < this.text.length())
                    this.cursorPos++;

            }

            this.unselect();
        }
    }

    private int moveToPreviousWord(int cursorPos) {
        while (cursorPos > 0 && this.text.charAt(cursorPos - 1) == ' ')
            cursorPos--;
        while (cursorPos > 0 && this.text.charAt(cursorPos - 1) != ' ')
            cursorPos--;
        return cursorPos;
    }

    private int moveToNextWord(int cursorPos) {
        while (cursorPos < this.text.length() && this.text.charAt(cursorPos) == ' ')
            cursorPos++;
        while (cursorPos < this.text.length() && this.text.charAt(cursorPos) != ' ')
            cursorPos++;
        return cursorPos;
    }
    private void handleDoubleClick(double mouseX) {
        setCursorPos(mouseX);

        int wordStart = this.cursorPos;
        int wordEnd = this.cursorPos;

        while (wordStart > 0 && this.text.charAt(wordStart - 1) != ' ') {
            wordStart--;
        }

        while (wordEnd < this.text.length() && this.text.charAt(wordEnd) != ' ') {
            wordEnd++;
        }

        if(this.selectionStart == wordStart && this.selectionEnd == wordEnd) {
            this.unselect();
            return;
        }

        this.selectionStart = wordStart;
        this.selectionEnd = wordEnd;
    }

    private void setCursorPos(double mouseX) {
        TextRenderer tr = getMinecraftClient().textRenderer;

        int relativeX = (int) mouseX - this.x - 4;

        int charIndex = 0;
        for(int i = 0; i < this.text.length(); i++) {
            Text a = FontHelper.toCustomFont(this.text.substring(0, i + 1), getConfig().getString("Font"));
            if(tr.getWidth(a) > relativeX)
                break;
            charIndex = i + 1;
        }

        this.cursorPos = charIndex;
    }

}

package messagemod;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class MessageInputScreen extends Screen {
    private TextFieldWidget textField;
    private ButtonWidget sendButton;
    private ButtonWidget cancelButton;

    public MessageInputScreen() {
        super(Text.literal("Send Message"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.textField = new TextFieldWidget(
                this.textRenderer,
                centerX - 150, centerY - 30,
                300, 20,
                Text.literal("Enter your message here")
        );
        this.textField.setMaxLength(256);
        this.textField.setChangedListener(this::onTextChanged);
        this.addSelectableChild(this.textField);

        this.sendButton = ButtonWidget.builder(
                        Text.literal("Send"),
                        button -> this.sendMessage()
                )
                .dimensions(centerX - 155, centerY + 20, 100, 20)
                .build();
        this.sendButton.active = false;
        this.addDrawableChild(this.sendButton);

        this.cancelButton = ButtonWidget.builder(
                        Text.literal("Cancel"),
                        button -> this.close()
                )
                .dimensions(centerX + 55, centerY + 20, 100, 20)
                .build();
        this.addDrawableChild(this.cancelButton);

        this.setInitialFocus(this.textField);
    }

    private void onTextChanged(String text) {
        if (this.sendButton != null) {
            this.sendButton.active = text != null && !text.trim().isEmpty();
        }
    }

    private void sendMessage() {
        String text = this.textField.getText();
        if (text != null && !text.trim().isEmpty() && this.client != null && this.client.player != null) {
            MessagePayload payload = new MessagePayload(text.trim());
            ClientPlayNetworking.send(payload);
            this.close();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.getTitle(),
                this.width / 2,
                20,
                0xFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                Text.literal("Enter your message (max 256 characters)"),
                this.width / 2 - 150,
                this.height / 2 - 50,
                0xAAAAAA
        );

        super.render(context, mouseX, mouseY, delta);
        this.textField.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 && this.sendButton.active) {
            this.sendMessage();
            return true;
        }
        if (keyCode == 256) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers) || this.textField.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.textField.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.textField);
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
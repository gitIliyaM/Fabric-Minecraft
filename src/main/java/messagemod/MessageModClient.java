package messagemod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageModClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("MessageMod");
    private static KeyBinding openGuiKeyBinding;

    @Override
    public void onInitializeClient() {
        LOGGER.info("MessageModClient initialized!");

        openGuiKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.minecraft.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.minecraft.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKeyBinding.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(new MessageInputScreen());
                }
            }
        });
    }
}
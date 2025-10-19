package messagemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageMod implements ModInitializer {
    public static final String MOD_ID = "messagemod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static SessionFactory sessionFactory;
    private static MessageRepository messageRepository;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Message Mod");

        PayloadTypeRegistry.playC2S().register(MessagePayload.ID, MessagePayload.CODEC);

        initializeDatabase();

        ServerPlayNetworking.registerGlobalReceiver(MessagePayload.ID, (payload, context) -> {
            handleMessage(payload.text(), context.player().getUuid());
        });
    }

    private void initializeDatabase() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(MessageEntity.class);

            sessionFactory = configuration.buildSessionFactory();
            messageRepository = new MessageRepository(sessionFactory);

            LOGGER.info("Database initialized successfully from hibernate.cfg.xml");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize database", e);
        }
    }

    private void handleMessage(String text, java.util.UUID playerUuid) {
        try {
            if (text != null && !text.trim().isEmpty() && text.length() <= 256) {
                MessageEntity message = new MessageEntity();
                message.setUuid(playerUuid);
                message.setText(text.trim());

                messageRepository.save(message);
                LOGGER.info("Message saved to database: '{}' from player {}", text, playerUuid);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to save message to database", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
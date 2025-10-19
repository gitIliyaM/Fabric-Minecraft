package messagemod;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record MessagePayload(String text) implements CustomPayload {
    public static final CustomPayload.Id<MessagePayload> ID =
            new CustomPayload.Id<>(Identifier.of(MessageMod.MOD_ID, "message"));

    public static final PacketCodec<RegistryByteBuf, MessagePayload> CODEC =
            PacketCodec.of(
                    (value, buf) -> buf.writeString(value.text),
                    buf -> new MessagePayload(buf.readString())
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
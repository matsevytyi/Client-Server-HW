package src.client;

import src.packet_handling.CustomKey;
import src.packet_handling.Message;
import src.packet_handling.Packet;
import src.packet_handling.PacketEncoder;

// Якщо чесно, я не зрозумів що мається на увазі під довільною генерацією, тому тут роблю одне повідомлення, а різні випадки покриваю юніт тестами

public interface MessageGenerator {
    public static byte[] generateMsg(CustomKey key) {
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message("ping", "1234", new byte[]{1, 2, 3, 4, 5, 6, 7, 8}).toBytes());
        return PacketEncoder.encodePacket(packet, key.getSecretKey());
    };
}

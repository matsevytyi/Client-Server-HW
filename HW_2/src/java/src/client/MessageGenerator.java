package src.client;


// Якщо чесно, я не зрозумів що мається на увазі під довільною генерацією, тому тут роблю одне повідомлення, а різні випадки покриваю юніт тестами

import org.example.CustomKey;
import org.example.Message;
import org.example.Packet;
import org.example.PacketEncoder;

public interface MessageGenerator {
    public static byte[] generateMsg(CustomKey key) {
        Packet packet = new Packet((byte) 1, new byte[]{1, 2, 3, 4, 5, 6, 7, 8}, new Message(1, 1, "DONE").toBytes());
        return PacketEncoder.encodePacket(packet, key.getSecretKey());
    };
}

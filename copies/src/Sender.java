import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.UUID;

public class Sender implements Runnable {
    private MulticastSocket socket;
    private UUID uuid;
    private InetAddress addr;
    private DatagramPacket sendPack;

    private static final int PORT = 4321;

    public Sender(String addressGroup) {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            addr = InetAddress.getByName(addressGroup);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.socket = socket;
        this.uuid = UUID.randomUUID();
        byte[] UUIDinb = ByteUtil.getIdAsByte(uuid);
        sendPack = new DatagramPacket(UUIDinb, UUIDinb.length, addr, PORT);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                System.err.println("Sender thread interrupted.");
                Thread.currentThread().interrupt();
                continue;
            }
            try {
                // System.out.println("Sending " + uuid);
                socket.send(sendPack);
            } catch (IOException e) {
                System.err.println("Error sending packet: " + e.getMessage());
            }
        }
    }

}
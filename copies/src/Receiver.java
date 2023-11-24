
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.UUID;

public class Receiver implements Runnable {
    private MulticastSocket socket;
    private InetAddress addr;
    private DatagramPacket receivePack;
    private static final int PORT = 4321;
    private Copies copy;

    public Receiver(String addressGroup, Copies copy) {
        try {
            socket = new MulticastSocket(PORT);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            addr = InetAddress.getByName(addressGroup);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        receivePack = new DatagramPacket(new byte[16], 16);
        this.copy = copy;
    }

    @Override
    public void run() {
        try {
            socket.joinGroup(addr);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        while (!Thread.interrupted()) {
            try {
//                System.out.println("Waiting for data...");
                socket.receive(receivePack);
                receivePack.getAddress();
//                System.out.println("Got packed with " + receivePack.getLength() + " bytes");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            UUID uuidback = ByteUtil.getIdFromByte(receivePack.getData());
//            System.out.println(receivePack.getPort());
            String addr = receivePack.getAddress().toString(), port = String.valueOf(receivePack.getPort());
            StringBuilder inetIddress = new StringBuilder();
            inetIddress.append(addr);
            inetIddress.append(" : ");
            inetIddress.append(port);
            copy.putId(uuidback, inetIddress.toString());



        }

    }
}
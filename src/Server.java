import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static final int PORT = 25111;
    public static final String HOSTNAME = "localhost";
    public static final int WORK_MILLIS = 5000;

    public static void main(String[] args) {
        String name = "[SERVER] ";
        final ServerSocketChannel serverChannel;
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(HOSTNAME, PORT));
            System.out.println(name + "Started.");
            while (true) {
                try (SocketChannel socketChannel = serverChannel.accept()) {
                    System.out.println(name + "Connected with Client.");
                    final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                    while (socketChannel.isConnected()) {
                        int bytesCount = socketChannel.read(inputBuffer);
                        if (bytesCount == -1) break;
                        String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                        inputBuffer.clear();
                        System.out.println(name + "Income Message: " + msg);
                        Thread.sleep(WORK_MILLIS);
                        msg = msg.replace(" ", "");
                        System.out.println(name + "Outcome Message: " + msg);
                        socketChannel.write(ByteBuffer.wrap((msg).getBytes(StandardCharsets.UTF_8)));
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
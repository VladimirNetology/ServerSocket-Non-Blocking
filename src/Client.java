import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 25111;

    public static void main(String[] args) throws IOException {
        String name = "[CLIENT] ";
        InetSocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        System.out.println(name + "Started.");
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(socketAddress);
        System.out.println(name + "Connected with Server.");

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
            String msg;
            while (true) {
                System.out.println(name + "Enter text for delete space: ");
                msg = scanner.nextLine();
                if ("end".equals(msg)) {
                    break;
                }
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(name + "SERVER:" + new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                inputBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }
}

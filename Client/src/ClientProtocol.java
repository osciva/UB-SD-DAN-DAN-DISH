import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientProtocol {
    private Socket socket;

    public ClientProtocol(Socket socket) {
        this.socket = socket;
    }

    public void sendHello(Socket socket, byte opCode, int id, String name) {
        try {
            DataOutputStream data_outPut = new DataOutputStream(socket.getOutputStream());
            byte[] buffer = {0, 0};
            data_outPut.writeByte(opCode); // Capçalera, serà un 1, perque es el HELLO
            data_outPut.writeInt(id);
            for (int i = 0; i < name.length(); i++) {
                char p = name.charAt(i);
                data_outPut.writeChar(p);
            }
            data_outPut.writeChar('0');
            data_outPut.write(0); // Indica el final de trama
            data_outPut.write(0); // Indica el final de trama
            data_outPut.flush();
            // data_outPut.close();
        } catch (IOException e) {
            throw new RuntimeException("I/O Error when creating or sending the output stream. Is the host connected?:\n" + e.getMessage());
        }

    }

    public void recivedHello(Socket socket) {

    }

}
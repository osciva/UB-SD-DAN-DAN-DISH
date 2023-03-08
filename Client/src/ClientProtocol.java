import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
    
    public boolean recivedReady(Socket socket) {

        return false;
    }
    public void sendPlay(Socket socket) {

    }
    public boolean recivedAdmit(Socket socket) {

        return false;
    }
    public void sendAction(Socket socket) {

    }
    public boolean recivedResult(Socket socket) {

        return false;
    }
    public void finalGame(Socket socket) {

    }

    public boolean recivedError(Socket socket){
        DataInputStream data_inPut = null;
        try {
            data_inPut = new DataInputStream(socket.getInputStream());
            byte opCode = data_inPut.readByte();
            System.out.println("The opCpde it's bad:\n" + opCode);
            byte error = data_inPut.readByte();
            System.out.println("The client has the following error number:\n" + error);
            int a = 1;
            String name = "";
            while(a!=48) {
                char e = data_inPut.readChar();
                if(e != 48) {
                    name += (char) e;
                }
                a = e;
            }
            System.out.println("The client has the following message of error:\n" + name);

            //HACER BUCLE WHILE QUE LEA HASTA QUE HAYA EL 0 DEL BUFFER
            byte primer = (byte) data_inPut.read();
            byte segon = (byte) data_inPut.read();
            System.out.println("The client has the following bytes:\n" + primer + segon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
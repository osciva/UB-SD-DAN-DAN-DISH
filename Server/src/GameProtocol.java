import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameProtocol {


    private Socket socket;
    private int id;
    private DataOutputStream data_outPut;
    private DataInputStream data_inPut;

    public GameProtocol(Socket socket) {
        try {
            data_outPut = new DataOutputStream(socket.getOutputStream());
            data_inPut = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.socket = socket;
    }

    public boolean recivedHello(Socket socket) {
        try {// (DataInputStream data_inPut = new DataInputStream(socket.getInputStream())) {
            byte opCode = data_inPut.readByte();
            if (opCode != 1) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {
                System.out.println("The client send the following opCode:\n" + opCode);
                this.id = data_inPut.readInt();
                System.out.println("The client send the following int:\n" + id);
                int a = 1;
                String name = "";
                while (a != 48) {
                    char e = data_inPut.readChar();
                    if (e != 48) {
                        name += (char) e;
                    }
                    a = e;
                }
                System.out.println("The client send the following name:\n" + name);

                // HACER BUCLE WHILE QUE LEA HASTA QUE HAYA EL 0 DEL BUFFER
                byte primer = (byte) data_inPut.read();
                byte segon = (byte) data_inPut.read();
                System.out.println("The client send the following bytes:\n" + primer + segon);
                // System.out.println("The client send the following message:\n" + opCode + id +
                // name + buffer);
                // data_inPut.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public void sendError(Socket socket, byte opCode, byte error, String msg) {
        try (DataOutputStream data_outPut = new DataOutputStream(socket.getOutputStream())) {
            // HACER UN SWITCH CASE, Y DEPENDE DEL ERROR QUE SEA, ENVIARA UN MENSAJE
            // DETERMINADO QUE LO PODEMOS PONER AQUI O SINO NADA ESTA BIEN ASI
            data_outPut.writeByte(opCode); // OpCode a tornar
            data_outPut.writeByte(error); // Indica el eror que ha fet
            for (int i = 0; i < msg.length(); i++) {
                char p = msg.charAt(i);
                data_outPut.writeChar(p);
            }
            data_outPut.writeChar('0');
            data_outPut.write(0); // Indica el final de trama
            data_outPut.write(0);
            data_outPut.flush();
            data_outPut.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    } // AUN NO LO USAMOS PERO ES ASI, COMPROBADO QUE FUNCIONA

    public void sendReady(Socket socket) {
        try {
            byte opCode = 2;
            data_outPut.writeByte(opCode);
            System.out.println("Enviem opCode to ready: \n" + opCode);
            data_outPut.writeInt(id);
            System.out.println("Enviem int to ready: \n" + id);
            data_outPut.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean recivedPlay(Socket socket) {
        try {
            byte opCode = data_inPut.readByte();
            if (opCode != 3) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {
                System.out.println("The server send the following opCode and want's play:\n" + opCode);
                this.id = data_inPut.readInt();
                System.out.println("The server send the following int and want's play:\n" + id);
                // data_inPut.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }

    public void sendAdmit(Socket socket) {
        try {
            byte opCode = 4;
            data_outPut.writeByte(opCode);
            System.out.println("Enviem opCode to Admit: \n" + opCode);
            int isAdmit = 1; // 1 si admitim, 0 si no (potser si hi ha un error enviem 0)
            data_outPut.writeInt(isAdmit);
            System.out.println("Enviem int to Admit: \n" + isAdmit);
            data_outPut.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public boolean recivedAction(Socket socket) {
        return false;
    }

    public void sendResult(Socket socket) {
    }

    public boolean recivedError(Socket socket) {
        return false;
    }

}
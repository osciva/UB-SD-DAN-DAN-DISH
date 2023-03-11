import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientProtocol {
    private Socket socket;
    private int id;
    private DataOutputStream data_outPut;
    private DataInputStream data_inPut;
    private util utilitat;

    public ClientProtocol(Socket socket) {
        this.socket = socket;
        try {
            data_outPut = new DataOutputStream(socket.getOutputStream());
            data_inPut = new DataInputStream(socket.getInputStream());
            utilitat = new util(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendHello(Socket socket, byte opCode, int id, String name) {
        try {
            utilitat.escriureByte((opCode)); // Capçalera, serà un 1, perque es el HELLO
            utilitat.escriureInt(id);
            for (int i = 0; i < name.length(); i++) {
                char p = name.charAt(i);
                data_outPut.writeChar(p);
            }
            utilitat.escriureChar('0');
            utilitat.escriureByte((byte)0); // Indica el final de trama
            utilitat.escriureByte((byte)0); // Indica el final de trama
            utilitat.ferFlush();
            // data_outPut.close();
        } catch (IOException e) {
            throw new RuntimeException(
                    "I/O Error when creating or sending the output stream. Is the host connected?:\n" + e.getMessage());
        }

    }

    public boolean recivedReady(Socket socket) {
        try {
            byte opCode = utilitat.llegirByte();
            if (opCode != 2) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {
                System.out.println("The server send the following opCode and it's ready:\n" + opCode);
                this.id = utilitat.llegirInt();
                System.out.println("The server send the following int and it's ready:\n" + id);
                // data_inPut.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public void sendPlay(Socket socket) {
        try {
            byte opCode = 3;
            utilitat.escriureByte(opCode);
            System.out.println("Enviem opCode to play: \n" + opCode);
            utilitat.escriureInt(id);
            System.out.println("Enviem int to play: \n" + id);
            utilitat.ferFlush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean recivedAdmit(Socket socket) {
        try {
            byte opCode = utilitat.llegirByte();
            if (opCode != 4) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {
                System.out.println("The server send the following opCode and it's admit:\n" + opCode);
                int isAdmit = utilitat.llegirInt();
                if (isAdmit == 1) {
                    boolean admit = true;
                    System.out.println("The server send the following bool and it's admit:\n" + isAdmit + admit);
                } else {
                    byte error = 4;
                    boolean admit = false;
                    // String msg = "NO S'ADMATEIX";
                    // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                }
                // data_inPut.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public void sendAction(Socket socket) {

    }

    public boolean recivedResult(Socket socket) {

        return false;
    }

    public void finalGame(Socket socket) {

    }

    public boolean recivedError(Socket socket) {
        DataInputStream data_inPut = null;
        try {
            data_inPut = new DataInputStream(socket.getInputStream());
            byte opCode = utilitat.llegirByte();
            System.out.println("The opCpde it's bad:\n" + opCode);
            byte error = utilitat.llegirByte();
            System.out.println("The client has the following error number:\n" + error);
            int a = 1;
            String name = "";
            while (a != 48) {
                char e = utilitat.llegirChar();;
                if (e != 48) {
                    name += (char) e;
                }
                a = e;
            }
            System.out.println("The client has the following message of error:\n" + name);

            // HACER BUCLE WHILE QUE LEA HASTA QUE HAYA EL 0 DEL BUFFER
            byte primer = utilitat.llegirByte();
            byte segon = utilitat.llegirByte();
            System.out.println("The client has the following bytes:\n" + primer + segon);
            System.out.println("Joc TANCAT");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
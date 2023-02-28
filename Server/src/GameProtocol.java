import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameProtocol {
    private Socket socket;
    
     public GameProtocol(Socket socket) {
        
        this.socket = socket;
    }

    public void recivedHello(Socket socket) throws IOException {
        DataInputStream data_inPut = new DataInputStream(socket.getInputStream());
        byte opCode = data_inPut.readByte();
        int id = data_inPut.readInt();
        String name = data_inPut.readLine();
        byte[] buffer = new byte[0];
        data_inPut.readFully(buffer); // o data_inPut.readyFully(byte[] buffer);
        System.out.println("The client send the following message:\n" + opCode + id +name+ buffer);

        if (opCode != 1) {
            byte error = 4;
            //String msg = "INICI DE SESSIÓ INCORRECTE";
            sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); //msg);
        }
        System.out.println("The client send the following message:\n" + opCode + id + name + buffer);
        data_inPut.close();
    }

    public void sendError(Socket socket, byte opCode, byte error, String msg) throws IOException {
        DataOutputStream data_outPut = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = {0, 0};
        //HACER UN SWITCH CASE, Y DEPENDE DEL ERROR QUE SEA, ENVIARA UN MENSAJE DETERMINADO QUE LO PODEMOS PONER AQUI O SINO NADA ESTA BIEN ASI
        data_outPut.writeByte(opCode); // OpCode a tornar
        data_outPut.writeByte(error); // Indica el eror que ha fet
        data_outPut.writeChars(msg); // Indica el missatge del error que ha fet
        data_outPut.write(buffer);
        data_outPut.flush();
        data_outPut.close();

    }
}
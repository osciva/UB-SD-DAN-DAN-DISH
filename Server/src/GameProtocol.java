import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameProtocol {
     //private util utilitat;

     private Socket socket;
    
     public GameProtocol(Socket socket) {
        
        this.socket = socket;
    }

    public void recivedHello(Socket socket) throws IOException {
        DataInputStream data_inPut = new DataInputStream(socket.getInputStream());
        byte opCode = data_inPut.readByte();
        System.out.println("The client send the following opCode:\n" + opCode);
        int id = data_inPut.readInt();
        System.out.println("The client send the following int:\n" + id);
        int a = 1;
        String name = "";

        while(a!=48) {
             char e = data_inPut.readChar();
             if(e != 48) {
                 name += (char) e;
             }
             a = e;
        }
        System.out.println("The client send the following name:\n" + name);

        //HACER BUCLE WHILE QUE LEA HASTA QUE HAYA EL 0 DEL BUFFER
        byte primer = (byte) data_inPut.read();
        byte segon = (byte) data_inPut.read();
        System.out.println("The client send the following bytes:\n" + primer + segon);

        if (opCode != 1) {
            byte error = 4;
            //String msg = "INICI DE SESSIÓ INCORRECTE";
            sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); //msg);
        }
        //System.out.println("The client send the following message:\n" + opCode + id + name + buffer);
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
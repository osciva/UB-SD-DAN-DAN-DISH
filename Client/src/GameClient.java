import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameClient {

    /*
     * TO DO.
     * Class that encapsulates the game's logic. Sequence of states following the
     * established protocol .
     */

    public void play(Socket socket, String message, int id) throws IOException {

        // DataOutputStream data_stream = new
        // DataOutputStream(socket.getOutputStream());
        // data_stream.writeUTF(message);
        // data_stream.write(id);
        ClientProtocol cp = new ClientProtocol(socket);
        cp.sendHello(socket, (byte) 1, id, message);
        // Creamos maquina d'estats

        if (cp.recivedReady(socket)) {
            cp.sendPlay(socket);
        }
        if (cp.recivedAdmit(socket)) {
            cp.sendAction(socket);
        }
        if (cp.receivedResult(socket)) {
            cp.finalGame(socket);
        }

        /*
         * else if(cp.recivedError(socket)){
         * System.out.println("Final del juego por error");
         * }
         */
        // ESTO LO HAREMOS AL FINAL. SE LLAMARA AL SEND ERROR EN EL CATCH DE LAS
        // OPERACIONES

        // Add game execution logic here
    }

}
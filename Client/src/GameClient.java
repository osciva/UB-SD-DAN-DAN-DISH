import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameClient {

    /*
    TO DO.
    Class that encapsulates the game's logic. Sequence of states following the established protocol .
     */

    public void play(Socket socket, String message, int id) {

            //DataOutputStream data_stream = new DataOutputStream(socket.getOutputStream());
            //data_stream.writeUTF(message);
            //data_stream.write(id);
            ClientProtocol cp = new ClientProtocol(socket);
            cp.sendHello(socket, (byte)1, id, message);
            //data_stream.flush();
            //data_stream.close();

        // Add game execution logic here
    }

    



}
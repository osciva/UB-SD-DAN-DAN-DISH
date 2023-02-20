import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class GameClient {

    /*
    TO DO.
    Class that encapsulates the game's logic. Sequence of states following the established protocol .
     */

    public void play(Socket socket, String message) {

        try {
            DataOutputStream data_stream = new DataOutputStream(socket.getOutputStream());
            data_stream.writeUTF(message);
            data_stream.flush();
            data_stream.close();
        } catch (IOException e) {
            throw new RuntimeException("I/O Error when creating or sending the output stream. Is the host connected?:\n"+e.getMessage());
        }

        // Add game execution logic here
    }



}
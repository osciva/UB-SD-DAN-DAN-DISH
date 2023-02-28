import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
/*
    TO DO
    Protocol dynamics from Server.
    Methods: run(), init(), play().
*/

public class GameHandler extends Thread {
    private Socket socket;

    public GameHandler(Socket socket) {
        this.socket = socket;
    }


    public void init(){

    }

    @Override
    public void run() {

        String message = null;
        try {
            play();
            /*DataInputStream data_input = new DataInputStream(socket.getInputStream());
            message = data_input.readUTF();
            System.out.println("The client send the following message:\n" + message);
            data_input.close();*/
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException("I/O Error when reading the client's message:\n" + e.getMessage());
        }


    }

    public void play(){
         GameProtocol gh = new GameProtocol(socket);
    }



}
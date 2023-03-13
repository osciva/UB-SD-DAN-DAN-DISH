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

    public void init() {

    }

    @Override
    public void run() {

        while (true) {
            String message = null;
            try {
                play();
                /*
                 * DataInputStream data_input = new DataInputStream(socket.getInputStream());
                 * message = data_input.readUTF();
                 * System.out.println("The client send the following message:\n" + message);
                 * data_input.close();
                 */
                break;
            } catch (IOException e) {
                throw new RuntimeException("I/O Error when reading the client's message:\n" + e.getMessage());
            }
        }

    }

    public void play() throws IOException {
        GameProtocol gh = new GameProtocol(socket);
        // gh.receivedHello(socket);
        if (gh.receivedHello(socket)) {
            gh.sendReady(socket);
        }
        if (gh.receivedPlay(socket)) {
            gh.sendAdmit(socket);
        }
        if (gh.receivedAction(socket)) {
            gh.sendResult(socket);
        }
        if (gh.receivedError(socket)) {
            System.out.println("Final del juego por error");
        }
    }

}
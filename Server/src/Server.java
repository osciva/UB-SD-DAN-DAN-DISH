import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;

public class Server {
    public static final String INIT_ERROR = "Server should be initialized with -p <port>";

    public static void main(String[] args) {

        if (args.length != 2) {
            throw new IllegalArgumentException("Wrong amount of arguments.\n" + INIT_ERROR);
        }

        if (!args[0].equals("-p")) {
            throw new IllegalArgumentException("Wrong argument keyword.\n"+INIT_ERROR);
        }

        int port;


        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("<port> should be an Integer. Use 0 for automatic allocation.");
        }

        ServerSocket ss = null;

        try {
            ss = new ServerSocket(port);
            System.out.println("Server up & listening on port "+port+"...\nPress Cntrl + C to stop.");
        } catch (IOException e) {
            throw new RuntimeException("I/O error when opening the Server Socket:\n" + e.getMessage());
        }

        Socket socket = null;

        /*
        TO DO:
        Create a new GameHandler for every client.
         */

        try {
            socket = ss.accept();
            System.out.println("Client accepted");
        } catch (IOException e) {
            throw new RuntimeException("I/O error when accepting a client:\n" + e.getMessage());
        } catch (SecurityException e) {
            throw new RuntimeException("Operation not accepted:\n"+e.getMessage());
        } catch (IllegalBlockingModeException e) {
            throw new RuntimeException("There is no connection ready to be accepted:\n"+e.getMessage());
        }

        String message = null;

        try {
            DataInputStream data_input = new DataInputStream(socket.getInputStream());
            message = data_input.readUTF();
            System.out.println("The client send the following message:\n"+message);
            ss.close();
            data_input.close();
        } catch (IOException e) {
            throw new RuntimeException("I/O Error when reading the client's message:\n"+e.getMessage());
        }

    }
}

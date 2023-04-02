import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.IllegalBlockingModeException;

public class Server {
    public static final String INIT_ERROR = "Server should be initialized with -p <port>";

    public static void main(String[] args) throws utilsError {

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
            throw new utilsError("<port> should be an Integer. Use 0 for automatic allocation."+e.getMessage());
        }

        ServerSocket ss = null;

        try {
            ss = new ServerSocket(port);
            ss.setSoTimeout(45000); //timeout en ms
            System.out.println("Server up & listening on port "+port+"...\nPress Cntrl + C to stop.");

        } catch (IOException e) {
            throw new utilsError("I/O error when opening the Server Socket:\n" + e.getMessage());
        }

        Socket socket = null;

        /*
        TO DO:
        Create a new GameHandler for every client.
         */
        while(true){
            try {
                socket = ss.accept();
                socket.setSoTimeout(120000); // Agregar 120s de timeout al socket aceptado
                System.out.println("S- [TCP Accept]");
            } catch (IOException e) {
                throw new utilsError("I/O error when accepting a client:\n" + e.getMessage());
            } catch (SecurityException e) {
                throw new utilsError("Operation not accepted:\n" + e.getMessage());
            } catch (IllegalBlockingModeException e) {
                throw new utilsError("There is no connection ready to be accepted:\n"+e.getMessage());
            }

            //Creating a new GameHandler for every client.
            Thread t = new Thread(new GameHandler(socket));
            t.start();
            /*
            GameHandler gh = new GameHandler(socket);
            gh.start();
             */
        }



    }
}
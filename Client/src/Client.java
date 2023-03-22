import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {

    public static final String INIT_ERROR = "Client should be initialized with -h <host> -p <port>";

    public static void main(String[] args)  throws utilsError {

        if (args.length != 4) {
            throw new utilsError("Wrong amount of arguments.\n" + INIT_ERROR);
        }

        if (!args[0].equals("-h") || !args[2].equals("-p")) {
            throw new utilsError("Wrong argument keywords.\n" + INIT_ERROR);
        }

        int port;

        try {
            port = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            throw new utilsError("<port> should be an Integer.");

        }

        String host = args[1];
        Socket socket;

        try {
            socket = new Socket(host, port);
            System.out.println("Client connected to server");
            System.out.println("C- [TCP Connect]");
        } catch (IllegalArgumentException e) {
            throw new utilsError("Proxy has invalid type or null:\n" + e.getMessage());
        } catch (SecurityException e) {
            throw new utilsError("Connection to the proxy denied for security reasons:\n" + e.getMessage());
        } catch (UnknownHostException e) {
            throw new utilsError("Host is Unknown:\n" + e.getMessage());
        } catch (IOException e) {
            throw new utilsError("I/O Error when creating the socket:\n" + e.getMessage() + ". Is the host listening?");
        }

        String message = "";
        int id =(int) (Math.random() * 90000) + 10000;
        Scanner sc = new Scanner(System.in);

        while (message.isEmpty()) {
            System.out.println("Write your nickname for DAN DAN DISH please: ");
            message = sc.nextLine();
            System.out.println("El teu id es: " + id);
            /*System.out.println("now, choose the integer that will represent your lobby during the game: ");
            boolean valido = false;

            while (!valido) {
                if (sc.hasNextInt()) {
                    id = sc.nextInt();
                    valido = true;
                } else {
                    System.out.println("Error: the chosen value isn't an int... Try again please:\n");
                    sc.next(); // descarta el valor inválido
                }
            }*/
        }

        /*
         * TO DO
         * Create a new GameClient class and call the game execution.
         */

        GameClient gameClient = new GameClient();
        try {
            gameClient.play(socket, message, id);
        } catch (IOException e) {
            throw new utilsError(e.getMessage());
        }

    }
}
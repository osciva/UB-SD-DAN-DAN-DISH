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
    private util utils;

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
                // Thread t = new Thread(new Client(socket)); //falta saber com poder passar
                // Client
                play();
                /*
                 * DataInputStream data_input = new DataInputStream(socket.getInputStream());
                 * message = data_input.readUTF();
                 * System.out.println("The client send the following message:\n" + message);
                 * data_input.close();
                 */
                break;
            } catch (IOException | utilsError e) {
                throw new RuntimeException("I/O Error when reading the client's message:\n" + e.getMessage());
            }
        }

    }

    public void play() throws IOException, utilsError {
        GameProtocol gh = new GameProtocol(socket);
        boolean isFinal = false;
        String resposta = "REHELLO";
        while (!resposta.equals("FINAL")) {
            switch (resposta.toUpperCase()) {
                case "ERROR":
                    utils.receiveError();
                    resposta = "FINAL";
                    System.out.println("Final del joc per error");
                    break;
                case "REHELLO":
                    resposta = gh.receivedHello(socket);
                    break;
                case "SEREADY":
                    resposta = gh.sendReady(socket);
                    break;
                case "REPLAY":
                    resposta = gh.receivedPlay(socket);
                    break;
                /*
                 * case "REPLAY2":
                 * resposta = gh.receivedPlay2(socket);
                 * break;
                 */
                case "SEADMIT":
                    resposta = gh.sendAdmit(socket);
                    break;
                case "JUGANT":
                    isFinal = false;
                    try {
                        while (socket.isConnected()) {
                            if (socket.isClosed() || isFinal) {
                                System.out.println("Final d'aquesta partida");
                                break;
                            }
                            if (gh.receivedAction(socket)) {
                                gh.sendResult(socket);
                            }
                            String resp = gh.jocAcabat(socket);

                            switch (resp) {
                                case "SI":
                                    isFinal = true;
                                    // gh.especial();
                                    resposta = "REPLAY";
                                    // resposta = "REPLAY2";
                                    break;
                                case "NO":
                                    isFinal = true;
                                    resposta = "FINAL";
                                    socket.close();
                                    break;
                                default:
                                    System.out.println("seguim jugant");
                                    break;
                            }

                        }
                    } catch (IOException error) {
                        System.out.println("El client s'ha desconnectat");
                    }
            }
        }
        System.out.println("S- [conexion closed]");
    }

}
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
                //Thread t = new Thread(new Client(socket)); //falta saber com poder passar Client
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
                    gh.receivedError(socket);
                    resposta = "FINAL";
                    System.out.println("Final del joc per error");
                case "REHELLO":
                    resposta = gh.receivedHello(socket);
                case "SEREADY":
                    resposta = gh.sendReady(socket);
                case "REPLAY":
                    resposta = gh.receivedPlay(socket);
                case "SEADMIT":
                    resposta = gh.sendAdmit(socket);
                case "JUGANT":
                    isFinal = false;
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
                                resposta = "REPLAY";
                                break;
                            case "NO":
                                isFinal = true;
                                resposta = "FINAL";
                                socket.close();
                                break;
                            default:
                                System.out.println("seguim jugant");
                        }

                    }
                }
            }
        }
        /*GameProtocol gh = new GameProtocol(socket);
        boolean isFinal = false;
        // gh.receivedHello(socket);
        if (gh.receivedHello(socket)) {
            gh.sendReady(socket);
        }
        if (gh.receivedPlay(socket)) {
            gh.sendAdmit(socket);
        }
        while (socket.isConnected()) {
            if(socket.isClosed() || isFinal){
                break;
            }
            if (gh.receivedAction(socket)) {
                gh.sendResult(socket);
            }
            String resp = gh.jocAcabat(socket);
            switch (resp) {
                case "SI":
                    gh.receivedPlay(socket); // Debemos hacer que cuando llame al receivedPlay vuelva a hacerse toddo el protocolo
                    isFinal = true;   // Posiblemente lo podemos conseguir con un bucle..
                    break;
                case "NO":
                    socket.close();
                    break;
                default:
                    System.out.println("seguim jugant");
            }

        }
        if (gh.receivedError(socket)) {
            System.out.println("Final del juego por error");
        }
    }*/

}
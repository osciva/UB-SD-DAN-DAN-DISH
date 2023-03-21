import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {

    /*
     * TO DO.
     * Class that encapsulates the game's logic. Sequence of states following the
     * established protocol .
     */

    public void play(Socket socket, String message, int id) throws IOException, utilsError {

        ClientProtocol cp = new ClientProtocol(socket);
        cp.sendHello(socket, id, message);
        // Creamos maquina d'estats
        String resposta = "REREADY";
        while (!resposta.equals("FINAL")) {
            switch (resposta.toUpperCase()) {
                case "ERROR":
                    System.out.println("final del joc per error");
                    break;
                case "REREADY":
                    resposta = cp.recivedReady(socket);
                case "SEPLAY":
                    resposta = cp.sendPlay(socket);
                case "READMIT":
                    resposta = cp.recivedAdmit(socket);
                case "SEACTION":
                    resposta = cp.sendAction(socket);
                case "JUGANT":
                    while(socket.isConnected()) {
                        cp.receivedResult(socket);
                        int num = cp.finalGame(socket);
                        if(num == 1) { // Vol jugar una altre partida
                            resposta= "SEPLAY";
                            break;
                        }else if(num == 2) { // No vol jugar més partidas
                            socket.close();
                            resposta = "FINAL";
                            break;
                        }else if(num == 3) {
                            cp.sendAction(socket); // Es segueix amb el joc
                        }
                    }
        }
        /*if (cp.recivedReady(socket)) {
            cp.sendPlay(socket);
        }
        if (cp.recivedAdmit(socket)) {
            cp.sendAction(socket);
        }
        while(socket.isConnected()) {
            cp.receivedResult(socket);
            int num = cp.finalGame(socket);
            if(num == 1) {
                cp.sendPlay(socket);
                break;
            }else if(num == 2) {
                socket.close();
                break;
            }else if(num == 3) {
                cp.sendAction(socket);
            }*/
                /*if (temp == true) {
                    cp.sendAction(socket);
                }
                if (temp == false) {
                    if(cp.finalGame(socket)){
                        cp.sendPlay(socket);
                    }else{
                        socket.close();
                        break;
                    }*/
                    //socket.close();
                    //break;

                }
            }


        /*
         * else if(cp.recivedError(socket)){
         * System.out.println("Final del juego por error");
         * }
         */
        // ESTO LO HAREMOS AL FINAL. SE LLAMARA AL SEND ERROR EN EL CATCH DE LAS
        // OPERACIONES

        // Add game execution logic here
    //}

}
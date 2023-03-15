import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameProtocol {

    private Socket socket;
    private int id;
    private String accioRebuda;
    private int contBales;
    private DataOutputStream data_outPut;
    private DataInputStream data_inPut;
    private util utils;

    public GameProtocol(Socket socket) {
        try {
            data_outPut = new DataOutputStream(socket.getOutputStream());
            data_inPut = new DataInputStream(socket.getInputStream());
            utils = new util(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.socket = socket;
    }

    public boolean receivedHello(Socket socket) {
        try {// (DataInputStream data_inPut = new DataInputStream(socket.getInputStream())) {
            byte opCode = utils.llegirByte();
            if (opCode != 1) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {
                System.out.println("The client sent the following opCode:\n" + opCode);
                id = utils.llegirInt();
                System.out.println("The client sent the following int:\n" + id);
                String name = utils.llegirString();

                // HACER BUCLE WHILE QUE LEA HASTA QUE HAYA EL 0 DEL BUFFER
                byte primer = utils.llegirByte();
                byte segon = utils.llegirByte();
                System.out.println("The client sent the following bytes:\n" + primer + segon);
                // System.out.println("The client sent the following message:\n" + opCode + id +
                // name + buffer);
                // data_inPut.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public void sendError(Socket socket, byte opCode, byte error, String msg) {
        try (DataOutputStream data_outPut = new DataOutputStream(socket.getOutputStream())) {
            // HACER UN SWITCH CASE, Y DEPENDE DEL ERROR QUE SEA, ENVIARA UN MENSAJE
            // DETERMINADO QUE LO PODEMOS PONER AQUI O SINO NADA ESTA BIEN ASI
            utils.escriureByte(opCode); // OpCode a tornar
            utils.escriureByte(error); // Indica el eror que ha fet
            for (int i = 0; i < msg.length(); i++) {
                char p = msg.charAt(i);
                utils.escriureChar(p);
            }
            utils.escriureChar('0');// Indica el final de trama
            byte[] array = { 0, 0 };
            utils.writeBytes(array);
            data_outPut.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    } // AUN NO LO USAMOS PERO ES ASI, COMPROBADO QUE FUNCIONA

    public void sendReady(Socket socket) {
        try {
            byte opCode = 2;
            utils.escriureByte(opCode);
            System.out.println("Enviem opCode to ready: \n" + opCode);
            utils.escriureInt(id);
            System.out.println("Enviem int to ready: \n" + id);
            utils.ferFlush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean receivedPlay(Socket socket) {
        try {
            byte opCode = utils.llegirByte();
            if (opCode != 3) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {
                System.out.println("The client sent the following opCode and wants to play:\n" + opCode);
                this.id = utils.llegirInt();
                System.out.println("The client sent the following int and wants to play:\n" + id);
                // data_inPut.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;

    }

    public void sendAdmit(Socket socket) {
        try {
            byte opCode = 4;
            utils.escriureByte(opCode);
            System.out.println("Enviem opCode to Admit: \n" + opCode);
            int isAdmit = 1; // 1 si admitim, 0 si no (potser si hi ha un error enviem 0)
            utils.escriureInt(isAdmit);
            System.out.println("Enviem int to Admit: \n" + isAdmit);
            utils.ferFlush();

        } catch (IOException msg) {
            throw new RuntimeException("error a sendAdmit");

        }
    }

    public boolean receivedAction(Socket socket) {
        try {
            byte opCode = utils.llegirByte();
            if (opCode != 5) {
                byte error = 5;
                // String msg = "PARAULA DESCONEGUDA";
                // sendError(socket, (byte) 8, error, "PARAULA DESCONEGUDA"); // msg);
                System.out.println("Error al rebre l'acció");
                return false;
            } else {
                System.out.println("The client sent the following opCode and wants to play:\n" + opCode);
                this.id = utils.llegirInt();
                String accio = utils.llegirAction();
                this.accioRebuda = accio;
                System.out.println("C -----ACTION " + accio + " -----> S");
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void sendResult(Socket socket) {
        try {
            byte opCode = 6;
            utils.escriureByte(opCode);
            System.out.println("Enviem opCode to result: \n" + opCode);
            int random = 0;
            if (contBales > 0) {
                random = (int) (Math.random() * 3 + 1);
            } else {
                random = (int) (Math.random() * 3 + 2);
            }
            String accioServer = "";
            String result = "";
            String accioClient = this.accioRebuda.toUpperCase();
            switch (random) {
                case 1:
                    accioServer = "SHOOT";
                    contBales -= 1;
                    if (accioClient.equals("SHOOT")) {
                        result = "DRAW0"; // Client i Servidor disparen, empat
                        utils.escriureString(result);
                        break;
                    } else if (accioClient.equals("CHARGE")) {
                        result = "ENDS0"; // Client recarrega, Servidor dispara i guanya
                        utils.escriureString(result);
                        break;
                    } else {
                        result = "SAFE1"; // Client bloqueja, Servidor dispara, bloqueig del client
                        utils.escriureString(result);
                        break;
                    }

                case 2:
                    accioServer = "BLOCK";
                    if (accioClient.equals("SHOOT")) {
                        result = "SAFE0"; // Client dispara, Servidor bloqueja.
                        utils.escriureString(result);
                        break;
                    } else if (accioClient.equals("CHARGE")) {
                        result = "PLUS1"; // Client recarrega una bala perque servidor bloqueja
                        utils.escriureString(result);
                        break;
                    } else {
                        result = "SAFE2"; // Client i Servidor bloquejen els dos
                        utils.escriureString(result);
                        break;
                    }
                case 3:
                    accioServer = "CHARGE";
                    contBales += 1;
                    if (accioRebuda.toUpperCase().equals("SHOOT")) {
                        result = "ENDS1"; // Client dispara, client guanya
                        utils.escriureString(result);
                        break;
                    } else if (accioRebuda.toUpperCase().equals("CHARGE")) {
                        result = "PLUS2"; // Client i Servidor recarreguen una bala
                        utils.escriureString(result);
                        break;

                    } else {
                        result = "PLUS0"; // Client bloqueja, Servidor recarrega una bala
                        utils.escriureString(result);
                        break;
                    }
                default:
                    System.out.println("Ha habido un error");
            }

        } catch (IOException msg) {
            throw new RuntimeException("error a result");

        }
    }

    public boolean receivedError(Socket socket) {
        return false;
    }

}
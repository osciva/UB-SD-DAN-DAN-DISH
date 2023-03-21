import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameProtocol {

    private Socket socket;
    private int id;
    private String accioRebuda;
    private int contBales;
    int finalInt;
    private DataOutputStream data_outPut;
    private DataInputStream data_inPut;
    private util utils;

    public GameProtocol(Socket socket) {
        try {
            data_outPut = new DataOutputStream(socket.getOutputStream());
            data_inPut = new DataInputStream(socket.getInputStream());
            utils = new util(socket);
            this.contBales = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.socket = socket;
        this.finalInt = 3;
    }

    public String receivedHello(Socket socket) throws utilsError{
        try {// (DataInputStream data_inPut = new DataInputStream(socket.getInputStream())) {
            byte opCode = utils.llegirByte();
            if (opCode != 1) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                //sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                //return false;
                return "ERROR";

            } else {

                id = utils.llegirInt();

                String name = utils.llegirString();
                System.out.println(name + " wants to play");
                // HACER BUCLE WHILE QUE LEA HASTA QUE HAYA EL 0 DEL BUFFER
                byte primer = utils.llegirByte();
                byte segon = utils.llegirByte();

                // System.out.println("The client sent the following message:\n" + opCode + id +
                // name + buffer);
                // data_inPut.close();
            }
        } catch (IOException e) {
            throw new utilsError("Error a receivedHello: "+e.getMessage());
        }
        //return true;
        return "SEREADY";
    }
/*
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
*/
    public String sendReady(Socket socket) throws utilsError {
        try {
            byte opCode = 2;
            utils.escriureByte(opCode);

            utils.escriureInt(id);
            System.out.println("C <------READY " +id+" --------- S");
            System.out.println("the following id got assigned: " + id);
            utils.ferFlush();

        } catch (IOException e) {
            throw new utilsError("Error a sendReady: "+e.getMessage());
        }

        return "REPLAY";
    }

    public String receivedPlay(Socket socket) throws utilsError{
        try {
            byte opCode = utils.llegirByte();
            if (opCode != 3) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                //return false;
            } else {
                this.id = utils.llegirInt();

                // data_inPut.close();
            }
        } catch (IOException e) {
            throw new utilsError("Error a receivedPlay: "+e.getMessage());
        }
        //return true;
        return "SEADMIT";

    }

    public String sendAdmit(Socket socket) throws utilsError{
        try {
            byte opCode = 4;
            utils.escriureByte(opCode);
            int isAdmit = 1; // 1 si admitim, 0 si no (potser si hi ha un error enviem 0)
            utils.escriureInt(isAdmit);
            System.out.println("C <------ ADMIT " +isAdmit+ " --------- S");
            utils.ferFlush();

        } catch (IOException e) {
            throw new utilsError("Error a sendAdmit: "+e.getMessage());
        }
        return "JUGANT";
    }

    public boolean receivedAction(Socket socket) throws utilsError{
        try {
            byte opCode = utils.llegirByte();
            if (opCode != 5) {
                byte error = 5;
                // String msg = "PARAULA DESCONEGUDA";
                // sendError(socket, (byte) 8, error, "PARAULA DESCONEGUDA"); // msg);
                System.out.println("Error al rebre l'acció");
                return false;
            } else {

                this.id = utils.llegirInt();
                String accio = utils.llegirAction();
                this.accioRebuda = accio;
                System.out.println("C ------- ACTION " + accio + " --------> S");
                return true;
            }
        } catch (IOException e) {
            throw new utilsError("Error a receivedAction: "+e.getMessage());
        }

    }

    public void sendResult(Socket socket) throws utilsError {
        try {
            byte opCode = 6;
            utils.escriureByte(opCode);
            String action = "";
            int random = 0;
            if (this.contBales > 0) {
                random = (int) (Math.random() * 3) + 1;
                if (random == 1){
                    action = "SHOOT";
                }
                else if (random == 2){
                    action = "BLOCK";
                }
                else if (random == 3){
                    action = "CHARGE";
                }

            } else {
                random = (int) (Math.random() * 2) + 2;
                if (random == 2){
                    action = "BLOCK";
                }
                else if (random == 3){
                    action = "CHARGE";
                }
            }
            String accioServer = "";
            String result = "";
            String accioClient = this.accioRebuda.toUpperCase();
            System.out.println("La accio escollida per el server es: " + random + " = " +action);
            switch (random) {
                case 1:
                    accioServer = "SHOOT";
                    this.contBales -= 1;

                    if (accioClient.equals("SHOOT")) {
                        result = "DRAW0"; // Client i Servidor disparen, empat
                        utils.escriureString(result);
                        System.out.println("Client i Servidor disparen --> empat");
                        this.finalInt = 2;
                        break;
                    } else if (accioClient.equals("CHARGE")) {
                        result = "ENDS0"; // Client recarrega, Servidor dispara i guanya
                        utils.escriureString(result);
                        System.out.println("Client recarrega, Servidor dispara --> servidor guanya");
                        this.finalInt = 2;
                        break;
                    } else {
                        result = "SAFE1"; // Client bloqueja, Servidor dispara, bloqueig del client
                        utils.escriureString(result);
                        System.out.println("Client bloqueja, Servidor dispara --> el joc segueix");
                        if(this.contBales ==1){
                            System.out.println("EL SERVIDOR ARA TE " + this.contBales + " BALA");
                        }
                        else{
                            System.out.println("EL SERVIDOR ARA TE " + this.contBales + " BALES");
                        }
                        break;
                    }

                case 2:
                    accioServer = "BLOCK";
                    if (accioClient.equals("SHOOT")) {
                        result = "SAFE0"; // Client dispara, Servidor bloqueja.
                        utils.escriureString(result);
                        System.out.println("Client dispara, Servidor bloqueja --> el joc segueix");
                        if(this.contBales ==1){
                            System.out.println("EL SERVIDOR SEGUEIX TENINT " + this.contBales + " BALA");
                        }
                        else{
                            System.out.println("EL SERVIDOR SEGUEIX TENINT " + this.contBales + " BALES");
                        }
                        break;
                    } else if (accioClient.equals("CHARGE")) {
                        result = "PLUS1"; // Client recarrega una bala perque servidor bloqueja
                        utils.escriureString(result);
                        System.out.println("Client recarrega una bala i servidor bloqueja --> el joc segueix");
                        if(this.contBales ==1){
                            System.out.println("EL SERVIDOR SEGUEIX TENINT " + this.contBales + " BALA");
                        }
                        else{
                            System.out.println("EL SERVIDOR SEGUEIX TENINT " + this.contBales + " BALES");
                        }
                        break;
                    } else {
                        result = "SAFE2"; // Client i Servidor bloquejen els dos
                        utils.escriureString(result);
                        System.out.println("Client i Servidor bloquejen --> el joc segueix");
                        if(this.contBales ==1){
                            System.out.println("EL SERVIDOR SEGUEIX TENINT " + this.contBales + " BALA");
                        }
                        else{
                            System.out.println("EL SERVIDOR SEGUEIX TENINT " + this.contBales + " BALES");
                        }
                        break;
                    }
                case 3:
                    accioServer = "CHARGE";
                    this.contBales += 1;

                    if (accioRebuda.toUpperCase().equals("SHOOT")) {
                        result = "ENDS1"; // Client dispara, client guanya
                        utils.escriureString(result);
                        System.out.println("Client dispara i Servidor recarrega --> client guanya");
                        this.finalInt = 2;
                        break;
                    } else if (accioRebuda.toUpperCase().equals("CHARGE")) {
                        result = "PLUS2"; // Client i Servidor recarreguen una bala
                        utils.escriureString(result);
                        System.out.println("Client i Servidor recarreguen una bala --> el joc segueix");
                        if(this.contBales ==1){
                            System.out.println("EL SERVIDOR ARA TE " + this.contBales + " BALA");
                        }
                        else{
                            System.out.println("EL SERVIDOR ARA TE " + this.contBales + " BALES");
                        }
                        break;

                    } else {
                        result = "PLUS0"; // Client bloqueja, Servidor recarrega una bala
                        utils.escriureString(result);
                        System.out.println("Client bloqueja, Servidor recarrega una bala --> el joc segueix");
                        if(this.contBales ==1){
                            System.out.println("EL SERVIDOR ARA TE " + this.contBales + " BALA");
                        }
                        else{
                            System.out.println("EL SERVIDOR ARA TE " + this.contBales + " BALES");
                        }
                        break;
                    }
                default:
                    System.out.println("Ha habido un error");
            }

        } catch (IOException e) {
            throw new utilsError("Error a sendResult: "+e.getMessage());
        }
    }

    public String jocAcabat(Socket socket) throws utilsError{
        try {
            String resp = utils.llegirString();
            return resp.toUpperCase();
        } catch (IOException e) {
            throw new utilsError("Error a jocAcabat: "+e.getMessage());
        }
    }

    public boolean receivedError(Socket socket) {
        return false;
    }

}
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Locale;
import java.util.Scanner;

public class ClientProtocol {
    private Socket socket;
    private int id;
    private int contBales;
    private String result;
    private DataOutputStream data_outPut;
    private DataInputStream data_inPut;
    private util utilitat;
    Scanner scanner = new Scanner(System.in);

    public ClientProtocol(Socket socket) {
        this.socket = socket;
        try {
            data_outPut = new DataOutputStream(socket.getOutputStream());
            data_inPut = new DataInputStream(socket.getInputStream());
            utilitat = new util(socket);
            this.contBales = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendHello(Socket socket, byte opCode, int id, String name) {
        try {
            utilitat.escriureByte((opCode)); // Capçalera, serà un 1, perque es el HELLO
            utilitat.escriureInt(id);
            for (int i = 0; i < name.length(); i++) {
                char p = name.charAt(i);
                utilitat.escriureChar(p);
            }
            utilitat.escriureChar('4');
            utilitat.escriureByte((byte) 0); // Indica el final de trama
            utilitat.escriureByte((byte) 0); // Indica el final de trama
            utilitat.ferFlush();
            // data_outPut.close();
        } catch (IOException e) {
            throw new RuntimeException(
                    "I/O Error when creating or sending the output stream. Is the host connected?:\n" + e.getMessage());
        }

    }

    public boolean recivedReady(Socket socket) {
        try {
            byte opCode = utilitat.llegirByte();
            if (opCode != 2) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {
                System.out.println("The server sent the following opCode showing that it's ready:\n" + opCode);
                this.id = utilitat.llegirInt();
                System.out.println("The server sent the following int showing that it's ready:\n" + id);
                // data_inPut.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public void sendPlay(Socket socket) {
        try {
            byte opCode = 3;
            utilitat.escriureByte(opCode);
            System.out.println("Enviem opCode to play: \n" + opCode);
            utilitat.escriureInt(id);
            System.out.println("Enviem int to play: \n" + id);
            utilitat.ferFlush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean recivedAdmit(Socket socket) {
        try {
            byte opCode = utilitat.llegirByte();
            if (opCode != 4) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {
                System.out.println("The server sent the following opCode and you got admitted:\n" + opCode);
                int isAdmit = utilitat.llegirInt();
                if (isAdmit == 1) {
                    boolean admit = true;
                    System.out.println("The server sent the following bool refered to admit:\n" + isAdmit + admit);
                } else {
                    byte error = 4;
                    boolean admit = false;
                    // String msg = "NO S'ADMATEIX";
                    // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                }
                // data_inPut.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public void sendAction(Socket socket) {
        try {
            byte opCode = 5;
            utilitat.escriureByte(opCode);
            System.out.println("Enviem opCode to play: \n" + opCode);
            utilitat.escriureInt(id);
            System.out.println("Enviem int to play: \n" + id);
            if(contBales > 0 ){
                System.out.println("Què vols fer? (SHOOT, BLOCK o CHARGE)");
            } else{
                System.out.println("Què vols fer? (BLOCK o CHARGE)");
            }
            String accion = scanner.nextLine().toUpperCase(Locale.ROOT);
            while (!accion.equalsIgnoreCase("SHOOT") && !accion.equalsIgnoreCase("BLOCK")
                    && !accion.equalsIgnoreCase("CHARGE")) {
                System.out.println("Perdona, no t'he entés... ");
                if(contBales > 0 ){
                    System.out.println("Què vols fer? (SHOOT, BLOCK o CHARGE)");
                } else{
                    System.out.println("Què vols fer? (BLOCK o CHARGE)");
                }
                accion = scanner.nextLine();

            }
            System.out.println("La acció triada es: " + accion.toUpperCase(Locale.ROOT));
            if(accion.toUpperCase().equals("CHARGE")){
                this.contBales += 1;
                System.out.println("EL CLIENT ARA TE" + contBales + "BALES");
            }
            else if(accion.toUpperCase().equals("SHOOT")){
                this.contBales -= 1;
                System.out.println("EL CLIENT ARA TE" + contBales + "BALES");
            }
            utilitat.escriureAction(accion);
            utilitat.ferFlush();
        } catch (IOException error) {
            throw new RuntimeException(error);
        }
    }

    public boolean receivedResult(Socket socket) {
        try {
            byte opCode = utilitat.llegirByte();
            System.out.println("EL OPCODE EEEEEEEEEEES: " + opCode);
            if (opCode != 6) {
                byte error = 6;
                // String msg = "MISSATGE MAL FORMAT";
                // sendError(socket, (byte) 8, error, "no s'ha rebut resultat"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {
                System.out.println("The server sent the following opCode showing the result:\n" + opCode);
                String resultat = utilitat.llegirString();
                this.result = resultat;
                System.out.println("EL RESULT ES: " + result + " Y EL RESULTAT ERA: "  + resultat);
                System.out.println("Result S-------" + opCode + " " + this.result + "------C");
                // data_inPut.close();
                if(!this.result.equals("DRAW0") && !this.result.equals("ENDS0") && !this.result.equals("ENDS1")){
                    System.out.println("encara no s'ha acabat la partida");
                    return true;
                }else{
                    return false;
                }

            }
        } catch (IOException msg) {
            // TODO Auto-generated catch block
            throw new RuntimeException("error a receivedResult");
        }
    }

    public void finalGame(Socket socket) {
        switch (result){
            case "ENDS0":
                System.out.println("S'ha acabat la partida y ha guanyat client");
                break;
            case "ENDS1":
                System.out.println("S'ha acabat la partida y ha guanyat servidor");
                break;
            case "DRAW0":
                System.out.println("S'ha acabat la partida y heu empatat");
                break;

        }
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean recivedError(Socket socket) {
        DataInputStream data_inPut = null;
        try {
            data_inPut = new DataInputStream(socket.getInputStream());
            byte opCode = utilitat.llegirByte();
            System.out.println("The opCode it's bad:\n" + opCode);
            byte error = utilitat.llegirByte();
            System.out.println("The client has the following error number:\n" + error);
            int a = 1;
            String name = "";
            while (a != 48) {
                char e = utilitat.llegirChar();
                if (e != 48) {
                    name += (char) e;
                }
                a = e;
            }
            System.out.println("The client has the following message of error:\n" + name);

            // HACER BUCLE WHILE QUE LEA HASTA QUE HAYA EL 0 DEL BUFFER
            byte primer = utilitat.llegirByte();
            byte segon = utilitat.llegirByte();
            System.out.println("The client has the following bytes:\n" + primer + segon);
            System.out.println("Joc TANCAT");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
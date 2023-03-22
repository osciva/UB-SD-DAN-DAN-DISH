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

    public void sendHello(Socket socket, int id, String name) throws utilsError {
        try {
            byte opCode = 1;
            utilitat.escriureByte((opCode)); // Capçalera, serà un 1, perque es el HELLO
            utilitat.escriureInt(id);
            this.id = id;
            for (int i = 0; i < name.length(); i++) {
                char p = name.charAt(i);
                utilitat.escriureChar(p);
            }
            utilitat.escriureByte((byte) 0); // Indica el final de trama
            utilitat.escriureByte((byte) 0); // Indica el final de trama
            System.out.println("HELLO C ------- " + opCode + " " + this.id + " " + name + "00 ---------> S");
            utilitat.ferFlush();
            // data_outPut.close();
        } catch (IOException e) {
            throw new utilsError(
                    "I/O Error when creating or sending the output stream. Is the host connected?:\n" + e.getMessage());
        }

    }

    public String recivedReady(Socket socket) throws utilsError {
        try {
            byte opCode = utilitat.llegirByte();
            if (opCode != 2) {
                byte error = 4;
                System.out.println("Error al opCode");
                return "ERROR";
            } else {
                this.id = utilitat.llegirInt();
                System.out.println("READY C <------ " + opCode + " " + this.id + " ---------- S");
            }
        } catch (IOException e) {
            throw new utilsError(e.getMessage());
        }
        return "SEPLAY";
    }

    public String sendPlay(Socket socket) throws utilsError {
        try {
            byte opCode = 3;
            utilitat.escriureByte(opCode);
            utilitat.escriureInt(id);
            System.out.println("PLAY  C ------- " + opCode + " " + this.id + " ---------> S");
            utilitat.ferFlush();

        } catch (IOException e) {
            throw new utilsError(e.getMessage());
        }

        return "READMIT";
    }

    public String recivedAdmit(Socket socket) throws utilsError {
        try {
            byte opCode = utilitat.llegirByte();
            if (opCode != 4) {
                byte error = 4;
                // String msg = "INICI DE SESSIÓ INCORRECTE";
                // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                System.out.println("Error al opCode");
                return "ERROR";

            } else {
                byte isAdmit = utilitat.llegirByte();
                if (isAdmit == 1) {
                    boolean admit = true;
                    System.out.println("ADMIT C <------ " + opCode + " 1 " + " ---------- S");
                } else {
                    byte error = 4;
                    boolean admit = false;
                    System.out.println("you were not admitted");
                    // String msg = "NO S'ADMATEIX";
                    // sendError(socket, (byte) 8, error, "INICI DE SESSIÓ INCORRECTE"); // msg);
                }
            }
        } catch (IOException e) {
            throw new utilsError(e.getMessage());

        }
        // return true;
        return "SEACTION";
    }

    public String sendAction(Socket socket) throws utilsError {
        String accion2 = "BLOCK";
        try {
            byte opCode = 5;
            utilitat.escriureByte(opCode);
            utilitat.escriureInt(id);
            if (contBales > 0) {
                System.out.println("Què vols fer? (SHOOT, BLOCK o CHARG)");
                accion2 = scanner.nextLine().toUpperCase(Locale.ROOT);
            } else {
                System.out.println("Què vols fer? (BLOCK o CHARG)");
                accion2 = scanner.nextLine().toUpperCase(Locale.ROOT);
                while (!accion2.equalsIgnoreCase("BLOCK") && !accion2.equalsIgnoreCase("CHARG")) {
                    System.out.println("Nomes pots fer BLOCK o CHARG...");
                    System.out.println("Què vols fer? (BLOCK o CHARG)");
                    accion2 = scanner.nextLine().toUpperCase(Locale.ROOT);
                }
            }
            String accion = accion2;
            while (!accion.equalsIgnoreCase("SHOOT") && !accion.equalsIgnoreCase("BLOCK")
                    && !accion.equalsIgnoreCase("CHARG")) {
                System.out.println("Perdona, no t'he entés... ");
                if (contBales > 0) {
                    System.out.println("Què vols fer? (SHOOT, BLOCK o CHARG)");
                } else {
                    System.out.println("Què vols fer? (BLOCK o CHARG)");
                }
                accion = scanner.nextLine();

            }
            System.out.println("La acció triada es: " + accion.toUpperCase(Locale.ROOT));
            if (accion.toUpperCase().equals("CHARG")) {
                this.contBales += 1;
                if (contBales == 1) {
                    System.out.println("EL CLIENT ARA TE " + contBales + " BALA");
                } else {
                    System.out.println("EL CLIENT ARA TE " + contBales + " BALES");
                }
            } else if (accion.toUpperCase().equals("SHOOT")) {
                this.contBales -= 1;
                if (contBales == 1) {
                    System.out.println("EL CLIENT ARA TE " + contBales + " BALA");
                } else {
                    System.out.println("EL CLIENT ARA TE " + contBales + " BALES");
                }
            }
            utilitat.escriureAction(accion);
            //utilitat.escriureInt(this.contBales);
            System.out.println("ACTION C ------- " + opCode + " " + accion + " ---------> S");
            utilitat.ferFlush();

        } catch (IOException e) {
            throw new utilsError(e.getMessage());
        }
        return "JUGANT";
    }

    public boolean receivedResult(Socket socket) throws utilsError {
        try {
            byte opCode = utilitat.llegirByte();
            if (opCode != 6) {
                byte error = 6;
                // String msg = "MISSATGE MAL FORMAT";
                // sendError(socket, (byte) 8, error, "no s'ha rebut resultat"); // msg);
                System.out.println("Error al opCode");
                return false;
            } else {

                this.result = utilitat.llegirString();
                System.out.println("RESULT S <------ " + opCode + " " + this.result + " ---------- C");
                switch (result) {
                    case "PLUS0":
                        System.out.println("Client bloqueja i Servidor recarrega una bala");
                        break;
                    case "PLUS1":
                        System.out.println("Client recarrega una bala i Servidor bloqueja");
                        break;
                    case "PLUS2":
                        System.out.println("Client i Servidor recarreguen una bala");
                        break;
                    case "SAFE0":
                        System.out.println("Client dispara i Servidor bloqueja la bala");
                        break;
                    case "SAFE1":
                        System.out.println("Client ha bloquejat la bala que Servidor ha disparat");
                        break;
                    case "SAFE2":
                        System.out.println("Ambdós jugadors han bloquejat");
                        break;

                }
                if (!this.result.equals("DRAW0") && !this.result.equals("ENDS0") && !this.result.equals("ENDS1")) {
                    System.out.println("el joc segueix");
                    return true;
                } else {
                    return false;
                }

            }
        } catch (IOException e) {
            throw new utilsError(e.getMessage());
        }
    }

    public int finalGame(Socket socket) throws utilsError {
        if (result.equals("ENDS0") || result.equals("ENDS1") || result.equals("DRAW0")) {
            switch (result) {
                case "ENDS0":
                    System.out.println("Servidor ha disparat mentres Client recarregava --> Guanya Servidor");
                    this.contBales = 0;
                    break;
                case "ENDS1":
                    System.out.println("Client ha disparat mentres Servidor recarregava --> Guanya Client");
                    this.contBales = 0;
                    break;
                case "DRAW0":
                    System.out.println("Ambdós jugadors han disparat --> Empat");
                    this.contBales = 0;
                    break;

            }
            String resposta = "";
            System.out.println("Vols jugar una altra partida? (Si o No) ");
            Scanner sc = new Scanner(System.in);
            resposta = sc.nextLine();
            while (!resposta.equalsIgnoreCase("SI") && !resposta.equalsIgnoreCase("NO")) {
                System.out.println("Perdona, no t'he entés... ");
                System.out.println("Vols jugar una altra partida? (Si o No) ");
                sc = new Scanner(System.in);
                resposta = sc.nextLine();
            }
            if (resposta.toUpperCase().equals("SI")) {
                try {
                    utilitat.escriureString("SI");
                    utilitat.ferFlush();
                } catch (IOException e) {
                    throw new utilsError(e.getMessage());
                }
                return 1;

            } else {
                try {
                    utilitat.escriureString("NO");
                    utilitat.ferFlush();
                    return 2;

                } catch (IOException e) {
                    throw new utilsError(e.getMessage());
                }
            }
        } else {
            try {
                utilitat.escriureString("Segueix");
                return 3;
            } catch (IOException e) {
                throw new utilsError(e.getMessage());
            }
        }
        /*
         * try {
         * socket.close();
         * } catch (IOException e) {
         * throw new RuntimeException(e);
         * }
         */
    }

    /*
     * public boolean recivedError(Socket socket) {
     * DataInputStream data_inPut = null;
     * try {
     * data_inPut = new DataInputStream(socket.getInputStream());
     * byte opCode = utilitat.llegirByte();
     * System.out.println("The opCode it's bad:\n" + opCode);
     * byte error = utilitat.llegirByte();
     * System.out.println("The client has the following error number:\n" + error);
     * int a = 1;
     * String name = "";
     * while (a != 48) {
     * char e = utilitat.llegirChar();
     * if (e != 48) {
     * name += (char) e;
     * }
     * a = e;
     * }
     * System.out.println("The client has the following message of error:\n" +
     * name);
     * 
     * // HACER BUCLE WHILE QUE LEA HASTA QUE HAYA EL 0 DEL BUFFER
     * byte primer = utilitat.llegirByte();
     * byte segon = utilitat.llegirByte();
     * System.out.println("The client has the following bytes:\n" + primer + segon);
     * System.out.println("Joc TANCAT");
     * } catch (IOException e) {
     * throw new RuntimeException(e);
     * }
     * return true;
     * }
     */
}
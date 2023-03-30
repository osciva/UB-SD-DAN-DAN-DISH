import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.Socket;

public class util {

    /* Objectes per escriure i llegir dades */
    private DataInputStream dis;
    private DataOutputStream dos;

    // CONSTRUCTOR
    public util(Socket socket) throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    // getter del data input stream
    public DataInputStream getDis() {
        return dis;
    }

    // setter del data input stream
    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }

    // getter del data output stream
    public DataOutputStream getDos() {
        return dos;
    }

    // setter del data output stream
    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

    /*
     * Format de les trames:
     * hello-> codi operació 1 (opcode: 1 byte , id: int32 , name: String , 00: 2
     * bytes)
     * ready-> codi operació 2 (id: int32, opcode: 1 byte)
     * play-> codi operació 3 (opcode: 1 byte , id: int32)
     * admit-> codi operació 4 (opcode: 1 byte, bool: 1 byte)
     * action-> codi operació 5 (opcode: 1 byte, action: cadena de 5 caràcters en
     * format UTF (2 bytes) 5*2 bytes)
     * result-> codi operació 6 (opcode:1 byte, result: cadena de 5 caràcters en
     * format UTF (2 bytes) 5*2 bytes)
     * error-> codi operació 8 (opcode: 1 byte , ErrCode: 1 byte , Msg: String , 00:
     * 2 bytes)
     */
    private final byte CARACTER_NO_RECONEGUT = 1,
            MISSATGE_DESCONEGUT = 2, MISSATGE_FORA_DE_PROTOCOL = 3, INICI_DE_SESSIO_INCORRECTE = 4,
            PARAULA_DESCONEGUDA = 5, MISSATGE_MAL_FORMAT = 6, ERROR_DESCONEGUT = 99;

    public void sendError(byte errCode) throws IOException {
        String error = "";
        switch (errCode) {
            case CARACTER_NO_RECONEGUT:
                error = "CARÀCTER NO RECONEGUT";
            case MISSATGE_DESCONEGUT:
                error = "MISSATGE DESCONEGUT";
            case MISSATGE_FORA_DE_PROTOCOL:
                error = "MISSATGE FORA DE PROTOCOL";
            case INICI_DE_SESSIO_INCORRECTE:
                error = "INICI DE SESSIÓ INCORRECTE";
            case PARAULA_DESCONEGUDA:
                error = "PARAULA DESCONEGUDA";
            case MISSATGE_MAL_FORMAT:
                error = "MISSATGE MAL FORMAT";
            default:
                error = "ERROR DESCONEGUT";
        }
        ;
        try {
            dos.writeByte(8);
            dos.writeByte(errCode);
            dos.writeChars(error);
            dos.writeByte(0);
            dos.writeByte(0);
            dos.flush();
            dos.close();
        } catch (IOException e) {
            throw new RuntimeException("No es pot enviar el missatge d'error: " + e.getMessage());
        }

    }

    public void receiveError() throws IOException {

        try {
            byte opcode = dis.readByte();
            byte error = dis.readByte();
            char fin = dis.readChar();
            String msg = "";
            while (fin != 0) {
                msg += fin;
                fin = dis.readChar();
            }
            dis.close();
            System.out.println("Error: " + error + ": " + msg);
        } catch (IOException e) {
            throw new RuntimeException("No s'ha pogut enviar l'error" + e.getMessage());
        }

    }

    /* Llegir un enter */

    public int llegirInt() throws IOException {
        return this.dis.readInt();

    }

    /* Escriure un enter */

    public void escriureInt(int number) throws IOException {
        this.dos.writeInt(number);
    }

    // llegir 1 char
    public char llegirChar() throws IOException {
        return this.dis.readChar();
    }

    // escriure 1 char
    public void escriureChar(char caracter) throws IOException {
        this.dos.writeChar(caracter);
    }

    /*
     * LLegir un string, 48 = 0 en ASCII
     * Fins que no hi hagi un char amb valor en ascii 48 (=0) segueix llegint
     */

    public String llegirString() throws IOException {
        char a = 'b';
        String name = "";
        while (a != (byte) 0) {
            char e = this.dis.readChar();

            if (e != (byte) 0) {
                name += (char) e;
            } else {
                // this.dos.writeChar((byte) 0);
                break; // Sale del bucle while si se encuentra un 0
            }
            a = e;
        }
        return name;
        /*
         * StringBuilder str = new StringBuilder();
         * char c;
         * 
         * while((c = dis.readChar()) != 0){
         * str.append(c);
         * }
         * return str.toString();
         */

    }

    /* Escriure un string */

    public void escriureString(String stringData) throws IOException {

        String msg = stringData;
        for (int i = 0; i < msg.length(); i++) {
            char p = msg.charAt(i);
            this.dos.writeChar(p);
        }
        this.dos.writeChar(0);
        this.dos.writeChar(0);

    }

    // llegir 1 byte
    public byte llegirByte() throws IOException {
        return this.dis.readByte();
    }

    // escriure 1 byte
    public void escriureByte(byte opcode) throws IOException {
        this.dos.writeByte(opcode);
    }

    public String readElls() throws IOException {
        StringBuilder str = new StringBuilder();

        char c;

        while ((c = dis.readChar()) != 0) {
            str.append(c);
        }
        return str.toString();

    }

    public void ferFlush() throws IOException {
        this.dos.flush();
    }

    // escriure varis bytes

    public void writeBytes(byte[] data) throws IOException {
        this.dos.write(data);
        this.dos.flush();
    }

    // llegir UTF
    public String llegirUTF() throws IOException {
        return this.dis.readUTF();
    }

    // escriure en UTF
    public void escriureUTF(String message) throws IOException {
        this.dos.writeUTF(message);
    }

    // llegir la acció
    public String llegirAction() throws IOException {

        String name = "";
        for (int i = 0; i < 5; i++) {
            char e = this.dis.readChar();
            name += (char) e;
        }
        return name;
    }

    // escriure la acció
    public void escriureAction(String action) throws IOException {
        String msg = action;
        for (int i = 0; i < msg.length(); i++) {
            char p = msg.charAt(i);
            this.dos.writeChar(p);

        }

        // this.dos.writeChar(0);
    }

}
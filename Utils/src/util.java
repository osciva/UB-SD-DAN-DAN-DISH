import java.io.IOException;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.ByteArrayOutputStream;


public class util {


    /* Objectes per escriure i llegir dades */
    private DataInputStream dis;
    private DataOutputStream dos;


    //CONSTRUCTOR
    public util(Socket socket) throws IOException {
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }


    //getter del data input stream
    public DataInputStream getDis() {
        return dis;
    }
    //setter del data input stream
    public void setDis(DataInputStream dis) {
        this.dis = dis;
    }
    //getter del data output stream
    public DataOutputStream getDos() {
        return dos;
    }
    //getter del data output stream
    public void setDos(DataOutputStream dos) {
        this.dos = dos;
    }

/*
Format de les trames:
hello-> codi operació 1 (opcode: 1 byte ,  id: int32 ,  name: String , 00:  2 bytes)
ready-> codi operació 2 (id: int32, opcode: 1 byte)
play-> codi operació 3 (opcode: 1 byte , id: int32)
admit-> codi operació 4 (opcode: 1 byte, bool: 1 byte)
action-> codi operació 5 (opcode: 1 byte, action: cadena de 5 caràcters en format UTF (2 bytes) 5*2 bytes)
result-> codi operació 6 (opcode:1 byte, result: cadena de 5 caràcters en format UTF (2 bytes)  5*2 bytes)
error-> codi operació 8 (opcode: 1 byte ,  ErrCode: 1 byte ,  Msg: String , 00:  2 bytes)
*/


    /* Llegir un enter */
    public int readInt() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(readBytes(dis));
        buffer.flip();
        return buffer.getInt();
    }


    /* Escriure un enter */
    public void writeInt(int number) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(number);
        writeBytes(dos, buffer.array());
    }


    /*LLegir un string, 48 = 0 en ASCII
     * Fins que no hi hagi un char amb valor en ascii 48 (=0) segueix llegint*/
    public void read_string() throws IOException {
        int a = 1;
        String name = "";
        while(a!=48) {
            char e = dis.readChar();
            if(e != 48) {
                name += (char) e;
            }
            a = e;
        }
        System.out.println("The client send the following String:\n" + name);

    }

    /*Escriure un string*/

    public void write_string(int headerLength, String stringData) throws IOException {
        byte[] headerBytes = new byte[headerLength];
        String headerString;
        int stringLength = 0;

        // Se crea la cabecera con la cantidad de bytes que codifican la longitud
        stringLength = stringData.length();

        headerString = String.valueOf(stringLength);
        int headerStringLength;
        // comprueba si la longitud de la cadena de la cabecera es menor que la longitud esperada para la cabecera.
        // De ser así, se añaden ceros al principio de la cadena hasta que tenga la longitud esperada.
        if ((headerStringLength = headerString.length()) < headerLength) {
            for (int i = headerStringLength; i < headerLength; i++) {
                headerString = "0" + headerString;
            }
        }
        for (int i = 0; i < headerLength; i++) {
            headerBytes[i] = (byte) headerString.charAt(i);
        }

        // Se envia la cabecera
        dos.write(headerBytes, 0, headerLength);

        // se enviar la cadena usando writeBytes
        dos.writeBytes(stringData);
    }



    public static byte[] readBytes(DataInputStream dis) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dis.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }


    public static void writeBytes(DataOutputStream dos, byte[] data) throws IOException {
        dos.write(data);
        dos.flush();
    }


    public static String readAction(InputStream input) throws IOException {
        //return read_string(input);
        return null;
    }

    public static void writeAction(OutputStream output, String action) throws IOException {
        //write_string(output, action);
    }

}
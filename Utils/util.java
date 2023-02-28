package Utils;

import java.io.IOException;
import java.io.OutputStream;

public class util {
    

    public static void writeBytes(OutputStream output, byte[] value) throws IOException{
        output.write(value);
    }


}

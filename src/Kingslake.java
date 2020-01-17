import java.io.*;
import java.net.Socket;

public class Kingslake {

    public static void main(String... args) throws IOException {
        try(Socket socket = new Socket("localhost", 57777);
            PrintWriter to = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())))
        {
            for(int i = 0; i<args.length;i++) {
                to.println(args[i]);
                to.flush();
                BufferedReader from = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println(from.readLine());
            }

        }
    }

}

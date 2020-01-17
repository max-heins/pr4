import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class KingslakeServer
{
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(57777);
        Socket socket = serverSocket.accept();
        InputStream ins = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
        String line = "";
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        while ((line = reader.readLine()) != null) {
            line = "* " + line + " *";
            writer.println(line);
        }


    }
}

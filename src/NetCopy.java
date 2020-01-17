import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Uebertraegt ein File zwischen zwei Hosts.
 * Arbeitet als Sender oder Empfaenger, je nach Kommandozeilenargument:
 * Wenn das einzige Kommandozeilenargument ein regulaeres File ist, startet das Programm als
 * Server und sendet das File an den ersten Client.
 * Andernfalls ist das Kommandozeilenargument ein Hostname und das Programm holt sich ein File
 * von diesem.
 * Der Sender schickt und der Empfaenger liest nacheinander:
 * Filename (ohne Pfad), 0-Byte, Inhalt des Files.
 */
public class NetCopy {
    /**
     * Port.
     */
    private static final int PORT = 5000;

    /**
     * Hauptprogramm.
     * Sendet oder empfaengt ein File.
     *
     * @param args Kommandozeilenargument: Lokaler Pfadname (Sender) oder Hostname (Empfaenger).
     * @throws IOException          wenn beim Zugriff aufs Filesystem oder aufs Netzwerk etwas schief geht.
     */
    public static void main(String... args) throws IOException {
        if(!send(args[0])) {
            System.out.println("Empfang");
            receive(args[0]);
        }
    }

    /**
     * Holt ein File vom Server.
     * @param hostname Hostname des Servers, der ein File liefert.
     * @throws IOException wenn beim Empfangen oder Schreiben des Files etwas schief geht.
     */
    public static void receive(String hostname) throws IOException {

        Socket socket = new Socket(hostname, PORT);
        InputStream ins = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));

        String filename = reader.readLine();
        System.out.println(filename);

        String line = "";
        File targetFile = new File(".\\src\\tast.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile, true));
        while ((line = reader.readLine()) != null) {
            writer.append((line + "\n"));
        }
        writer.close();

    }

    /**
     * Schickt ein File zum Client.
     *
     * @param filename Pfadname eines lokalen Files.
     * @return true, wenn filename versendet wurde; false ansonsten.
     * @throws IOException wenn filename existiert, aber beim Lesen oder Senden etwas schief ging.
     */
    public static boolean send(String filename) throws IOException {
        try
        {
            filename.substring(0, filename.lastIndexOf("."));
        }
        catch (StringIndexOutOfBoundsException e)
        {
            return false;
        }
            if(!File.createTempFile(filename.substring(0, filename.lastIndexOf(".")), filename.substring(filename.lastIndexOf("."))).exists())
        {
            return false;
        }

        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();
        System.out.println("Verbindung hergestellt : " + socket);
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(filename);

        filename = ".\\src\\" + filename;
        File transferFile = new File (filename);
        String content = Files.readString(Paths.get(filename));
        writer.println(content);

        socket.close();
        System.out.println("Ende");
        return true;
    }
}
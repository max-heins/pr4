import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;

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
        System.out.println("Anfang");
        System.out.println(args[0]);
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
        //TODO
        InputStream initi = new FileInputStream(new File(hostname));
        Socket socket = new Socket(hostname, PORT);
        InputStream i = socket.getInputStream();
        byte[] buffer = new byte[i.available()];
        i.read(buffer);

        File targetFile = new File("./");
        OutputStream o = new FileOutputStream(targetFile);
        o.write(buffer);
    }

    /**
     * Schickt ein File zum Client.
     *
     * @param filename Pfadname eines lokalen Files.
     * @return true, wenn filename versendet wurde; false ansonsten.
     * @throws IOException wenn filename existiert, aber beim Lesen oder Senden etwas schief ging.
     */
    public static boolean send(String filename) throws IOException {
        if(!File.createTempFile(filename, filename.substring(filename.lastIndexOf("."))).exists())
        {
            return false;
        }
        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();
        System.out.println("Accepted connection : " + socket);
        File transferFile = new File (filename);
        byte [] bytearray  = new byte [(int)transferFile.length()];
        FileInputStream fin = new FileInputStream(transferFile);
        BufferedInputStream bin = new BufferedInputStream(fin);
        bin.read(bytearray,0,bytearray.length);
        OutputStream out = socket.getOutputStream();
        System.out.println("Sending Files...");
        out.write(bytearray,0,bytearray.length);
        out.flush();
        socket.close();
        System.out.println("File transfer complete");
        return false;
    }
}
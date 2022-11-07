import java.util.Scanner;
import java.net.URL;
import java.io.*;

class Practica {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://raw.githubusercontent.com/cardstdani/practica-java/main/Diccionario.txt");
            Scanner s = new Scanner(url.openStream());

            System.out.println(s.next());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

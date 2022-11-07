import java.util.Scanner;
import java.net.URL;
import java.io.*;

class Practica {
    public static void main(String[] args) {
        System.out.println("Juguemos a Wordle");
        System.out.println("Piensa una palabra ...");
        System.out.println("Y dime si la acierto: ");

        // GENERAR PALABRA Y ESPERAR ENTRADA DE 0 Y 1 DEL USUARIO
        System.out.println(generateRandomWord());

        try {
            URL url = new URL("https://raw.githubusercontent.com/cardstdani/practica-java/main/Diccionario.txt");
            Scanner s = new Scanner(url.openStream());

            //System.out.println(s.next());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // AQUI HABRIA QUE METERLE EN UN FUTURO ARGUMRNTOS PARA QUE GENERE DEPENDIENDO DE LA ENTRADA DEL USUARIO
    public static String generateRandomWord() {
        String word = "";
        try {
            URL url = new URL("https://raw.githubusercontent.com/cardstdani/practica-java/main/Diccionario.txt");
            Scanner s = new Scanner(url.openStream());
            int indexOfWord = (int) (Math.random() * 65780 + 1);
            for (int i = 0; i < indexOfWord-1; i++){
                word = s.next();
            }
            //PUTO JAVA NO ME DEJA HACER AQU EL RETURN
        } catch (IOException ex) {
            ex.printStackTrace();
            word = "ERROR";
        }

        return word.toUpperCase();
    }
}

import java.util.Scanner;
import java.net.URL;
import java.io.*;

class Practica {
    public static void main(String[] args) {
        System.out.println("Juguemos a Wordle");
        System.out.println("Piensa una palabra ...");
        System.out.println("Y dime si la acierto: ");

        // GENERAR PALABRA Y ESPERAR ENTRADA DE 0 Y 1 DEL USUARIO
        //System.out.println(generateRandomWord());

        try {
            File doc = new File("./Diccionario.txt");
            Scanner s = new Scanner(doc);

            System.out.println(s.next());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // AQUI HABRIA QUE METERLE EN UN FUTURO ARGUMRNTOS PARA QUE GENERE DEPENDIENDO DE LA ENTRADA DEL USUARIO
    public static String generateRandomWord() {
        String word = "";
        try {
            File doc = new File("./Diccionario.txt");
            Scanner s = new Scanner(doc);
            int indexOfWord = (int) (Math.random() * 7893600 + 1);
            for (int i = 0; i < indexOfWord-1; i++){
                word = s.next();
            }
            //PUTO JAVA NO ME DEJA HACER AQUI EL RETURN
        } catch (IOException ex) {
            ex.printStackTrace();
            word = "ERROR";
        }

        return word.toUpperCase();
    }
}

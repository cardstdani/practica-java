import java.util.Scanner;
import java.io.*;

class Practica {
    public static void main(String[] args) {
        String diccionario[] = generarDiccionario("./Diccionario.txt");
        
        
        System.out.println("Juguemos a Wordle");
        System.out.println("Piensa una palabra ...");
        System.out.println("Y dime si la acierto: ");

        // GENERAR PALABRA Y ESPERAR ENTRADA DE 0 Y 1 DEL USUARIO
        System.out.println(generateRandomWord(diccionario));
    }
    
    // AQUI HABRIA QUE METERLE EN UN FUTURO ARGUMRNTOS PARA QUE GENERE DEPENDIENDO DE LA ENTRADA DEL USUARIO
    public static String generateRandomWord(String diccionario[]) {
        return diccionario[(int)(Math.random() * diccionario.length + 1) - 1];
    }

    public static String[] generarDiccionario(String ruta){
        String diccionario[] = {};
        try {
            File doc = new File(ruta);
            Scanner s = new Scanner(doc);
            diccionario = (s.useDelimiter("\\A").next()).split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return diccionario;
    }
}

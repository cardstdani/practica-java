import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

class Practica {
    final static int intentos = 6;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] diccionario = generarDiccionario("C:\\Users\\danie\\Desktop\\JavaProjects\\StandardJavaProject\\src\\Diccionario.txt");


        System.out.println("Juguemos a Wordle");
        System.out.println("Piensa una palabra ...");
        System.out.println("Y dime si la acierto: ");

        int[] entrada = new int[]{0, 0, 0, 0, 0};
        for (int intento = 0; intento < intentos; intento++) {
            String word = diccionario[new Random().nextInt(diccionario.length)];
            System.out.println(word);
            entrada = stringToIntArray(in.nextLine(), 5);


            if (validar(entrada)) {
                System.out.println("La hacerte ¿?: ");
                diccionario = updateDict(diccionario, entrada);
            } else {
                System.out.println("Error, intenta otra vez");
                intento--;
            }
        }
    }

    //Método que valida que el usuario no haga trampas
    public static boolean validar(int[] s) {
        for(int i=0; i<s.length;i++) {
            if(s[i]==-1){
                return false;
            }
        }
        return true;
    }

    public static String[] updateDict(String[] diccionario, int[] entrada) {
        return diccionario;
    }

    public static String[] generarDiccionario(String ruta) {
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

    public static int[] stringToIntArray(String in, int max) {
        int result[] = new int[max];
        char[] descomposicion = in.toCharArray();

        if(descomposicion.length<5) {
            result[0]=-1;
            return result;
        }

        for (int i = 0; i < descomposicion.length && i < max; i++) {
            result[i] = descomposicion[i]>='0' && descomposicion[i]<='2' ? (int)descomposicion[i]-48:-1;
        }
        return result;
    }
}

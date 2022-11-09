import java.util.Scanner;
import java.io.*;

class Practica {
    final static int intentos = 5;
    public static void main(String[] args) {
        String diccionario[] = generarDiccionario("./Diccionario.txt");
        
        
        System.out.println("Juguemos a Wordle");
        System.out.println("Piensa una palabra ...");
        System.out.println("Y dime si la acierto: ");

        int entrada[] = {0,0,0,0,0}; 
        String prevWord = "";
        for(int intento = 0; intento < intentos; intento++){
            String actualWord = generateRandomWord(diccionario, entrada, prevWord);
            System.out.println(actualWord);
            System.out.println("La hacerte ¿?: ");
            Scanner in = new Scanner(System.in);
            entrada = stringToIntArray(in.nextLine(), 5);
            prevWord = actualWord;
        }
    }
    
    // AQUI HABRIA QUE METERLE EN UN FUTURO ARGUMRNTOS PARA QUE GENERE DEPENDIENDO DE LA ENTRADA DEL USUARIO
    public static String generateRandomWord(String diccionario[], int entrada[], String prevWord) {
        String result = "";
        int prevIndex = 0;
        String oldSelectedWord = "";
        for(int i = 0; i < diccionario.length; i++){
            for(int v = 0; v < entrada.length; v++){
                if(entrada[v] == 1){
                    
                }else if(entrada[v] == 2){

                }else{

                }
            }
        }
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

    public static int[] stringToIntArray(String in, int max){
        int result[] = new int[max]; 
        char[] descomposicion = in.toCharArray();
        for (int i = 0; i < descomposicion.length && i < max; i++){
            result[i] = Integer.parseInt(String.valueOf(descomposicion[i]));
        }
        return result;
    }
}

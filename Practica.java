import java.util.Scanner;
import java.io.*;

class Practica {
    final static int intentos = 10;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
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
            
            entrada = stringToIntArray(in.nextLine(), 5);
            prevWord = actualWord;
        }
    }
    
    // AQUI HABRIA QUE METERLE EN UN FUTURO ARGUMRNTOS PARA QUE GENERE DEPENDIENDO DE LA ENTRADA DEL USUARIO
    public static String generateRandomWord(String diccionario[], int entrada[], String prevWord) {
        String result = "";
        int prevIndex = 0;
        String oldSelectedWord = "";
        String searchedWord[] = new String[5];
        String searchedLetter = "";
        boolean have2 = false;
        if(prevWord.length() == 5){
            for(int v = 0; v < entrada.length; v++){
                if(entrada[v] == 1){
                    searchedLetter = searchedLetter + Character.toString(prevWord.charAt(v));
                }else if(entrada[v] == 2){
                    have2 = true;
                    searchedWord[v] = Character.toString(prevWord.charAt(v));
                }
            }
            for(int i = 0; i < diccionario.length; i++){
                if(!diccionario[i].equals(prevWord)){
                    boolean pass = have2;
                    if(have2){
                        for (int v = 0; v < searchedWord.length; v++){
                            if(pass){
                                pass = (Character.toString(diccionario[i].charAt(v)).equals(searchedWord[v])) ? true:false;
                            }
                        }
                    }


                    if(pass){
                        result = diccionario[i];
                    }else{
                        char[] palabra = diccionario[i].toCharArray();
                        int coincidencias = 0;
                        for(int v = 0; v < palabra.length; v++){
                            for (int k = 0; k < searchedLetter.length();k++){
                                if(Character.toString(palabra[v]).equals(Character.toString(searchedLetter.charAt(k)))){
                                    coincidencias++;
                                }
                            }
                        }
                        if(searchedLetter.length() != 0 && coincidencias >= searchedLetter.length()){
                            oldSelectedWord = result;
                            result = diccionario[i];
                        }
                    }
                }
            }
            if(result.equals("")){
                result = diccionario[(int)(Math.random() * diccionario.length + 1) - 1];
            }
        }else result = diccionario[(int)(Math.random() * diccionario.length + 1) - 1];
        return result;
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

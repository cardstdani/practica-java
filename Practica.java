import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

class Practica {
    final static int intentos = 6;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] diccionario = generarDiccionario(".\\Diccionario.txt");
        String[] diccionarioOriginal = diccionario;

        System.out.println("Juguemos a Wordle");
        System.out.println("Piensa una palabra ...");
        System.out.println("Y dime si la acierto: ");

        int[] entrada = new int[]{0, 0, 0, 0, 0};
        char[] noEstan = new char[]{};
        char[][][] estan = new char[][][]{{{}, {}}, {{}, {}}, {{}, {}}, {{}, {}}, {{}, {}}};
        for (int intento = 0; intento < intentos; intento++) {
            System.out.println(diccionario.length);
            String word = diccionario.length>0 ? diccionario[new Random().nextInt(diccionario.length)] : diccionarioOriginal[new Random().nextInt(diccionario.length)];
            System.out.println(word);
            entrada = stringToIntArray(in.nextLine(), 5);


            if (validar(entrada)) {
                System.out.println("La hacerte ¿?: ");
                for (int i = 0; i < entrada.length; i++) {
                    switch (entrada[i]) {
                        case 0: {
                            if (!in(noEstan, word.charAt(i))) {
                                noEstan = pushToArray(noEstan, word.charAt(i));
                            }
                            break;
                        }
                        case 1: {
                            if (!in(estan[i][0], word.charAt(i))) {
                                estan[i][0] = pushToArray(estan[i][0], word.charAt(i));
                            }
                            break;
                        }
                        case 2: {
                            if (estan[i][1].length < 1) {
                                estan[i][1] = pushToArray(estan[i][1], word.charAt(i));
                            }
                            break;
                        }
                    }
                }

                diccionario = updateDict(diccionario, noEstan, estan);
            } else {
                System.out.println("Error, intenta otra vez");
                intento--;
            }
        }
    }

    public static String[] updateDict(String[] diccionario, char[] noEstan, char[][][] estan) {
        String[] tmpDict = new String[]{};

        for (int i = 0; i < diccionario.length; i++) {
            boolean result = true;
            for (int j = 0; j < diccionario[i].length(); j++) {
                char letra = diccionario[i].charAt(j);
                if(in(noEstan,letra) | (estan[j][0].length>0)?in(estan[j][0],letra):false | (estan[j][1].length>0) ? letra!=estan[j][1][0]:false) {
                    result &= false;
                    break;
                }
            }
            if(result) tmpDict = pushToArray(tmpDict, diccionario[i]);
        }
        return tmpDict;
    }

    //Método que valida que el usuario no haga trampas
    public static boolean validar(int[] s) {
        for (int i = 0; i < s.length; i++) {
            if (s[i] == -1) {
                return false;
            }
        }
        return true;
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

        if (descomposicion.length < 5) {
            result[0] = -1;
            return result;
        }

        for (int i = 0; i < descomposicion.length && i < max; i++) {
            result[i] = descomposicion[i] >= '0' && descomposicion[i] <= '2' ? (int) descomposicion[i] - 48 : -1;
        }
        return result;
    }

    //Posible overriding
    public static char[] pushToArray(char[] arr, char elem) {
        char[] tmp = new char[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            tmp[i] = arr[i];
        }
        tmp[arr.length] = elem;
        return tmp;
    }

    public static String[] pushToArray(String[] arr, String elem) {
        String[] tmp = new String[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            tmp[i] = arr[i];
        }
        tmp[arr.length] = elem;
        return tmp;
    }

    public static boolean in(char[] arr, char elem) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == elem) {
                return true;
            }
        }
        return false;
    }
}

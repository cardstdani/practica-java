import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

class Practica {
    final static int intentos = 6;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] diccionario = generarDiccionario("C:\\Users\\danie\\Desktop\\JavaProjects\\StandardJavaProject\\src\\Diccionario.txt");
        String[] diccionarioOriginal = diccionario;

        System.out.println("Juguemos a Wordle");
        System.out.println("Piensa una palabra ...");
        System.out.println("Y dime si la acierto: ");

        int[] entrada = new int[]{0, 0, 0, 0, 0};
        char[] abecedario = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        char[][][] posibleEstructura = new char[][][]{{abecedario, {}}, {abecedario, {}}, {abecedario, {}}, {abecedario, {}}, {abecedario, {}}};
        for (int intento = 0; intento < intentos; intento++) {
            System.out.println(diccionario.length);
            String word = diccionario.length > 0 ? diccionario[new Random().nextInt(diccionario.length)] : diccionarioOriginal[new Random().nextInt(diccionario.length)];
            System.out.println(word);
            entrada = stringToIntArray(in.nextLine(), 5);


            if (validar(entrada, word, posibleEstructura)) {
                System.out.println("La hacerte ¿?: ");
                for (int i = 0; i < entrada.length; i++) {
                    char letra = word.charAt(i);
                    switch (entrada[i]) {
                        case 0: {
                            for (int a = 0; a < posibleEstructura.length; a++) {
                                if(!in(posibleEstructura[a][1], letra)) posibleEstructura[a] = new char[][]{deleteFromArray(posibleEstructura[a][0], letra), pushToArray(posibleEstructura[a][1], letra)};
                            }
                            break;
                        }
                        case 1: {
                            for (int a = 0; a < posibleEstructura.length; a++) {
                                if (a == i) {
                                    posibleEstructura[a] = new char[][]{deleteFromArray(posibleEstructura[a][0], letra), pushToArray(posibleEstructura[a][1], letra)};
                                } else {
                                    if(!in(posibleEstructura[a][0], letra) && in(posibleEstructura[a][1], letra)) {
                                        posibleEstructura[a] = new char[][]{pushToArray(posibleEstructura[a][0], letra), deleteFromArray(posibleEstructura[a][1], letra)};
                                    }
                                }
                            }
                            break;
                        }
                        case 2: {
                            posibleEstructura[i] = new char[][]{new char[]{letra}, deleteFromArray(abecedario, letra)};
                            break;
                        }
                    }
                }
                System.out.println(Arrays.deepToString(posibleEstructura));


                diccionario = updateDict(diccionario, posibleEstructura);
            } else {
                System.out.println("Error, intenta otra vez");
                intento--;
            }
        }
    }

    public static String[] updateDict(String[] diccionario, char[][][] estan) {
        String[] tmpDict = new String[]{};

        for (int i = 0; i < diccionario.length; i++) {
            boolean result = true;
            for (int j = 0; j < diccionario[i].length(); j++) {
                char letra = diccionario[i].charAt(j);
                if (in(estan[j][1], letra)) {
                    result &= false;
                    break;
                }
            }
            if (result) tmpDict = pushToArray(tmpDict, diccionario[i]);
        }
        return tmpDict;
    }

    //Método que valida que el usuario no haga trampas
    public static boolean validar(int[] s, String word, char[][][] posibleEstructura) {
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

    public static char[] deleteFromArray(char[] arr, char elem) {
        char[] tmp = new char[]{};
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != elem) tmp = pushToArray(tmp, arr[i]);
        }
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

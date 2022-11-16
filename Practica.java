import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

class Practica {
    final static int intentos = 6, maxInputLength = 5;

    public static void main(String[] args) {
        jugar();
    }

    public static void jugar(){
        Scanner in = new Scanner(System.in);
        String[] diccionario = generarDiccionario("./Diccionario2.txt"); // mejor poner ./ en vez de .\\ si no no funca en linux
        String[] diccionarioOriginal = diccionario;
        debugStringArray(diccionarioOriginal); // DEBUG
        debugStringArray(diccionario); // DEBUG

        System.out.println("Juguemos a Wordle");
        System.out.println("Piensa una palabra ...");
        System.out.println("Y dime si la acierto: ");

        int[] entrada;
        char[] abecedario = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        /**
         * En este array se almacenan las letras que pueden ir en cada posicion en el primer sub-array. En el segundo, todas las que deben ir
         */
        char[][][] posibleEstructura = new char[][][]{{abecedario, {}}, {abecedario, {}}, {abecedario, {}}, {abecedario, {}}, {abecedario, {}}};

        boolean win = false;

        for (int intento = 0; intento < intentos; intento++) {
            System.out.println(diccionario.length); //DEBUG

            String word = diccionario.length > 0 ? diccionario[new Random().nextInt(diccionario.length)] : diccionarioOriginal[new Random().nextInt(diccionarioOriginal.length)];
            System.out.println(word);
            System.out.println("La hacerte ¿?: ");
            entrada = stringToIntArray(in.nextLine());


            if (validar(entrada, word, posibleEstructura)) {
                if(Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2})){
                    intento = intentos;
                    win = true;
                }else{
                    for (int i = 0; i < entrada.length; i++) {
                        char letra = word.charAt(i);
                        switch (entrada[i]) {
                            case 0: {
                                for (int a = 0; a < posibleEstructura.length; a++) {
                                    if (posibleEstructura[a][0].length != 1 & in(posibleEstructura[a][0], letra))
                                        posibleEstructura[a] = new char[][]{deleteFromArray(posibleEstructura[a][0], letra), posibleEstructura[a][1]};
                                }
                                break;
                            }
                            case 1: {
                                for (int a = 0; a < posibleEstructura.length; a++) {
                                    if (a == i) {
                                        posibleEstructura[a] = new char[][]{deleteFromArray(posibleEstructura[a][0], letra), posibleEstructura[a][1]};
                                    } else {
                                        if (posibleEstructura[a][0].length != 1) {
                                            posibleEstructura[a] = new char[][]{posibleEstructura[a][0], pushToArray(posibleEstructura[a][1], letra)};
                                        }
                                    }
                                }
                                break;
                            }
                            case 2: {
                                for (int a = 0; a < posibleEstructura.length; a++) {
                                    if (a == i) {
                                        posibleEstructura[i] = new char[][]{new char[]{letra}, new char[]{}};
                                    } else {
                                        if (in(posibleEstructura[a][1], letra)) {
                                            posibleEstructura[a] = new char[][]{posibleEstructura[a][0], deleteFromArray(posibleEstructura[a][1], letra)};
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    System.out.println(Arrays.deepToString(posibleEstructura)); //DEBUG
                }
            } else {
                if (Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2})) {
                    System.out.println("Error, aunque se considera ganador");
                    intento = intentos;
                    win = true;
                } else {
                    System.out.println("Error, intenta otra vez");
                    intento--;
                }
            }
        }

        if(win){
            System.out.println("GANEEEE!");
        }else{
            System.out.println("Perdi :(");
            System.out.print("¿Cual era la palabra oculta? (Introducir sin tildes): ");
            String palabraOculta = in.nextLine().toUpperCase();
            System.out.print("\n¿La puedo añadir a mi diccionario? (Si, No): ");
            boolean canInclude = in.nextLine().toLowerCase() == "si";

            if(canInclude){
                // LA INCLUYE
            }else{
                // NO LA INCLUYE
            }
        }

        System.out.print("\n¿Otra partida? (Si, No): ");
        String newGame = in.nextLine().toLowerCase();
        if(newGame.equals("si")){
            jugar();
        }else if(newGame.equals("no")){
            System.out.println("Hasta la próxima :)");
        }
    }

    //Método que valida que el usuario no haga trampas
    public static boolean validar(int[] entrada, String word, char[][][] posibleEstructura) {
        //Validación de entrada correcta
        boolean allOnes = true;
        for (int i = 0; i < entrada.length; i++) {
            if (entrada[i] == -1) {
                return false;
            }

            allOnes &= entrada[i] == 1 & posibleEstructura[i][1].length>0;
        }
        if (allOnes) return false;

        //Validación anti-hackers
        for (int i = 0; i < entrada.length; i++) {
            char letra = word.charAt(i);

            switch (entrada[i]) {
                case 0: {
                    for (int a = i + 1; a < posibleEstructura.length; a++) {
                        if (word.charAt(a) == letra & entrada[a] == 1) {
                            return false;
                        }
                    }

                    if ((posibleEstructura[i][0].length == 1 & posibleEstructura[i][0][0] == letra) | in(posibleEstructura[i][1], letra)) {
                        return false;
                    }
                    break;
                }
                case 1: {
                    if (posibleEstructura[i][0].length == 1 & posibleEstructura[i][0][0] == letra) {
                        return false;
                    }

                    boolean result = true;
                    for (int a = 0; a < posibleEstructura.length; a++) {
                        if (in(posibleEstructura[a][0], letra)) {
                            result = false;
                            break;
                        }
                    }
                    if (result) return false;
                    break;
                }
                case 2: {
                    if (!in(posibleEstructura[i][0], letra)) {
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }

    public static String[] updateDict(String[] diccionario, char[][][] estan) {
        String[] tmpDict = new String[]{};

        for (int i = 0; i < diccionario.length; i++) {
            boolean result = true;
            for (int j = 0; j < diccionario[i].length(); j++) {
                char letra = diccionario[i].charAt(j);
                if (!in(estan[j][0], letra)) {
                    result &= false;
                    break;
                }
            }
            if (result) tmpDict = pushToArray(tmpDict, diccionario[i]);
        }
        return tmpDict;
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

    public static int[] stringToIntArray(String in) {
        int result[] = new int[maxInputLength];
        char[] descomposicion = in.toCharArray();

        if (descomposicion.length < 5) {
            result[0] = -1;
            return result;
        }

        for (int i = 0; i < descomposicion.length && i < maxInputLength; i++) {
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

    /**
     * @param arr
     * @param elem
     * @return
     */
    public static boolean in(char[] arr, char elem) {
        if (arr.length < 1) {
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == elem) {
                return true;
            }
        }
        return false;
    }

    public static void debugStringArray(String arr[]){
        System.out.print("\n{ ");
        for (int i = 0; i < arr.length;i++){
            System.out.print(arr[i]+", ");
        }
        System.out.println("}");
    }
}

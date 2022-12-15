import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;

class Practica {
    final static int intentos = 6, maxInputLength = 5;
    final static String rutaDicc = "./Diccionario.txt", rutaLogs = "./Logs.txt";

    public static void main(String[] args) {
        boolean repeat = false;
        do{
            printLogs(rutaLogs);
            Scanner in = new Scanner(System.in);
            String[] diccionario = generarDiccionario(rutaDicc); // mejor poner ./ en vez de .\\ si no no funca en linux
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
                entrada = stringToIntArray(in.next());


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
                        diccionario = updateDict(diccionario, posibleEstructura);
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

                String palabraOculta = "";
                do{
                    System.out.print("¿Cual era la palabra oculta? (Introducir sin tildes ni mierdas raras): ");
                    palabraOculta = in.next().toUpperCase();
                }while(palabraOculta.length() != maxInputLength);
                System.out.print("\n¿La puedo añadir a mi diccionario? (Si, No): ");

                if(in.next().equalsIgnoreCase("si")){
                    addWordToOriginalDicc(palabraOculta, diccionarioOriginal, rutaDicc);
                }
            }

            System.out.print("\n¿Otra partida? (Si, No): ");
            String other = in.next();
            if(other.equalsIgnoreCase("si")){
                repeat = true;
            }else if(other.equalsIgnoreCase("no")){
                System.out.println("Hasta la próxima :)");
                repeat = false;
            }else{
                repeat = false;
            }

            generateLog(rutaLogs, win);

            printLogs(rutaLogs);
        }while(repeat);
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

                    if ((posibleEstructura[i][0].length == 1 & posibleEstructura[i][0][0] == letra)) {
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

    public static String intArrayToString(int[] in) {
        String result = "";

        for (int i = 0; i < in.length; i++) {
            result = result+in[i] + " ";
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

    public static void addWordToOriginalDicc(String word, String[] dicc, String ruta){
        if(dicc.length < 99){
            try {
                File doc = new File(ruta);
                Scanner s = new Scanner(doc);
                String file = (s.useDelimiter("\\A").next());
                FileWriter writer = new FileWriter(ruta);
                writer.write(file+" "+word);
                writer.close();   
            } catch (Exception err) {
                
            }
        }else{
            System.out.println("No me entran mas palabras en el diccionario.");
        }
    }

    public static String stringArrayToString(String[] arr){
        String result = "";
        for (int i = 0; i<arr.length;i++){
            result = result + arr[i] + " ";
        }
        return result;
    }

    public static void printLogs(String ruta){
        int[] logs = getLogs(ruta);

        System.out.println("--------"+"\nPartidas Jugadas: "+logs[0]+"\nPartidas Ganadas por el usuario: "+logs[1]+"\nPartidas Perdidas por el usuario: "+logs[2]+"\n--------");
    }

    public static void generateLog(String ruta, boolean win){
        File doc = new File(ruta);

        try { 
            int[] logs = getLogs(ruta);
            doc.createNewFile(); 
            FileWriter writer = new FileWriter(doc);

            logs[0]++;

            if(win){
                logs[2]++;
            }else{
                logs[1]++;
            }
            
            writer.write(intArrayToString(logs));
            writer.close();
        } catch (Exception e) { }
    }

    public static int[] getLogs(String ruta){
        File doc = new File(ruta);
        int[] result = {0,0,0};
        try {
            Scanner s = new Scanner(doc);

            String file = (s.useDelimiter("\\A").next());
            
            Scanner i = new Scanner(file);
            for(int k = 0; k < result.length; k++)
                result[k] = i.nextInt();
        } catch (Exception e) { }
        
        return result;
    }
}

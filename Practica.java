import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.*;
/**
 * Wordle realizado con java.
 * @author Daniel García Solla | Ivan Álvaro Luis
 * @version 1.0, 24/12/2022
 */
public class Practica {
    /**
     * Se declaran cuatro constantes, para en un futuro poder cambiar facilmente la ruta del diccionario, la ruta de los logs, la longitud de las palabras y el numero de intentos de cada partida.
     */
    final static int intentos = 6, maxInputLength = 5;
    final static String rutaDicc = "./Diccionario.txt", rutaLogs = "./Logs.txt";
    /**
     * Metodo principal del programa.
     */
    public static void main(String[] args) {
        boolean repeat = false;
        /**
         * Variable utilizada para controlar el bucle do while.
         * Esta variable se utiliza, en caso de que el jugador quiera jugar otra partida.
         * Valores de tipo boolean
         */
        do{
            printLogs(rutaLogs);
            Scanner in = new Scanner(System.in);
            /**
             * Almacena el contenido del diccionario en un array de Strings
             * Es inicializada con el valor que devuelve el metodo generarDiccionario(ruta del archivo txt)
             */
            String[] diccionario = generarDiccionario(rutaDicc);
            /**
             * Almacena el contenido con el que se inicializa la variable diccionario
             */
            String[] diccionarioOriginal = diccionario;

            //debugStringArray(diccionarioOriginal); // DEBUG
            //debugStringArray(diccionario); // DEBUG

            System.out.println("Juguemos a Wordle");
            System.out.println("Piensa una palabra ...");
            System.out.println("Y dime si la acierto: ");
            /**
             * Variable en la que se almacenara, la entrada del usuario
             */
            int[] entrada;
            /**
             * char Array donde se almacenan todos los caracteres del abecedario
             */
            char[] abecedario = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            /**
             * En este array se almacenan las letras que pueden ir en cada posicion en el primer sub-array. En el segundo, todas las que deben ir
             */
            char[][][] posibleEstructura = new char[][][]{{abecedario, {}}, {abecedario, {}}, {abecedario, {}}, {abecedario, {}}, {abecedario, {}}};
            /**
             * Variable de tipo boolean, indica quien ganó la partida
             */
            boolean win = false;
            /**
             * Bucle for, utilizado para controlar el numero de intentos del programa para adivinar la palabra que piensa el jugador
             */
            for (int intento = 0; intento < intentos; intento++) {
                //System.out.println(diccionario.length); //DEBUG
                /**
                 * Almacena la palabra que se genera segun la entrada del usuario
                 * En el caso de que no se encuentre ninguna palabra que cumpla los requisitos de la entrada del usuario, se generara una palabra aleatoria tomada del diccionario original
                 */
                String word = diccionario.length > 0 ? diccionario[new Random().nextInt(diccionario.length)] : diccionarioOriginal[new Random().nextInt(diccionarioOriginal.length)];
                System.out.println(word);
                System.out.println("La hacerte ¿?: ");
                /**
                 * Se le asigna un valor a la variable de entrada
                 */
                entrada = stringToIntArray(in.next());

                /**
                 * Se valida la entrada del jugador
                 * Para ello se utiliza un metodo denomidado validar, el cual devuelve una variable boolean, tambien detecta la parte ganadora y si el usuario intentó hacer trampas
                 */
                if (validar(entrada, word, posibleEstructura)) {
                    /**
                     * Comprueba si la entrada del jugador esta conformada por todo 2 y en el caso de que sea asi declara ganador al programa
                     */
                    if(Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2})){
                        intento = intentos;
                        win = true;
                    }else{
                        /**
                         * Recorre las posiciones del array entrada
                         * Recoge todos los posibles casos de cada posicion y le asigna una letra a la posible estructura, despues actualiza el diccionario segun los parametros obtenidos
                         */
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
                        //System.out.println(Arrays.deepToString(posibleEstructura)); //DEBUG
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
            /**
             * Comprueba el ganador y en caso de que gane el usario le pregunta si puede añadir la palabra al diccionario, posteriormente la añade
             */
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
            /**
             * Pregunta al usuario si desea jugar otra partida, en caso afirmativo asigna a repeat el valor true y se entra en el bucle do while.
             */
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
            /**
             * Se generan los logs incluyendo la partida actual
             */
            generateLog(rutaLogs, win);
            /**
             * Muestra en pantalla los logs generados
             */
            printLogs(rutaLogs);
        }while(repeat);
    }

    /**
     * Metodo que se encarga de validar la entrada del usuario
     * @param entrada int array con los numeros del 0 al 2
     * @param word palabra actual generada a traves del diccionario
     * @param posibleEstructura estructura de la siguiente palabra en funcion de la entrada
     * @return devuelve una variable de tipo boolean indicando si la entrada es correcta y el jugador no ha intentado hacer trampas
     */
    public static boolean validar(int[] entrada, String word, char[][][] posibleEstructura) {
        /**
         * Validacion de entrada correcta
         */
        boolean allOnes = true;
        for (int i = 0; i < entrada.length; i++) {
            if (entrada[i] == -1) {
                return false;
            }

            allOnes &= entrada[i] == 1 & posibleEstructura[i][1].length>0;
        }
        if (allOnes) return false;

        /**
         * Validacion anti trampas
         */
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
    /**
     * Funcion encargada de actualizar las palabras del diccionario acorde con las letras buscadas
     * @param diccionario diccionario a actualizar
     * @param estan letras que se encuentran en las palabras buscadas
     * @return nuevo diccionario
     */
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
    /**
     * Genera una String Array a partir de un fichero
     * @param ruta Ruta del fichero
     * @return String array
     */
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
    /**
     * Convierte una array formada por String a una array formada por int
     * @param in String Array a convertir
     * @return int Array convertida
     */
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
    /**
     * Convierte una array formada por int a una variable String
     * @param in int Array a convertir
     * @return String convertida
     */
    public static String intArrayToString(int[] in) {
        String result = "";

        for (int i = 0; i < in.length; i++) {
            result = result+in[i] + " ";
        }
        return result;
    }
    /**
     * Inserta elementos en una array de tipo char
     * @param arr array a modificar
     * @param elem elemento a insertar
     * @return array resultante
     */
    //Posible overriding
    public static char[] pushToArray(char[] arr, char elem) {
        char[] tmp = new char[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            tmp[i] = arr[i];
        }
        tmp[arr.length] = elem;
        return tmp;
    }
    /**
     * Inserta elementos en una array de tipo String
     * @param arr array a modificar
     * @param elem elemento a insertar
     * @return array resultante
     */
    public static String[] pushToArray(String[] arr, String elem) {
        String[] tmp = new String[arr.length + 1];
        for (int i = 0; i < arr.length; i++) {
            tmp[i] = arr[i];
        }
        tmp[arr.length] = elem;
        return tmp;
    }
    /**
     * Elimina elementos de una array dada
     * @param arr array con elemento a eliminar
     * @param elem elemento a eliminar
     * @return array con el elemento eliminado
     */
    public static char[] deleteFromArray(char[] arr, char elem) {
        char[] tmp = new char[]{};
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != elem) tmp = pushToArray(tmp, arr[i]);
        }
        return tmp;
    }

    /**
     * Comprueba si un elemento esta dentro de un array
     * @param arr array de tipo char
     * @param elem elemento de tipo char a comprobar
     * @return boolean (true: el elemento esta en el array, false: el elemento no esta en el array)
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
    /**
     * Muestra en pantalla el contenido de un array de tipo String
     * @param arr String Array a mostrar
     */
    public static void debugStringArray(String arr[]){
        System.out.print("\n{ ");
        for (int i = 0; i < arr.length;i++){
            System.out.print(arr[i]+", ");
        }
        System.out.println("}");
    }
    /**
     * Escribe en un fichero los elementos de un String Array y le añade la variable word
     * @param word Palabra añadida al fichero
     * @param dicc Array con el contenido previo del fichero
     * @param ruta ruta del fichero
     */
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
    /**
     * Convierte un String Array en una String
     * @param arr String Array a convertir
     * @return String convertida
     */
    public static String stringArrayToString(String[] arr){
        String result = "";
        for (int i = 0; i<arr.length;i++){
            result = result + arr[i] + " ";
        }
        return result;
    }
    /**
     * Escribe en pantalla el contenido de un fichero (solo admite ficheros con datos de tipo int)
     * @param ruta ruta del fichero
     */
    public static void printLogs(String ruta){
        int[] logs = getLogs(ruta);

        System.out.println("--------"+"\nPartidas Jugadas: "+logs[0]+"\nPartidas Ganadas por el usuario: "+logs[1]+"\nPartidas Perdidas por el usuario: "+logs[2]+"\n--------");
    }
    /**
     * Genera un fichero con el numero de partidas jugadas y las partidas ganadas
     * @param ruta ruta del fichero
     * @param win variable que añade una partida ganada en caso de que este en true, si es false añade una partida perdida
     */
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
    /**
     * Obtiene la informacion de un fichero y la devuelve en forma de int Array
     * @param ruta ruta del fichero
     * @return contenido del fichero en int Array
     */
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

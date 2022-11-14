import java.util.*;
import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PracticaPRO {
    final static int intentos = 6, maxInputLength = 5;
    final static HashSet<Character> abecedario = new HashSet<>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
    final static Random r = new Random(300);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] diccionario = generarDiccionario(".\\Diccionario2.txt");
        String[] diccionarioOriginal = diccionario;

        System.out.println("Juguemos a Wordle");
        System.out.println("Piensa una palabra ...");
        System.out.println("Y dime si la acierto: ");

        int[] entrada;

        /**
         * En este array se almacenan las letras que pueden ir en cada posicion en el primer sub-array. En el segundo, todas las que deben ir
         */
        ArrayList<ArrayList<HashSet<Character>>> posibleEstructura = new ArrayList<>(maxInputLength);
        IntStream.range(0, maxInputLength).forEach(i -> posibleEstructura.add(new ArrayList<>(Arrays.asList(abecedario, new HashSet<>()))));

        for (int intento = 0; intento < intentos; intento++) {
            System.out.println(diccionario.length);
            String word = diccionario.length > 0 ? diccionario[r.nextInt(diccionario.length)] : diccionarioOriginal[r.nextInt(diccionarioOriginal.length)];
            System.out.println(word);
            entrada = stringToIntArray(in.nextLine());

            if (validar(entrada, word, posibleEstructura)) {
                for (int i = 0; i < entrada.length; i++) {
                    char letra = word.charAt(i);
                    switch (entrada[i]) {
                        case 0: {
                            for (int a = 0; a < posibleEstructura.size(); a++) {
                                if (posibleEstructura.get(a).get(0).size() != 1 & posibleEstructura.get(a).get(0).contains(letra)) {
                                    posibleEstructura.get(a).get(0).remove(letra);
                                }
                            }
                            break;
                        }
                        case 1: {
                            for (int a = 0; a < posibleEstructura.size(); a++) {
                                if (a == i) {
                                    posibleEstructura.get(a).get(0).remove(letra);
                                } else {
                                    if (posibleEstructura.get(a).get(0).size() != 1) {
                                        posibleEstructura.get(a).get(1).add(letra);
                                    }
                                }
                            }
                            break;
                        }
                        case 2: {
                            for (int a = 0; a < posibleEstructura.size(); a++) {
                                if (a == i) {
                                    posibleEstructura.set(i, new ArrayList<>(Arrays.asList(new HashSet<>(Arrays.asList(letra)), new HashSet<>())));
                                } else {
                                    if (posibleEstructura.get(a).get(1).contains(letra)) {
                                        posibleEstructura.get(a).get(1).remove(letra);
                                    }
                                }
                            }
                            break;
                        }
                    }
                }

                diccionario = updateDict(diccionario, posibleEstructura);
                System.out.println(Arrays.deepToString(posibleEstructura.toArray()));
            } else {
                if (entrada == new int[]{2, 2, 2, 2, 2}) {
                    System.out.println("Error, aunque se considera ganador");
                    break;
                } else {
                    System.out.println("Error, intenta otra vez");
                    System.out.println(Arrays.deepToString(posibleEstructura.toArray()));
                    intento--;
                }
            }
        }
    }

    //Método que valida que el usuario no haga trampas
    public static boolean validar(int[] entrada, String word, ArrayList<ArrayList<HashSet<Character>>> posibleEstructura) {
        //Validación de entrada correcta
        boolean allOnes = true;
        for (int i = 0; i < entrada.length; i++) {
            if (entrada[i] == -1) {
                return false;
            }

            allOnes &= entrada[i] == 1 & posibleEstructura.get(i).get(1).size() > 0;
        }
        if (allOnes) return false;

        //Validación anti-hackers
        for (int i = 0; i < entrada.length; i++) {
            char letra = word.charAt(i);

            switch (entrada[i]) {
                case 0: {
                    for (int a = i + 1; a < posibleEstructura.size(); a++) {
                        if (word.charAt(a) == letra & entrada[a] == 1) {
                            return false;
                        }
                    }

                    if ((posibleEstructura.get(i).get(0).size() == 1 & posibleEstructura.get(i).get(0).contains(letra)) | posibleEstructura.get(i).get(1).contains(letra)) {
                        return false;
                    }
                    break;
                }
                case 1: {
                    if (posibleEstructura.get(i).get(0).size() == 1 & posibleEstructura.get(i).get(0).contains(letra)) {
                        return false;
                    }

                    boolean result = true;
                    for (int a = 0; a < posibleEstructura.size(); a++) {
                        if (posibleEstructura.get(a).get(0).contains(letra)) {
                            result = false;
                            break;
                        }
                    }
                    if (result) return false;
                    break;
                }
                case 2: {
                    if (!posibleEstructura.get(i).get(0).contains(letra)) {
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }

    public static String[] updateDict(String[] diccionario, ArrayList<ArrayList<HashSet<Character>>> estan) {
        ArrayList<String> tmpDict = new ArrayList<>();
        for (int i = 0; i < diccionario.length; i++) {
            boolean result = true;
            for (int j = 0; j < diccionario[i].length(); j++) {
                char letra = diccionario[i].charAt(j);
                if (!estan.get(j).get(0).contains(letra) & !estan.get(j).get(1).contains(letra)) {
                    result &= false;
                    break;
                }
            }
            if (result) tmpDict.add(diccionario[i]);
        }
        return tmpDict.toArray(new String[0]);
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
        if (in.length() < 5) return new int[]{-1};
        int result[] = new int[maxInputLength];
        ArrayList<Character> descomposicion = new ArrayList<>(in.chars().mapToObj(i -> (char) i).collect(Collectors.toList()));

        for (int i = 0; i < descomposicion.size() && i < maxInputLength; i++) {
            result[i] = descomposicion.get(i) >= '0' && descomposicion.get(i) <= '2' ? (int) descomposicion.get(i) - 48 : -1;
        }
        return result;
    }
}

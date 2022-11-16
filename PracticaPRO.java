import java.util.*;
import java.io.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Letra {
    HashSet<Character> estan;
    ArrayList<Character> debenEstar;

    public Letra(HashSet<Character> param1, ArrayList<Character> param2) {
        estan = (HashSet<Character>) param1.clone();
        debenEstar = (ArrayList<Character>) param2.clone();
    }

    public String toString() {
        return String.format("%s %s", estan.toString(), debenEstar.toString());
    }
}

class PracticaPRO {
    final static int intentos = 6, maxInputLength = 5;
    final static HashSet<Character> abecedario = new HashSet<>(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'));
    final static Random r = new Random(300);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            String[] diccionario = generarDiccionario(".\\Diccionario2.txt");
            String[] diccionarioOriginal = diccionario;

            System.out.println("Juguemos a Wordle");
            System.out.println("Piensa una palabra ...");
            System.out.println("Y dime si la acierto: ");

            int[] entrada = {0, 0, 0, 0, 0};

            /**
             * En este array se almacenan las letras que pueden ir en cada posicion en el primer sub-array. En el segundo, todas las que deben ir
             */
            ArrayList<Letra> posibleEstructura = new ArrayList<>(maxInputLength);
            IntStream.range(0, maxInputLength).forEach(i -> posibleEstructura.add(new Letra(abecedario, new ArrayList<>())));
            for (int intento = 0; (intento < intentos) & !Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2}); intento++) {
                System.out.println(diccionario.length);
                String word = diccionario.length > 0 ? diccionario[r.nextInt(diccionario.length)] : diccionarioOriginal[r.nextInt(diccionarioOriginal.length)];
                System.out.println(word);
                entrada = stringToIntArray(in.next());

                if (validar(entrada, word, posibleEstructura)) {
                    for (int i = 0; i < entrada.length; i++) {
                        char letra = word.charAt(i);
                        switch (entrada[i]) {
                            case 0: {
                                for (Letra a : posibleEstructura) {
                                    if (a.estan.size() != 1 & a.estan.contains(letra))
                                        a.estan.remove(letra);
                                }
                                break;
                            }
                            case 1: {
                                for (int a = 0; a < posibleEstructura.size(); a++) {
                                    if (a == i) {
                                        posibleEstructura.get(a).estan.remove(letra);
                                    } else {
                                        if (posibleEstructura.get(a).estan.size() != 1)
                                            posibleEstructura.get(a).debenEstar.add(letra);
                                    }
                                }
                                break;
                            }
                            case 2: {
                                for (int a = 0; a < posibleEstructura.size(); a++) {
                                    if (a == i) {
                                        posibleEstructura.set(i, new Letra(new HashSet<>(Arrays.asList(letra)), new ArrayList<>()));
                                    } else {
                                        if (posibleEstructura.get(a).debenEstar.contains(letra))
                                            posibleEstructura.get(a).debenEstar.remove((Character) letra);
                                    }
                                }
                                break;
                            }
                        }
                    }

                    diccionario = updateDict(diccionario, posibleEstructura);
                } else {
                    if (Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2})) {
                        System.out.println("Error, aunque se considera ganador");
                        break;
                    } else {
                        System.out.println("Error, intenta otra vez");
                        intento--;
                    }
                }

                System.out.println(Arrays.deepToString(posibleEstructura.toArray()));
            }

            boolean result = Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2});
            System.out.println(result ? "GANAR!!" : "PERDER!!");
            System.out.println("Quiere usted jugar una partida más?");
            if(in.next().equalsIgnoreCase("no")) break;
        }
    }

    //Método que valida que el usuario no haga trampas
    public static boolean validar(int[] entrada, String word, ArrayList<Letra> posibleEstructura) {
        //Validación de entrada correcta
        boolean allOnes = true;
        for (int i = 0; i < entrada.length; i++) {
            if (entrada[i] == -1) {
                return false;
            }

            allOnes &= entrada[i] == 1 & posibleEstructura.get(i).debenEstar.size() > 0;
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

                    if (posibleEstructura.get(i).estan.size() == 1 & posibleEstructura.get(i).estan.contains(letra)) {
                        return false;
                    }
                    break;
                }
                case 1: {
                    if (posibleEstructura.get(i).estan.size() == 1 & posibleEstructura.get(i).estan.contains(letra)) {
                        return false;
                    }

                    boolean result = true;
                    for (Letra a : posibleEstructura) {
                        if (a.estan.contains(letra)) {
                            result = false;
                            break;
                        }
                    }
                    if (result) return false;
                    break;
                }
                case 2: {
                    if (!posibleEstructura.get(i).estan.contains(letra)) return false;
                    break;
                }
            }
        }
        return true;
    }

    public static String[] updateDict(String[] diccionario, ArrayList<Letra> estan) {
        ArrayList<String> tmpDict = new ArrayList<>();
        for (String i : diccionario) {
            boolean result = true;
            for (int j = 0; j < i.length(); j++) {
                char letra = i.charAt(j);
                if (!estan.get(j).estan.contains(letra) & !estan.get(j).debenEstar.contains(letra)) {
                    result = false;
                    break;
                }
            }
            if (result) tmpDict.add(i);
        }
        return tmpDict.toArray(new String[0]);
    }

    public static String[] generarDiccionario(String ruta) {
        String[] diccionario = {};
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
        int[] result = new int[maxInputLength];
        ArrayList<Character> descomposicion = new ArrayList<>(in.chars().mapToObj(i -> (char) i).collect(Collectors.toList()));

        for (int i = 0; i < descomposicion.size() && i < maxInputLength; i++) {
            result[i] = descomposicion.get(i) >= '0' && descomposicion.get(i) <= '2' ? (int) descomposicion.get(i) - 48 : -1;
        }
        return result;
    }
}

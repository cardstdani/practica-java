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

        do {
            PriorityQueue<String> diccionario = generarDiccionario("./Diccionario.txt", new PriorityQueue<>());
            PriorityQueue<String> diccionarioOriginal = diccionario;

            System.out.println("Juguemos a Wordle");
            System.out.println("Piensa una palabra ...");
            System.out.println("Y dime si la acierto: ");

            int[] entrada = {0, 0, 0, 0, 0};

            /*
              En este array se almacenan las letras que pueden ir en cada posicion en el primer sub-array. En el segundo, todas las que deben ir
             */
            ArrayList<Letra> posibleEstructura = new ArrayList<>(maxInputLength);
            IntStream.range(0, maxInputLength).forEach(i -> posibleEstructura.add(new Letra(abecedario, new ArrayList<>())));

            diccionario = updateDict(diccionario, posibleEstructura);
            System.out.println(diccionario);

            for (int intento = 0; (intento < intentos) & !Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2}); intento++) {
                System.out.println(diccionario.size());
                String word = diccionario.size() > 0 ? diccionario.peek() : diccionarioOriginal.peek();
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
        } while (!in.next().equalsIgnoreCase("no"));
    }

    public static double scoreWord(String word, PriorityQueue<String> dict) {
        String[] combinations = {"00000", "00001", "00002", "00010", "00011", "00012", "00020", "00021", "00022", "00100", "00101", "00102", "00110", "00111", "00112", "00120", "00121", "00122", "00200", "00201", "00202", "00210", "00211", "00212", "00220", "00221", "00222", "01000", "01001", "01002", "01010", "01011", "01012", "01020", "01021", "01022", "01100", "01101", "01102", "01110", "01111", "01112", "01120", "01121", "01122", "01200", "01201", "01202", "01210", "01211", "01212", "01220", "01221", "01222", "02000", "02001", "02002", "02010", "02011", "02012", "02020", "02021", "02022", "02100", "02101", "02102", "02110", "02111", "02112", "02120", "02121", "02122", "02200", "02201", "02202", "02210", "02211", "02212", "02220", "02221", "02222", "10000", "10001", "10002", "10010", "10011", "10012", "10020", "10021", "10022", "10100", "10101", "10102", "10110", "10111", "10112", "10120", "10121", "10122", "10200", "10201", "10202", "10210", "10211", "10212", "10220", "10221", "10222", "11000", "11001", "11002", "11010", "11011", "11012", "11020", "11021", "11022", "11100", "11101", "11102", "11110", "11111", "11112", "11120", "11121", "11122", "11200", "11201", "11202", "11210", "11211", "11212", "11220", "11221", "11222", "12000", "12001", "12002", "12010", "12011", "12012", "12020", "12021", "12022", "12100", "12101", "12102", "12110", "12111", "12112", "12120", "12121", "12122", "12200", "12201", "12202", "12210", "12211", "12212", "12220", "12221", "12222", "20000", "20001", "20002", "20010", "20011", "20012", "20020", "20021", "20022", "20100", "20101", "20102", "20110", "20111", "20112", "20120", "20121", "20122", "20200", "20201", "20202", "20210", "20211", "20212", "20220", "20221", "20222", "21000", "21001", "21002", "21010", "21011", "21012", "21020", "21021", "21022", "21100", "21101", "21102", "21110", "21111", "21112", "21120", "21121", "21122", "21200", "21201", "21202", "21210", "21211", "21212", "21220", "21221", "21222", "22000", "22001", "22002", "22010", "22011", "22012", "22020", "22021", "22022", "22100", "22101", "22102", "22110", "22111", "22112", "22120", "22121", "22122", "22200", "22201", "22202", "22210", "22211", "22212", "22220", "22221", "22222"};
        double finalScore = 0;

        for (int i = 0; i < combinations.length; i++) { //Por cada elemento en combinaciones
            double p = 0;

            for (String word2 : dict) { //Por cada elemento en dict
                p += checkString(word2, word, combinations[i]) ? 1.0 : 0.0;
            }

            p /= Double.valueOf(dict.size()); //Partial score becomes PROBABILITY of x P(x)
            finalScore += p * (Math.log(1.0 / p) / Math.log(2));
        }
        System.out.println(word + " " + finalScore);
        return -finalScore;
    }

    public static boolean checkString(String s1, String s2, String c) { //Comprueba razonadamente que s1 se puede formar con s2 y c
        /* PREc.
        todas las string de length 5
        */
        HashMap<String, Boolean> noEsta = new HashMap<>();
        HashMap<String, ArrayList<ArrayList<Integer>>> esta = new HashMap<>();

        for (int i = 0; i < s2.length(); i++) {
            String key = String.valueOf(s2.charAt(i));
            switch (c.charAt(i)) {
                case '0': {
                    noEsta.put(key, true);
                    break;
                }
                case '1': {
                    if (esta.containsKey(key)) {
                        esta.get(key).get(1).add(i);
                    } else {
                        ArrayList<ArrayList<Integer>> tmp = new ArrayList<>();
                        tmp.add(new ArrayList<>(Arrays.asList()));
                        tmp.add(new ArrayList<>(Arrays.asList(i)));
                        esta.put(key, tmp);
                    }
                    break;
                }
                case '2': {
                    if (esta.containsKey(key)) {
                        esta.get(key).get(0).add(i);
                    } else {
                        ArrayList<ArrayList<Integer>> tmp = new ArrayList<>();
                        tmp.add(new ArrayList<>(Arrays.asList(i)));
                        tmp.add(new ArrayList<>(Arrays.asList()));
                        esta.put(key, tmp);
                    }
                    break;
                }
            }
        }

        for (int i = 0; i < s1.length(); i++) {
            String key = String.valueOf(s1.charAt(i));
            if (noEsta.containsKey(key)) {
                return false;
            }
            if (esta.containsKey(key)) {
                if (esta.get(key).get(0).contains(i) | !esta.get(key).get(1).contains(i)) {
                    return false;
                }
            }
        }

        return true;
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

    public static PriorityQueue<String> updateDict(PriorityQueue<String> diccionario, ArrayList<Letra> estan) {
        PriorityQueue<String> tmpDict = new PriorityQueue<>((i, j) -> Double.compare(scoreWord(i, diccionario), scoreWord(j, diccionario)));
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
        return tmpDict;
    }

    public static PriorityQueue<String> generarDiccionario(String ruta, PriorityQueue<String> currentDict) {
        /*PriorityQueue<String> diccionario = new PriorityQueue<String>(new Comparator<String>() {
            public int compare(String s1, String s2) {
                if (scoreWord(s2, currentDict) > scoreWord(s1, currentDict)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });*/

        PriorityQueue<String> diccionario = new PriorityQueue<>((i, j) -> Double.compare(scoreWord(i, currentDict), scoreWord(j, currentDict)));
        try {
            File doc = new File(ruta);
            Scanner s = new Scanner(doc);
            for(String i : (s.useDelimiter("\\A").next()).split(" ")) {
                diccionario.add(i);
            }
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

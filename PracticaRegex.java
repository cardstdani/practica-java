import java.util.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ParalelMapUpdate implements Callable<LinkedHashMap<String, Double>> {
    int thread, chunkSize;
    String[] dict;

    public static double scoreWord(String word, String[] dict) {
        double finalScore = 0;

        for (String i : PracticaRegex.combinations) { //Por cada elemento en combinaciones
            String pattern = PracticaRegex.generatePattern(PracticaRegex.stringToIntArray(i), word);

            double p =  IntStream.range(0, dict.length).mapToObj(j -> dict[j].matches(pattern) ? 1.0 : 0.0).mapToDouble(f -> f.doubleValue()).parallel().sum();

            p /= dict.length; //Partial score becomes PROBABILITY of x P(x)
            finalScore += p>0 ? p * (Math.log(p) / Math.log(2)) : 0;
        }
        return finalScore;
    }

    public ParalelMapUpdate(int a, int chunkS, String[] wholeDict) {
        thread = a;
        chunkSize = chunkS;
        dict = wholeDict;
    }

    @Override
    public LinkedHashMap<String, Double> call() {
        LinkedHashMap<String, Double> newDict = new LinkedHashMap<>();
        for (int i = (thread * chunkSize); i < ((thread + 1) * chunkSize); i++) {
            newDict.put(dict[i], scoreWord(dict[i], dict));
        }
        return newDict;
    }
}

class PracticaRegex {
    public  final static  String[] combinations = {"00000", "00001", "00002", "00010", "00011", "00012", "00020", "00021", "00022", "00100", "00101", "00102", "00110", "00111", "00112", "00120", "00121", "00122", "00200", "00201", "00202", "00210", "00211", "00212", "00220", "00221", "00222", "01000", "01001", "01002", "01010", "01011", "01012", "01020", "01021", "01022", "01100", "01101", "01102", "01110", "01111", "01112", "01120", "01121", "01122", "01200", "01201", "01202", "01210", "01211", "01212", "01220", "01221", "01222", "02000", "02001", "02002", "02010", "02011", "02012", "02020", "02021", "02022", "02100", "02101", "02102", "02110", "02111", "02112", "02120", "02121", "02122", "02200", "02201", "02202", "02210", "02211", "02212", "02220", "02221", "02222", "10000", "10001", "10002", "10010", "10011", "10012", "10020", "10021", "10022", "10100", "10101", "10102", "10110", "10111", "10112", "10120", "10121", "10122", "10200", "10201", "10202", "10210", "10211", "10212", "10220", "10221", "10222", "11000", "11001", "11002", "11010", "11011", "11012", "11020", "11021", "11022", "11100", "11101", "11102", "11110", "11111", "11112", "11120", "11121", "11122", "11200", "11201", "11202", "11210", "11211", "11212", "11220", "11221", "11222", "12000", "12001", "12002", "12010", "12011", "12012", "12020", "12021", "12022", "12100", "12101", "12102", "12110", "12111", "12112", "12120", "12121", "12122", "12200", "12201", "12202", "12210", "12211", "12212", "12220", "12221", "12222", "20000", "20001", "20002", "20010", "20011", "20012", "20020", "20021", "20022", "20100", "20101", "20102", "20110", "20111", "20112", "20120", "20121", "20122", "20200", "20201", "20202", "20210", "20211", "20212", "20220", "20221", "20222", "21000", "21001", "21002", "21010", "21011", "21012", "21020", "21021", "21022", "21100", "21101", "21102", "21110", "21111", "21112", "21120", "21121", "21122", "21200", "21201", "21202", "21210", "21211", "21212", "21220", "21221", "21222", "22000", "22001", "22002", "22010", "22011", "22012", "22020", "22021", "22022", "22100", "22101", "22102", "22110", "22111", "22112", "22120", "22121", "22122", "22200", "22201", "22202", "22210", "22211", "22212", "22220", "22221", "22222"};
    public final static int intentos = 6, maxInputLength = 5;
    public final static int n = Runtime.getRuntime().availableProcessors(); // numero de hilos

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        do {
            LinkedHashMap<String, Double> diccionario = generarDiccionario("./Diccionario.txt");
            LinkedHashMap<String, Double> diccionarioOriginal = diccionario;

            System.out.println("Juguemos a Wordle\nPiensa una palabra ...\nY dime si la acierto: ");

            int[] entrada = {0, 0, 0, 0, 0};
            diccionario = updateDict(diccionario, "^[A-Z][A-Z][A-Z][A-Z][A-Z]$");
            String globalPattern = "";
            for (int intento = 0; (intento < intentos) & !Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2}); intento++) {
                String word = Collections.max(diccionario.size() > 0 ? diccionario.entrySet() : diccionarioOriginal.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();

                System.out.println(diccionario.size() + " " + diccionario);
                System.out.println(word);
                entrada = stringToIntArray(in.next());

                String pattern = generatePattern(entrada, word);
                if (validar(entrada, word, globalPattern)) {
                    globalPattern += pattern.substring(1, pattern.length() - 3);
                    diccionario = updateDict(diccionario, pattern);
                } else {
                    if (Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2})) {
                        System.out.println("Error, aunque se considera ganador");
                        break;
                    } else {
                        System.out.println("Error, intenta otra vez");
                        intento--;
                    }
                }
            }

            boolean result = Arrays.equals(entrada, new int[]{2, 2, 2, 2, 2});
            System.out.println(result ? "GANAR!!" : "PERDER!!");
            System.out.println("Quiere usted jugar una partida más?");
        } while (!in.next().equalsIgnoreCase("no"));
        System.exit(0);
    }

    public static String generatePattern(int[] entrada, String word) {
        StringBuilder pattern = new StringBuilder();
        LinkedHashSet<Character> procesed = new LinkedHashSet<>();

        for (int j = 0; j < entrada.length; j++) {
            char letra = word.charAt(j);
            if (!procesed.contains(letra)) {
                List<Boolean> tmpList = IntStream.range(j + 1, entrada.length).mapToObj(k -> word.charAt(k) == letra & entrada[k] == 2).collect(Collectors.toList());
                boolean condition = tmpList.size() > 0 ? Collections.max(tmpList) : false;
                if (entrada[j] == 0) {
                    if (!condition) procesed.add(letra);
                } else {
                    procesed.add(letra);
                }
                pattern.append(new String[]{!condition ? String.format("(?=[^%s]*$)", letra) : String.format("(?!.{%s}%s)", j, letra), String.format("(?!.{%s}%s)(?=.*%s)", j, letra, letra),
                        String.format("(?=.{%s}%s)", j, letra)}[entrada[j]]);
            }
        }

        return String.format("^%s.*$", pattern);
    }

    //Método que valida que el usuario no haga trampas
    public static boolean validar(int[] entrada, String word, String globalPattern) {
        LinkedHashSet<Character> procesed = new LinkedHashSet<>();
        for (int i = 0; i < entrada.length; i++) {
            char letra = word.charAt(i);
            if (!procesed.contains(letra)) {
                switch (entrada[i]) {
                    case 0:
                        List<Boolean> tmpList = IntStream.range(i + 1, entrada.length).mapToObj(k -> word.charAt(k) == letra & entrada[k] == 1).collect(Collectors.toList());
                        boolean condition = tmpList.size() > 0 ? Collections.max(tmpList) : false;
                        List<Boolean> tmpTwoList = IntStream.range(i + 1, entrada.length).mapToObj(k -> word.charAt(k) == letra & entrada[k] == 2).collect(Collectors.toList());
                        boolean twoAheadCondition = tmpTwoList.size() > 0 ? Collections.max(tmpTwoList) : false;
                        if (globalPattern.contains(String.format("(?=.{%s}%s)", i, letra)) | globalPattern.contains(String.format("(?=.*%s)", letra)) & !twoAheadCondition | condition) {
                            System.out.println("Error 0 en caracter " + letra);
                            return false;
                        }
                        break;
                    case 1:
                        if (globalPattern.contains(String.format("(?=.{%s}%s)", i, letra))) return false;
                        break;
                    case 2:
                        if (globalPattern.contains(String.format("(?!.{%s}%s)", i, letra)) | globalPattern.contains(String.format("(?=[^%s]*$)", letra))) {
                            System.out.println("Error 2 en caracter " + letra);
                            return false;
                        }
                        break;
                }
                procesed.add(letra);
            }
        }
        return true;
    }

    public static LinkedHashMap<String, Double> updateDict(LinkedHashMap<String, Double> diccionario, String pattern) {
        LinkedHashMap<String, Double> tmpDict = new LinkedHashMap<>();
        for (String i : diccionario.keySet()) {
            if (i.matches(pattern)) tmpDict.put(i, 0.0);
        }

        int chunkSize = (tmpDict.size() / n);
        ArrayList<RunnableFuture<LinkedHashMap<String, Double>>> threads = new ArrayList<>();
        String[] keySet = tmpDict.keySet().toArray(new String[0]);
        ExecutorService pool = Executors.newFixedThreadPool(n);
        for (int i = 0; i < n; i++) {
            threads.add(new FutureTask<>(new ParalelMapUpdate(i, chunkSize, keySet)));
            pool.execute(threads.get(threads.size()-1));
        }

        for (RunnableFuture<LinkedHashMap<String, Double>> t : threads) {
            try {
                tmpDict.putAll(t.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return tmpDict;
    }

    public static LinkedHashMap<String, Double> generarDiccionario(String ruta) {
        LinkedHashMap<String, Double> diccionario = new LinkedHashMap<>();
        try {
            File doc = new File(ruta);
            Scanner s = new Scanner(doc);
            for (String i : (s.useDelimiter("\\A").next()).split(" ")) {
                diccionario.put(i, 0.0);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return diccionario;
    }

    public static int[] stringToIntArray(String in) {
        if (in.length() < maxInputLength) return new int[]{-1};
        int[] result = new int[maxInputLength];
        ArrayList<Character> descomposicion = new ArrayList<>(in.chars().mapToObj(i -> (char) i).collect(Collectors.toList()));

        for (int i = 0; i < descomposicion.size() && i < maxInputLength; i++) {
            result[i] = descomposicion.get(i) >= '0' && descomposicion.get(i) <= '2' ? descomposicion.get(i) - '0' : -1;
        }
        return result;
    }
}

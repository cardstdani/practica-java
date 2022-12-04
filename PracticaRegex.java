import java.net.URL;
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

        for (int[] i : PracticaRegex.combinations) { //Por cada elemento en combinaciones
            String pattern = PracticaRegex.generatePattern(i, word);

            double p = IntStream.range(0, dict.length).mapToDouble(j -> (dict[j].matches(pattern) ? 1.0 : 0.0)).parallel().sum();

            p /= dict.length; //Partial score becomes PROBABILITY of x P(x)
            finalScore += p > 0 ? p * (Math.log(p) / Math.log(2)) : 0;
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
    public final static int[][] combinations = {{0, 0, 0, 0, 0}, {0, 0, 0, 0, 1}, {0, 0, 0, 0, 2}, {0, 0, 0, 1, 0}, {0, 0, 0, 1, 1}, {0, 0, 0, 1, 2}, {0, 0, 0, 2, 0}, {0, 0, 0, 2, 1}, {0, 0, 0, 2, 2}, {0, 0, 1, 0, 0}, {0, 0, 1, 0, 1}, {0, 0, 1, 0, 2}, {0, 0, 1, 1, 0}, {0, 0, 1, 1, 1}, {0, 0, 1, 1, 2}, {0, 0, 1, 2, 0}, {0, 0, 1, 2, 1}, {0, 0, 1, 2, 2}, {0, 0, 2, 0, 0}, {0, 0, 2, 0, 1}, {0, 0, 2, 0, 2}, {0, 0, 2, 1, 0}, {0, 0, 2, 1, 1}, {0, 0, 2, 1, 2}, {0, 0, 2, 2, 0}, {0, 0, 2, 2, 1}, {0, 0, 2, 2, 2}, {0, 1, 0, 0, 0}, {0, 1, 0, 0, 1}, {0, 1, 0, 0, 2}, {0, 1, 0, 1, 0}, {0, 1, 0, 1, 1}, {0, 1, 0, 1, 2}, {0, 1, 0, 2, 0}, {0, 1, 0, 2, 1}, {0, 1, 0, 2, 2}, {0, 1, 1, 0, 0}, {0, 1, 1, 0, 1}, {0, 1, 1, 0, 2}, {0, 1, 1, 1, 0}, {0, 1, 1, 1, 1}, {0, 1, 1, 1, 2}, {0, 1, 1, 2, 0}, {0, 1, 1, 2, 1}, {0, 1, 1, 2, 2}, {0, 1, 2, 0, 0}, {0, 1, 2, 0, 1}, {0, 1, 2, 0, 2}, {0, 1, 2, 1, 0}, {0, 1, 2, 1, 1}, {0, 1, 2, 1, 2}, {0, 1, 2, 2, 0}, {0, 1, 2, 2, 1}, {0, 1, 2, 2, 2}, {0, 2, 0, 0, 0}, {0, 2, 0, 0, 1}, {0, 2, 0, 0, 2}, {0, 2, 0, 1, 0}, {0, 2, 0, 1, 1}, {0, 2, 0, 1, 2}, {0, 2, 0, 2, 0}, {0, 2, 0, 2, 1}, {0, 2, 0, 2, 2}, {0, 2, 1, 0, 0}, {0, 2, 1, 0, 1}, {0, 2, 1, 0, 2}, {0, 2, 1, 1, 0}, {0, 2, 1, 1, 1}, {0, 2, 1, 1, 2}, {0, 2, 1, 2, 0}, {0, 2, 1, 2, 1}, {0, 2, 1, 2, 2}, {0, 2, 2, 0, 0}, {0, 2, 2, 0, 1}, {0, 2, 2, 0, 2}, {0, 2, 2, 1, 0}, {0, 2, 2, 1, 1}, {0, 2, 2, 1, 2}, {0, 2, 2, 2, 0}, {0, 2, 2, 2, 1}, {0, 2, 2, 2, 2}, {1, 0, 0, 0, 0}, {1, 0, 0, 0, 1}, {1, 0, 0, 0, 2}, {1, 0, 0, 1, 0}, {1, 0, 0, 1, 1}, {1, 0, 0, 1, 2}, {1, 0, 0, 2, 0}, {1, 0, 0, 2, 1}, {1, 0, 0, 2, 2}, {1, 0, 1, 0, 0}, {1, 0, 1, 0, 1}, {1, 0, 1, 0, 2}, {1, 0, 1, 1, 0}, {1, 0, 1, 1, 1}, {1, 0, 1, 1, 2}, {1, 0, 1, 2, 0}, {1, 0, 1, 2, 1}, {1, 0, 1, 2, 2}, {1, 0, 2, 0, 0}, {1, 0, 2, 0, 1}, {1, 0, 2, 0, 2}, {1, 0, 2, 1, 0}, {1, 0, 2, 1, 1}, {1, 0, 2, 1, 2}, {1, 0, 2, 2, 0}, {1, 0, 2, 2, 1}, {1, 0, 2, 2, 2}, {1, 1, 0, 0, 0}, {1, 1, 0, 0, 1}, {1, 1, 0, 0, 2}, {1, 1, 0, 1, 0}, {1, 1, 0, 1, 1}, {1, 1, 0, 1, 2}, {1, 1, 0, 2, 0}, {1, 1, 0, 2, 1}, {1, 1, 0, 2, 2}, {1, 1, 1, 0, 0}, {1, 1, 1, 0, 1}, {1, 1, 1, 0, 2}, {1, 1, 1, 1, 0}, {1, 1, 1, 1, 1}, {1, 1, 1, 1, 2}, {1, 1, 1, 2, 0}, {1, 1, 1, 2, 1}, {1, 1, 1, 2, 2}, {1, 1, 2, 0, 0}, {1, 1, 2, 0, 1}, {1, 1, 2, 0, 2}, {1, 1, 2, 1, 0}, {1, 1, 2, 1, 1}, {1, 1, 2, 1, 2}, {1, 1, 2, 2, 0}, {1, 1, 2, 2, 1}, {1, 1, 2, 2, 2}, {1, 2, 0, 0, 0}, {1, 2, 0, 0, 1}, {1, 2, 0, 0, 2}, {1, 2, 0, 1, 0}, {1, 2, 0, 1, 1}, {1, 2, 0, 1, 2}, {1, 2, 0, 2, 0}, {1, 2, 0, 2, 1}, {1, 2, 0, 2, 2}, {1, 2, 1, 0, 0}, {1, 2, 1, 0, 1}, {1, 2, 1, 0, 2}, {1, 2, 1, 1, 0}, {1, 2, 1, 1, 1}, {1, 2, 1, 1, 2}, {1, 2, 1, 2, 0}, {1, 2, 1, 2, 1}, {1, 2, 1, 2, 2}, {1, 2, 2, 0, 0}, {1, 2, 2, 0, 1}, {1, 2, 2, 0, 2}, {1, 2, 2, 1, 0}, {1, 2, 2, 1, 1}, {1, 2, 2, 1, 2}, {1, 2, 2, 2, 0}, {1, 2, 2, 2, 1}, {1, 2, 2, 2, 2}, {2, 0, 0, 0, 0}, {2, 0, 0, 0, 1}, {2, 0, 0, 0, 2}, {2, 0, 0, 1, 0}, {2, 0, 0, 1, 1}, {2, 0, 0, 1, 2}, {2, 0, 0, 2, 0}, {2, 0, 0, 2, 1}, {2, 0, 0, 2, 2}, {2, 0, 1, 0, 0}, {2, 0, 1, 0, 1}, {2, 0, 1, 0, 2}, {2, 0, 1, 1, 0}, {2, 0, 1, 1, 1}, {2, 0, 1, 1, 2}, {2, 0, 1, 2, 0}, {2, 0, 1, 2, 1}, {2, 0, 1, 2, 2}, {2, 0, 2, 0, 0}, {2, 0, 2, 0, 1}, {2, 0, 2, 0, 2}, {2, 0, 2, 1, 0}, {2, 0, 2, 1, 1}, {2, 0, 2, 1, 2}, {2, 0, 2, 2, 0}, {2, 0, 2, 2, 1}, {2, 0, 2, 2, 2}, {2, 1, 0, 0, 0}, {2, 1, 0, 0, 1}, {2, 1, 0, 0, 2}, {2, 1, 0, 1, 0}, {2, 1, 0, 1, 1}, {2, 1, 0, 1, 2}, {2, 1, 0, 2, 0}, {2, 1, 0, 2, 1}, {2, 1, 0, 2, 2}, {2, 1, 1, 0, 0}, {2, 1, 1, 0, 1}, {2, 1, 1, 0, 2}, {2, 1, 1, 1, 0}, {2, 1, 1, 1, 1}, {2, 1, 1, 1, 2}, {2, 1, 1, 2, 0}, {2, 1, 1, 2, 1}, {2, 1, 1, 2, 2}, {2, 1, 2, 0, 0}, {2, 1, 2, 0, 1}, {2, 1, 2, 0, 2}, {2, 1, 2, 1, 0}, {2, 1, 2, 1, 1}, {2, 1, 2, 1, 2}, {2, 1, 2, 2, 0}, {2, 1, 2, 2, 1}, {2, 1, 2, 2, 2}, {2, 2, 0, 0, 0}, {2, 2, 0, 0, 1}, {2, 2, 0, 0, 2}, {2, 2, 0, 1, 0}, {2, 2, 0, 1, 1}, {2, 2, 0, 1, 2}, {2, 2, 0, 2, 0}, {2, 2, 0, 2, 1}, {2, 2, 0, 2, 2}, {2, 2, 1, 0, 0}, {2, 2, 1, 0, 1}, {2, 2, 1, 0, 2}, {2, 2, 1, 1, 0}, {2, 2, 1, 1, 1}, {2, 2, 1, 1, 2}, {2, 2, 1, 2, 0}, {2, 2, 1, 2, 1}, {2, 2, 1, 2, 2}, {2, 2, 2, 0, 0}, {2, 2, 2, 0, 1}, {2, 2, 2, 0, 2}, {2, 2, 2, 1, 0}, {2, 2, 2, 1, 1}, {2, 2, 2, 1, 2}, {2, 2, 2, 2, 0}, {2, 2, 2, 2, 1}, {2, 2, 2, 2, 2}};
    public final static int intentos = 6, maxInputLength = 5;
    public final static int n = Runtime.getRuntime().availableProcessors(); // numero de hilos

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        do {
            LinkedHashMap<String, Double> diccionario = generarDiccionario("https://raw.githubusercontent.com/cardstdani/practica-java/main/Data/Diccionario4.txt");
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
                boolean condition = IntStream.range(j + 1, entrada.length).mapToLong(k -> (word.charAt(k) == letra & entrada[k] == 2 ? 1 : 0)).sum() > 0;
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
                        if (globalPattern.contains(String.format("(?=.{%s}%s)", i, letra)) | globalPattern.contains(String.format("(?=.*%s)", letra)) & !twoAheadCondition | condition)
                            return false;

                        break;
                    case 1:
                        if (globalPattern.contains(String.format("(?=.{%s}%s)", i, letra))) return false;
                        break;
                    case 2:
                        if (globalPattern.contains(String.format("(?!.{%s}%s)", i, letra)) | globalPattern.contains(String.format("(?=[^%s]*$)", letra)))
                            return false;
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
            pool.execute(threads.get(threads.size() - 1));
        }

        for (RunnableFuture<LinkedHashMap<String, Double>> t : threads) {
            try {
                tmpDict.putAll(t.get());
            } catch (Exception e) {
                System.out.println(e.getStackTrace().toString());
            }
        }
        return tmpDict;
    }

    public static LinkedHashMap<String, Double> generarDiccionario(String ruta) {
        LinkedHashMap<String, Double> diccionario = new LinkedHashMap<>();
        try {
            Scanner s = new Scanner(new URL(ruta).openStream(), "UTF-8");
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

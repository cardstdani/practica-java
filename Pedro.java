import java.util.Scanner;
import java.net.URL;
import java.util.*;
import java.io.*;

class Main implements Runnable {
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

    public static HashMap<String, Double> sortMap(HashMap<String, Double> dict) {
        String[] combinations = {"00000", "00001", "00002", "00010", "00011", "00012", "00020", "00021", "00022", "00100", "00101", "00102", "00110", "00111", "00112", "00120", "00121", "00122", "00200", "00201", "00202", "00210", "00211", "00212", "00220", "00221", "00222", "01000", "01001", "01002", "01010", "01011", "01012", "01020", "01021", "01022", "01100", "01101", "01102", "01110", "01111", "01112", "01120", "01121", "01122", "01200", "01201", "01202", "01210", "01211", "01212", "01220", "01221", "01222", "02000", "02001", "02002", "02010", "02011", "02012", "02020", "02021", "02022", "02100", "02101", "02102", "02110", "02111", "02112", "02120", "02121", "02122", "02200", "02201", "02202", "02210", "02211", "02212", "02220", "02221", "02222", "10000", "10001", "10002", "10010", "10011", "10012", "10020", "10021", "10022", "10100", "10101", "10102", "10110", "10111", "10112", "10120", "10121", "10122", "10200", "10201", "10202", "10210", "10211", "10212", "10220", "10221", "10222", "11000", "11001", "11002", "11010", "11011", "11012", "11020", "11021", "11022", "11100", "11101", "11102", "11110", "11111", "11112", "11120", "11121", "11122", "11200", "11201", "11202", "11210", "11211", "11212", "11220", "11221", "11222", "12000", "12001", "12002", "12010", "12011", "12012", "12020", "12021", "12022", "12100", "12101", "12102", "12110", "12111", "12112", "12120", "12121", "12122", "12200", "12201", "12202", "12210", "12211", "12212", "12220", "12221", "12222", "20000", "20001", "20002", "20010", "20011", "20012", "20020", "20021", "20022", "20100", "20101", "20102", "20110", "20111", "20112", "20120", "20121", "20122", "20200", "20201", "20202", "20210", "20211", "20212", "20220", "20221", "20222", "21000", "21001", "21002", "21010", "21011", "21012", "21020", "21021", "21022", "21100", "21101", "21102", "21110", "21111", "21112", "21120", "21121", "21122", "21200", "21201", "21202", "21210", "21211", "21212", "21220", "21221", "21222", "22000", "22001", "22002", "22010", "22011", "22012", "22020", "22021", "22022", "22100", "22101", "22102", "22110", "22111", "22112", "22120", "22121", "22122", "22200", "22201", "22202", "22210", "22211", "22212", "22220", "22221", "22222"};
        Iterator dictIterator = dict.entrySet().iterator();
        while (dictIterator.hasNext()) { //Por cada elemento en dict
            Map.Entry element = (Map.Entry) dictIterator.next();

            double finalScore = 0;

            for (int i = 0; i < combinations.length; i++) { //Por cada elemento en combinaciones
                double p = 0;
                Iterator dictIterator2 = dict.entrySet().iterator();
                while (dictIterator2.hasNext()) { //Por cada elemento en dict
                    Map.Entry e = (Map.Entry) dictIterator2.next();
                    p += checkString((String) e.getKey(), (String) element.getKey(), combinations[i]) ? 1.0 : 0.0;
                }

                p /= Double.valueOf(dict.size()); //Partial score becomes PROBABILITY of x P(x)
                finalScore += p * (Math.log(1.0 / p) / Math.log(2));
            }

            dict.put((String) element.getKey(), finalScore);
            System.out.println(element.getKey() + " : " + element.getValue());
        }
        return dict;
    }

    public static HashMap<String, Double> sortMap2(HashMap<String, Double> dict) {
        String[] combinations = {"00000", "00001", "00002", "00010", "00011", "00012", "00020", "00021", "00022", "00100", "00101", "00102", "00110", "00111", "00112", "00120", "00121", "00122", "00200", "00201", "00202", "00210", "00211", "00212", "00220", "00221", "00222", "01000", "01001", "01002", "01010", "01011", "01012", "01020", "01021", "01022", "01100", "01101", "01102", "01110", "01111", "01112", "01120", "01121", "01122", "01200", "01201", "01202", "01210", "01211", "01212", "01220", "01221", "01222", "02000", "02001", "02002", "02010", "02011", "02012", "02020", "02021", "02022", "02100", "02101", "02102", "02110", "02111", "02112", "02120", "02121", "02122", "02200", "02201", "02202", "02210", "02211", "02212", "02220", "02221", "02222", "10000", "10001", "10002", "10010", "10011", "10012", "10020", "10021", "10022", "10100", "10101", "10102", "10110", "10111", "10112", "10120", "10121", "10122", "10200", "10201", "10202", "10210", "10211", "10212", "10220", "10221", "10222", "11000", "11001", "11002", "11010", "11011", "11012", "11020", "11021", "11022", "11100", "11101", "11102", "11110", "11111", "11112", "11120", "11121", "11122", "11200", "11201", "11202", "11210", "11211", "11212", "11220", "11221", "11222", "12000", "12001", "12002", "12010", "12011", "12012", "12020", "12021", "12022", "12100", "12101", "12102", "12110", "12111", "12112", "12120", "12121", "12122", "12200", "12201", "12202", "12210", "12211", "12212", "12220", "12221", "12222", "20000", "20001", "20002", "20010", "20011", "20012", "20020", "20021", "20022", "20100", "20101", "20102", "20110", "20111", "20112", "20120", "20121", "20122", "20200", "20201", "20202", "20210", "20211", "20212", "20220", "20221", "20222", "21000", "21001", "21002", "21010", "21011", "21012", "21020", "21021", "21022", "21100", "21101", "21102", "21110", "21111", "21112", "21120", "21121", "21122", "21200", "21201", "21202", "21210", "21211", "21212", "21220", "21221", "21222", "22000", "22001", "22002", "22010", "22011", "22012", "22020", "22021", "22022", "22100", "22101", "22102", "22110", "22111", "22112", "22120", "22121", "22122", "22200", "22201", "22202", "22210", "22211", "22212", "22220", "22221", "22222"};

        for(Object keyElement : dict.keySet().toArray()) { //Por cada elemento en dict
            double finalScore = 0;

            for (int i = 0; i < combinations.length; i++) { //Por cada elemento en combinaciones
                double p = 0;
                
                for(Object keyE : dict.keySet().toArray()) { //Por cada elemento en dict
                    p += checkString((String) keyE, (String) keyElement, combinations[i]) ? 1.0 : 0.0;
                }

                p /= Double.valueOf(dict.size()); //Partial score becomes PROBABILITY of x P(x)
                finalScore += p * (Math.log(1.0 / p) / Math.log(2));
            }

            dict.put((String) keyElement, finalScore);
            System.out.println(keyElement + " : " + dict.get(keyElement));
        }
        return dict;
    }
    int totalThreads;
    int thread;
    public Main(int a, int b){
        thread = a;
        totalThreads = b;
    }

    public void run() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/cardstdani/practica-java/main/Diccionario.txt");
            Scanner s = new Scanner(url.openStream());

            HashMap<String, Double> dict = new HashMap<>();
            while (s.hasNext()) {
                dict.put(s.next(), 0.0);
            }

            int chunkSize = dict.size() / totalThreads;
            System.out.println(chunkSize);
            int initElement = chunkSize * thread; 
            int actualEl = 0;
            HashMap<String, Double> newDict = new HashMap<>();
            for(Object keyElement : dict.keySet().toArray()) {
                if(actualEl >= initElement && actualEl < initElement + chunkSize){
                    newDict.put(""+keyElement, 0.0);  
                }
                actualEl++;
            }

            sortMap2(newDict);

            System.out.println(newDict.size());
        } catch (IOException ex) {

        }
    }
}


class Pedro{
    public static void main (String[] args){
        int n = 8; // nummero de hilos
        for(int i = 0; i < n; i++){
            Thread object = new Thread(new Main(i,n));
            object.start();
        }
    }
}
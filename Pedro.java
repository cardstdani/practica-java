import java.util.Scanner;
import java.net.URL;
import java.util.*;
import java.io.*;

class Practica {

  public static void main(String[] args) {
    try {
      URL url = new URL("https://raw.githubusercontent.com/cardstdani/practica-java/main/Diccionario.txt");
      Scanner s = new Scanner(url.openStream());

      HashMap < String, String > dict = new HashMap < String, String > ();
      while (s.hasNext()) {
        dict.put(s.next(), "");
      }
      Iterator dictIterator = dict.entrySet().iterator();

      while (dictIterator.hasNext()) {

        Map.Entry mapElement = (Map.Entry) dictIterator.next();
        String marks = (String) mapElement.getValue();

        System.out.println(mapElement.getKey() + " : " +
          marks);
      }
    } catch (IOException ex) {

    }

    System.out.println("Juguemos a Wordle");
    System.out.println("Piensa una palabra ...");
    System.out.println("Y dime si la acierto: ");
  }
}

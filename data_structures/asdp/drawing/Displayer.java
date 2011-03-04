package drawing;

import java.io.*;

public class Displayer {

    public static void main (String... args) throws IOException {
        Display D = new Display ();
        BufferedReader inp = 
            new BufferedReader (new InputStreamReader (System.in));

        while (true) {
            String line = inp.readLine ();
            if (line == null)
                break;
            D.append (line);
            D.append ("\n");
        }

    }
}



















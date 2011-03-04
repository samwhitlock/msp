package drawing;

import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

/** The main class of the CS61B drawing system. */
public class Main {
    
    // Warning: The main program is not quite right.  It should work for 
    // correct arguments, but its error behavior does not conform
    // to the project specifications.  You must fix it.

    public static void main (String[] args) {
        Scanner input;
        PrintWriter output;
        boolean waitToClose;
        waitToClose = false;
        try {
            switch (args.length) {
            case 0:
                Usage ();
                return;
            case 1: case 2:
                if (args[0].equals ("-"))
                    input = new Scanner (System.in);
                else {
                    waitToClose = true;
                    input = new Scanner (new FileReader (args[0]));
                }
                break;
            default:
                Usage ();
                System.exit (1);
                return;
            }

            switch (args.length) {
            case 1:
                output = new PrintWriter (new Display ());
                break;
            case 2:
                waitToClose = false;
                if (args[1].equals ("-"))
                    output = new PrintWriter (System.out);
                else
                    output = new PrintWriter (args[1]);
                break;
            default:
                throw new Error ("internal error: impossible case");
            }

            translate (input, output);
            if (waitToClose) {
                System.err.printf ("Type <RETURN> or <ENTER> to quit.%n");
                try {
                    System.in.read ();
                } catch (IOException e) {
                }
            }
            output.close ();
        } catch (IOException e) {
            System.err.printf ("error: %s%n", e.getMessage ());
            System.exit (1);
        }
    }

    /** Read the input program from INPUT and write a Postscript translation
     *  to OUTPUT. */
    static void translate (Scanner input, PrintWriter output) {
        Interpreter machine = new Interpreter (input, output);
        output.println("%!PS-Adobe-2.0");
        output.println();
        machine.readExecute ();
        output.println();
        output.println("showpage");
    }

    static void Usage () {
        System.err.printf ("Usage: java draw  INPUTFILE [ OUTPUTFILE ]%n"
                           + "  Either file may be '-', "
                           + "denoting standard input/output.%n");
    }

}


package drawing;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.InputStream;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;
import java.util.regex.MatchResult;

/** A Display is a Writer that sends all output written to it to a 
 *  Postscript viewer that interprets the output as Postscript commands,
 *  displaying the results on the screen.   As with other Writers, output
 *  written to a Display need not get seen by the viewer until the user
 *  calls flush(). */
public class Display extends Writer {

    static final String DEFAULT_DISPLAY_PROGRAM = "gs";
    static final String DEFAULT_DEVICE = "x11";
    static final int DEFAULT_WIDTH = 600;
    static final int DEFAULT_HEIGHT = 600;

    /** A new Display that sends output to an execution of PROGRAM. */
    public Display (String program) {
        this.program = program;
        this.process = null;
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
        this.device = DEFAULT_DEVICE;
        this.closed = false;
    }

    /** A new Display that sends output to a default viewer. */
    public Display () {
        this (DEFAULT_DISPLAY_PROGRAM);
    }

    /** The current display width, in pixels. */
    public int getWidth () {
        return width;
    }

    /** Set the current display width in pixels to WIDTH>0.  Invalid after
     *  the first character of output is written. */
    public void setWidth (int width) {
        if (process != null || closed)
            throw new IllegalStateException ("too late to set width");
        else if (width <= 0)
            throw new IllegalArgumentException ("invalid width");
        this.width = width;
    }

    /** The current display height, in pixels. */
    public int getHeight () {
        return height;
    }

    /** Set the current display height in pixels to WIDTH>0.  Invalid after
     *  the first character of output is written. */
    public void setHeight (int height) {
        if (process != null || closed)
            throw new IllegalStateException ("too late to set height");
        else if (height <= 0)
            throw new IllegalArgumentException ("invalid height");
        this.height = height;
    }

    /** Write CBUF[OFF .. OFF+LEN-1] to the current viewer program, starting
     *  the display if necessary. */
    public void write (char[] cbuf, int off, int len) throws IOException {
        int nextOff;
        startProcess ();
        nextOff = off;
        for (int i = off; i < off + len; i += 1) {
            if (cbuf[i] == '\n') {
                displayIn.write (cbuf, nextOff, i - nextOff + 1); 
                displayIn.flush ();
                nextOff = i + 1;
                try {
                    switch (responseQueue.take ()) {
                    case OK:
                        break;
                    case ERROR:
                        close ();
                        throw new IOException ("Postscript error");
                    case QUIT:
                        close ();
                        throw new IOException ("Viewer program terminated");
                    }
                } catch (InterruptedException e) {
                }
            }
        }
        if (nextOff < off + len)
            displayIn.write (cbuf, nextOff, off + len - nextOff);
    }

    /** Terminate the current viewer program. Further output to the 
     *  display causes an exception. */
    public void close () {
        if (closed)
            return;
        closed = true;
        if (process == null)
            return;
        try {
            process.getOutputStream ().close ();
            process.getInputStream ().close ();
        } catch (IOException e) {
        }
        process.destroy ();
    }

    /** Make sure that any pending output has been transmitted to the display
     *  program. */
    public void flush () throws IOException {
        if (process == null || closed)
            return;
        displayIn.flush ();
    }

    private void startProcess () throws IOException {
        if (closed)
            throw new IllegalStateException ("Display is closed");
        if (process != null)
            return;
        ProcessBuilder p = 
            new ProcessBuilder (program,
                                "-sDEVICE=" + device,
                                String.format ("-g%dx%d", width, height));
        Map<String, String> env = p.environment ();
        env.remove ("XFILESEARCHPATH");
        process = p.start ();
        stdoutReader = new OutReader ();
        displayIn = new OutputStreamWriter (process.getOutputStream ());
        stdoutReader.start ();
    }

    private static Pattern RESPONSE_PATN =
        Pattern.compile ("(?s)(Error:.*?)?GS(?:<\\d+)?>"
                         + "|>>showpage, press <return> to continue<<");

    private class OutReader extends Thread {
        public void run () {
            try {
                boolean foundOne;
                InputStream displayOut = process.getInputStream ();
                Scanner displayMsgs = new Scanner (displayOut);
                foundOne = false;
                while (displayMsgs.findWithinHorizon (RESPONSE_PATN, 0) != null) {
                    MatchResult mat = displayMsgs.match ();
                    if (foundOne)
                        responseQueue.put (mat.group (1) != null ? ERROR : OK);
                    foundOne = true;
                }
                responseQueue.put (QUIT);
            } catch (InterruptedException e) {
            }
        }
    }
                
    private static final int 
        OK = 0, 
        ERROR = 1,
        QUIT = 2;

    private LinkedBlockingQueue<Integer> responseQueue 
                = new LinkedBlockingQueue<Integer> ();
    private boolean closed;
    private Writer displayIn;
    private Process process;
    private Thread stdoutReader;
    private int width, height;
    private String program;
    private String device;

}

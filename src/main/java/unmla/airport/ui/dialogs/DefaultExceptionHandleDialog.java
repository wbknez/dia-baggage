package unmla.airport.ui.dialogs;

import java.awt.Frame;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 
 */
public class DefaultExceptionHandleDialog implements Thread.UncaughtExceptionHandler {

    /**
     * 
     * @return
     */
    private Frame findActiveOrVisibleFrame() {
        final Frame[] frames = JFrame.getFrames();
        
        for(final Frame frame : frames) {
            if(frame.isActive()) {
                return frame;
            }
        }
        
        for(final Frame frame : frames) {
            if(frame.isVisible()) {
                return frame;
            }
        }
        
        return null;
    }
    
    private void generateDialog(final Thread t, final Throwable e) {
        final String stackTrace = this.getStrackTrace(e);
        
        JOptionPane.showMessageDialog(this.findActiveOrVisibleFrame(), 
                                        stackTrace, 
                                        "Uncaught Exception from " + t, 
                                        JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * 
     * @param e
     * @return
     */
    private String getStrackTrace(final Throwable e) {
        if(e == null) {
            throw new NullPointerException();
        }
        
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        
        e.printStackTrace(printWriter);
        printWriter.close();
        
        return stringWriter.toString();
    }

    /**
     * 
     * @param t
     * @param e
     */
    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        if(SwingUtilities.isEventDispatchThread()) {
            this.generateDialog(t, e);
            System.exit(0);
        }
        else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    generateDialog(t, e);
                    System.exit(0);
                }
            });
        }
    }
}

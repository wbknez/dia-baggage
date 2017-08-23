package unmla.airport.ui.panels;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import unmla.airport.ui.contrib.MessageConsole;

/**
 * 
 */
public class ConsolePanel extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -6772603402479135424L;

    /** */
    private final MessageConsole    console;
    
    public ConsolePanel() {
        final JTextArea textArea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(textArea);
        this.console = new MessageConsole(textArea, true);
        
        console.redirectErr(Color.red, null);
        console.redirectOut();
        console.setMessageLines(1300);
        
        this.setLayout(new java.awt.BorderLayout());
        this.add(scrollPane, java.awt.BorderLayout.CENTER);
    }
}

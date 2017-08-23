package unmla.airport.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import unmla.airport.simul.SimulationController;
import unmla.airport.ui.actions.ExitSimulationAction;
import unmla.airport.ui.actions.PauseSimulationAction;
import unmla.airport.ui.actions.ResetSimulationAction;
import unmla.airport.ui.actions.StartSimulationAction;
import unmla.airport.ui.actions.ZoomInAction;
import unmla.airport.ui.actions.ZoomOutAction;
import unmla.airport.ui.panels.GraphPanel;
import unmla.airport.ui.pick.PickData;
import unmla.airport.ui.tabpanes.ConsoleTabPane;
import unmla.airport.ui.tabpanes.SettingsTabPane;

/**
 * 
 */
public class UiController {

    /** */
    private final SimulationController  controller;
    /** */
    private final GraphPanel            graphPanel;
    /** */
    private final JFrame                window;
    
    /* *** Abstract actions. *** */
    private final AbstractAction        startSimulation;
    private final AbstractAction        pauseSimulation;
    private final AbstractAction        resetSimulation;
    private final AbstractAction        exitSimulation;
    
    private final AbstractAction        zoomInAction;
    private final AbstractAction        zoomOutAction;
    
    /* *** Panels. *** */
    private final ConsoleTabPane        consolePane;
    private final SettingsTabPane       settingsPane;
    
    /* *** Picking. *** */
    private final PickData              pickData;
    
    public UiController(final SimulationController controller) {
        if(controller == null) {
            throw new NullPointerException();
        }
        
        this.controller = controller;
        this.controller.setUiController(this);
        
        // picking
        this.pickData = new PickData();
        
        this.graphPanel = new GraphPanel(this.controller);
        this.window = new JFrame("Denver International Airport Simulation");
        
        // action creation
        this.startSimulation = new StartSimulationAction(this.controller);
        this.pauseSimulation = new PauseSimulationAction(this.controller);
        this.resetSimulation = new ResetSimulationAction(this.controller);
        this.exitSimulation = new ExitSimulationAction();
        
        this.zoomInAction = new ZoomInAction(this.controller);
        this.zoomOutAction = new ZoomOutAction(this.controller);
        
        // panel creation
        this.consolePane = new ConsoleTabPane(controller);
        this.settingsPane = new SettingsTabPane(this.controller);
    }   
   
    private void setDefaultLAF() {
        try {
            final String osProperty = System.getProperty("os.name");
            
            /*
             * We do this because the GTK/GNOME3 theme on the CS machines is so 
             * dark that the menus can't be seen.
             */
            if(!osProperty.equals("Linux")) {
                UIManager.setLookAndFeel
                                (UIManager.getSystemLookAndFeelClassName());
            }
        }
        catch(Exception ex) {
            // doesn't matter what the exception is
            // and there's nothing to be done about it; no exception thrown 
            // from the above will affect the program in any way
        }
    }
    
    public void setUpUi() {
        this.setDefaultLAF();
        
        this.sizeWindow();
        this.centerWindow();
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.setLayout(new BorderLayout());
        
        this.createMenuBar();
        this.createToolbar();
        
        // main graph panel
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(this.graphPanel.getViewer(), BorderLayout.CENTER);
        mainPanel.add(this.consolePane, BorderLayout.SOUTH);
        
        this.window.add(mainPanel, BorderLayout.CENTER);
        this.window.add(this.settingsPane, BorderLayout.WEST);
        
//        this.window.pack();
        
        this.window.setVisible(true);
    }
    
    private void createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        
        // Simulation
        final JMenu simMenu = new JMenu("Simulation");
        
        simMenu.add(this.createMenuItem(this.startSimulation));
        simMenu.add(this.createMenuItem(this.pauseSimulation));
        simMenu.add(this.createMenuItem(this.resetSimulation));
        
        simMenu.addSeparator();
        
        simMenu.add(this.exitSimulation);
        
        final JMenu viewMenu = new JMenu("View");
        
        viewMenu.add(this.createMenuItem(this.zoomInAction));
        viewMenu.add(this.createMenuItem(this.zoomOutAction));

        viewMenu.addSeparator();
        
        viewMenu.add(new unmla.airport.ui.actions.ViewCarouselLevelAction(controller));
        viewMenu.add(new unmla.airport.ui.actions.ViewCheckInLevelAction(controller));
        // bind
        menuBar.add(simMenu);
        menuBar.add(viewMenu);
        
        this.window.setJMenuBar(menuBar);
    }
    
    private JMenuItem createMenuItem(final AbstractAction action) {
        if(action == null) {
            throw new NullPointerException();
        }
        
        final JMenuItem menuItem = new JMenuItem(action);
        //menuItem.setIcon(null);
        
        return menuItem;
    }
    
    private void createToolbar() {
        final JToolBar toolBar = new JToolBar();
        
        toolBar.setFloatable(false);
        toolBar.add(this.createToolBarButton(this.startSimulation));
        toolBar.add(this.createToolBarButton(this.pauseSimulation));
        toolBar.add(this.createToolBarButton(this.resetSimulation));
        toolBar.addSeparator();
        toolBar.add(this.createToolBarButton(this.zoomInAction));
        toolBar.add(this.createToolBarButton(this.zoomOutAction));
        toolBar.addSeparator();
        
        this.window.add(toolBar, BorderLayout.PAGE_START);
    }
    
    private JButton createToolBarButton(final AbstractAction action) {
        if(action == null) {
            throw new NullPointerException();
        }
        
        final JButton toolButton = new JButton(action);
        toolButton.setPreferredSize(new Dimension(24, 24));
        toolButton.setSize(new Dimension(24, 24));
        toolButton.setText("");
        
        return toolButton;
    }
    
    public GraphPanel getGraphPanel() {
        return this.graphPanel;
    }
    
    public PickData getPickData() {
        return this.pickData;
    }
    
    public void requestDisplayUpdate() {
        if(this.controller.isRunning()) {
            try {
                SwingUtilities.invokeAndWait((new DisplayUpdater(
                                                            this.controller)));
            }
            catch(InterruptedException iEx) {
                // nothing to do
            }
            catch(InvocationTargetException itEx) {
                // nothing to do
            }
        }
        else {
            if(this.controller.getSimulation().getStatistics()
                    .isUpdateNeeded()) {
                this.controller.getSimulation().getStatistics()
                    .notifyListeners();
            }
            this.pickData.prepareCache();
            this.graphPanel.getViewer().repaint();
        }
    }
    
    public ConsoleTabPane getConsoleTabPane() {
        return this.consolePane;
    }
    
    public SettingsTabPane getSettingsTabPane() {
        return this.settingsPane;
    }
    
    /**
     * Centers the window on the screen.
     */
    private void centerWindow() {
        final Dimension screenSize = 
                                    Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension windowSize = this.window.getSize();
        
        this.window.setLocation((screenSize.width - windowSize.width) / 2,
                            (screenSize.height - windowSize.height) / 2);
    }
    
    /**
     * Sizes the window to 80% of the desktop size.
     */
    private void sizeWindow() {
        final Dimension screenSize = 
                                    Toolkit.getDefaultToolkit().getScreenSize();
        final double widthRatio = .7d;
        final double heightRatio = .8d;
       
        this.window.setSize((int)(screenSize.width * widthRatio), 
                                    (int)(screenSize.height * heightRatio));
    }
}

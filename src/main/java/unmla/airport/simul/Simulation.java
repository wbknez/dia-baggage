package unmla.airport.simul;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import unmla.airport.Airport;
import unmla.airport.IdPool;
import unmla.airport.Statistics;
import unmla.airport.simul.command.SimulationCommand;

/**
 * 
 */
public class Simulation implements Runnable {
    
    public static enum UpdateType {
        Continuous,
        LockStep,
        ;
    }
    
    private final Airport                                   airport;
    
    private final Statistics								statistics;
    
    private final IdPool									idPool;
    
    private final ConcurrentLinkedQueue<SimulationCommand>  commandQueue;
    
    private final AtomicBoolean                             isRunning;
    
    private final SimulationClock                           simulClock;
    
    private final SimulationController                      simulController;
    
    private double                                          simulModifier;
    
    private int                                             simulTickSpacing;
    
    private final Thread                                    simulThread;
    
    private UpdateType                                      updateType;
    
    public Simulation() {
        this.airport = new Airport();
        this.statistics = new Statistics();
        this.idPool = new IdPool();
        this.commandQueue = new ConcurrentLinkedQueue<SimulationCommand>();
        this.isRunning = new AtomicBoolean(false);
        this.simulClock = new SimulationClock(1.0d);
        this.simulController = new SimulationController(this);
        this.simulModifier = 1.0d;
        this.simulTickSpacing = 500;
        this.simulThread = new Thread(this);
        this.updateType = UpdateType.Continuous;
        
        this.simulThread.start();
    }
    
    public void addCommand(final SimulationCommand command) {
        if(command == null) {
            throw new NullPointerException();
        }
        
        this.commandQueue.offer(command);
    }
    
    public Airport getAirport() {
        return this.airport;
    }
    
    public Statistics getStatistics() {
    	return this.statistics;
    }
    
    public IdPool getIdPool() {
    	return this.idPool;
    }
    
    public SimulationController getController() {
        return this.simulController;
    }
    
    public SimulationClock getClock() {
        return this.simulClock;
    }
    
    @Override
    public void run() {
        this.initialize();
        
        while(true) {
            if(!this.commandQueue.isEmpty()) {
                SimulationCommand command;
                
                while((command = this.commandQueue.poll()) != null) {
                    command.execute(this);
                }
            }
            
            if(this.isRunning.get() == false) {
                try {
                    synchronized(this.simulController) {
                        this.simulController.wait();
                    }
                }
                catch(InterruptedException iEx) {
                    System.out.println("Interrupted!");
                }
            }
            
            /*
             * This is a mild perversion of time counter's use as most objects 
             * are  not allowed to do this, but in this case it's necessary to 
             * allow for a scalable simulation.
             */
            final double totalStep = this.simulClock.getTimeStep() * 
                                        this.simulModifier;
            
            this.simulClock.advance(totalStep);
            System.out.println(this.simulClock);
            while(this.simulClock.canStep()) {
                this.simulClock.step();
                // update airport
                airport.step(this.simulClock.getTimeStep(), this);
            }
            
            this.simulController.requestRepaint();
            
            try {
                Thread.sleep(this.simulTickSpacing);
            }
            catch(InterruptedException iEx) {
                
            }
            
            /*
             * Required as good practice.
             */
            if(this.simulThread.isInterrupted()) {
                break;
            }
            
            if(this.updateType == UpdateType.LockStep) {
                try {
                    synchronized(this.simulController) {
                        this.simulController.wait();
                    }
                }
                catch(InterruptedException iEx) {
                    System.out.println("Interrupted!");
                }
            }
        }
    }
    
    public int getTickSpacing() {
        return this.simulTickSpacing;
    }
    
    public void initialize() {
        this.airport.initialize(this);
    }
    
    public void reset() {
        System.out.println("Reset!");
        this.simulClock.reset();
        this.airport.reset(this);
    }
    
    /**
     * 
     * @param isRunning
     */
    public void setIsRunning(final boolean isRunning) {
        if(!this.isRunning.compareAndSet(!isRunning, isRunning)) {
            throw new AssertionError("This should never happen!");
        }
    }
    
    /**
     * Sets the time step modifier to the specified value.
     * 
     * @param modifier The modifier to use.
     * @throws IllegalArgumentException If <code>modifier</code> is less than or 
     * equal to zero.
     */
    public void setModifier(final double modifier) {
        if(Double.compare(modifier, 0) <= 0) {
            throw new IllegalArgumentException("Time steps must be positive!");
        }
        
        if(Double.compare(this.simulModifier, modifier) != 0) {
            System.out.println("Simulation modifier set to: " + modifier);
            this.simulModifier = modifier;
        }
    }
    
    public void setTickSpacing(final int tickSpacing) {
        if(tickSpacing > 0 && tickSpacing != this.simulTickSpacing) {
            System.out.println("Simulation tick spacing set to: " + 
                                    tickSpacing);
            this.simulTickSpacing = tickSpacing;
        }
    }
}

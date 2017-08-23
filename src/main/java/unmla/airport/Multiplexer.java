package unmla.airport;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * 
 */
public class Multiplexer extends AbstractQueue<LuggageTransporter> 
                                 implements Queue<LuggageTransporter> {
    
    private final ArrayList<LuggageTransporter> inputs;
    
    /**
     * Constructor.
     */
    public Multiplexer() {
        this.inputs = new ArrayList<LuggageTransporter>();
    }

    @Override
    public boolean offer(LuggageTransporter transporter) {
        if(!this.inputs.contains(transporter)) {
            return this.inputs.add(transporter);
        }
        
        return false;
    }

    @Override
    public LuggageTransporter peek() {
        if(this.inputs.isEmpty()) {
            return null;
        }
        
        final LuggageTransporter element = this.inputs.get(0);
        return element;
    }

    @Override
    public LuggageTransporter poll() {
        if(this.inputs.isEmpty()) {
            throw new NoSuchElementException();
        }
        
        final LuggageTransporter element = this.inputs.remove(0);
        return element;
    }

    @Override
    public Iterator<LuggageTransporter> iterator() {
        return this.inputs.iterator();
    }

    @Override
    public int size() {
        return this.inputs.size();
    }
}

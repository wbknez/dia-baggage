package unmla.airport.ui.table;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import unmla.airport.Luggage;
import unmla.airport.LuggageTransporter;

/**
 * 
 */
public class LuggageTableModel extends AbstractTableModel {

    /**
     * For serialization; generated by Eclipse.
     */
    private static final long serialVersionUID = 7082764715552704951L;

    private final String[]  columnHeaders   = {"ID", "Destination", "Time", 
                                                "Flight"};
    
    private final ArrayList<Luggage>    luggageList;
    
    public LuggageTableModel() {
        this.luggageList = new ArrayList<Luggage>();
    }
    
    @Override
    public int getColumnCount() {
        return this.columnHeaders.length;
    }
    
    public void clear() {
        
    }
    
    @Override
    public String getColumnName(int index) {
        return this.columnHeaders[index];
    }

    @Override
    public int getRowCount() {
        return this.luggageList.size();
    }
    
    public void update(final LuggageTransporter trans) {
        this.luggageList.clear();
        
        if(trans == null) {
            return;
        }
        
        final Object[] luggage = trans.getContents().toArray();
        
        if(luggage != null) {
            for(Object l : luggage) {
                this.luggageList.add((Luggage)l);
            }
        }
    }
    
    public Luggage getLuggageAt(int rowIndex) {
        if(this.luggageList.isEmpty() || rowIndex >= this.luggageList.size()) {
            return null;
        }
        
        return this.luggageList.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final Luggage target = this.luggageList.get(rowIndex);
        
        switch(columnIndex) {
            case 0:
                return target.getId();
            case 1:
                return target.getDestination().getID();
            case 2:
                return target.getTimer();
            case 3:
                return target.getFlightTime();
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}

package unmla.airport.ui;

/**
 * 
 */
public class AirportUiComponent extends UiComponent {

    private double minX;
    
    private double minY;
    
    private double maxX;
    
    private double maxY;
    
    /**
     * Constructor.
     */
    public AirportUiComponent() {   
    }
    
    public double getMinX() {
        return this.minX;
    }
    
    public double getMinY() {
        return this.minY;
    }
    
    public double getMaxX() {
        return this.maxX;
    }
    
    public double getMaxY() {
        return this.maxY;
    }
    
    public double getHeight() {
        return this.maxY - this.minY;
    }
    
    public double getWidth() {
        return this.maxX - this.minX;
    }
    
    public void setMinX(final double minX) {
        this.minX = minX;
    }
    
    public void setMinY(final double minY) {
        this.minY = minY;
    }
    
    public void setMaxX(final double maxX) {
        this.maxX = maxX;
    }
    
    public void setMaxY(final double maxY) {
        this.maxY = maxY;
    }
}

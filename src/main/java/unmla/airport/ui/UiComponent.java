package unmla.airport.ui;

import java.awt.Color;

//import java.awt.Color;

/**
 * 
 */
public class UiComponent {
    
    public static class Colors {
        
        /* *** Edges and Vertices. *** */
        /* capacity-based colors. */
        public static final Color   Capacity01  = new Color(37, 215, 10);
        public static final Color   Capacity25  = new Color(191, 238, 108);
        public static final Color   Capacity50  = new Color(245, 238, 37);
        public static final Color   Capacity75  = new Color(243, 222, 118);
        public static final Color   Capacity99  = new Color(204, 102, 0);
        public static final Color   FullColor   = new Color(255, 165, 11);
        
        /* the rest */
        public static final Color   DisabledColor   = new Color(255, 0, 0);
        public static final Color   MarkedColor     = new Color(153, 0, 153);
        public static final Color   PickedColor     = new Color(30, 245, 223);
        public static final Color   WorkingColor    = new Color(0, 127, 0);
        
        /* *** Custom. *** */
        public static final Color   CarouselBorderColor = Color.pink;
        public static final Color   CheckInBorderColor  = Color.cyan;
        public static final Color   GateBorderColor     = Color.blue;
        public static final Color   ScannerBorderColor  = Color.black;
        public static final Color   SwitchBorderColor   = Color.black;
        
        /**
         * Constructor.
         */
        private Colors() {
        }
    }
    
    public static final int CoreLevel   = 0;
    
    public static final int OutgoingLevel   = 1;
    
    public static final int IncomingLevel   = 2;

    private double  x;
    
    private double  y;
    
//    private Color   color;
    
    private int     level;
    
    /**
     * Constructor.
     */
    public UiComponent() {
        this.level = 1;
    }
    
 //   public Color getColor() {
 //       return this.color;
 //   }
    
    public int getLevel() {
        return this.level;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
}

package unmla.airport.graph.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.uci.ics.jung.graph.DirectedSparseGraph;

/**
 * 
 */
public class GraphMLParser<S, T> extends DefaultHandler {
    
    public static class GraphAttribute {
        
        /**
         * 
         */
        public static enum AttributeType {
            
            /**
             * Double based attribute, used for time based values.
             */
            Double,
            
            /**
             * Float based attribute, currently unused.
             */
            Float,
            
            /**
             * Integer based attribute, used for identification numbers and timing 
             * information.
             */
            Int,
            
            /** 
             * String based attribute, used for descriptions and class 
             * identifiers. 
             */
            String,
            
            /**
             * 
             */
            Unknown,
            ;
        }
        
        public static enum Target {
            /** */
            Edge,
            /** */
            Neither,
            /** */
            Node,
            ;
        }
        
        /** The long-form name of this attribute. */
        private final String        name;
        /** The object type this attribute maps to. */
        private final AttributeType type;
        /** The default value of this attribute; may be <code>null</code>. */
        private Object              value;
        /** */
        private final String        id;
        /** */
        private final Target        target;
                
        /**
         * Constructor.
         * 
         * @param name The long-form name to use.
         * @param type The object type to map the attribute to.
         * @param value The default value to use, if any.
         * @throws NullPointerException If either <code>name</code> or 
         * <code>type</code> are <code>null</code>.
         */
        public GraphAttribute(final String name, String id,
                            final AttributeType type, 
                            final Object value,
                            final Target target) {
            if(name == null) {
                throw new NullPointerException();
            }
            
            if(id == null) {
                throw new NullPointerException();
            }
            
            if(target == null) {
                throw new NullPointerException();
            }
            
            if(type == null) {
                throw new NullPointerException();
            }
            
            this.name = name;
            this.id = id;
            this.target = target;
            this.type = type;
            this.value = value;
        }
        
        /**
         * 
         * @return
         */
        public Target getTarget() {
            return this.target;
        }
        
        /**
         * Returns the Java object type this attribute should map to.
         * 
         * @return
         */
        public AttributeType getAttributeType() {
            return this.type;
        }
        
        /**
         * Returns the default value of this attribute.
         * 
         * @return
         */
        public Object getDefaultValue() {
            return this.value;
        }
        
        /**
         * Returns the long-form string identifier for this attribute.
         * @return
         */
        public String getLongName() {
            return this.name;
        }
        
        public String getShortId() {
            return this.id;
        }
        
        /**
         * 
         * @param obj
         */
        public void setDefaultValue(final Object obj) {
            if(obj == null) {
                throw new NullPointerException();
            }
            
            this.value = obj;
        }
        
        /*
         * Debugging only.
         */
        @Override
        public String toString() {
            return "(" + this.name + ": " + this.type + ", " + this.value + ")";
        }
    }

    private final HashMap<String, GraphAttribute>   attributes;
    /** Mapping of short-form to long-form vertex attribute names. */
    private final HashMap<String, String>           edgeNames;
    /** Mapping of short-form to long-form vertex attribute names. */
    private final HashMap<String, String>           vertexNames;
    /** Mapping of short-form identifiers to created vertices. */
    private final HashMap<String, S>                vertices;
    
    private final TypeFactory<String, T, S, T>      edgeFactory;
    
    private final TypeFactory<String, S, S, T>      vertexFactory;
    
    private final DirectedSparseGraph<S, T>         graph;
    
    /* *** Parsed attributes and values. *** */
    
    /** Temporary storage buffer. */
    private String                                  temp;
    
    /* *** Attribute parsing. *** */
    
    private GraphAttribute                          graphAttribute;
    
    /* *** Data parsing. *** */
    private String                                  dataId;
    
    /* *** Generic object parsing. *** */
    
    private int                                     objCapacity;
    
    private String                                  objClassType;
    
    private String                                  objId;
    
    private String                                  objName;
    
    private double                                  objTime;
    
    /* *** Node parsing. *** */
    
    /* *** Edge parsing. *** */
    
    private int                                     edgeLength;
    
    private String                                  edgeSource;
    
    private String                                  edgeTarget;
    
    /* *** UI parsing. *** */
    /** The x-coordinate of the node. */
    private double                                  x;
    /** The y-coordinate of the node. */
    private double                                  y;
    
    /* *** Statistics *** */
    /*
     * This is an unfortunate evil to get around how the graph data is actually 
     * generated.
     */
    private double                                  maxX;
    
    private double                                  maxY;
    
    private double                                  minX;
    
    private double                                  minY;
    
    private int                                     uiLevel;
    
    private boolean                                 inLabel;
    
    /**
     * Constructor.
     */
    public GraphMLParser() {
        this.attributes = new HashMap<String, GraphAttribute>();
        this.edgeNames = new HashMap<String, String>();
        this.vertexNames = new HashMap<String, String>();
        this.vertices = new HashMap<String, S>();
        
        this.edgeFactory = new TypeFactory<String, T, S, T>();
        this.vertexFactory = new TypeFactory<String, S, S, T>();
        
        this.graph = new DirectedSparseGraph<S,T>();
        
        this.temp = "";
        this.graphAttribute = null;
    }
    
    public int getObjCapacity() {
        return this.objCapacity;
    }
    
    public String getObjClassType() {
        return this.objClassType;
    }
    
    public String getObjId() {
        return this.objId;
    }
    
    public String getObjName() {
        return this.objName;
    }
    
    public double getObjTime() {
        return this.objTime;
    }
    
    public int getEdgeLength() {
        return this.edgeLength;
    }
    
    public int getUiLevel() {
        return this.uiLevel;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
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
    
    private void evaluateMinAndMax() {
        if(Double.compare(this.x, this.minX) < 0) {
            this.minX = this.x;
        }
        else if(Double.compare(this.x, this.maxX) > 0) {
            this.maxX = this.x;
        }
        
        if(Double.compare(this.y, this.minY) < 0) {
            this.minY = this.y;
        }
        else if(Double.compare(this.y, this.maxY) > 0) {
            this.maxY = this.y;
        }
    }
    
    public void addEdgeCreator(final String id, 
                                    final TypeCreator<T, S, T> creator) {
        if(id == null) {
            throw new NullPointerException();
        }
        
        if(creator == null) {
            throw new NullPointerException();
        }
        
        this.edgeFactory.put(id, creator);
    }
    
    public void addVertexCreator(final String id, 
                                    final TypeCreator<S, S, T> creator) {
        if(id == null) {
            throw new NullPointerException();
        }
        
        if(creator == null) {
            throw new NullPointerException();
        }
        
        this.vertexFactory.put(id, creator);
    }
    
    /**
     * 
     * @return
     */
    public DirectedSparseGraph<S,T> getGraph() {
        return this.graph;
    }
    
    public DirectedSparseGraph<S, T> parse(final InputStream input) 
                throws ParserConfigurationException, SAXException, IOException {
        final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        final SAXParser saxParser = saxFactory.newSAXParser();
        
        saxParser.parse(input, this);
        
        return this.getGraph();
    }
    
    @Override
    public void startElement(String uri, String localName,
                     String qName, Attributes attributes) throws SAXException {
        this.temp = "";
        
        if(qName.equals("data")) {
            this.dataId = attributes.getValue("key");
        }
        else if(qName.equals("key")) {
            final String name = attributes.getValue("attr.name");
            final String type = attributes.getValue("attr.type");
            final String id = attributes.getValue("id");
            final String forWhom = attributes.getValue("for");
            final String yfiles = attributes.getValue("yfiles.type");
            
            if(name != null && type != null && forWhom != null) {
                final GraphAttribute.Target target = (forWhom.equals("node") ?
                                                 GraphAttribute.Target.Node :
                                                 GraphAttribute.Target.Edge);
                
                this.graphAttribute = new GraphAttribute(name, id,
                                                this.parseAttributeType(type), 
                                                null,
                                                target);
            }
            
            if(yfiles != null && yfiles.equals("nodegraphics") && 
                    forWhom.equals("node")) {
                this.graphAttribute = new GraphAttribute("nodegraphics", id,
                                            GraphAttribute.AttributeType.String,
                                            null,
                                            GraphAttribute.Target.Node);
            }
        }
        else if(qName.equals("edge")) {
            this.objId = attributes.getValue("id");
            this.edgeSource = attributes.getValue("source");
            this.edgeTarget = attributes.getValue("target");
            
            this.objCapacity = (Integer)this.attributes.get(
                               this.edgeNames.get("Capacity"))
                               .getDefaultValue();
            this.objClassType = (String)this.attributes.get(
                                this.edgeNames.get("ClassType"))
                                .getDefaultValue();
            this.objTime = (Double)this.attributes.get(
                               this.edgeNames.get("DefaultTime"))
                               .getDefaultValue();
            this.edgeLength = (Integer)this.attributes.get(
                               this.edgeNames.get("Length"))
                               .getDefaultValue();
            
        }
        else if(qName.equals("node")) {
            this.objId = attributes.getValue("id");
            
            this.objCapacity = (Integer)this.attributes.get(
                               this.vertexNames.get("Capacity"))
                               .getDefaultValue();
            this.objClassType = (String)this.attributes.get(
                                this.vertexNames.get("ClassType"))
                                .getDefaultValue();
            this.objTime = (Double)this.attributes.get(
                               this.vertexNames.get("DefaultTime"))
                               .getDefaultValue();
            this.uiLevel = (Integer)this.attributes.get(
                                this.vertexNames.get("Level"))
                                .getDefaultValue();
        }
        else if(qName.equals("y:Geometry")) {
            final String strX = attributes.getValue("x");
            final String strY = attributes.getValue("y");
            
            this.x = Double.parseDouble(strX);
            this.y = Double.parseDouble(strY);
            
            this.evaluateMinAndMax();
        }
        else if(qName.equals("y:NodeLabel")) {
            // not used, might be for importing graphics settings
            this.inLabel = true;
        }
    }
    
    @Override
    public void characters(char[] buffer, int start, int length) 
                                throws SAXException {
        this.temp = new String(buffer, start, length);
        
        if(this.inLabel) {
            this.objName = this.temp;
            this.inLabel = false;
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) 
                                throws SAXException {
        if(qName.equals("data")) {
            if(this.dataId.equals(this.vertexNames.get("Capacity")) ||
                    this.dataId.equals(this.edgeNames.get("Capacity"))) {
                this.objCapacity = Integer.parseInt(this.temp);
            }
            else if(this.dataId.equals(this.vertexNames.get("ClassType")) ||
                    this.dataId.equals(this.edgeNames.get("ClassType"))) {
                this.objClassType = this.temp;
            }
            else if(this.dataId.equals(this.vertexNames.get("DefaultTime")) ||
                    this.dataId.equals(this.edgeNames.get("DefaultTime"))) {
                this.objTime = Double.parseDouble(this.temp);
            }
            else if(this.dataId.equals(this.edgeNames.get("Length"))) {
                this.edgeLength = Integer.parseInt(this.temp);
            }
            else if(this.dataId.equals(this.vertexNames.get("Level"))) {
                this.uiLevel = Integer.parseInt(this.temp);
            }
        }
        else if(qName.equals("default")) {
            this.graphAttribute.setDefaultValue(this.parseDefaultValue(
                    this.graphAttribute.getAttributeType(), 
                    this.temp));
        }
        else if(qName.equals("key")) {
            if(this.graphAttribute != null) {
                this.attributes.put(this.graphAttribute.getShortId(), 
                                    this.graphAttribute);
                if(this.graphAttribute.getTarget() == 
                        GraphAttribute.Target.Node) {
                    this.vertexNames.put(this.graphAttribute.getShortId(),
                                            this.graphAttribute.getLongName());
                    this.vertexNames.put(this.graphAttribute.getLongName(),
                                            this.graphAttribute.getShortId());
                }
                else {
                    this.edgeNames.put(this.graphAttribute.getShortId(),
                                            this.graphAttribute.getLongName());
                    this.edgeNames.put(this.graphAttribute.getLongName(),
                                            this.graphAttribute.getShortId());                    
                }
            }
        }
        else if(qName.equals("edge")) {
            if(this.objClassType == null) {
                throw new SAXException("The class type was not parsed!");
            }
                
            final TypeCreator<T, S, T> creator = 
                    this.edgeFactory.get(this.objClassType);
            final T edge = creator.create(this);
            
            this.graph.addEdge(edge, this.vertices.get(this.edgeSource), 
                    this.vertices.get(this.edgeTarget));
        }
        else if(qName.equals("node")) {
            if(this.objClassType == null) {
                throw new SAXException("The class type was not parsed!");
            }
            
            final TypeCreator<S, S, T> creator = 
                    this.vertexFactory.get(this.objClassType);
            final S vertex = creator.create(this);
            
            this.graph.addVertex(vertex);
            this.vertices.put(this.objId, vertex);
        }
        else if(qName.equals("y:NodeLabel")) {
            // empty
        }
    }
    
    private GraphAttribute.AttributeType parseAttributeType(final String type) {
        for(GraphAttribute.AttributeType attributeType : 
                                            GraphAttribute.AttributeType.values()) {
            if(attributeType.name().equalsIgnoreCase(type)) {
                return attributeType;
            }
        }
        
        return GraphAttribute.AttributeType.Unknown;
    }
    
    private Object parseDefaultValue(
                                    final GraphAttribute.AttributeType attributeType, 
                                    final String value) {
        if(attributeType == null) {
            throw new NullPointerException();
        }
        
        if(value == null) {
            throw new NullPointerException();
        }
        
        if(value.equals("")) {
            return "";
        }
        
        if(attributeType == GraphAttribute.AttributeType.Double) {
            return Double.parseDouble(value);
        }
        else if(attributeType == GraphAttribute.AttributeType.Int) {
            return Integer.parseInt(value);
        }
        else if(attributeType == GraphAttribute.AttributeType.String) {
            return value;
        }
        
        return "";
    }
}

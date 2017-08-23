package unmla.airport.graph.parser;

import org.xml.sax.SAXException;

/**
 * 
 * @param T
 */
public interface TypeCreator<T, V, E> {
    
    /**
     * 
     * @param dataList
     * @param attributes
     * @param names
     * @return
     */
    T create(final GraphMLParser<V,E> parser) throws SAXException;
}

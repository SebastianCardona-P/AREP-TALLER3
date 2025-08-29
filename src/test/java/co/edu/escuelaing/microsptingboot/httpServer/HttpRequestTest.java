package co.edu.escuelaing.microsptingboot.httpServer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Pruebas unitarias para HttpRequest
 *
 * @author Sebastian
 */
public class HttpRequestTest {

    @Test
    public void testGetValueWithValidParameter() throws URISyntaxException {
        URI uri = new URI("http://localhost:35000/test?name=Sebastian&age=25");
        HttpRequest request = new HttpRequest(uri);

        assertEquals("Sebastian", request.getValue("name"));
        assertEquals("25", request.getValue("age"));
    }

    @Test
    public void testGetValueWithNonExistentParameter() throws URISyntaxException {
        URI uri = new URI("http://localhost:35000/test?name=Sebastian&age=25");
        HttpRequest request = new HttpRequest(uri);

        assertEquals("", request.getValue("city"));
    }

    @Test
    public void testGetValueWithEmptyQuery() throws URISyntaxException {
        URI uri = new URI("http://localhost:35000/test");
        HttpRequest request = new HttpRequest(uri);

        assertEquals("", request.getValue("name"));
    }

    @Test
    public void testGetValueWithEmptyParameterValue() throws URISyntaxException {
        URI uri = new URI("http://localhost:35000/test?name=&age=25");
        HttpRequest request = new HttpRequest(uri);

        assertEquals("", request.getValue("name"));
        assertEquals("25", request.getValue("age"));
    }

    @Test
    public void testGetValueWithParameterWithoutValue() throws URISyntaxException {
        URI uri = new URI("http://localhost:35000/test?flag&name=Sebastian");
        HttpRequest request = new HttpRequest(uri);

        assertEquals("", request.getValue("flag"));
        assertEquals("Sebastian", request.getValue("name"));
    }

    @Test
    public void testGetValueWithSingleParameter() throws URISyntaxException {
        URI uri = new URI("http://localhost:35000/test?name=Sebastian");
        HttpRequest request = new HttpRequest(uri);

        assertEquals("Sebastian", request.getValue("name"));
    }

    @Test
    public void testGetValueWithComplexQuery() throws URISyntaxException {
        URI uri = new URI("http://localhost:35000/calculate/suma?a=10&b=20&operation=sum&debug=true");
        HttpRequest request = new HttpRequest(uri);

        assertEquals("10", request.getValue("a"));
        assertEquals("20", request.getValue("b"));
        assertEquals("sum", request.getValue("operation"));
        assertEquals("true", request.getValue("debug"));
    }

    @Test
    public void testGetValueWithNullParameterName() throws URISyntaxException {
        URI uri = new URI("http://localhost:35000/test?name=Sebastian&age=25");
        HttpRequest request = new HttpRequest(uri);

        assertEquals("", request.getValue(null));
    }
}

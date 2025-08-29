package co.edu.escuelaing.microsptingboot.httpServer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para HttpResponse
 *
 * @author Sebastian
 */
public class HttpResponseTest {

    @Test
    public void testHttpResponseInstantiation() {
        // Verificar que se puede instanciar la clase HttpResponse
        HttpResponse response = new HttpResponse();
        assertNotNull(response);
    }

    @Test
    public void testHttpResponseIsNotNull() {
        HttpResponse response = new HttpResponse();
        assertNotNull(response);
        assertEquals(HttpResponse.class, response.getClass());
    }

    @Test
    public void testMultipleInstances() {
        HttpResponse response1 = new HttpResponse();
        HttpResponse response2 = new HttpResponse();

        assertNotNull(response1);
        assertNotNull(response2);
        assertNotSame(response1, response2);
    }
}

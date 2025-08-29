package co.edu.escuelaing.microsptingboot.httpServer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import co.edu.escuelaing.microsptingboot.controller.GreetingController;
import co.edu.escuelaing.microsptingboot.controller.CalcuteController;

/**
 * Pruebas unitarias para HttpServer
 *
 * @author Sebastian
 */
public class HttpServerTest {

    @TempDir
    Path tempDir;

    private String originalBasePath;

    @BeforeEach
    public void setUp() {
        // Backup del basePath original para restaurarlo después
        originalBasePath = getBasePath();

        // Limpiar servicios antes de cada prueba
        HttpServer.services.clear();
    }

    @AfterEach
    public void tearDown() {
        // Restaurar el basePath original
        setBasePath(originalBasePath);
    }

    // Métodos helper para acceder a campos privados usando reflection
    private String getBasePath() {
        try {
            var field = HttpServer.class.getDeclaredField("basePath");
            field.setAccessible(true);
            return (String) field.get(null);
        } catch (Exception e) {
            return "src/main/java/resources/";
        }
    }

    private void setBasePath(String path) {
        try {
            var field = HttpServer.class.getDeclaredField("basePath");
            field.setAccessible(true);
            field.set(null, path);
        } catch (Exception e) {
            // Ignorar si no se puede setear
        }
    }

    @Test
    public void testLoadServicesWithSpecificController() {
        String[] args = {"co.edu.escuelaing.microsptingboot.controller.GreetingController"};
        HttpServer.loadServices(args);

        Map<String, Method> services = HttpServer.services;
        assertTrue(services.containsKey("/greeting"));
        assertTrue(services.containsKey("/hello"));
        assertTrue(services.containsKey("/status"));
        assertTrue(services.containsKey("/welcome"));
    }

    @Test
    public void testLoadServicesAutomatically() {
        String[] args = {};
        HttpServer.loadServices(args);

        Map<String, Method> services = HttpServer.services;

        // Verificar que se cargaron servicios de ambos controladores
        assertTrue(services.containsKey("/greeting"));
        assertTrue(services.containsKey("/hello"));
        assertTrue(services.containsKey("/status"));
        assertTrue(services.containsKey("/welcome"));
        assertTrue(services.containsKey("/calculate/suma"));
        assertTrue(services.containsKey("/calculate/resta"));
    }

    @Test
    public void testLoadServicesWithNullArgs() {
        HttpServer.loadServices(null);

        Map<String, Method> services = HttpServer.services;

        // Debería cargar automáticamente todos los controladores
        assertTrue(services.size() > 0);
        assertTrue(services.containsKey("/greeting"));
        assertTrue(services.containsKey("/calculate/suma"));
    }

    @Test
    public void testStaticFilesConfiguration() throws IOException {
        String testStaticPath = "/static";
        HttpServer.staticfiles(testStaticPath);

        String expectedPath = "target/classes" + testStaticPath + "/";
        assertEquals(expectedPath, getBasePath());
    }

    @Test
    public void testStaticFilesConfigurationWithoutLeadingSlash() throws IOException {
        String testStaticPath = "public";
        HttpServer.staticfiles(testStaticPath);

        String expectedPath = "target/classes/" + testStaticPath + "/";
        assertEquals(expectedPath, getBasePath());
    }

    @Test
    public void testCopyStaticFiles() throws IOException {
        // Crear estructura de archivos de prueba
        Path sourceDir = tempDir.resolve("source");
        Path destDir = tempDir.resolve("dest");

        Files.createDirectories(sourceDir);
        Files.createDirectories(destDir);

        // Crear archivos de prueba
        Files.write(sourceDir.resolve("test.html"), "<html><body>Test</body></html>".getBytes());
        Files.write(sourceDir.resolve("style.css"), "body { color: red; }".getBytes());

        Path subDir = sourceDir.resolve("images");
        Files.createDirectories(subDir);
        Files.write(subDir.resolve("test.png"), "fake-image-data".getBytes());

        // Ejecutar copia
        HttpServer.copyStaticFiles(sourceDir.toString(), destDir.toString());

        // Verificar que los archivos se copiaron
        assertTrue(Files.exists(destDir.resolve("test.html")));
        assertTrue(Files.exists(destDir.resolve("style.css")));
        assertTrue(Files.exists(destDir.resolve("images/test.png")));

        // Verificar contenido
        assertEquals("<html><body>Test</body></html>", Files.readString(destDir.resolve("test.html")));
        assertEquals("body { color: red; }", Files.readString(destDir.resolve("style.css")));
    }

    @Test
    public void testFindRestControllers() {
        // Este test verifica que el método de exploración automática funciona
        String[] args = {};
        HttpServer.loadServices(args);

        Map<String, Method> services = HttpServer.services;

        // Debe encontrar al menos los controladores conocidos
        assertFalse(services.isEmpty());

        // Verificar que encuentra métodos específicos
        assertTrue(services.containsKey("/greeting"));
        assertTrue(services.containsKey("/calculate/suma"));
        assertTrue(services.containsKey("/calculate/resta"));
    }

    @Test
    public void testServiceRegistration() throws NoSuchMethodException {
        // Limpiar servicios
        HttpServer.services.clear();

        // Registrar un servicio manualmente para testing
        Method greetingMethod = GreetingController.class.getMethod("greeting", String.class);
        HttpServer.services.put("/test-greeting", greetingMethod);

        assertTrue(HttpServer.services.containsKey("/test-greeting"));
        assertEquals(greetingMethod, HttpServer.services.get("/test-greeting"));
    }

    @Test
    public void testProcessRequestWithValidService() throws URISyntaxException, IOException {
        // Configurar servicios
        HttpServer.loadServices(new String[]{});

        // Crear una URI de prueba
        URI requestUri = new URI("http://localhost:35000/app/greeting?name=TestUser");

        // Crear un StringWriter para capturar la salida
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        // Usar reflection para llamar al método privado processRequest
        try {
            Method processRequestMethod = HttpServer.class.getDeclaredMethod("processRequest", URI.class, PrintWriter.class);
            processRequestMethod.setAccessible(true);
            processRequestMethod.invoke(null, requestUri, out);

            String output = stringWriter.toString();
            assertTrue(output.contains("HTTP/1.1 200 OK"));
            assertTrue(output.contains("Hola TestUser"));
        } catch (Exception e) {
            fail("Error al ejecutar processRequest: " + e.getMessage());
        }
    }

    @Test
    public void testProcessRequestWithInvalidService() throws URISyntaxException, IOException {
        // Limpiar servicios para simular servicio no encontrado
        HttpServer.services.clear();

        URI requestUri = new URI("http://localhost:35000/app/nonexistent");

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        try {
            Method processRequestMethod = HttpServer.class.getDeclaredMethod("processRequest", URI.class, PrintWriter.class);
            processRequestMethod.setAccessible(true);
            processRequestMethod.invoke(null, requestUri, out);

            String output = stringWriter.toString();
            assertTrue(output.contains("404 Not Found"));
        } catch (Exception e) {
            fail("Error al ejecutar processRequest: " + e.getMessage());
        }
    }

    @Test
    public void testHandleRequestTypeHTML() throws URISyntaxException, IOException {
        // Crear un archivo HTML de prueba
        setBasePath(tempDir.toString() + "/");

        Path htmlFile = tempDir.resolve("test.html");
        Files.write(htmlFile, "<html><body>Test HTML</body></html>".getBytes());

        URI requestUri = new URI("http://localhost:35000/test.html");

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        try {
            Method handlerequestTypeMethod = HttpServer.class.getDeclaredMethod("handlerequestType", URI.class, PrintWriter.class, OutputStream.class);
            handlerequestTypeMethod.setAccessible(true);
            handlerequestTypeMethod.invoke(null, requestUri, out, new ByteArrayOutputStream());

            String output = stringWriter.toString();
            assertTrue(output.contains("HTTP/1.1 200 OK"));
            assertTrue(output.contains("text/html"));
            assertTrue(output.contains("Test HTML"));
        } catch (Exception e) {
            fail("Error al manejar archivo HTML: " + e.getMessage());
        }
    }

    @Test
    public void testHandleRequestTypeCSS() throws URISyntaxException, IOException {
        setBasePath(tempDir.toString() + "/");

        Path cssFile = tempDir.resolve("style.css");
        Files.write(cssFile, "body { color: blue; }".getBytes());

        URI requestUri = new URI("http://localhost:35000/style.css");

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        try {
            Method handlerequestTypeMethod = HttpServer.class.getDeclaredMethod("handlerequestType", URI.class, PrintWriter.class, OutputStream.class);
            handlerequestTypeMethod.setAccessible(true);
            handlerequestTypeMethod.invoke(null, requestUri, out, new ByteArrayOutputStream());

            String output = stringWriter.toString();
            assertTrue(output.contains("HTTP/1.1 200 OK"));
            assertTrue(output.contains("text/css"));
            assertTrue(output.contains("body { color: blue; }"));
        } catch (Exception e) {
            fail("Error al manejar archivo CSS: " + e.getMessage());
        }
    }

    @Test
    public void testHandleRequestTypeJS() throws URISyntaxException, IOException {
        setBasePath(tempDir.toString() + "/");

        Path jsFile = tempDir.resolve("script.js");
        Files.write(jsFile, "console.log('Hello World');".getBytes());

        URI requestUri = new URI("http://localhost:35000/script.js");

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        try {
            Method handlerequestTypeMethod = HttpServer.class.getDeclaredMethod("handlerequestType", URI.class, PrintWriter.class, OutputStream.class);
            handlerequestTypeMethod.setAccessible(true);
            handlerequestTypeMethod.invoke(null, requestUri, out, new ByteArrayOutputStream());

            String output = stringWriter.toString();
            assertTrue(output.contains("HTTP/1.1 200 OK"));
            assertTrue(output.contains("text/javascript"));
            assertTrue(output.contains("console.log('Hello World');"));
        } catch (Exception e) {
            fail("Error al manejar archivo JavaScript: " + e.getMessage());
        }
    }

    @Test
    public void testHandleRequestTypeNotFound() throws URISyntaxException {
        URI requestUri = new URI("http://localhost:35000/nonexistent.html");

        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        try {
            Method handlerequestTypeMethod = HttpServer.class.getDeclaredMethod("handlerequestType", URI.class, PrintWriter.class, OutputStream.class);
            handlerequestTypeMethod.setAccessible(true);
            handlerequestTypeMethod.invoke(null, requestUri, out, new ByteArrayOutputStream());

            String output = stringWriter.toString();
            assertTrue(output.contains("404 Not Found"));
        } catch (Exception e) {
            fail("Error al manejar archivo no encontrado: " + e.getMessage());
        }
    }

    @Test
    public void testIsFileExists() throws IOException {
        // Crear un archivo temporal
        Path testFile = tempDir.resolve("exists.txt");
        Files.write(testFile, "test content".getBytes());

        try {
            Method isFileExistsMethod = HttpServer.class.getDeclaredMethod("isFileExists", String.class);
            isFileExistsMethod.setAccessible(true);

            Boolean exists = (Boolean) isFileExistsMethod.invoke(null, testFile.toString());
            assertTrue(exists);

            Boolean notExists = (Boolean) isFileExistsMethod.invoke(null, tempDir.resolve("nonexistent.txt").toString());
            assertFalse(notExists);
        } catch (Exception e) {
            fail("Error al verificar existencia de archivo: " + e.getMessage());
        }
    }
}

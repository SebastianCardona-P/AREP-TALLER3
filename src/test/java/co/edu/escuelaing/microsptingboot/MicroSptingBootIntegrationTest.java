package co.edu.escuelaing.microsptingboot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import co.edu.escuelaing.microsptingboot.httpServer.HttpServer;
import co.edu.escuelaing.microsptingboot.controller.GreetingController;
import co.edu.escuelaing.microsptingboot.controller.CalcuteController;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Pruebas de integración para el microframework completo
 *
 * @author Sebastian
 */
public class MicroSptingBootIntegrationTest {

    @BeforeEach
    public void setUp() {
        // Limpiar servicios antes de cada prueba
        HttpServer.services.clear();
    }

    @Test
    public void testFrameworkInitialization() {
        // Simular inicialización del framework
        String[] args = {};
        HttpServer.loadServices(args);

        // Verificar que se han cargado todos los servicios esperados
        Map<String, Method> services = HttpServer.services;

        assertFalse(services.isEmpty(), "El framework debe cargar al menos un servicio");

        // Verificar servicios de GreetingController
        assertTrue(services.containsKey("/greeting"), "Debe existir el endpoint /greeting");
        assertTrue(services.containsKey("/hello"), "Debe existir el endpoint /hello");
        assertTrue(services.containsKey("/status"), "Debe existir el endpoint /status");
        assertTrue(services.containsKey("/welcome"), "Debe existir el endpoint /welcome");

        // Verificar servicios de CalcuteController
        assertTrue(services.containsKey("/calculate/suma"), "Debe existir el endpoint /calculate/suma");
        assertTrue(services.containsKey("/calculate/resta"), "Debe existir el endpoint /calculate/resta");
    }

    @Test
    public void testEndToEndCalculatorFunctionality() {
        // Cargar servicios
        HttpServer.loadServices(new String[]{});

        // Verificar que las operaciones matemáticas funcionan correctamente
        String sumaResult = CalcuteController.calculate("15", "25");
        assertEquals("La suma de 15 + 25 = 40", sumaResult);

        String restaResult = CalcuteController.resta("50", "20");
        assertEquals("La resta de 50 - 20 = 30", restaResult);

        // Verificar manejo de errores
        String errorResult = CalcuteController.calculate("abc", "123");
        assertEquals("Error: Los parámetros deben ser números válidos", errorResult);
    }

    @Test
    public void testEndToEndGreetingFunctionality() {
        // Cargar servicios
        HttpServer.loadServices(new String[]{});

        // Verificar funcionalidad de saludos
        String greeting = GreetingController.greeting("Usuario");
        assertEquals("Hola Usuario", greeting);

        String hello = GreetingController.helloService("Ana", "30");
        assertEquals("Hola hola Ana, tienes 30 años", hello);

        String status = GreetingController.status();
        assertEquals("El servidor está funcionando correctamente", status);

        String welcome = GreetingController.welcome("Carlos", "25", "Medellín");
        assertEquals("Bienvenido Carlos, tienes 25 años y vives en Medellín", welcome);
    }

    @Test
    public void testServiceMethodMapping() {
        HttpServer.loadServices(new String[]{});
        Map<String, Method> services = HttpServer.services;

        // Verificar que los métodos están correctamente mapeados
        Method greetingMethod = services.get("/greeting");
        assertNotNull(greetingMethod);
        assertEquals("greeting", greetingMethod.getName());

        Method calculateMethod = services.get("/calculate/suma");
        assertNotNull(calculateMethod);
        assertEquals("calculate", calculateMethod.getName());

        Method restaMethod = services.get("/calculate/resta");
        assertNotNull(restaMethod);
        assertEquals("resta", restaMethod.getName());
    }

    @Test
    public void testFrameworkWithSpecificController() {
        // Probar carga de un controlador específico
        String[] args = {"co.edu.escuelaing.microsptingboot.controller.GreetingController"};
        HttpServer.loadServices(args);

        Map<String, Method> services = HttpServer.services;

        // Solo deben estar los servicios del GreetingController
        assertTrue(services.containsKey("/greeting"));
        assertTrue(services.containsKey("/hello"));
        assertTrue(services.containsKey("/status"));
        assertTrue(services.containsKey("/welcome"));

        // No deben estar los servicios del CalcuteController si se carga específicamente otro
        // (esto depende de la implementación actual que carga automáticamente)
    }

    @Test
    public void testFrameworkRobustness() {
        // Probar con argumentos inválidos
        String[] invalidArgs = {"com.invalid.Controller"};

        // No debe fallar, simplemente no cargar el controlador inválido
        assertDoesNotThrow(() -> {
            HttpServer.loadServices(invalidArgs);
        });

        // Probar con argumentos null
        assertDoesNotThrow(() -> {
            HttpServer.loadServices(null);
        });

        // Probar con array vacío
        assertDoesNotThrow(() -> {
            HttpServer.loadServices(new String[]{});
        });
    }

    @Test
    public void testStaticFilesIntegration() {
        // Probar configuración de archivos estáticos
        assertDoesNotThrow(() -> {
            HttpServer.staticfiles("/public");
        });

        assertDoesNotThrow(() -> {
            HttpServer.staticfiles("static");
        });
    }

    @Test
    public void testCompleteFrameworkWorkflow() {
        // Simular un flujo completo del framework

        // 1. Inicialización
        String[] args = {};
        HttpServer.loadServices(args);

        // 2. Verificar que los servicios están disponibles
        Map<String, Method> services = HttpServer.services;
        assertFalse(services.isEmpty());

        // 3. Probar funcionalidad de cada endpoint
        assertTrue(services.containsKey("/greeting"));
        assertTrue(services.containsKey("/calculate/suma"));

        // 4. Verificar que las operaciones funcionan
        String greetingResult = GreetingController.greeting("TestUser");
        assertEquals("Hola TestUser", greetingResult);

        String calculationResult = CalcuteController.calculate("10", "5");
        assertEquals("La suma de 10 + 5 = 15", calculationResult);

        // 5. Configurar archivos estáticos
        assertDoesNotThrow(() -> {
            HttpServer.staticfiles("/assets");
        });
    }

    @Test
    public void testFrameworkParameterHandling() {
        // Probar manejo de parámetros del framework

        // Con valores por defecto
        String defaultGreeting = GreetingController.greeting("World");
        assertEquals("Hola World", defaultGreeting);

        String defaultCalculation = CalcuteController.calculate("0", "0");
        assertEquals("La suma de 0 + 0 = 0", defaultCalculation);

        // Con valores personalizados
        String customWelcome = GreetingController.welcome("Juan", "35", "Cali");
        assertEquals("Bienvenido Juan, tienes 35 años y vives en Cali", customWelcome);

        // Con valores límite
        String negativeCalculation = CalcuteController.resta("-10", "-5");
        assertEquals("La resta de -10 - -5 = -5", negativeCalculation);
    }
}

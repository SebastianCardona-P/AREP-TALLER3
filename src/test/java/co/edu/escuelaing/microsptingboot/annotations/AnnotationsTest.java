package co.edu.escuelaing.microsptingboot.annotations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import co.edu.escuelaing.microsptingboot.controller.GreetingController;
import co.edu.escuelaing.microsptingboot.controller.CalcuteController;

/**
 * Pruebas unitarias para las anotaciones del framework
 *
 * @author Sebastian
 */
public class AnnotationsTest {

    @Test
    public void testRestControllerAnnotationPresent() {
        assertTrue(GreetingController.class.isAnnotationPresent(RestController.class));
        assertTrue(CalcuteController.class.isAnnotationPresent(RestController.class));
    }

    @Test
    public void testGetMappingAnnotationPresent() throws NoSuchMethodException {
        Method greetingMethod = GreetingController.class.getMethod("greeting", String.class);
        assertTrue(greetingMethod.isAnnotationPresent(GetMapping.class));

        GetMapping getMapping = greetingMethod.getAnnotation(GetMapping.class);
        assertEquals("/greeting", getMapping.value());
    }

    @Test
    public void testGetMappingValues() throws NoSuchMethodException {
        // Test GreetingController methods
        Method greetingMethod = GreetingController.class.getMethod("greeting", String.class);
        GetMapping greetingMapping = greetingMethod.getAnnotation(GetMapping.class);
        assertEquals("/greeting", greetingMapping.value());

        Method helloMethod = GreetingController.class.getMethod("helloService", String.class, String.class);
        GetMapping helloMapping = helloMethod.getAnnotation(GetMapping.class);
        assertEquals("/hello", helloMapping.value());

        Method statusMethod = GreetingController.class.getMethod("status");
        GetMapping statusMapping = statusMethod.getAnnotation(GetMapping.class);
        assertEquals("/status", statusMapping.value());

        Method welcomeMethod = GreetingController.class.getMethod("welcome", String.class, String.class, String.class);
        GetMapping welcomeMapping = welcomeMethod.getAnnotation(GetMapping.class);
        assertEquals("/welcome", welcomeMapping.value());

        // Test CalcuteController methods
        Method calculateMethod = CalcuteController.class.getMethod("calculate", String.class, String.class);
        GetMapping calculateMapping = calculateMethod.getAnnotation(GetMapping.class);
        assertEquals("/calculate/suma", calculateMapping.value());

        Method restaMethod = CalcuteController.class.getMethod("resta", String.class, String.class);
        GetMapping restaMapping = restaMethod.getAnnotation(GetMapping.class);
        assertEquals("/calculate/resta", restaMapping.value());
    }

    @Test
    public void testRequestParamAnnotation() throws NoSuchMethodException {
        Method greetingMethod = GreetingController.class.getMethod("greeting", String.class);

        // Verificar que el parámetro tiene la anotación @RequestParam
        Annotation[][] paramAnnotations = greetingMethod.getParameterAnnotations();
        assertTrue(paramAnnotations.length > 0);
        assertTrue(paramAnnotations[0].length > 0);
        assertTrue(paramAnnotations[0][0] instanceof RequestParam);

        RequestParam requestParam = (RequestParam) paramAnnotations[0][0];
        assertEquals("name", requestParam.value());
        assertEquals("World", requestParam.defaultValue());
    }

    @Test
    public void testRequestParamAnnotationValues() throws NoSuchMethodException {
        Method helloMethod = GreetingController.class.getMethod("helloService", String.class, String.class);

        Annotation[][] paramAnnotations = helloMethod.getParameterAnnotations();

        // Primer parámetro: name
        RequestParam nameParam = (RequestParam) paramAnnotations[0][0];
        assertEquals("name", nameParam.value());
        assertEquals("World", nameParam.defaultValue());

        // Segundo parámetro: age
        RequestParam ageParam = (RequestParam) paramAnnotations[1][0];
        assertEquals("age", ageParam.value());
        assertEquals("0", ageParam.defaultValue());
    }

    @Test
    public void testCalculateMethodAnnotations() throws NoSuchMethodException {
        Method calculateMethod = CalcuteController.class.getMethod("calculate", String.class, String.class);

        // Verificar anotación GetMapping
        assertTrue(calculateMethod.isAnnotationPresent(GetMapping.class));
        GetMapping getMapping = calculateMethod.getAnnotation(GetMapping.class);
        assertEquals("/calculate/suma", getMapping.value());

        // Verificar anotaciones de parámetros
        Annotation[][] paramAnnotations = calculateMethod.getParameterAnnotations();

        // Primer parámetro: a
        RequestParam aParam = (RequestParam) paramAnnotations[0][0];
        assertEquals("a", aParam.value());
        assertEquals("0", aParam.defaultValue());

        // Segundo parámetro: b
        RequestParam bParam = (RequestParam) paramAnnotations[1][0];
        assertEquals("b", bParam.value());
        assertEquals("0", bParam.defaultValue());
    }

    @Test
    public void testWelcomeMethodAnnotations() throws NoSuchMethodException {
        Method welcomeMethod = GreetingController.class.getMethod("welcome", String.class, String.class, String.class);

        Annotation[][] paramAnnotations = welcomeMethod.getParameterAnnotations();

        // Primer parámetro: name
        RequestParam nameParam = (RequestParam) paramAnnotations[0][0];
        assertEquals("name", nameParam.value());
        assertEquals("Usuario", nameParam.defaultValue());

        // Segundo parámetro: age
        RequestParam ageParam = (RequestParam) paramAnnotations[1][0];
        assertEquals("age", ageParam.value());
        assertEquals("0", ageParam.defaultValue());

        // Tercer parámetro: city
        RequestParam cityParam = (RequestParam) paramAnnotations[2][0];
        assertEquals("city", cityParam.value());
        assertEquals("Ciudad Desconocida", cityParam.defaultValue());
    }

    @Test
    public void testAnnotationRetentionPolicy() {
        // Verificar que las anotaciones están disponibles en runtime
        RestController restController = GreetingController.class.getAnnotation(RestController.class);
        assertNotNull(restController);

        try {
            Method greetingMethod = GreetingController.class.getMethod("greeting", String.class);
            GetMapping getMapping = greetingMethod.getAnnotation(GetMapping.class);
            assertNotNull(getMapping);

            Annotation[][] paramAnnotations = greetingMethod.getParameterAnnotations();
            assertTrue(paramAnnotations[0][0] instanceof RequestParam);
        } catch (NoSuchMethodException e) {
            fail("Method not found: " + e.getMessage());
        }
    }

    @Test
    public void testMethodsWithoutAnnotations() throws NoSuchMethodException {
        // Verificar que métodos sin anotaciones no las tienen
        Class<?> objectClass = Object.class;
        Method toStringMethod = objectClass.getMethod("toString");

        assertFalse(toStringMethod.isAnnotationPresent(GetMapping.class));
        assertFalse(toStringMethod.isAnnotationPresent(RestController.class));
    }
}

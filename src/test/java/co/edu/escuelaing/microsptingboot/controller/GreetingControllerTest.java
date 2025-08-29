package co.edu.escuelaing.microsptingboot.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para GreetingController
 *
 * @author Sebastian
 */
public class GreetingControllerTest {

    @Test
    public void testGreetingWithDefaultName() {
        String result = GreetingController.greeting("World");
        assertEquals("Hola World", result);
    }

    @Test
    public void testGreetingWithCustomName() {
        String result = GreetingController.greeting("Sebastian");
        assertEquals("Hola Sebastian", result);
    }

    @Test
    public void testGreetingWithEmptyName() {
        String result = GreetingController.greeting("");
        assertEquals("Hola ", result);
    }

    @Test
    public void testGreetingWithNullName() {
        String result = GreetingController.greeting(null);
        assertEquals("Hola null", result);
    }

    @Test
    public void testHelloServiceWithDefaultValues() {
        String result = GreetingController.helloService("World", "0");
        assertEquals("Hola hola World, tienes 0 años", result);
    }

    @Test
    public void testHelloServiceWithCustomValues() {
        String result = GreetingController.helloService("Sebastian", "25");
        assertEquals("Hola hola Sebastian, tienes 25 años", result);
    }

    @Test
    public void testHelloServiceWithEmptyValues() {
        String result = GreetingController.helloService("", "");
        assertEquals("Hola hola , tienes  años", result);
    }

    @Test
    public void testHelloServiceWithNullValues() {
        String result = GreetingController.helloService(null, null);
        assertEquals("Hola hola null, tienes null años", result);
    }

    @Test
    public void testStatus() {
        String result = GreetingController.status();
        assertEquals("El servidor está funcionando correctamente", result);
    }

    @Test
    public void testWelcomeWithDefaultValues() {
        String result = GreetingController.welcome("Usuario", "0", "Ciudad Desconocida");
        assertEquals("Bienvenido Usuario, tienes 0 años y vives en Ciudad Desconocida", result);
    }

    @Test
    public void testWelcomeWithCustomValues() {
        String result = GreetingController.welcome("María", "30", "Bogotá");
        assertEquals("Bienvenido María, tienes 30 años y vives en Bogotá", result);
    }

    @Test
    public void testWelcomeWithEmptyValues() {
        String result = GreetingController.welcome("", "", "");
        assertEquals("Bienvenido , tienes  años y vives en ", result);
    }

    @Test
    public void testWelcomeWithNullValues() {
        String result = GreetingController.welcome(null, null, null);
        assertEquals("Bienvenido null, tienes null años y vives en null", result);
    }

    @Test
    public void testWelcomeWithSpecialCharacters() {
        String result = GreetingController.welcome("José María", "25", "San José");
        assertEquals("Bienvenido José María, tienes 25 años y vives en San José", result);
    }
}

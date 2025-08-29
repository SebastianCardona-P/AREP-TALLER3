package co.edu.escuelaing.microsptingboot.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para CalcuteController
 *
 * @author Sebastian
 */
public class CalcuteControllerTest {

    @Test
    public void testCalculateSumaWithValidNumbers() {
        String result = CalcuteController.calculate("5", "3");
        assertEquals("La suma de 5 + 3 = 8", result);
    }

    @Test
    public void testCalculateSumaWithDefaultValues() {
        String result = CalcuteController.calculate("0", "0");
        assertEquals("La suma de 0 + 0 = 0", result);
    }

    @Test
    public void testCalculateSumaWithNegativeNumbers() {
        String result = CalcuteController.calculate("-5", "3");
        assertEquals("La suma de -5 + 3 = -2", result);
    }

    @Test
    public void testCalculateSumaWithBothNegativeNumbers() {
        String result = CalcuteController.calculate("-10", "-5");
        assertEquals("La suma de -10 + -5 = -15", result);
    }

    @Test
    public void testCalculateSumaWithLargeNumbers() {
        String result = CalcuteController.calculate("1000000", "2000000");
        assertEquals("La suma de 1000000 + 2000000 = 3000000", result);
    }

    @Test
    public void testCalculateSumaWithInvalidFirstParameter() {
        String result = CalcuteController.calculate("abc", "5");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }

    @Test
    public void testCalculateSumaWithInvalidSecondParameter() {
        String result = CalcuteController.calculate("5", "xyz");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }

    @Test
    public void testCalculateSumaWithBothInvalidParameters() {
        String result = CalcuteController.calculate("abc", "xyz");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }

    @Test
    public void testCalculateSumaWithEmptyParameters() {
        String result = CalcuteController.calculate("", "");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }

    @Test
    public void testCalculateSumaWithFloatingPointNumbers() {
        String result = CalcuteController.calculate("5.5", "3.2");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }

    @Test
    public void testRestaWithValidNumbers() {
        String result = CalcuteController.resta("10", "3");
        assertEquals("La resta de 10 - 3 = 7", result);
    }

    @Test
    public void testRestaWithDefaultValues() {
        String result = CalcuteController.resta("0", "0");
        assertEquals("La resta de 0 - 0 = 0", result);
    }

    @Test
    public void testRestaWithNegativeResult() {
        String result = CalcuteController.resta("3", "10");
        assertEquals("La resta de 3 - 10 = -7", result);
    }

    @Test
    public void testRestaWithNegativeNumbers() {
        String result = CalcuteController.resta("-5", "-3");
        assertEquals("La resta de -5 - -3 = -2", result);
    }

    @Test
    public void testRestaWithLargeNumbers() {
        String result = CalcuteController.resta("5000000", "2000000");
        assertEquals("La resta de 5000000 - 2000000 = 3000000", result);
    }

    @Test
    public void testRestaWithInvalidFirstParameter() {
        String result = CalcuteController.resta("abc", "5");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }

    @Test
    public void testRestaWithInvalidSecondParameter() {
        String result = CalcuteController.resta("5", "xyz");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }

    @Test
    public void testRestaWithBothInvalidParameters() {
        String result = CalcuteController.resta("abc", "xyz");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }

    @Test
    public void testRestaWithEmptyParameters() {
        String result = CalcuteController.resta("", "");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }

    @Test
    public void testRestaWithFloatingPointNumbers() {
        String result = CalcuteController.resta("10.5", "3.2");
        assertEquals("Error: Los parámetros deben ser números válidos", result);
    }
}

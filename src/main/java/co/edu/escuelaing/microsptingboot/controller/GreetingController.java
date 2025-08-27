package co.edu.escuelaing.microsptingboot.controller;

import co.edu.escuelaing.microsptingboot.annotations.GetMapping;
import co.edu.escuelaing.microsptingboot.annotations.RequestParam;
import co.edu.escuelaing.microsptingboot.annotations.RestController;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author Sebastian
 */
@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public static String greeting() {
		return "Hola Mundo! desde controller";
	}
}

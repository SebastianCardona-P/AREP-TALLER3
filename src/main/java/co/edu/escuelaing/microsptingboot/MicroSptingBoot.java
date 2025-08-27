/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package co.edu.escuelaing.microsptingboot;

import co.edu.escuelaing.microsptingboot.httpServer.HttpServer;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author Sebastian
 */
public class MicroSptingBoot {

    public static void main(String[] args) throws IOException, URISyntaxException {
        System.out.println("Starting MicroSpringBoot");
        
        HttpServer.startServer(args);
    }
}

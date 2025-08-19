package dev.bakr.readiction_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReadictionBackendApplication {
    public static void main(String[] args) {
        /* Start the embedded servlet container (default is Tomcat). Then register the DispatcherServlet with this container
        (Spring’s front controller that routes requests to appropriate controller and handles the Spring MVC workflow)
        */
        SpringApplication.run(ReadictionBackendApplication.class, args);
    }
}

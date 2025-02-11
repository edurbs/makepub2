package com.github.edurbs.makepub2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.edurbs.makepub2.app.usecase.meps.ConvertBibles;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "makepub2")
@Push
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static void convertBibles() {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        ConvertBibles convertBibles = context.getBean(ConvertBibles.class);
        convertBibles.execute();
        context.close(); 
    }

}

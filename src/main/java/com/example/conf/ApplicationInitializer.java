package com.example.conf;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class ApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(SpringConfig.class);

		DispatcherServlet servlet = new DispatcherServlet(rootContext);

		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", servlet);
		dispatcher.setLoadOnStartup(0);
		dispatcher.addMapping("/");
	}
}

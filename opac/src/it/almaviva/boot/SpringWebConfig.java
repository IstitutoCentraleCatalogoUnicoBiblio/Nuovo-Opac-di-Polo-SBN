/*******************************************************************************
 * Copyright (C) 2019 ICCU - Istituto Centrale per il Catalogo Unico
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package it.almaviva.boot;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import it.almaviva.utils.Costanti;


//Classe di avvio di spring occhio sempre al package nel component scan
@Configuration
@Import(PersistenceConfig.class)
@EnableWebMvc
@ComponentScan({ Costanti.scanner_package })
public class SpringWebConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {

		this.applicationContext = applicationContext;
	}
	@SuppressWarnings("unused")
	private ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver bean = new InternalResourceViewResolver();

	    bean.setPrefix("/");
	    bean.setSuffix(".jsp");
	    return bean;
		
	}
	 @Bean(name = "multipartResolver")
	    public CommonsMultipartResolver getResolver() throws IOException {
	        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
	        // no limit
	        resolver.setMaxUploadSize(-1);
	        return resolver;
	    }
/*
	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setEnableSpringELCompiler(true);
		engine.setTemplateResolver(templateResolver());
		return engine;
	}

	private ITemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setApplicationContext(applicationContext);
		resolver.setPrefix("/WEB-INF/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		return resolver;
	}
*/
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	
		registry.addResourceHandler("/*").addResourceLocations("/resources/");
	
	}
	
	@Bean
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
	    Jackson2ObjectMapperBuilder jsonMapperBuilder = new Jackson2ObjectMapperBuilder();
	    return jsonMapperBuilder;
	}
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*");
	}


}

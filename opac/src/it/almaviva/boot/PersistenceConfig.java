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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.almaviva.boot.appVersion.Version;
import it.almaviva.boot.appVersion.VersioningReader;
import it.almaviva.utils.Costanti;

//Classe di configurazione Database occhio sempre al package
@Configuration
@EnableTransactionManagement
@ComponentScan({
	Costanti.scanner_package
	})
public class PersistenceConfig {
	
	private Logger log = LoggerFactory.getLogger(PersistenceConfig.class);

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		return em;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		Properties p = new Properties();
		Version version = VersioningReader.getVersion();

		log.info("-------------------------------------------------------------------------------");
		log.info("------------------------------- STARTING OPAC ---------------------------------");
		try {
			p.load(new FileInputStream(System.getProperty(Costanti.user_home) + Costanti.file_properties_opac_installazione));
			log.info("-->Versione Opac: " + version.getVersion());
			log.info("-->Release: " + version.getLast_release());
			log.info("-------------------------------------------------------------------------------");
			
			log.info("lettura conf. db da file");
			
//			p.load(this.getClass().getResourceAsStream("/dbconfig.properties"));
			ds.setDriverClassName(p.getProperty(Costanti.db_driver));
			ds.setUrl(p.getProperty(Costanti.db_url));
			ds.setUsername(p.getProperty(Costanti.db_user));
			ds.setPassword(p.getProperty(Costanti.db_psw));
			ds.setSchema(p.getProperty(Costanti.db_schema));
		} catch (IOException e) {
		
			log.info("Erroooor lettura " + Costanti.file_properties_opac_installazione + " file db: ", e);
		}
		
		return ds;
	}

	@Bean
	public PlatformTransactionManager txManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

}

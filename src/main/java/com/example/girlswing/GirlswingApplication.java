package com.example.girlswing;

import com.example.girlswing.UI.LoginForm;
import com.example.girlswing.UI.LoginPage;
import mdlaf.MaterialLookAndFeel;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		WebMvcAutoConfiguration.class,
        SpringDataWebAutoConfiguration.class,
        EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
        WebSocketServletAutoConfiguration.class})
@EnableTransactionManagement
public class GirlswingApplication{

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new MaterialLookAndFeel());
		}
		catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace ();
		}
		ConfigurableApplicationContext context =
				new SpringApplicationBuilder(GirlswingApplication.class).headless(false).run(args);


		EventQueue.invokeLater(() -> {
			LoginForm app = context.getBean(LoginForm.class);
			app.setSize(600,400);
			app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			app.setLocationRelativeTo(null);
			app.setTitle("Rocketgirls");
			app.setVisible(true);
		});
	}
}

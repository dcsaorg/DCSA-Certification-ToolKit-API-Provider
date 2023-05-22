package org.dcsa.api.provider.ctk;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CtkApplication {

	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(CtkApplication.class, args);
		// Generate the URL and display it in the console
		String url = "http://localhost:9000/bookingView";
		System.out.println("Application started. Open the following URL in your browser: " + url);
	}

	public static void restart() {
		ApplicationArguments args = context.getBean(ApplicationArguments.class);
		Thread thread = new Thread(() -> {
			context.close();
			context = SpringApplication.run(CtkApplication.class, args.getSourceArgs());
		});
		thread.setDaemon(false);
		thread.start();
	}

}

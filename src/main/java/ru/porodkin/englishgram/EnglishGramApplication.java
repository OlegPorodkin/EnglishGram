package ru.porodkin.englishgram;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.ApiContextInitializer;
import ru.porodkin.englishgram.config.ApplicationProperties;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableConfigurationProperties({ApplicationProperties.class})
public class EnglishGramApplication {

    private final static Logger log = LoggerFactory.getLogger(EnglishGramApplication.class);

    public static void main(String[] args) {
        ApiContextInitializer.init();

        SpringApplication app = new SpringApplication(EnglishGramApplication.class);
        Map<String, Object> props = new HashMap<>();
        props.put("spring.profiles.active", "dev");
        app.setDefaultProperties(props);
        Environment env = app.run(args).getEnvironment();

        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String port = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }

        log.info("\n------------------------------------------------------------\n\t" +
                "Application is running! Access URLs:\n\t" +
                "Local: \t\t{}://localhost:{}{}\n\t"+
                "External: \t{}://{}:{}{}\n\t" +
                "Profile(s) \t{}\n------------------------------------------------------------",
                protocol,
                port,
                contextPath,
                protocol,
                hostAddress,
                port,
                contextPath,
                env.getActiveProfiles());

//        ConfigurableApplicationContext context = SpringApplication.run(EnglishGramApplication.class, args);
    }
}

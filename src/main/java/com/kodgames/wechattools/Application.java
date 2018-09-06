package com.kodgames.wechattools;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigFile;
import com.ctrip.framework.apollo.ConfigFileChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.core.enums.ConfigFileFormat;
import com.ctrip.framework.apollo.model.ConfigFileChangeEvent;
import com.ctrip.framework.foundation.Foundation;
import com.kodgames.wechattools.configurer.SwaggerConfigurer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class Application {
    private static final String ENV = "env";
    private static final String DEV = "dev";
    private static final String PRO = "pro";
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private Config config = ConfigService.getAppConfig();
    public static void main(String[] args) {
        if (!setSystemProperties()) {
            return;
        }
        String channel = Foundation.server().getEnvType();
        SwaggerConfigurer.active = DEV.equalsIgnoreCase(channel);
        SpringApplication.run(Application.class, args);
        ConfigFile logbackFile = ConfigService.getConfigFile("logback", ConfigFileFormat.XML);
        if (!logbackFile.hasContent()) {
            logger.error("No config file content found for " + logbackFile.getNamespace());
        } else {
            try {
                initLogback(IOUtils.toInputStream(logbackFile.getContent(), StandardCharsets.UTF_8.name()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logbackFile.addChangeListener(new ConfigFileChangeListener() {
            @Override
            public void onChange(ConfigFileChangeEvent changeEvent) {
                logger.info("logback changed....");
                try {
                    initLogback(IOUtils.toInputStream(changeEvent.getNewValue(), StandardCharsets.UTF_8.name()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static void initLogback(InputStream inputStream) {
        logger.debug("init logback with InputStream");
        // assume SLF4J is bound to logback in the current environment
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            // Call context.reset() to clear any previous configuration, e.g. default
            // configuration. For multi-step configuration, omit calling context.reset().
            context.reset();
            configurator.doConfigure(inputStream);
        } catch (JoranException je) {
            je.printStackTrace();
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(config.getIntProperty("server.port", 9010));
        return factory;
    }


    private static boolean setSystemProperties() {
        Properties properties = new Properties();
        Resource resource = new ClassPathResource("apollo.properties");
        try {
            properties.load(resource.getInputStream());
            String env = properties.getProperty("env");
            String meta = properties.getProperty("meta");
            String cluster = properties.getProperty("cluster");
            if (StringUtils.isAnyEmpty(env, meta, cluster)) {
                logger.error("can not get properties from apollo.properties");
                return false;
            }

            if (!env.equals(DEV) && !env.equals(PRO)) {
                logger.error("env properties is incorrect");
                return false;
            }

            System.setProperty("env", env);
            System.setProperty(env + "_meta", meta);
            System.setProperty("apollo.cluster", cluster);
            return true;
        } catch (IOException e) {
            logger.error("get properties from apollo.properties failed, {}", e);
            return false;
        }
    }
}



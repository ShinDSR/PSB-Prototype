package shin_nc.psb_test.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import io.github.cdimascio.dotenv.Dotenv;

public class DotenvApplicationListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent>{

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        Dotenv dotenv = Dotenv.load();
        Map<String, Object> envMap = new HashMap<>();
        dotenv.entries().forEach(entry -> envMap.put(entry.getKey(), entry.getValue()));

        ConfigurableEnvironment environment = event.getEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("dotenv", envMap));
    }
}

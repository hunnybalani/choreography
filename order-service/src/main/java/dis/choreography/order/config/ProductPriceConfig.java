package dis.choreography.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ProductPriceConfig {

    // product price map
    @Bean
    public Map<Integer, Integer> productPrice() {
        return Map.of(
                1, 100,
                2, 500,
                3, 1200
        );
    };

}

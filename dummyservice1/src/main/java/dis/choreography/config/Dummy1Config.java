package dis.choreography.config;

import dis.choreography.events.inventory.InventoryEvent;
import dis.choreography.service.Dummy1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class Dummy1Config {

  @Autowired
  private Dummy1Service service;

  @Bean
  public Function<Flux<InventoryEvent>, Flux<InventoryEvent>> dummy1Processor() {
    return flux -> flux.flatMap(this::processDummy);
  }

  private Mono<InventoryEvent> processDummy(InventoryEvent event) {
    return Mono.fromSupplier(() -> this.service.processDummy(event));
  }

}


package dis.choreography.service;

import dis.choreography.events.inventory.InventoryEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Dummy2Service {

  @Transactional
  public InventoryEvent processDummy(InventoryEvent inventoryEvent) {
    System.out.println("Dummy Service 2");
    System.out.println(inventoryEvent);
    return inventoryEvent;
  }
}

package dis.choreography.service;

import dis.choreography.events.inventory.InventoryEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Dummy3Service {

  @Transactional
  public InventoryEvent processDummy(InventoryEvent inventoryEvent) {
    System.out.println("Dummy Service 3");
    System.out.println(inventoryEvent);
    return inventoryEvent;
  }
}

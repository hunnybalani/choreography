package dis.choreography.inventory.service;

import dis.choreography.dto.InventoryDto;
import dis.choreography.events.inventory.InventoryEvent;
import dis.choreography.events.inventory.InventoryStatus;
import dis.choreography.events.order.OrderEvent;
import dis.choreography.inventory.entity.OrderInventoryConsumption;
import dis.choreography.inventory.repository.OrderInventoryConsumptionRepository;
import dis.choreography.inventory.repository.OrderInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    @Autowired
    private OrderInventoryRepository inventoryRepository;

    @Autowired
    private OrderInventoryConsumptionRepository consumptionRepository;

    @Transactional
    public InventoryEvent newOrderInventory(OrderEvent orderEvent){
        InventoryDto dto = InventoryDto.of(orderEvent.getPurchaseOrder().getOrderId(), orderEvent.getPurchaseOrder().getProductId());
        return inventoryRepository.findById(orderEvent.getPurchaseOrder().getProductId())
                .filter(i -> i.getAvailableInventory() > 0 )
                .map(i -> {
                    i.setAvailableInventory(i.getAvailableInventory() - 1);
                    consumptionRepository.save(OrderInventoryConsumption.of(orderEvent.getPurchaseOrder().getOrderId(), orderEvent.getPurchaseOrder().getProductId(), 1));
                    return new InventoryEvent(dto, InventoryStatus.RESERVED);
                })
                .orElse(new InventoryEvent(dto, InventoryStatus.REJECTED));
    }

    @Transactional
    public void cancelOrderInventory(OrderEvent orderEvent){
        consumptionRepository.findById(orderEvent.getPurchaseOrder().getOrderId())
                .ifPresent(ci -> {
                    inventoryRepository.findById(ci.getProductId())
                            .ifPresent(i ->
                                i.setAvailableInventory(i.getAvailableInventory() + ci.getQuantityConsumed())
                            );
                    consumptionRepository.delete(ci);
                });
    }

}

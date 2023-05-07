package dis.choreography.order.config;

import dis.choreography.events.inventory.InventoryStatus;
import dis.choreography.events.order.OrderStatus;
import dis.choreography.events.payment.PaymentStatus;
import dis.choreography.order.entity.PurchaseOrder;
import dis.choreography.order.repository.PurchaseOrderRepository;
import dis.choreography.order.service.OrderCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class OrderStatusUpdateEventHandler {

    @Autowired
    private PurchaseOrderRepository repository;

    @Autowired
    private OrderCommandService orderCommandService;

    @Transactional
    public void updateOrder(final UUID id, Consumer<PurchaseOrder> consumer){
        this.repository
                .findById(id)
                .ifPresent(consumer.andThen(this::updateOrder));

    }

    private void updateOrder(PurchaseOrder purchaseOrder){
        if(Objects.isNull(purchaseOrder.getInventoryStatus()) || Objects.isNull(purchaseOrder.getPaymentStatus()))
            return;
        var isComplete = PaymentStatus.RESERVED.equals(purchaseOrder.getPaymentStatus()) && InventoryStatus.RESERVED.equals(purchaseOrder.getInventoryStatus());
        var orderStatus = isComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        purchaseOrder.setOrderStatus(orderStatus);
        this.orderCommandService.raiseOrderEvent(purchaseOrder, orderStatus);
    }

}

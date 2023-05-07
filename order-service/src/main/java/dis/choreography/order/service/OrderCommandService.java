package dis.choreography.order.service;

import dis.choreography.dto.OrderRequestDto;
import dis.choreography.dto.PurchaseOrderDto;
import dis.choreography.events.order.OrderEvent;
import dis.choreography.events.order.OrderStatus;
import dis.choreography.order.entity.PurchaseOrder;
import dis.choreography.order.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderCommandService {

    @Autowired
    private Map<Integer, Integer> productPriceMap;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private Sinks.Many<OrderEvent> orderSink;

    private long start, finish, responseTime;

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDTO){
        start = System.currentTimeMillis();
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.save(this.dtoToEntity(orderRequestDTO));
        this.raiseOrderEvent(purchaseOrder, OrderStatus.ORDER_CREATED);
        return purchaseOrder;
    }

    private PurchaseOrder dtoToEntity(final OrderRequestDto dto){
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(dto.getOrderId());
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(productPriceMap.get(purchaseOrder.getProductId()));
        return purchaseOrder;
    }

    public void raiseOrderEvent(final PurchaseOrder purchaseOrder, OrderStatus orderStatus){
        var dto = PurchaseOrderDto.of(
            purchaseOrder.getId(),
            purchaseOrder.getProductId(),
            purchaseOrder.getPrice(),
            purchaseOrder.getUserId()
        );
        var orderEvent = new OrderEvent(dto, orderStatus);
        this.orderSink.tryEmitNext(orderEvent);
        System.out.println(orderStatus);
        if(orderStatus != OrderStatus.ORDER_CREATED) {
            finish = System.currentTimeMillis();
            responseTime = finish - start;
            System.out.println("Response Time: " + responseTime + " milliseconds");
        }
    }

}

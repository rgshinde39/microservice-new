package com.example.orderservice.service;

import com.example.orderservice.event.OrderPlacedEvent;
import com.example.orderservice.dto.OrderLineItemDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderLineItem;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String createOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderLineItemList(orderRequest.getOrderLineItemDtos().stream().map(this::mapToEntity).toList());

        Boolean isInStock = webClientBuilder.build().get().uri("http://inventory-service/api/inventory/"+orderRequest.getOrderLineItemDtos().get(0).getSkuCode()).retrieve().bodyToMono(Boolean.class).block();
        log.info("is product in stock {}", isInStock);
        if(isInStock) {
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "order created";
        } else {
            throw new IllegalArgumentException("product not in stock");
        }
    }

    private OrderLineItem mapToEntity(OrderLineItemDto dto) {
        OrderLineItem entity = new OrderLineItem();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}

package com.gyp.orderservice.entities;

import java.io.Serial;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gyp.common.entities.AbstractEntity;
import com.gyp.common.enums.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ORDERS")
public class OrderEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = 3812126953716399075L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(name = "event_id")
	private String eventId;

	@Column(name = "customer_email")
	private String customerEmail;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column(name = "total_amount")
	private Double totalAmount;

	@JsonIgnore
	@OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL)
	private List<OrderDetailEntity> orderDetailEntityList;
}

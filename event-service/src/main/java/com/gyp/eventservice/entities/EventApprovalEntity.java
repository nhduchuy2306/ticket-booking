package com.gyp.eventservice.entities;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.gyp.common.enums.event.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Table(name = "EVENTAPPROVAL")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventApprovalEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -9093127325388435905L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private ApprovalStatus status;

	@Column(name = "approved_by")
	private String approvedBy;

	@Column(name = "approval_date")
	private LocalDateTime approvalDate;

	@Column(name = "rejection_reason")
	private String rejectionReason;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private EventEntity eventEntity;
}

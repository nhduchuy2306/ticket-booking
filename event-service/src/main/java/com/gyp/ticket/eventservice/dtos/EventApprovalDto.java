package com.gyp.ticket.eventservice.dtos;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventApprovalDto {
	private String id;
	private ApprovalStatus status;
	private String approvedBy;
	private LocalDateTime approvalDate;
	private String rejectionReason;
	private String eventId;
}

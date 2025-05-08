package com.gyp.eventservice.dtos.eventapproval;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventApprovalRequestDto {
	private ApprovalStatus status;
	private String approvedBy;
	private LocalDateTime approvalDate;
	private String rejectionReason;
	private String eventId;
}

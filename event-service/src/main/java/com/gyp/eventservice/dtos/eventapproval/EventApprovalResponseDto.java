package com.gyp.eventservice.dtos.eventapproval;

import java.time.LocalDateTime;

import com.gyp.common.enums.event.ApprovalStatus;
import com.gyp.eventservice.dtos.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EventApprovalResponseDto extends AbstractDto {
	private String id;
	private ApprovalStatus status;
	private String approvedBy;
	private LocalDateTime approvalDate;
	private String rejectionReason;

	// Optional
	//	private EventSummaryDto event;
}

package com.gyp.eventservice.services;

import java.util.List;

import com.gyp.eventservice.dtos.eventapproval.EventApprovalResponseDto;

public interface EventApprovalService {
	EventApprovalResponseDto submitForApproval(String eventId);

	EventApprovalResponseDto approveEvent(String eventId, String approvedBy);

	EventApprovalResponseDto rejectEvent(String eventId, String rejectedBy, String reason);

	List<EventApprovalResponseDto> getPendingApprovals();

	EventApprovalResponseDto getApprovalStatus(String eventId);
}

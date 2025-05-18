package com.gyp.salechannelservice.services;

import java.util.List;
import java.util.Optional;

import com.gyp.salechannelservice.dtos.boxoffice.BoxOfficeRequestDto;
import com.gyp.salechannelservice.dtos.boxoffice.BoxOfficeResponseDto;

public interface BoxOfficeService {
	BoxOfficeResponseDto createBoxOffice(BoxOfficeRequestDto boxOffice, String channelId);

	BoxOfficeResponseDto updateBoxOffice(String id, BoxOfficeRequestDto boxOffice);

	Optional<BoxOfficeResponseDto> getBoxOfficeById(String id);

	Optional<BoxOfficeResponseDto> getBoxOfficeBySaleChannelId(String channelId);

	List<BoxOfficeResponseDto> getAllBoxOffices();

	List<BoxOfficeResponseDto> getBoxOfficesByStaffMember(String staffId);

	void addStaffMember(String boxOfficeId, String staffId);

	void removeStaffMember(String boxOfficeId, String staffId);

	void addTerminal(String boxOfficeId, String terminalId);

	void removeTerminal(String boxOfficeId, String terminalId);

	void deleteBoxOffice(String id);
}

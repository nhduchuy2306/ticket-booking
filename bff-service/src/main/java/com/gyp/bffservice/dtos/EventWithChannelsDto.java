package com.gyp.bffservice.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventWithChannelsDto {
	private Object events;
	private List<Object> saleChannels;
}

package com.gyp.salechannelservice.controllers;

import com.gyp.common.controllers.AbstractController;
import com.gyp.salechannelservice.dtos.salechannel.SaleChannelRequestDto;
import com.gyp.salechannelservice.services.SaleChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SaleChannelController.SALE_CHANNEL_CONTROLLER_PATH)
@RequiredArgsConstructor
public class SaleChannelController extends AbstractController {
	public static final String SALE_CHANNEL_CONTROLLER_PATH = "/sale-channels";

	private final SaleChannelService saleChannelService;

	@GetMapping
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(saleChannelService.getAllSaleChannels());
	}

	@GetMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> getById(@PathVariable(ID_PARAM) String id) {
		return ResponseEntity.ok(saleChannelService.getSaleChannelById(id));
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody SaleChannelRequestDto requestDto) {
		return ResponseEntity.ok(saleChannelService.createSaleChannel(requestDto));
	}

	@PutMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> update(@PathVariable(ID_PARAM) String id, @RequestBody SaleChannelRequestDto entity) {
		return ResponseEntity.ok(saleChannelService.updateSaleChannel(id, entity));
	}

	@DeleteMapping("/{" + ID_PARAM + "}")
	public ResponseEntity<?> delete(@PathVariable(ID_PARAM) String id) {
		saleChannelService.deleteSaleChannel(id);
		return ResponseEntity.noContent().build();
	}
}

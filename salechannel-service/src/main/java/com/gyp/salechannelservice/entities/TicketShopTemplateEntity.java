package com.gyp.salechannelservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// TicketShopTemplate for reusable ticket shop designs
@Entity
@Table(name = "TICKETSHOPTEMPLATES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TicketShopTemplateEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -2101983754643028169L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(length = 1000)
	private String description;

	@Column(name = "primary_color")
	private String primaryColor;

	@Column(name = "secondary_color")
	private String secondaryColor;

	@Column(name = "header_style")
	private String headerStyle;

	@Column(name = "footer_style")
	private String footerStyle;

	@Column(name = "event_card_style")
	private String eventCardStyle;

	@Column(name = "thumbnail")
	private String thumbnailUrl;

	@Column(name = "is_default")
	private Boolean defaultData;
}

package com.gyp.salechannelservice.entities;

import java.io.Serial;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TICKETSHOP")
@NoArgsConstructor
@AllArgsConstructor
public class TicketShopEntity extends AbstractEntity {
	@Serial
	private static final long serialVersionUID = -5778164280137371124L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id")
	private String id;

	@Column(nullable = false, unique = true)
	private String subdomain;

	@Column(name = "organizer_id", nullable = false)
	private String organizerId;

	@Column(name = "custom_domain")
	private String customDomain;

	// Shop Theme/Styling
	@Column(name = "primary_color")
	private String primaryColor;

	@Column(name = "secondary_color")
	private String secondaryColor;

	@Column(name = "logo_url")
	private String logoUrl;

	@Column(name = "banner_url")
	private String bannerUrl;

	// SEO information
	@Column(name = "meta_title")
	private String metaTitle;

	@Column(name = "meta_description", length = 500)
	private String metaDescription;

	// Contact Information
	@Column(name = "contact_email")
	private String contactEmail;

	@Column(name = "contact_phone")
	private String contactPhone;

	// Social Media Links
	@Column(name = "facebook_url")
	private String facebookUrl;

	@Column(name = "instagram_url")
	private String instagramUrl;

	@Column(name = "twitter_url")
	private String twitterUrl;

	// Shop Settings
	@Column(name = "show_sold_out_events")
	private boolean showSoldOutEvents;

	@Column(name = "allow_guest_checkout")
	private boolean allowGuestCheckout;

	@Column(name = "require_account")
	private boolean requireAccount;

	@Column(name = "terms_url")
	private String termsUrl;

	@Column(name = "privacy_url")
	private String privacyUrl;

	@OneToOne
	@JoinColumn(name = "sale_channel_id", nullable = false)
	private SaleChannelEntity saleChannelEntity;
}

package com.gyp.authservice.controllers;

import com.gyp.authservice.dtos.useraccount.UserAccountResponseDto;
import com.gyp.authservice.services.IamService;
import com.gyp.common.constants.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(IamController.IAM_CONTROLLER_PATH)
public class IamController {
	public static final String IAM_CONTROLLER_PATH = "iam";
	private static final String LOGIN_PATH = "/oauth/login";
	private static final String CODE_REQUEST_PARAM = AppConstants.QUESTION_MARK + "code" + AppConstants.EQUALS;

	private final IamService iamService;

	@GetMapping(LOGIN_PATH)
	public String oauthLogin(
			@RequestParam(name = "redirect_uri", required = false) String redirectUri,
			@RequestParam(name = "client_id", required = false) String clientId, Model model) {
		String actionUrl = "/auths/iam/oauth/login";
		model.addAttribute("redirect_uri", redirectUri);
		model.addAttribute("client_id", clientId);
		model.addAttribute("action_url", actionUrl);
		return "login";
	}

	@PostMapping(LOGIN_PATH)
	public String processLogin(
			@RequestParam(name = "username") String username,
			@RequestParam(name = "password") String password,
			@RequestParam(name = "redirect_uri") String redirectUri,
			@RequestParam(name = "client_id") String clientId, Model model) {
		try {
			log.info("Processing login request for user: {}", username);
			log.info("Redirect URI: {}", redirectUri);
			log.info("Client ID: {}", clientId);
			if(StringUtils.isEmpty(redirectUri) || StringUtils.isEmpty(redirectUri)
					|| StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientId)) {
				model.addAttribute("error", "Invalid redirect URI or client ID");
				return "login";
			}
			UserAccountResponseDto user = iamService.validateUser(username, password);
			String authCode = iamService.generateAuthCode(user, clientId);
			return "redirect:" + redirectUri + CODE_REQUEST_PARAM + authCode;
		} catch(Exception e) {
			model.addAttribute("error", "Invalid username or password");
			model.addAttribute("redirect_uri", redirectUri);
			model.addAttribute("client_id", clientId);
			return "login";
		}
	}
}

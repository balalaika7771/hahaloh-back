package org.example.hahallohback.OAuth.controller;

import base.controller.abstractions.OAuthController;
import base.service.abstractions.OAuthService;
import lombok.AllArgsConstructor;
import org.example.hahallohback.OAuth.service.OAuthHHService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/oauth/hh")
@AllArgsConstructor
public class OAuthHHController implements OAuthController {

  private final OAuthHHService oAuthHHService;
  @Override
  public OAuthService svc() {
    return oAuthHHService;
  }




}

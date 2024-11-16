package base.service.abstractions;


import base.constants.entity.StateType;


public interface OAuthService {

  String registerStateAndGetAuthUrl(Long userId);

  void saveToken(String code, String state);

  StateType stateType();
}

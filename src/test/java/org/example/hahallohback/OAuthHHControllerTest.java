package org.example.hahallohback;

import base.constants.entity.StateType;
import base.model.SimpleToken;
import java.time.LocalDateTime;
import java.util.UUID;
import org.example.hahallohback.OAuth.service.OAuthHHService;
import org.example.hahallohback.OAuth.service.UserStateService;
import org.example.hahallohback.security.request.AuthRequest;
import org.example.hahallohback.security.response.AuthResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class OAuthHHControllerTest {

  @Container
  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>("postgres:14.2")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("password");

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private UserStateService userStateService;

  @SpyBean
  private OAuthHHService oAuthHHService; // Частично мокаем сервис

  @DynamicPropertySource
  static void postgresProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
  }

  private static String token;

  @BeforeAll
  static void registerUserAndLogin(@Autowired WebTestClient webTestClient) {
    AuthRequest testUser = new AuthRequest("testuser", "testpassword");

    // Регистрируем и логиним тестового пользователя один раз для всех тестов
    webTestClient.post()
        .uri("/api/auth/register")
        .contentType(APPLICATION_JSON)
        .bodyValue(testUser)
        .exchange()
        .expectStatus().isOk();

    AuthResponse authResponse = webTestClient.post()
        .uri("/api/auth/login")
        .contentType(APPLICATION_JSON)
        .bodyValue(testUser)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AuthResponse.class)
        .returnResult()
        .getResponseBody();

    assertThat(authResponse).isNotNull();
    token = authResponse.getToken();
  }


  @Test
  void testAuthorizeRedirect() {
    // Отправляем запрос на авторизацию
    webTestClient.get()
        .uri("/api/oauth/hh/authorize")
        .header("Authorization", "Bearer " + token)
        .exchange()
        .expectStatus().isFound()  // Проверяем, что статус — 302 (редирект)
        .expectHeader().value("Location", location -> {
          assertThat(location).contains("https://hh.ru/oauth/authorize");
        });

    // Проверяем, что состояние сохранено в базе данных
    var state = userStateService.findLastSavedState(2L, StateType.OAUTH_HH);
    assertThat(state).isNotNull();
  }

  @Test
  void testCallbackAndTokenExchange() {

    // Заглушаем только метод exchangeCodeForTokens
    Mockito.doReturn(new SimpleToken("mockedAccessToken", "mockedRefreshToken", LocalDateTime.now().plusHours(1)))
        .when(oAuthHHService).exchangeCodeForTokens(anyLong(), anyString());

    // Генерируем состояние и сохраняем его для теста
    String state = UUID.randomUUID().toString();
    userStateService.storeState(2L, state, StateType.OAUTH_HH); // Предполагается, что ID пользователя — 2

    // Отправляем запрос на callback с валидным `code` и `state`
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/oauth/hh/callback")
            .queryParam("code", "testCode")
            .queryParam("state", state)
            .build())
        .header("Authorization", "Bearer " + token)
        .exchange()
        .expectStatus().isFound()  // Проверяем, что статус — 302 (редирект в личный кабинет)
        .expectHeader().value("Location", location -> {
          assertThat(location).isEqualTo("/api/doc-ui/");
        });

    // Проверяем, что состояние удалено, и при повторном запросе выбрасывается исключение
    Exception exception = assertThrows(RuntimeException.class, () -> {
      userStateService.retrieveUserIdByState(state);
    });

    assertThat(exception.getMessage()).isEqualTo("State not found!"); // Проверяем сообщение исключения
  }
}

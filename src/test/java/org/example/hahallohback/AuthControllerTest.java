package org.example.hahallohback;

import org.example.hahallohback.core.dto.UserDto;
import org.example.hahallohback.security.response.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
class AuthControllerTest {

  @Container
  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>("postgres:14.2")
          .withDatabaseName("testdb")
          .withUsername("user")
          .withPassword("password");

  @Autowired
  private WebTestClient webTestClient;

  @DynamicPropertySource
  static void postgresProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    registry.add("spring.datasource.username", postgresContainer::getUsername);
    registry.add("spring.datasource.password", postgresContainer::getPassword);
  }

  private UserDto testUser;

  @BeforeEach
  void setUp() {
    testUser = new UserDto("testuser", "testpassword");
  }

  @Test
  void loginUser() {
    // Сначала регистрируем пользователя
    webTestClient.post()
        .uri("/api/auth/register")
        .contentType(APPLICATION_JSON)
        .bodyValue(testUser)
        .exchange()
        .expectStatus().isOk();

    // Создаем запрос авторизации
    UserDto authRequest = new UserDto("testuser", "testpassword");

    // Проверяем авторизацию и получение токена
    webTestClient.post()
        .uri("/api/auth/login")
        .contentType(APPLICATION_JSON)
        .bodyValue(authRequest)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AuthResponse.class) // Ожидаем AuthResponse для получения токена
        .value(response -> {
          assertThat(response.getUsername()).isEqualTo("testuser");
          assertThat(response.getToken()).isNotBlank(); // Проверяем, что токен не пустой
        });

    String token = webTestClient.post()
        .uri("/api/auth/login")
        .contentType(APPLICATION_JSON)
        .bodyValue(authRequest)
        .exchange()
        .expectStatus().isOk()
        .expectBody(AuthResponse.class)
        .returnResult()
        .getResponseBody()
        .getToken();

    // Используем полученный токен для вызова whoAmI
    webTestClient.get()
        .uri("/api/auth/who_am_i")
        .header("Authorization", "Bearer " + token)
        .exchange()
        .expectStatus().isOk()
        .expectBody(UserDto.class)
        .value(userDto -> assertThat(userDto.getUsername()).isEqualTo("testuser"));
  }

}

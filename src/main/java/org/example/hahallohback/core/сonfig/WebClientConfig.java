package org.example.hahallohback.core.сonfig;

import io.netty.channel.ChannelOption;
import io.netty.resolver.DefaultAddressResolverGroup;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


@Configuration
public class WebClientConfig {

  @Bean
  public WebClient.Builder webClientBuilder() {
    HttpClient httpClient = HttpClient.create(ConnectionProvider.newConnection())
        .resolver(DefaultAddressResolverGroup.INSTANCE)
        .responseTimeout(Duration.ofSeconds(5))
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);

    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient));
  }
}


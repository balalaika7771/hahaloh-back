package org.example.hahallohback.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.hahallohback.security.constans.ExcludedPaths;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@Order(1)
public class RemoveAuthorizationHeaderFilter extends OncePerRequestFilter {

  @Override
  public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    String requestURI = request.getRequestURI();

    System.out.println("sss " + requestURI);
    if (ExcludedPaths.PATHS.stream().anyMatch(path -> requestURI.matches(path.replace("**", ".*")))) {
      // Удаляем заголовок Authorization, если запрос соответствует исключению
      System.out.println("czczcz " + requestURI);
      request = new HttpServletRequestWrapper(request) {
        @Override
        public String getHeader(String name) {
          if ("Authorization".equalsIgnoreCase(name)) {
            return null;
          }
          return super.getHeader(name);
        }
      };
    }

    chain.doFilter(request, response);
  }
}

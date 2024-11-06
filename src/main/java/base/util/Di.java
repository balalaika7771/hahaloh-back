package base.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


/**
 * Утилиты для работы с DI-контейнером
 *
 * @author Ivan Zhendorenko
 */
@Component
@RequiredArgsConstructor
public class Di {

  private final ApplicationContext ctx;

  /**
   * Получить новый инстанс бина
   *
   * @param clazz Класс бина
   * @param <T>   Тип бина
   * @return Бин
   */
  public <T> T getNewBean(Class<T> clazz) {
    return ctx.getBean(clazz);
  }
}

package base.service.abstractions;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Базовая абстракция для любого сервиса Jpa
 *
 * @param <D> Тип дто
 * @param <E> Тип Сущности
 * @param <I> Тип идентификатора сущности
 * @author Ivan Zhendorenko
 */
public interface BaseJpaService<D, E, I> extends BaseService<D, E> {

  /**
   * Репозиторий
   *
   * @return Репозиторий
   */
  JpaRepository<E, I> repo();
}

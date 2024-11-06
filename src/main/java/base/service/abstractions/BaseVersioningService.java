package base.service.abstractions;

import base.abstractions.Versionable;


/**
 * Сервис, работающий с версионными сущностями
 *
 * @param <D> Тип дто
 * @param <E> Тип Сущности
 * @param <I> Тип идентификатора сущности
 * @author Ivan Zhendorenko
 */
public interface BaseVersioningService<D, E extends Versionable<E>, I> extends BaseJpaService<D, E, I> {

}

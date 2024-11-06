package base.service.jpa;


/**
 * Сервис со всеми CRUD-операциями
 *
 * @param <D> Тип дто
 * @param <E> Тип Сущности
 * @param <I> Тип идентификатора сущности
 * @author Ivan Zhendorenko
 */
public interface CrudJpaService<D, E, I> extends CreateJpaService<D, E, I>, ReadJpaService<D, E, I>, UpdateJpaService<D, E, I>, DeleteJpaService<D, E, I> {

}

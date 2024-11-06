package base.service.crudversioning;


import base.abstractions.Versionable;


/**
 * Сервис со всеми CRUD-операциями
 *
 * @param <E> Тип версионируемой сущности
 * @param <I> Тип идентификатора сущности
 * @author Ivan Zhendorenko
 */
public interface CrudVersioningService<D, E extends Versionable<E>, I> extends CreateVersioningService<D, E, I>, ReadVersioningService<D, E, I>,
    UpdateVersioningService<D, E, I>, DeleteVersioningService<D, E, I> {

}

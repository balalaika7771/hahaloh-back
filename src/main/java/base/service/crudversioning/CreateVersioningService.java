package base.service.crudversioning;

import base.abstractions.Versionable;
import base.service.abstractions.BaseVersioningService;
import base.service.jpa.CreateJpaService;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.function.ToLongFunction;

import static base.util.Strings.camelToSnake;


/**
 * Сервис, способный создавать сущности
 *
 * <p>
 * {@inheritDoc}
 *
 * @author Ivan Zhendorenko
 */
public interface CreateVersioningService<D, E extends Versionable<E>, I> extends BaseVersioningService<D, E, I>, CreateJpaService<D, E, I> {

  EntityManager em();

  @Override
  default E save(E entity) {
    var branchId = (Long) em().createNativeQuery("select nextval('%s')".formatted(branchSeqName(entity))).getSingleResult();
    entity.setId(null).setBranchId(branchId).setVersion(-1);
    return CreateJpaService.super.save(entity);
  }

  @Override
  default List<E> saveAll(Iterable<E> entities) {
    ToLongFunction<E> branchId = e -> (Long) em().createNativeQuery("select nextval('%s')".formatted(branchSeqName(e))).getSingleResult();
    entities.forEach(e -> e.setId(null).setBranchId(branchId.applyAsLong(e)).setVersion(-1));
    return CreateJpaService.super.saveAll(entities);
  }

  /**
   * Название сиквенса для ветки версий
   *
   * @param entity Инстанс сущности
   * @return Название сиквенса
   */
  default String branchSeqName(E entity) {
    var className = entity.getClass().getSimpleName();
    var seqPrefix = camelToSnake(className);
    return "%s_branch_id_seq".formatted(seqPrefix);
  }
}

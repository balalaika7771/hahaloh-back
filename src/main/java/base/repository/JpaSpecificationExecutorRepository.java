package base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * Репозиторий, поддерживающий работу через спецификацию
 *
 * @author Ivan Zhendorenko
 */
@NoRepositoryBean
public interface JpaSpecificationExecutorRepository<E, I> extends JpaRepository<E, I>, JpaSpecificationExecutor<E> {

}

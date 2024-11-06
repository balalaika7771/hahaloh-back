package base.abstractions;

/**
 * Проекция версионной сущности для легковестных запросов
 *
 * @author Ivan Zhendorenko
 */
public interface VersionInfo {

  /**
   * {@link Identifiable#getId() Идентификатор}
   */
  Long getId();

  /**
   * {@link Versionable#getVersion() Версия}
   */
  Integer getVersion();

  /**
   * {@link Versionable#getBranchId() Идентификатор ветки}
   */
  Long getBranchId();
}

package base.controller.crud.jpa;


/**
 * Контроллер с CUD-операциями
 *
 * @author Ivan Zhendorenko
 */
public interface CudController<D, E, I> extends CreateController<D, E, I>, UpdateController<D, E, I>, DeleteController<D, E, I> {

}

package base.controller.crud.jpa;


/**
 * Контроллер с Create-операциями
 *
 * @author Ivan Zhendorenko
 */
public interface CreateController<D, E, I> extends SingleCreateController<D, E, I>, MultiplyCreateController<D, E, I> {

}

package vn.com.amc.qtdl.tableau_proxy.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author hungnd.os
 * @Date 20/09/2023
 */
public abstract class AbstractGenericService<T, ID extends Serializable> {

    protected abstract JpaRepository<T, ID> getRepository();

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public List<T> findAll(Sort sort) {
        return getRepository().findAll(sort);
    }

    public List<T> findAllById(Iterable<ID> ids) {
        return getRepository().findAllById(ids);
    }

    public <S extends T> S save(S entity) {
        return getRepository().save(entity);
    }

    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return getRepository().saveAll(entities);
    }

    public void flush() {
        getRepository().flush();
    }

    public <S extends T> S saveAndFlush(S entity) {
        return getRepository().saveAndFlush(entity);
    }

    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    public void delete(T entity) {
        getRepository().delete(entity);
    }

    public void deleteInBatch(Iterable<T> entities) {
        getRepository().deleteInBatch(entities);
    }

    public void deleteAllInBatch() {
        getRepository().deleteAllInBatch();
    }

    public T findById(ID id) {
        Optional<T> t = getRepository().findById(id);
        if (t.isPresent()) return t.get();
        return null;
    }

    public <S extends T> List<S> findAll(Example<S> example) {
        return getRepository().findAll(example);
    }

    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return getRepository().findAll(example, sort);
    }
}

package lk.ijse.dep.dao;

import lk.ijse.dep.entity.SuperEntity;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class CrudDAOImpl<T extends SuperEntity,ID extends Serializable> implements CrudDAO<T,ID> {
    protected EntityManager entityManager;
    private Class<T> entity;

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    protected CrudDAOImpl(){
        entity = (Class<T>)(((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
    }

    @Override
    public List<T> findAll() throws Exception {
        return entityManager.createQuery("FROM " + entity.getName()).getResultList();
    }

    @Override
    public T find(ID pk) throws Exception {
        return (T) entityManager.find(entity,pk);
    }

    @Override
    public void save(T entity) throws Exception {
        entityManager.persist(entity);
    }

    @Override
    public void update(T entity) throws Exception {
        entityManager.merge(entity);
    }

    @Override
    public void delete(ID pk) throws Exception {
        entityManager.remove(entityManager.getReference(entity,pk));
    }
}

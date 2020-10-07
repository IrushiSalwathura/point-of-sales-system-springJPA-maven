package lk.ijse.dep.dao;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.io.Serializable;

public interface SuperDAO extends Serializable {
    void setEntityManager(EntityManager entityManager);
}

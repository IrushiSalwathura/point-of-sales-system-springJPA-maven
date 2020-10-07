package lk.ijse.dep.dao.custom.impl;

import lk.ijse.dep.dao.custom.QueryDAO;
import lk.ijse.dep.entity.CustomEntity;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
@Component
public class QueryDAOImpl implements QueryDAO {
    private EntityManager entityManager;


    public List<CustomEntity> searchOrder() throws Exception {
        return (List<CustomEntity>) entityManager.createNativeQuery("SELECT O.id, O.date, C.id, C.name, SUM(OD.qty*od.unitPrice)\n" +
                "FROM `Order` O\n" +
                "INNER JOIN Customer C ON O.customerId = C.id\n" +
                "INNER JOIN orderdetail OD ON O.id = OD.orderId\n" +
                "GROUP BY O.id").getSingleResult();
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}

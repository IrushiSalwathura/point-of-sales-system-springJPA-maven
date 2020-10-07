package lk.ijse.dep.business.custom.impl;

import lk.ijse.dep.business.custom.CustomerBO;
import lk.ijse.dep.dao.DAOFactory;
import lk.ijse.dep.dao.DAOType;
import lk.ijse.dep.dao.custom.CustomerDAO;
import lk.ijse.dep.db.JPAUtil;
import lk.ijse.dep.entity.Customer;
import lk.ijse.dep.util.CustomerTM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
@Component
public class CustomerBOImpl implements CustomerBO {
    @Autowired
    private CustomerDAO customerDAO;
    public String getNewCustomerId() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(em);
        String lastCustomerId;

        try {
            em.getTransaction().begin();
            lastCustomerId = customerDAO.getLastCustomerId();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
        if (lastCustomerId == null) {
                return "C001";
            } else {
                int maxId = Integer.parseInt(lastCustomerId.replace("C", ""));
                maxId = maxId + 1;
                String id = "";
                if (maxId < 10) {
                    id = "C00" + maxId;
                } else if (maxId < 100) {
                    id = "C0" + maxId;
                } else {
                    id = "C" + maxId;
                }
                return id;
            }
    }

    public List<CustomerTM> getAllCustomers() throws Exception{
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(em);
        List<Customer> allCustomers = null;

        try {
            em.getTransaction().begin();
            allCustomers = customerDAO.findAll();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }
        ArrayList<CustomerTM> customers = new ArrayList<>();
            for (Customer customer : allCustomers) {
                customers.add(new CustomerTM(customer.getId(),customer.getName(),customer.getAddress()));
            }
            return customers;

    }

    public void saveCustomer(String id, String name, String address) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(em);
        try {
            em.getTransaction().begin();
            customerDAO.save(new Customer(id,name,address));
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }

    }

    public void updateCustomer(String name, String address, String id) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(em);
        try {
            em.getTransaction().begin();
            customerDAO.update(new Customer(id,name,address));
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    public void deleteCustomer(String customerId) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        customerDAO.setEntityManager(em);
        try {
            em.getTransaction().begin();
            customerDAO.delete(customerId);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }
    }
}

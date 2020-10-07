package lk.ijse.dep.business.custom.impl;

import lk.ijse.dep.business.custom.ItemBO;
import lk.ijse.dep.dao.DAOFactory;
import lk.ijse.dep.dao.DAOType;
import lk.ijse.dep.dao.custom.ItemDAO;
import lk.ijse.dep.db.JPAUtil;
import lk.ijse.dep.entity.Item;
import lk.ijse.dep.util.ItemTM;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class ItemBOImpl implements ItemBO {
    private static ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);

    public String getNewItemCode() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        itemDAO.setEntityManager(em);
        String lastItemCode;
        Transaction tx = null;

        try {
            em.getTransaction().begin();
            lastItemCode = itemDAO.getLastItemCode();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
        if(lastItemCode == null){
            return "I001";
        }else{
            int maxId=  Integer.parseInt(lastItemCode.replace("I",""));
            maxId = maxId + 1;
            String id = "";
            if (maxId < 10) {
                id = "I00" + maxId;
            } else if (maxId < 100) {
                id = "I0" + maxId;
            } else {
                id = "I" + maxId;
            }
            return id;
        }
    }

    public List<ItemTM> getAllItems() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        itemDAO.setEntityManager(em);
        List<Item> allItems = null;
        Transaction tx = null;

        try {
            em.getTransaction().begin();
            allItems = itemDAO.findAll();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }

        ArrayList<ItemTM> items = new ArrayList<>();
        for (Item item : allItems) {
            items.add(new ItemTM(item.getCode(),item.getDescription(),item.getUnitPrice().doubleValue(),item.getQtyOnHand()));
        }
        return items;
    }

    public void saveItem(String code, String description, double unitPrice, int qtyOnHand) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        itemDAO.setEntityManager(em);
        Transaction tx = null;
        try {
            em.getTransaction().begin();
            itemDAO.save(new Item(code,description, BigDecimal.valueOf(unitPrice),qtyOnHand));
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    public void updateItem(String description, double unitPrice, int qtyOnHand, String code) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        itemDAO.setEntityManager(em);
        Transaction tx = null;
        try {
            em.getTransaction().begin();
            itemDAO.update(new Item(code,description,BigDecimal.valueOf(unitPrice),qtyOnHand));
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    public void deleteItem(String itemCode) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        itemDAO.setEntityManager(em);
        Transaction tx = null;
        try {
            em.getTransaction().begin();
            itemDAO.delete(itemCode);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }
    }
}

package lk.ijse.dep.business.custom.impl;

import lk.ijse.dep.business.custom.OrderBO;
import lk.ijse.dep.dao.DAOFactory;
import lk.ijse.dep.dao.DAOType;
import lk.ijse.dep.dao.custom.*;
import lk.ijse.dep.db.JPAUtil;
import lk.ijse.dep.entity.CustomEntity;
import lk.ijse.dep.entity.Item;
import lk.ijse.dep.entity.Order;
import lk.ijse.dep.entity.OrderDetail;
import lk.ijse.dep.util.OrderDetailTM;
import lk.ijse.dep.util.OrderTM;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Component
public class OrderBOImpl implements OrderBO {
    private OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
    private OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDERDETAIL);
    private ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
    private QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOType.QUERY);
    private CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);

    public void placeOrder(OrderTM order, List<OrderDetailTM> orderDetails) throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        orderDAO.setEntityManager(em);
        orderDetailDAO.setEntityManager(em);
        itemDAO.setEntityManager(em);
        customerDAO.setEntityManager(em);
        Transaction tx = null;

        try {
            em.getTransaction().begin();
            orderDAO.save(new Order(order.getOrderId(), order.getOrderDate(), customerDAO.find(order.getCustomerId())));
            for (OrderDetailTM orderDetail : orderDetails) {
                orderDetailDAO.save(new OrderDetail(order.getOrderId(), orderDetail.getItemCode(), orderDetail.getQty(), BigDecimal.valueOf(orderDetail.getUnitPrice())));
                Object i = itemDAO.find(orderDetail.getItemCode());
                Item item = (Item) i;
                item.setQtyOnHand(item.getQtyOnHand() - orderDetail.getQty());
                itemDAO.update(item);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }


    public String getNewOrderId() throws Exception {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        orderDAO.setEntityManager(em);
        String lastOrderId;
        Transaction tx = null;
        try {
            em.getTransaction().begin();
            lastOrderId = orderDAO.getLastOrderId();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }finally{
            em.close();
        }
            if (lastOrderId == null){
                return "OD001";
            }else{
                int maxId=  Integer.parseInt(lastOrderId.replace("OD",""));
                maxId = maxId + 1;
                String id = "";
                if (maxId < 10) {
                    id = "OD00" + maxId;
                } else if (maxId < 100) {
                    id = "OD0" + maxId;
                } else {
                    id = "OD" + maxId;
                }
                return id;
            }
    }

    public List<OrderTM> searchOrder() throws Exception{
            EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
            queryDAO.setEntityManager(em);
            List<CustomEntity> searchOrders = null;
            Transaction tx = null;

            try{
                em.getTransaction().begin();
                searchOrders =  queryDAO.searchOrder();
                em.getTransaction().commit();
            }catch(Throwable t){
                em.getTransaction().rollback();
                throw t;
            }
            List<OrderTM> allOrders = new ArrayList<>();
            for (CustomEntity searchOrder : searchOrders) {
                allOrders.add(new OrderTM(searchOrder.getOrderId(),searchOrder.getOrderDate(),
                        searchOrder.getCustomerName()
                        ,searchOrder.getCustomerId(),searchOrder.getTotal()));
            }
            return allOrders;

    }
}

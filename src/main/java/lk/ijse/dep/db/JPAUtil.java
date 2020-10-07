package lk.ijse.dep.db;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Properties;

public class JPAUtil {
    private static EntityManagerFactory emf = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory(){
        Properties jpaProp = new Properties();
        try {
            jpaProp.load(JPAUtil.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return Persistence.createEntityManagerFactory("DEP",jpaProp);
    }

    public static EntityManagerFactory getEntityManagerFactory(){
        return emf;
    }
}

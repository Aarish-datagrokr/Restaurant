package org.datagrokr.assignment.restaurant.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

public class TableRepository {
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;


    public TableRepository() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("table_pu");
        this.entityManager = this.entityManagerFactory.createEntityManager();
    }

    public void setTableStatus(int tableNo, boolean booked) {
    	if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("Update Table set isBooked = "+booked+" where tableNo = "+tableNo );
        query.executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    public int getTableFor(int members) {
    	if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();
        Query query;
        if(members>0 && members<=2) {
        	try {
            query = entityManager.createQuery("Select tableNo from Table where capacity = 2 and isBooked = false");
            query.setMaxResults(1);
            entityManager.getTransaction().commit();            
            entityManager.clear();
            return (int) query.getSingleResult();
        	} catch(jakarta.persistence.NoResultException e) {
                entityManager.clear();
                System.out.println(e.getMessage());
                return 0;
        	}
        }
        if(members>2 && members<=4) {
        	try {
            query = entityManager.createQuery("Select tableNo from Table where capacity = 4 and isBooked = false");
            query.setMaxResults(1);
            entityManager.getTransaction().commit();
            entityManager.clear();            
            return (int) query.getSingleResult();
        	} catch(jakarta.persistence.NoResultException e) {
                entityManager.clear();
                System.out.println(e.getMessage());
        		return 0;
        	}
        }
        else {
            entityManager.clear();
            return 0;

        }
}
}

package org.datagrokr.assignment.restaurant.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.ws.rs.ServiceUnavailableException;
import javassist.tools.rmi.ObjectNotFoundException;

import org.datagrokr.assignment.restaurant.entities.Bookings;

import java.sql.Time;
import java.util.Collection;

public class BookingsRepository {
    private EntityManager entityManager;
    private EntityManagerFactory entityManagerFactory;
    private TableRepository tableRepository;

    public BookingsRepository() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("booking_pu");
        this.entityManager = this.entityManagerFactory.createEntityManager();
        this.tableRepository = new TableRepository();
    }

    public void save(Bookings booking) {
    	if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();
        entityManager.persist(booking);
        entityManager.getTransaction().commit();
    }

    public int getTableNoByPhoneNo(String phoneNo) throws ObjectNotFoundException {
    	try {
        if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();    		
        Query query = entityManager.createQuery("Select tableNo from Bookings where phoneNo='"+phoneNo+"'");
        query.setMaxResults(1);
        entityManager.clear();
        return (int) query.getSingleResult();
    	} catch(jakarta.persistence.NoResultException e)
    	{
            entityManager.clear();
    		throw new ObjectNotFoundException("No booking under Phone no : "+phoneNo);
    	}
    }
    
    public void delete(String phoneNo) throws ObjectNotFoundException {
    	if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();
        int tableNo = getTableNoByPhoneNo(phoneNo);
        if(tableNo==0) { 
            entityManager.getTransaction().commit();
	        entityManager.clear();
        	throw new ObjectNotFoundException("Phone no not found");
        }
        	if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();

	        Query query = entityManager.createQuery("Delete from Bookings where phoneNo ='"+phoneNo+"'");
	        query.executeUpdate();
	        tableRepository.setTableStatus(tableNo,false);
	        entityManager.getTransaction().commit();
	        entityManager.clear();
    }

    public void update(String phoneNo,int members, Time reservationTime) throws Exception{
    	if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();
        Query query;
        int tableAvailable=0;
        try
        {
        tableRepository.setTableStatus(getTableNoByPhoneNo(phoneNo), false);
        entityManager.clear();
        }catch(ObjectNotFoundException e)
        {
            entityManager.clear();
            entityManager.getTransaction().commit();
    		throw new ObjectNotFoundException("No booking under phone no : "+phoneNo);
        }
        if(members>0 && members<=2) {
        	try {
            if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();
            query = entityManager.createQuery("Select tableNo from Table where capacity = 2 and isBooked = false");
            query.setMaxResults(1);
            entityManager.clear();
            tableAvailable=(int) query.getSingleResult();
        	} catch(jakarta.persistence.NoResultException e) {
                entityManager.clear();
        		throw new ObjectNotFoundException("No table available at the moment");
        	}
        }
        if(members>2 && members<=4) {
        	try {
            if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();        		
            query = entityManager.createQuery("Select tableNo from Table where capacity = 4 and isBooked = false");
            query.setMaxResults(1);
            entityManager.clear();            
            tableAvailable=(int) query.getSingleResult();
        	} catch(jakarta.persistence.NoResultException e) {
                entityManager.clear();
        		throw new ServiceUnavailableException("No table available at the moment");
        	}
        }
        if(tableAvailable!=0) {
        	if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();
            query = entityManager.createNativeQuery("Update bookings set members = ? , reservation_time = ? ,table_no=?  where phone_no = ?");
            query.setParameter(1,members);
            query.setParameter(2,reservationTime);
            query.setParameter(3,tableAvailable);
            query.setParameter(4,phoneNo);
   
            tableRepository.setTableStatus(tableAvailable, true);
            query.executeUpdate();
            entityManager.getTransaction().commit();
            entityManager.clear();
        }
        entityManager.clear();
    }
    
    @SuppressWarnings("unchecked")
	public Collection<Bookings> getAllBookings(){
    	Query query;
    	if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();
    	query=entityManager.createQuery("Select b from Bookings b");
    	return (Collection<Bookings>) query.getResultList();
    }


}

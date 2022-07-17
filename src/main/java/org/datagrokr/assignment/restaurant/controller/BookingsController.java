package org.datagrokr.assignment.restaurant.controller;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import javassist.tools.rmi.ObjectNotFoundException;

import org.datagrokr.assignment.restaurant.entities.Bookings;
import org.datagrokr.assignment.restaurant.repository.BookingsRepository;
import org.datagrokr.assignment.restaurant.repository.TableRepository;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Collection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Path("Bookings")
public class BookingsController implements ContainerResponseFilter{
    BookingsRepository bookingsRepository = new BookingsRepository();
    TableRepository tableRepository = new TableRepository();
    
    @Override
    public void filter(ContainerRequestContext requestContext, 
      ContainerResponseContext responseContext) throws IOException {
          responseContext.getHeaders().add(
            "Access-Control-Allow-Origin", "*");
          responseContext.getHeaders().add(
            "Access-Control-Allow-Credentials", "true");
          responseContext.getHeaders().add(
           "Access-Control-Allow-Headers",
           "origin, content-type, accept, authorization");
          responseContext.getHeaders().add(
            "Access-Control-Allow-Methods", 
            "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }

    @POST
    @Path("/Book-For")
//    @Consumes(MediaType.APPLICATION_JSON)
    public Response BookFor(String data) {
    	try {
    		
    		System.out.println(data);    		
    		JSONParser parser = new JSONParser();  
    		JSONObject bookingData = (JSONObject) parser.parse(data);  
    		    		
    		String name = (String) bookingData.get("name");
    		System.out.println(name);
    		String phoneNo = (String) bookingData.get("phoneNo");
    		int members = Integer.parseInt((String) bookingData.get("members"));
    		Time reservationTime =java.sql.Time.valueOf(LocalTime.parse((String) bookingData.get("reservationTime")));
  
  
        	if(LocalTime.now().isAfter(LocalTime.parse("20:00"))) return Response.status(500).entity("Bookings not allowed after 8pm.").build();
        	int tableNo = tableRepository.getTableFor(members);
            if(tableNo!=0) {
                Bookings booking = new Bookings(name,phoneNo,members,reservationTime,tableNo);
                bookingsRepository.save(booking);
                tableRepository.setTableStatus(tableNo,true);
                return Response.status(200).entity("Table No : "+tableNo+" booked for Mr/Mrs "+name).build();
            }
            else {
                return Response.status(500).entity("No table available at the moment").build();
            }
    	} catch(Exception e) {return Response.status(500).entity("Check your details and try again.").build();
    	}
    }
    
    @DELETE
    @Path("Cancel-Booking")
    public Response DeleteBooking( String phoneNo) {
    	try {
    	bookingsRepository.delete(phoneNo);
    	return Response.status(200).entity("Booking canceled under phone No : "+phoneNo).build();
    	} catch(ObjectNotFoundException e) {
            return Response.status(500).entity("No booking record under phone No : "+phoneNo).build();
    	}
    }
    
    @PUT
    @Path("Change-Booking-Details")
    public Response UpdateBooking(String data) throws Exception {
    	try {
		JSONParser parser = new JSONParser();  
		JSONObject bookingData = (JSONObject) parser.parse(data);  
		    		
		String phoneNo = (String) bookingData.get("phoneNo");
		int members = Integer.parseInt((String) bookingData.get("members"));
		Time reservationTime =java.sql.Time.valueOf(LocalTime.parse((String) bookingData.get("reservationTime")));

    	try {
    	bookingsRepository.update(phoneNo,members,reservationTime);
    	return Response.status(200).entity("Booking updated under phone No : "+phoneNo+", your table no is : "+bookingsRepository.getTableNoByPhoneNo(phoneNo)).build();
    	} catch(ObjectNotFoundException e) {
            return Response.status(500).entity("No booking under phone number : "+phoneNo).build();
    	}
    	}catch(ServiceUnavailableException e) {
            return Response.status(500).entity(e.getMessage()).build();

    	}
    }
    
    @GET
    @Path("get-all-bookings")
    public Response getAllBookings(){
    	Collection<Bookings> bookings = bookingsRepository.getAllBookings();
    	String entity = "";
    	for(Bookings b : bookings) {
    		String data = "Name : "+b.getName()+" "+"Phone no : "+b.getPhoneNo()+" "+"Members : "+b.getMembers()+" "+"Reservation time : "+b.getReservationTime()+" Table no :"+b.getTableNo()+"\r\n";
    		entity=entity+data;
    	}
    	if(entity.equals("")) return Response.status(500).entity("No bookings available").build();
    	else return Response.status(200).entity(entity).build();
    }
    
}
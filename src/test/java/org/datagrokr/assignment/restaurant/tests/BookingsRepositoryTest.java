package org.datagrokr.assignment.restaurant.tests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Time;

import org.datagrokr.assignment.restaurant.entities.Bookings;
import org.datagrokr.assignment.restaurant.repository.BookingsRepository;
import org.datagrokr.assignment.restaurant.repository.TableRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javassist.tools.rmi.ObjectNotFoundException;

class BookingsRepositoryTest {
	Bookings booking;
	BookingsRepository bookingsRepository;
	TableRepository tableRepository;
	
	@BeforeEach
	void setUp() throws Exception {
		booking = new Bookings();
		bookingsRepository = new BookingsRepository();
		tableRepository = new TableRepository();
	}

	@AfterEach
	void tearDown() throws Exception {
		booking.setName("");
		booking.setMembers(0);
		booking.setPhoneNo("");
		booking.setReservationTime(null);
		booking.setTableNo(0);
		}

	@Test
	@DisplayName("Add Booking test")
	void addBooking() throws ObjectNotFoundException {
		booking.setName("Unit test 1");
		booking.setMembers(2);
		booking.setPhoneNo("0000000000");
		booking.setReservationTime(Time.valueOf("12:00:00"));
		booking.setTableNo(5);
		bookingsRepository.save(booking);
		Integer tableNo = bookingsRepository.getTableNoByPhoneNo(booking.getPhoneNo());
		assertTrue(tableNo.equals(5));		
	}
	

	@Test
	@DisplayName("Cancel Booking test")
	void cancelBooking() throws ObjectNotFoundException {
		String phoneNo = "0000000000";
			bookingsRepository.delete(phoneNo);
			assertEquals(bookingsRepository.getTableNoByPhoneNo(phoneNo),0);
		}
	}




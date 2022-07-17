package org.datagrokr.assignment.restaurant.entities;

import jakarta.persistence.*;

import java.sql.Time;

@Entity
@jakarta.persistence.Table(name="bookings")
public class Bookings {
	
	@Column(name="name")
	private String name;

	@Id
	@Column(name="phone_no")
	private String phoneNo;
	
	@Column(name="members")
	private int members;
	
	@Column(name="reservation_time")
	private Time reservationTime;
	
	@Column(name="table_no")
	private int tableNo;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public int getMembers() {
		return members;
	}
	public void setMembers(int members) {
		this.members = members;
	}
	public Time getReservationTime() {
		return reservationTime;
	}
	public void setReservationTime(Time reservationTime) {
		this.reservationTime = reservationTime;
	}
	public int getTableNo() {
		return tableNo;
	}
	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}
	public Bookings(String name, String phoneNo, int members, Time reservationTime, int tableNo) {
		super();
		this.name = name;
		this.phoneNo = phoneNo;
		this.members = members;
		this.reservationTime = reservationTime;
		this.tableNo = tableNo;
	}
	
	public Bookings() {}
	@Override
	public String toString() {
		return "Bookings [name=" + name + ", phoneNo=" + phoneNo + ", members=" + members + ", reservationTime="
				+ reservationTime + ", tableNo=" + tableNo + "]";
	}
	
		


}

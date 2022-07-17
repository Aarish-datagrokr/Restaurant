package org.datagrokr.assignment.restaurant.entities;
import jakarta.persistence.*;
@Entity
@jakarta.persistence.Table(name="all_tables")
public class Table {
	
	@Id
	@Column(name="table_no")
	private int tableNo;
	
	@Column(name="capacity")
	private int capacity;
	
	@Column(name="is_booked")
	private boolean isBooked;

	public int getTableNo() {
		return tableNo;
	}
	public void setTableNo(int tableNo) {
		this.tableNo = tableNo;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public boolean GetIsBooked() {
		return isBooked;
	}
	public void setIsBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}
	
	@Override
	public String toString() {
		return "Table [tableNo=" + tableNo + ", capacity=" + capacity + ", isBooked=" + isBooked + "]";
	}


}

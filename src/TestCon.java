import java.util.ArrayList;

public class TestCon {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList<String> createQ = new ArrayList<String>();
		createQ.add("create table guest(phone_number int, last_name varchar2(25), first_name varchar2(25), email_address varchar2(50), guest_id int, reservation_id int )");
		createQ.add("create table rooms(hotel_id int, guest_id int, status varchar2(1), room_type varchar2(25), room_number int)");
		createQ.add("create table reservation(checkin date, checkout date, guest_id int, reservation_id int)");
		createQ.add("create table bill(bill_status varchar2(2), bill_date date, bill_id int, reservation_id int, guest_id int)");
		createQ.add("create table parking(parking_status varchar2(1), spot_number int, guest_id int)");
		createQ.add("create table hotel(hotel_id int, name varchar2(25), location varchar2(25))");
		
		
		
		OracleCon.execute("select table_name from tabs");
		
		
	}
}

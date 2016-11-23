 
/*
*
* FlowLayoutDemo.java
*
*/
 
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.*;
import java.util.*;
 
public class HotelViewer extends JFrame{
    
     
	public static ArrayList<String> dropQ;
	public static ArrayList<String> createQ;
	public static ArrayList<String> insertQ;
	public static ArrayList<String> selectQ;
	
	public void initDrop(){
		dropQ = new ArrayList<String>();
		dropQ.add("drop table guest");
		dropQ.add("drop table rooms");
		dropQ.add("drop table reservation");
		dropQ.add("drop table bill");
		dropQ.add("drop table parking");
		dropQ.add("drop table hotel");
	}
	
	public void initCreate(){
		createQ = new ArrayList<String>();
		createQ.add("create table guest(phone_number int, last_name varchar2(25), first_name varchar2(25), email_address varchar2(50), guest_id int, reservation_id int )");
		createQ.add("create table rooms(hotel_id int, guest_id int, status varchar2(1), room_type varchar2(25), room_number int)");
		createQ.add("create table reservation(checkin date, checkout date, guest_id int, reservation_id int)");
		createQ.add("create table bill(bill_status varchar2(2), bill_date date, bill_id int, reservation_id int, guest_id int)");
		createQ.add("create table parking(parking_status varchar2(1), spot_number int, guest_id int)");
		createQ.add("create table hotel(hotel_id int, name varchar2(25), location varchar2(25))");
	}
	
	public void initInsert(){
		insertQ = new ArrayList<String>();
		insertQ.add("insert into bill(bill_status, bill_date, bill_id, reservation_id, guest_id) values ('P', '02-JAN-16',	1, 1,	1)");
		insertQ.add("insert into guest(phone_number, last_name, first_name, email_address, guest_id, reservation_id) values ('1234567890','Davis','Mike','mike1@gmail.com',1,1)");
		insertQ.add("insert into rooms(hotel_id, guest_id, status, room_type, room_number) values (12,1,	'B',	'Double',	2)");
		insertQ.add("insert into reservation(checkin, checkout, guest_id, reservation_id) values ('02-JAN-16',	'09-JAN-16',	1,	1)");
		insertQ.add("insert into parking(parking_status, spot_number, guest_id) values ('B',	1,	1)");
		insertQ.add("insert into hotel(hotel_id, name, location) values (1,	'Supreme Time Resort',	'New York, USA')");
	}
	
	public void initSelect(){
		selectQ = new ArrayList<String>();
		selectQ.add("select * from bill where bill_date between '02-JAN-16' and '05-JAN-16' order by bill_date desc");
		selectQ.add("select g.first_name, g.last_name, h.name, r.room_number from guest g, hotel h, rooms r where g.guest_id = r.guest_id and h.hotel_id = r.hotel_id");
		selectQ.add("select * from guest where GUEST_ID between 5 and 9");
		selectQ.add("select guest.first_name, guest.last_name, guest.guest_id, bill.bill_status, bill.bill_id from bill inner join guest on guest.guest_id = bill.guest_id where bill.bill_status = 'U'");
		selectQ.add("select g.first_name, g.last_name, r.checkin, r.checkout from GUEST g, RESERVATION r where g.GUEST_ID = r.GUEST_ID");
	}
	
	
	
    public HotelViewer(String name) {
        super(name);
        initDrop();
        initCreate();
        initInsert();
        initSelect();
    }
     
    public void addComponentsToPane(final Container pane) {
        final JPanel p1 = new JPanel();
        final JPanel p2 = new JPanel();
        
        JTextArea textArea = new JTextArea(10, 50);
        textArea.setEditable(false);
        p2.add(textArea);
        
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        pane.add(scrollPane);
        
        
        JButton drop = new JButton("Drop Tables");
        p1.add(drop);
        
        drop.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				for(String q : dropQ){
					OracleCon.execute(q);
					
					if(OracleCon.exception()){
						textArea.append(OracleCon.getExceptionMsg()+"\n");
					}else{
						
						textArea.append("Dropped table "+q.split(" ")[2]+" \n");
					}
					
					
					OracleCon.close();
				}
				
				
			}
        });
        
        
        JButton create = new JButton("Create Tables");
        p1.add(create);
        
        create.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				for(String q : createQ){
					OracleCon.execute(q);
					
					if(OracleCon.exception()){
						textArea.append(OracleCon.getExceptionMsg()+"\n");
					}else{
						
						textArea.append("Created table\n");
					}
					
					
					OracleCon.close();
				}
				
			}
        });
        
        JButton insert = new JButton("Insert Data");
        p1.add(insert);
        
        insert.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				for(String q : insertQ){
					OracleCon.execute(q);
					
					if(OracleCon.exception()){
						textArea.append(OracleCon.getExceptionMsg()+"\n");
					}else{
						
						textArea.append("Inserted Data\n");
					}
					
					OracleCon.close();
				}
				
			}
        });
        
        String[] queries = { "Bill between dates", "Guest Hotel Room", "Guest between 5 and 9", "bill inner join guest", "Guest Reservation" };

        //Create the combo box, select item at index 4.
        //Indices start at 0, so 4 specifies the pig.
        JComboBox qComboBox = new JComboBox(queries);
        
        p1.add(qComboBox);
        JButton run = new JButton("Run");
        p1.add(run);
        
        run.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ResultSet rs = OracleCon.execute(selectQ.get(qComboBox.getSelectedIndex()));
				
				textArea.setText("");
				
				if(!OracleCon.exception()){
					ResultSetMetaData rsmd;
					try {
						rsmd = rs.getMetaData();
						int columnsNumber = rsmd.getColumnCount();
						
						for(int i= 1; i <= columnsNumber; i++){
							textArea.append(rsmd.getColumnName(i)+"\t");
						}
						textArea.append("\n");
						
						while(rs.next()){
							for(int i= 1; i <= columnsNumber; i++){
								textArea.append(rs.getString(i)+"\t");
							}
							textArea.append("\n");
						}
						
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
				}else{
					textArea.setText(OracleCon.getExceptionMsg());
				}
				OracleCon.close();
			}
        });
        
        pane.add(p1, BorderLayout.NORTH);
        pane.add(p2, BorderLayout.SOUTH);
        
        
        
    }
     
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        HotelViewer frame = new HotelViewer("Hotel Management System");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
     
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Schedule a job for the event dispatchi thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
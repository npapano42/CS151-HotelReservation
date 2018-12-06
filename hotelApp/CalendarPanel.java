package hotelApp;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;

public class CalendarPanel extends JPanel {

	private DataStorage db;
	LocalDate date;
	ArrayList<JButton> buttons;
	
	public CalendarPanel(DataStorage db) {
		this.db = db;
		this.date = LocalDate.now();
		this.buttons = new ArrayList<JButton>();
		setLayout(new FlowLayout());
		JPanel header = new JPanel(new FlowLayout());
		JTextField month = new JTextField(date.getMonth().toString() + " " + date.getYear());
		JTextArea rooms = new JTextArea(10, 20);
		JScrollPane scroll = new JScrollPane(rooms);
		
		rooms.setEditable(false);
		month.setEditable(false);
		JButton prev = new JButton("<");
		JButton next = new JButton(">");
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				date = date.minusMonths(1);
				month.setText(date.getMonth().toString() + " " + date.getYear());
				refresh();
			}
		});
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				date = date.plusMonths(1);
				month.setText(date.getMonth().toString() + " " + date.getYear());
				refresh();
			}
		});
		
		JPanel calendarGrid = new JPanel(new GridLayout(0,7));
		calendarGrid.add(new JLabel("Su"));
		calendarGrid.add(new JLabel("Mo"));
		calendarGrid.add(new JLabel("Tu"));
		calendarGrid.add(new JLabel("We"));
		calendarGrid.add(new JLabel("Th"));
		calendarGrid.add(new JLabel("Fr"));
		calendarGrid.add(new JLabel("Sa"));
		
		int firstDayOfWeek = date.withDayOfMonth(1).getDayOfWeek().getValue();
		int lastDay = date.withDayOfMonth(date.lengthOfMonth()).getDayOfMonth();
		Integer firstDay = 1;
		for(int i = 0; i < 42; i++) {
			JButton b = new JButton("");
			buttons.add(b);
			if (i >= firstDayOfWeek && i < lastDay + firstDayOfWeek) {
				b.setText(firstDay.toString());
				firstDay = firstDay.intValue() + 1;
			}
			calendarGrid.add(b);
		}
		for (JButton b : buttons) {
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (checkDate(b.getText())) {
						TimeInterval ti = new TimeInterval(date.withDayOfMonth(Integer.parseInt(b.getText())), date.withDayOfMonth(Integer.parseInt(b.getText())));
						ArrayList<Room> availableRooms = db.getAvailableRooms(ti, 0);
						ArrayList<Room> unavailableRooms = db.getRooms();
						unavailableRooms.removeAll(availableRooms);
						rooms.setText("Available Rooms: " + "\n");
						for (Room r: availableRooms) {
							rooms.append(r.getRoomNumber() + "\n");
						}
						rooms.append("Reserved Rooms: " + "\n");
						for (Room r: unavailableRooms) {
							rooms.append(r.getRoomNumber() + "\n");
						}
					}
				}
			});
		}
		header.add(month);
		header.add(prev);
		header.add(next);
		JPanel calendarArea = new JPanel(new BorderLayout());
		calendarArea.add(header, BorderLayout.NORTH);
		calendarArea.add(calendarGrid, BorderLayout.SOUTH);
		JPanel roomArea = new JPanel();
		roomArea.add(scroll);
		add(calendarArea);
		add(roomArea);
	}
	
	public void refresh() {
		int firstDayOfWeek = date.withDayOfMonth(1).getDayOfWeek().getValue();
		int lastDay = date.withDayOfMonth(date.lengthOfMonth()).getDayOfMonth();
		Integer firstDay = 1;
		for (int i = 0; i < 42; i++) {
			if (i >= firstDayOfWeek && i < lastDay + firstDayOfWeek) {
				buttons.get(i).setText(firstDay.toString());
				firstDay = firstDay.intValue() + 1;
			}
			else {
				buttons.get(i).setText("");
			}
		}
	}
	
	public boolean checkDate(String s) throws NumberFormatException {
		try {
			Integer.parseInt(s);
			return true;
		}
		catch (NumberFormatException nfe) {
			return false;
		}
	}
}

package mm0921;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import mm0921.DAL.DatabaseHelper;
import mm0921.Models.Cart;
import mm0921.Models.Tool;

public class main {
	private static DatabaseHelper dbHelper;
	private static ArrayList<Tool> allTools;
	private static Cart cart;
	
	public static void main(String[] args) {
		System.out.print("Loading tool list...\n\n");
		  
		dbHelper = new DatabaseHelper();
		allTools = dbHelper.selectAllTools();
		printToolList();
		  
		promptForToolEntry();
	}
	
	public static void printToolList() {
		String header = String.format("%15s%15s%15s%15s%20s%20s%20s\n", "Tool Type", "Brand", "Tool Code", "Daily Rate", "Weekday Charge", "Weekend Charge", "Holiday Charge");
		String seperator = String.format("%s", "----------------------------------------------------------------------------------------------------------------------------");
		System.out.print(header);
		System.out.print(seperator);
		
		for (Tool tool : allTools) {
			System.out.println();
			String toolDetails = String.format("%15s%15s%15s%15s%20s%20s%20s", tool.getToolType(), tool.getBrandName(), tool.getToolCode(), tool.getDailyRate(), tool.isWeekdayCharge(), tool.isWeekendCharge(), tool.isHolidayCharge());
			System.out.print(toolDetails);
		}
		System.out.println();
	}
	
	public static void promptForToolEntry() {
		Tool toolInCart = new Tool(); 
		cart = new Cart();
		
		String toolCode = getToolCodeFromUser();
		if (!toolCode.equals("")) {
			toolInCart = dbHelper.selectToolByToolCode(toolCode);
			if (toolInCart != null) {
				cart.setTool(toolInCart);
				
				getRentalDayCount();

			} else {
				System.out.print("Unable to find tool code. Please select a tool from the table: \n");
				printToolList();
				promptForToolEntry();
			}
		} else {
			System.out.print("Please select a tool from the table: \n");
			printToolList();
			promptForToolEntry();
		}
	}
	
	public static String getToolCodeFromUser() {
		try {
			System.out.print("\nPlease enter tool code: "); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
			return reader.readLine(); 
		} catch (IOException ex) {
			System.out.print("IOException: " + ex.getMessage());
			return "";
		}
	}
	
	public static void getRentalDayCount() {
		try {
			System.out.print("Please enter number of rental days: "); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
			String inputString = reader.readLine();
			if (!inputString.equals("")) {
				try {
				int rentalDayCount =  Integer.parseInt(inputString); 
				if (rentalDayCount > 0) {
					cart.setRentalDayCount(rentalDayCount);
					
					getDiscountPercent();
				} else {
					System.out.print("Invalid number of rental days. Please enter a value greater than 0.\n");
					getRentalDayCount();
				}
				} catch (NumberFormatException ex) {
					System.out.print("NumberFormatException: " + ex.getMessage());
					System.out.println();
					getRentalDayCount();
				}
			} else {
				System.out.print("Invalid number of rental days. Please enter a value greater than 0.\n");
				getRentalDayCount();
			}
		} catch (IOException ex) {
			System.out.print("IOException: " + ex.getMessage());
			System.out.println();
			getRentalDayCount();
		}
	}
	
	public static void getDiscountPercent() {
		try {
			System.out.print("Please enter discount percent (0-100): "); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
			String inputString = reader.readLine();
			if (!inputString.equals("")) {
				try {
				int discountPercent = Integer.parseInt(inputString); 
				if (discountPercent >= 0 && discountPercent <= 100) {
					cart.setDiscountPercent(discountPercent);
					
					getCheckoutDate();
				} else {
					System.out.print("Invalid discount. Please enter a value between 0-100.\n");
					getDiscountPercent();
				}
				} catch (NumberFormatException ex) {
					System.out.print("NumberFormatException: " + ex.getMessage());
					System.out.println();
					getDiscountPercent();
				}
			} else {
				System.out.print("Invalid discount. Please enter a value between 0-100.\n");
				getDiscountPercent();
			}
		} catch (IOException ex) {
			System.out.print("IOException: " + ex.getMessage());
			System.out.println();
			getDiscountPercent();
		}
	}
	
	public static void getCheckoutDate() {
		try {
			System.out.print("Please enter checkout date (mm/dd/yy): "); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
			String checkoutDate = reader.readLine(); 
			if (!checkoutDate.equals("")) {
				SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
				cart.setCheckoutDate(dateFormatter.parse(checkoutDate));
				
				promptCheckout();
			} else {
				System.out.print("Invalid date. Please enter a valid checkout date.\n");
				getCheckoutDate();
			}
			
		} catch (IOException ex) {
			System.out.print("IOException: " + ex.getMessage());
			System.out.println();
			getCheckoutDate();
		} catch (ParseException ex) {
			System.out.print("ParseException: " + ex.getMessage());
			System.out.println();
			getCheckoutDate();
		}
	}
	
	public static void promptCheckout() {
		try {
			System.out.print("\nWould you like to checkout? (Y/N)");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String inputString = reader.readLine();
			if (!inputString.equals("")) {
				boolean shouldCheckout = inputString.equalsIgnoreCase("Y");
				if (shouldCheckout) {
					System.out.print("\n" + cart.getRentalAgreement() + "\n\n");
					
					promptForNewEntry();
				} else {
					promptCheckout();
				}
			} else {
				promptCheckout();
			}
		} catch (IOException ex) {
			System.out.print("IOException: " + ex.getMessage());
			System.out.println();
			promptCheckout();
		}
	}
	
	public static void promptForNewEntry() {
		try {
			System.out.print("Would you like to place another entry? (Y/N)");
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String inputString = reader.readLine();
			if (!inputString.equals("")) {
				if (inputString.equalsIgnoreCase("Y")) {
					cart = new Cart();
					printToolList();
					promptForToolEntry();
				} else {
					System.out.print("\nUser entry completed.");
				}
			} else {
				promptForNewEntry();
			}
		} catch (IOException ex) {
			System.out.print("IOException: " + ex.getMessage());
			System.out.println();
			promptForNewEntry();
		}
	}
}

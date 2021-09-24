package mm0921.Tests;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import mm0921.DAL.DatabaseHelper;
import mm0921.Models.Cart;
import mm0921.Models.Tool;
import mm0921.Utils.HolidayUtils;

public class Checkout_JAKD_Test4 {
	Cart cart; 
	
	@Before
	public void setUp() throws Exception {
		DatabaseHelper dbHelper = new DatabaseHelper();
		dbHelper.populateTools();
		
		cart = new Cart();
		Tool jakd = dbHelper.selectToolByToolCode("JAKD");
		cart.setTool(jakd);
		
		Calendar checkoutCalendar = Calendar.getInstance();
		checkoutCalendar.set(2015, 8, 3);
		cart.setCheckoutDate(checkoutCalendar.getTime());
		cart.setRentalDayCount(6);
		cart.setDiscountPercent(0);
	}
	
	@Test
	public void testCheckoutDateToLaborDay() {
		assertFalse("Checkout date is not Labor Day.", HolidayUtils.getLaborDayObservedDate(cart.getCheckoutDate()).equals(cart.getCheckoutDate()));
	}

	@Test
	public void testDueDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		Calendar dueDateCalendar = Calendar.getInstance();
		dueDateCalendar.set(2015, 8, 9);
		assertEquals(dateFormat.format(dueDateCalendar.getTime()), dateFormat.format(cart.calculateDueDate()), "Due date calculation - checkout date plus rental day count.");
	}

	@Test
	public void testChargeDays() {
		assertEquals(3, cart.calculateNumberOfChargeDays(), "Charge days do not include weekend or holiday days for Jackhammer tool type.");
	}

	@Test
	public void testPrediscountCharge() {
		NumberFormat decimalFormatter = new DecimalFormat("#0.00");
		assertEquals(decimalFormatter.format(8.97), decimalFormatter.format(cart.getTool().getDailyRate() * cart.calculateNumberOfChargeDays()), "Pre-discount charge calculation - daily rental charge * chargable days.");
	}
	
	@Test
	public void testDiscountPercentage() {
		assertTrue(cart.getDiscountPercent() <= 100);
		assertTrue(cart.getDiscountPercent() >= 0);
	}
		
	@Test
	public void testDiscountAmount() {
		NumberFormat decimalFormatter = new DecimalFormat("#0.00");
		double prediscountCharge = cart.getTool().getDailyRate() * cart.calculateNumberOfChargeDays();
		assertEquals(decimalFormatter.format(0.00), decimalFormatter.format(prediscountCharge * ((double)cart.getDiscountPercent()/100)), "Discount Amount - Pre-discount charge * discount percentage.");
	}
	
	@Test
	public void testFinalCharge() {
		NumberFormat decimalFormatter = new DecimalFormat("#0.00");
		double prediscountCharge = cart.getTool().getDailyRate() * cart.calculateNumberOfChargeDays();
		double discountAmount = ((double)cart.getDiscountPercent()/100) * prediscountCharge;
		assertEquals(decimalFormatter.format(8.97), decimalFormatter.format(prediscountCharge - discountAmount), "Final Charge - Pre-discount charge - discount amount");
	}
}
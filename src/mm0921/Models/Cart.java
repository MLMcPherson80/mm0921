package mm0921.Models;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mm0921.Utils.HolidayUtils;
import mm0921.Utils.PercentException;
import mm0921.Utils.RentalCountException;

public class Cart {
	private Tool tool;
	private int rentalDayCount;
	private int discountPercent;
	private Date checkoutDate;
	
	public Tool getTool() {
		return tool;
	}
	public void setTool(Tool tool){
		this.tool = tool;
	}
	public int getRentalDayCount() {
		return rentalDayCount;
	}
	public void setRentalDayCount(int rentalDayCount) {
		try {
			if (rentalDayCount < 1)
				throw new RentalCountException("Please enter a rental day count greater than or equal to 1.");
			else
				this.rentalDayCount = rentalDayCount;
		} catch (RentalCountException ex) {
			System.out.print(ex.getMessage());
		}
	}
	public int getDiscountPercent() {
		return discountPercent;
	}
	public void setDiscountPercent(int discountPercent) {
		try {
			if (discountPercent < 0 || discountPercent > 100)
				throw new PercentException("Please enter a percentage value between 0-100");
			else
				this.discountPercent = discountPercent;
		} catch (PercentException ex) {
			System.out.print(ex.getMessage());
		}
	}
	public Date getCheckoutDate() {
		return checkoutDate;
	}
	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	
	public String getRentalAgreement() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
		
		Date dueDate = calculateDueDate();
		int chargeDays = calculateNumberOfChargeDays();
		double prediscountCharge = chargeDays * this.tool.getDailyRate();
		double discountAmount = ((double)this.discountPercent/100) * prediscountCharge;
		double finalCharge = prediscountCharge - discountAmount;
		
		String rentalAgreementText = 
			"Tool code: " + this.tool.getToolCode()
			+ "\nTool type: " + this.tool.getToolType()
			+ "\nTool brand: " + this.tool.getBrandName()
			+ "\nRental days: " + this.rentalDayCount
			+ "\nCheck out date: " + dateFormat.format(this.checkoutDate)
			+ "\nDue date: " + dateFormat.format(dueDate)
			+ "\nDaily rental charge: " + this.tool.getDailyRate()
			+ "\nCharge days: " + chargeDays
			+ "\nPre-discount charge: " + currencyFormatter.format(prediscountCharge)
			+ "\nDiscount percent: " + this.discountPercent + "%"
			+ "\nDiscount amount: " + currencyFormatter.format(discountAmount)
			+ "\nFinal charge: " + currencyFormatter.format(finalCharge);
		
		return rentalAgreementText;
	}
	
	public Date calculateDueDate() {
		Calendar dueDateCalendar = Calendar.getInstance();
		dueDateCalendar.setTime(this.checkoutDate);
		dueDateCalendar.add(Calendar.DAY_OF_YEAR, this.rentalDayCount);
		
		return dueDateCalendar.getTime();
	}
	
	public int calculateNumberOfChargeDays() {
		int chargeDays = 0;
		for (int i = 0; i < this.rentalDayCount; i++) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(this.checkoutDate);
			calendar.add(Calendar.DAY_OF_YEAR, i+1);
			boolean isWeekday = isDateWeekday(calendar);
			boolean isWeekend = isDateWeekend(calendar);
			boolean isHoliday = isDateHoliday(calendar);
			
			if (isHoliday) {
				if (isWeekday && this.tool.isWeekdayCharge() && this.tool.isHolidayCharge())
					chargeDays++;
				else if (isWeekend && this.tool.isWeekendCharge() && this.tool.isHolidayCharge())
					chargeDays++;
			} else {
				if (isWeekday && this.tool.isWeekdayCharge())
					chargeDays++;
				else if (isWeekend && this.tool.isWeekendCharge())
					chargeDays++;
			}
		}
		
		return chargeDays;
	}
	
	public boolean isDateWeekday(Calendar calendar) {
		boolean isWeekday = false;
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case 2:	//Monday
			case 3:	//Tuesday
			case 4:	//Wednesday
			case 5:	//Thursday
			case 6:	//Friday
				isWeekday = true;
				break;
			default:
				isWeekday = false;
				break;
		}
		
		return isWeekday;
	}
	
	public boolean isDateWeekend(Calendar calendar) {
		boolean isWeekend = false;
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
			case 1:	//Sunday
			case 7:	//Saturday
				isWeekend = true;
				break;
			default:
				isWeekend = false;
				break;
		}
		
		return isWeekend;
	}
	
	public boolean isDateHoliday(Calendar calendar) {
		boolean isHoliday = false;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
		Date fojObservedDate = HolidayUtils.getFourthOfJulyObservedDate(calendar.getTime());
		Date ldObservedDate = HolidayUtils.getLaborDayObservedDate(calendar.getTime());
		
		if (dateFormatter.format(calendar.getTime()).equals(dateFormatter.format(fojObservedDate)))
			isHoliday = true;
		else if (dateFormatter.format(calendar.getTime()).equals(dateFormatter.format(ldObservedDate)))
			isHoliday = true;
		else
			isHoliday = false;
		
		return isHoliday;
	}
}

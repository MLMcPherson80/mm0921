package mm0921.Models;

public class Tool {
	private String toolType;
	private String brandName;
	private String toolCode;
	private double dailyRate;
	private boolean weekdayCharge;
	private boolean weekendCharge;
	private boolean holidayCharge;
	
	public Tool(String type, String name, String code, double rate, boolean isWeekdayCharge, boolean isWeekendCharge, boolean isHolidayCharge) {
		this.toolType = type;
		this.brandName = name;
		this.toolCode = code;
		this.dailyRate = rate;
		this.weekdayCharge = isWeekdayCharge;
		this.weekendCharge = isWeekendCharge;
		this.holidayCharge = isHolidayCharge;
	}
	
	public Tool() {
	}

	public String getToolType() {
		return toolType;
	}
	public void setToolType(String name) {
		this.toolType = name;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getToolCode() {
		return toolCode;
	}
	public void setToolCode(String code) {
		this.toolCode = code;
	}
	public double getDailyRate() {
		return dailyRate;
	}
	public void setDailyRate(double dailyRate) {
		this.dailyRate = dailyRate;
	}
	public boolean isWeekdayCharge() {
		return weekdayCharge;
	}
	public void setWeekdayCharge(boolean weekdayCharge) {
		this.weekdayCharge = weekdayCharge;
	}
	public boolean isWeekendCharge() {
		return weekendCharge;
	}
	public void setWeekendCharge(boolean weekendCharge) {
		this.weekendCharge = weekendCharge;
	}
	public boolean isHolidayCharge() {
		return holidayCharge;
	}
	public void setHolidayCharge(boolean holidayCharge) {
		this.holidayCharge = holidayCharge;
	}
}

package mm0921.Utils;

import java.util.Calendar;
import java.util.Date;

public class HolidayUtils {
	public static Date getFourthOfJulyObservedDate(Date date) {
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		
		Calendar foj_Calendar = Calendar.getInstance();
		foj_Calendar.set(dateCalendar.get(Calendar.YEAR), 6, 4);
		if (foj_Calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			foj_Calendar.add(Calendar.DAY_OF_YEAR, 1);
			return foj_Calendar.getTime();
		} else if (foj_Calendar.get(Calendar.DAY_OF_WEEK) == 7) {
			foj_Calendar.add(Calendar.DAY_OF_YEAR, -1);
			return foj_Calendar.getTime();
		} else
			return foj_Calendar.getTime();
	}
	
	public static Date getLaborDayObservedDate(Date date) {
		Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(date);
		
		Calendar ld_Calendar = Calendar.getInstance();
		ld_Calendar.set(dateCalendar.get(Calendar.YEAR), 8, 1);
		Date laborDayObservedDate = ld_Calendar.getTime();
		for (int i = 1; i < 8; i++) {
			if (ld_Calendar.get(Calendar.DAY_OF_WEEK) == 2) {
				laborDayObservedDate = ld_Calendar.getTime();
				break;
			}
			
			ld_Calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		return laborDayObservedDate;
	}
}

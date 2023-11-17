package com.sigma.affinity;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sigma.model.PieBean;
import com.sigma.model.db.SigmaDocumentPersistence5;

public class PieUtil {
public static void main(String[] args) {
	System.out.println("Hi !");
	List<PieBean> startDays = new PieUtil().getStartDays(12);
	for(PieBean bean : startDays) {
		System.out.println(bean);		
	}
}
public List<PieBean> getDocumentSummary(int noOfMonths, JdbcTemplate jdbcTemplate,
		String tId) {
	List<PieBean> startDays = new PieUtil().getStartDays(noOfMonths);
	SigmaDocumentPersistence5 sigmaDocumentPersistence5 = new SigmaDocumentPersistence5();
	for(PieBean pBean : startDays) {
		sigmaDocumentPersistence5.getMonthlyDocumentSummary(jdbcTemplate, pBean, tId);
		pBean.setStart(null);
		pBean.setBw(null);
	}
	return startDays;
}
private List<PieBean> getStartDays(int noOfMonths) {
	List<PieBean> dates = new ArrayList<PieBean>();
	LocalDate currentDate = LocalDate.now();
	for(int increment=0; increment<noOfMonths; increment ++){		
		if(increment == 0)
			currentDate = getDates(currentDate, dates);
		else {
			currentDate = currentDate.minusDays(1);
			currentDate = getDates(currentDate, dates);
		}
	}
	return dates;
}
private LocalDate getDates(LocalDate date, List<PieBean> dates) {			
	LocalDate firstDayOfMonth = date.withDayOfMonth(1);
	LocalDate lastDayOfMonth = date.withDayOfMonth(date.lengthOfMonth());	
	Date startDate = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
	Date bwDate = Date.from(lastDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
	PieBean pieBean =  new PieBean(startDate, bwDate);
	Month month = firstDayOfMonth.getMonth();
	int year = firstDayOfMonth.getYear();
	pieBean.setMonthYear(month.getDisplayName(TextStyle.SHORT, Locale.getDefault())+"-"+year);
	dates.add(pieBean);
	return firstDayOfMonth;
}
}

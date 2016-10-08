
import java.util.GregorianCalendar;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DateCounter extends Application{

	private Label timeSummaryLabel;
	private ObservableList<Integer> daysList;
	private ObservableList<String> monthsList;
	private ObservableList<Integer> yearsList;
	private ComboBox<Integer> days;
	private ComboBox<String> months;
	private ComboBox<Integer> years;
	
	@Override
	public void start(Stage primaryStage) {
		
		Label label = new Label("Wybierz date");
		timeSummaryLabel = new Label("Minęło: ? lat, ? miesięcy, ? dni.");
		
		daysList = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31);
		monthsList = FXCollections.observableArrayList("January","February","March","April","May","June","July","August","September",
				"October","November","December");
		yearsList = FXCollections.observableArrayList(setYearsData());
		
		days = new ComboBox<Integer>(daysList);
		days.setValue(daysList.get(0));
		months = new ComboBox<String>(monthsList);
		months.setValue(monthsList.get(0));
		years = new ComboBox<Integer>(yearsList);
		years.setValue(yearsList.get(yearsList.size()-10));
				
		months.setOnAction(e->setDaysPerMonth(monthNum(months.getValue()),leapYer(years.getValue())));
		years.setOnAction(e->setDaysPerMonth(monthNum(months.getValue()),leapYer(years.getValue())));
		
		Button countButton = new Button("Przelicz");
		countButton.setPrefWidth(100);
		countButton.setOnAction(e -> countDate(days.getValue(),months.getValue(),years.getValue()));
		Button clearButton = new Button("Wyczyść");
		clearButton.setPrefWidth(100);
		clearButton.setOnAction(e->clearDate());
		
		HBox hbox = new HBox(days,months,years);
		hbox.setSpacing(10);
		hbox.setPadding(new Insets(5));
		HBox buttonHBox = new HBox(countButton,clearButton);
		buttonHBox.setSpacing(20);
		buttonHBox.setAlignment(Pos.BASELINE_CENTER);
		VBox vBox = new VBox(label,hbox, buttonHBox,timeSummaryLabel);
		vBox.setSpacing(20);
		vBox.setAlignment(Pos.BASELINE_CENTER);
		
		// stage settings
		Scene scene = new Scene(vBox,300,200);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Date Counter");
		primaryStage.show();
		
	}
	
	private void clearDate() {
		
		timeSummaryLabel.setText("Minęło: ? lat, ? miesięcy, ? dni.");
		days.setValue(daysList.get(0));
		months.setValue(monthsList.get(0));
		years.setValue(yearsList.get(0));
	}

	// method set proper days number for chosen month
	private void setDaysPerMonth(int monthNum, boolean leapYer) {
		
		int daysInMonth = daysPerMonth(monthNum,leapYer);
		Integer [] daysPerMonth = new Integer [daysInMonth];
		for (int i = 0; i < daysInMonth; i++) { daysPerMonth[i] = i+1;}
		
		int dayPicked = days.getValue();
		daysList.clear();
		daysList.addAll(daysPerMonth);
		if (dayPicked < daysInMonth) { days.setValue(dayPicked);}
		else { days.setValue(daysInMonth);}
	}

	// method counts how many days, month and years pass from given date
	private void countDate(Integer day, String month, Integer year) {
		
		// current date
		GregorianCalendar actualDate = new GregorianCalendar();
		int actualDay = actualDate.get(5);			// number of current day
		int actualMonth = actualDate.get(2) + 1;	// number of current month
		int actualYear = actualDate.get(1);			// number of current year
		
		// calculates how much time has pass from chosen date to current day
		int yearsPass = actualYear - year;
		int monthsPass = actualMonth - monthNum(month);
		int daysPass = actualDay - day;
		if (daysPass < 0) { 
			--monthsPass; 
			daysPass = actualDay + daysPerMonth (monthNum(month), leapYer(year)) - day;
		}
		if (monthsPass < 0) { 
			--yearsPass;
			monthsPass = 12 + monthsPass;
		}
		if (yearsPass < 0 ) {
			System.out.println(">>> BLAD! Wybrano date pozniejsza, niz aktualna!");
			daysPass = monthsPass = yearsPass = 0;
		}
		
		// print time summary 
		String timeSummaryTxt = timeSummary(yearsPass, monthsPass, daysPass);
		//timeSummaryLabel.setText("Minęło: " + yearsPass + " lat, " + monthsPass + " miesięcy, " + daysPass + " dni.");
		timeSummaryLabel.setText(timeSummaryTxt);
		
		System.out.println("Minęło: " + yearsPass + " lat, " + monthsPass + " miesięcy, " + daysPass + " dni.");
	}

	// display proper summary of time that has pass
	private String timeSummary(int yearsPass, int monthsPass, int daysPass) {
		
		String summary = "Minęło : ";
		// years
		if (yearsPass == 1) {summary += yearsPass + " rok, ";}
		else if (yearsPass == 2 || yearsPass == 3 || yearsPass == 4) {summary += yearsPass + " lata, ";}
		else {summary += yearsPass + " lat, ";}
		// months
		if (monthsPass == 1) {summary += monthsPass + " miesiąc, ";}
		else if (monthsPass == 2 || monthsPass == 3 || monthsPass == 4) {summary += monthsPass + " miesiące, ";}
		else {summary += monthsPass + " miesięcy, ";}
		// days
		if (daysPass == 1) {summary += daysPass + " dzień.";}
		else {summary += daysPass + " dni.";}
		
		return summary;
	}

	// helper method generates years array from startYear till finishYear (actual year)
	private Integer[] setYearsData() {
		
		GregorianCalendar actualDate = new GregorianCalendar();
		int startYear = 1939;
		int finishYear = actualDate.get(1);
		int range = finishYear - startYear + 1;
		Integer[] result = new Integer[range];
		
		for (int i = 0; i < range; i++) {
			result[i] = startYear++;
		}
		return result;
	}
	
	// method returns number of month (translate name of a month for its number)
	private int monthNum(String month){
		
		int monthNum = 0;
		
		switch (month){
			case "January": 	monthNum = 1; break;
			case "February": 	monthNum = 2; break;
			case "March": 		monthNum = 3; break;
			case "April":		monthNum = 4; break;
			case "May":			monthNum = 5; break;
			case "June": 		monthNum = 6; break;
			case "July":		monthNum = 7; break;
			case "August":		monthNum = 8; break;
			case "September":	monthNum = 9; break;
			case "October":		monthNum = 10; break;
			case "November":	monthNum = 11; break;
			case "December":	monthNum = 12; break;
		}
		return monthNum;
	}
	
	// method returns true if year is leap year (or false if it's opposite)
	private boolean leapYer(int year){
		if (((year%4 == 0) && (year%100 != 0)) || (year%400 == 0)) return true;
		else return false;
	}
	
	// method returns maximum number of days in a month
	private int daysPerMonth (int monthNum, boolean leapYear){
		
		if (monthNum == 1 || monthNum == 3 || monthNum == 5  || monthNum == 7 || monthNum == 8 || monthNum == 10 || monthNum == 12) { return 31;}
		else if (monthNum == 4 || monthNum == 6 || monthNum == 9 || monthNum == 11) { return 30;}
		else if (monthNum == 2 && !leapYear) {return 28;}
		else {return 29;}
	}	

	// main
	public static void main(String[] args){
		launch(args);
	}

}

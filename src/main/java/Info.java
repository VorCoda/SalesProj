import java.sql.SQLException;
import java.util.ArrayList;

//Класс, осуществляющий вывод информации
public class Info {

    private final Database database;
    private final ArrayList<Sale> sales;

    public Info(Database database) throws SQLException {
        this.database = database;
        sales = database.getAllSalesList();
    }

    public void printCountryWithHighestProfit(){
        String country = database.getCountryWithHighestProfit("Europe", "Asia");
        System.out.println("Страна с самым высоким общим доходом среди регионов Европы и Азии: " + country);
    }

    public void printCountryWithHighestProfitInRange(){
        String country = database.getCountryWithHighestProfitInRange(
                "Middle East", "North Africa", "Sub-Saharan Africa");
        System.out.println("Страна, у которой самый высокий общий доход в пределах от 420 тыс до 440 тыс, " + "\n" +
                "среди регионов Ближний Восток и Северная Африка" + " и СубСахарская Африка: " + country);
    }
}

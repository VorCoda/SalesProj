import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        Database db = Database.getInstance();
        ArrayList<Sale> sales = Parser.parser();

        Map<String, Integer> map = db.getSoldItemsCount();


    //Вывод информации по заданиям с поиском страны
//      Info info = new Info(db);
//      info.printCountryWithHighestProfit();
//      info.printCountryWithHighestProfitInRange();

        //Вывод диаграммы
//       Diagramm diag = new Diagramm(db, map);
//       diag.setVisible(true);




    }

}

import org.sqlite.JDBC;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Класс, осуществляющий работу с БД
public class Database {
    private static final String url = "jdbc:sqlite:src/main/resources/database.db" ;
    private  static  Database  instance = null;
    private final Connection connection;


    public Database() throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(url);

    }

    //Создает  единственный экземпляр БД
    public static Database getInstance() throws SQLException{
        if ( instance == null){
            instance = new Database();
        }
        return instance;
    }

    //Создает таблицу Sales
    public void createTableSales(){
        String createSales = """
                CREATE TABLE  IF NOT EXISTS Sales(
                region TEXT,
                country TEXT,
                itemType TEXT,
                salesChannel TEXT,
                orderPriority TEXT,
                orderDate TEXT,
                countOfUnitsSold INT,
                totalProfit REAL
                )
                """;
        try (Statement statement = connection.createStatement()){

            statement.execute(createSales);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Заполняет поля таблицы Sales
    public void fillTableSales(Sale sale){

        String query = "INSERT INTO Sales(region, country, itemType," +
                " salesChannel, orderPriority, orderDate, countOfUnitsSold, totalProfit) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query)){

            statement.setObject(1, sale.region);
            statement.setObject(2, sale.country);
            statement.setObject(3, sale.itemType);
            statement.setObject(4, sale.salesChannel);
            statement.setObject(5, sale.orderPriority);
            statement.setObject(6, sale.orderDate);
            statement.setObject(7, sale.countOfUnitsSold);
            statement.setObject(8, sale.totalProfit);
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Заполнения данными таблицы в БД
    public void inputData(ArrayList<Sale> sales){
        try{
                Database database = Database.getInstance();
                //Заполняем таблицу Sales данными
                for(Sale s : sales){
                    database.fillTableSales(s);
                }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Вывод список всех продаж
    public  ArrayList<Sale> getAllSalesList() throws SQLException {
        String query = "SELECT * FROM Sales";
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)){
                return getDataFromResultSet(resultSet);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Считывает и вывод список из sql запроса
    private static ArrayList<Sale> getDataFromResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Sale> salesLst = new ArrayList<>();
        while(resultSet.next()){
            String region = resultSet.getString("region");
            String country = resultSet.getString("country");
            String itemType = resultSet.getString("itemType");
            String salesChannel = resultSet.getString("salesChannel");
            String orderPriority = resultSet.getString("orderPriority");
            String orderDate = resultSet.getString("orderDate");
            Integer countOfUnitsSold = resultSet.getInt("countOfUnitsSold");
            Double totalProfit = resultSet.getDouble("totalProfit");


            Sale sale = new Sale(region, country, itemType, salesChannel,orderPriority,
                    orderDate, countOfUnitsSold, totalProfit);
            salesLst.add(sale);
        }

        return salesLst;
    }

    //Выводит страну с самым высоким ОБЩИМ доходом среди регионов 2 регионов (по задаче Европы и Азии)
    public String getCountryWithHighestProfit(String region1, String region2){

       String query = """           
                   Select Sales.country, Sales.totalProfit
                   from Sales
                   Where Sales.region = ? OR Sales.region = ?
                   Order by Sales.totalProfit desc
                   LIMIT 1
                   """;

        try(PreparedStatement statement = connection.prepareStatement(query);){

            statement.setObject(1, region1);
            statement.setObject(2, region2);

            return statement.executeQuery().getString("country");

        } catch (SQLException e) {
            System.err.println("Ошибка SQL: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Выводит страну, у которой ОБЩИЙ доход самый большой
    // и в пределах от 420 до 440 тыс., среди 3 регионов
    // (по задаче Ближний Восток, Северная Африка и СубСахарская Африка)
    public String getCountryWithHighestProfitInRange(String region1, String region2, String region3){
        String query = """                   
                Select Sales.country, Sales.totalProfit
                from Sales
                Where Sales.region = ? OR Sales.region = ?  OR Sales.region = ?
                AND Sales.totalProfit BETWEEN 420000.0 AND 440000.0
                """;

        try ( PreparedStatement statement = connection.prepareStatement(query);){

            statement.setObject(1, region1);
            statement.setObject(2, region2);
            statement.setObject(3, region3);

            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return resultSet.getString("country");
                }
                else {
                    return "Такой страны нет" ;

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //Получаем map: словарь, где ключ - регион, значение - общее кол-во проданного товара
    public Map<String, Integer> getSoldItemsCount(){

        //Запрос, формирующий таблицу регион - общее кол-во проданных товаров всех типов
        String query =  """
                    Select Sales.region, SUM(Sales.countOfUnitsSold) as total
                    From Sales
                    Group by Sales.region
                    Order by total desc
                    """;
        try(Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)){

             //данные таблицы
            Map<String, Integer> map = new HashMap<>();
            //идем по строчкам таблицы
            while(resultSet.next()){
                String region = resultSet.getString("region");
                Integer countOfUnitsSold = resultSet.getInt("total");
                //Если такой регион уже встречался - добавляем к старому кол-во проданного товара
                //новое кол-во проданного товара
                if(map.containsKey(region)){
                    map.put(region, map.get(region) + countOfUnitsSold);
                }
                else {
                    map.put(region, countOfUnitsSold); //иначе добавляем регион
                }
            }
            return map;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

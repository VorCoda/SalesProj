import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Класс, описывающий продажи
public class Sale {
    public final String region;
    public final String country;
    public final String itemType;
    public final String salesChannel;
    public final String orderPriority;
    public final String orderDate;
    public final Integer countOfUnitsSold;
    public final Double totalProfit;


    //Конструктор
    public Sale(String region, String country, String itemType, String salesChannel,
                String orderPriorety, String orderDate,
                Integer countOfUnitsSold, Double totalProfit){
        this.region = region;
        this.country = country;
        this.itemType = itemType;
        this.salesChannel = salesChannel;
        this.orderPriority = orderPriorety;
        this.orderDate = orderDate;
        this.countOfUnitsSold = countOfUnitsSold;
        this.totalProfit = totalProfit;
    }

}

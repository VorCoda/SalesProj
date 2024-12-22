import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Класс, осуществляющий парсинг csv файла
public class Parser {
    public static ArrayList<Sale> parser() throws IOException{
        //содержит объекты sale и всю информацию к каждой продаже
        ArrayList<Sale> sales = new ArrayList<>();
    List<String[]> fileLines = null; //хранит строки файла в виде массивов
        try {
            CSVReader reader = new CSVReader(new FileReader("Продажа продуктов в мире.csv"));
            fileLines = reader.readAll();
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        //Читаем каждую строки и записываем в базу(sales)
        boolean isHeadline = true;
        for(String[] fileline : fileLines){
            if(isHeadline){
                isHeadline = false;
                continue;
            }
            sales.add(new Sale(fileline[0], fileline[1],fileline[2], fileline[3],
                    fileline[4], fileline[5],
                    Integer.parseInt(fileline[6]), Double.parseDouble(fileline[7])));
        }

        return sales;
    }

}

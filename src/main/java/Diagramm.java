import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

//Класс, реализующий создание диаграммы по общему количеству проданных товаров, объединив их по регионам
//т.е диаграмма (регион - товар(его проданное общее кол-во))
public class Diagramm extends JFrame {

    private final Database database;
    private final ArrayList<Sale> sales;
    private final int DEFAULT_PADDING = 15;

    //Конструктор, инициализирующий БД
    public Diagramm(Database database, Map<String, Integer> map) throws SQLException {
        this.database = database;
        sales = database.getAllSalesList();
        init(map);
    }

    private void init(Map<String, Integer> map){
        CategoryDataset dataset = createDataset(map);
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart); //все окно панели с данными
        chartPanel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_PADDING ,DEFAULT_PADDING,
                DEFAULT_PADDING ,DEFAULT_PADDING )); //параметры границ отступа
        chartPanel.setBackground(Color.WHITE); //цвет фона
        add(chartPanel);

        pack(); //сборка
        setTitle("Общее кол-во проданных товаров по регионам"); //заголовок диаграммы
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //при нажатии на крест - закрытие окна
    }

    //Создание набора данных
    private CategoryDataset createDataset(Map<String, Integer> map){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        map.forEach((key,value) -> {
            dataset.setValue(value, "region" , key);
        });
        return dataset;
    }

    //Создаем диаграмму(chart)
    private JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createBarChart("Общее кол-во проданных товаров по регионам",
                "Регион",
                "Общее кол-во проданных товаров", dataset);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4.0));

        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        return chart;
    }

}

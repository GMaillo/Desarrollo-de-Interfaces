package Vista;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import Modelo.Persona;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;

public class EstadisticasCumpleanosControlador {
	 @FXML
	    private BarChart<String, Integer> barChart;

	    @FXML
	    private CategoryAxis xAxis;

	    private ObservableList<String> monthNames = FXCollections.observableArrayList();

	    /**
	     * Inicializa la clase de controlador. Este método se llama automáticamente
	     * después de que se haya cargado el archivo fxml.
	     */
	    @FXML
	    private void initialize() {

	        // Obtiene un array con los nombres de los meses en inglés.

	        String[] months = DateFormatSymbols.getInstance(Locale.ENGLISH).getMonths();

	        // Lo convierte en una lista y agréguelo a nuestra ObservableList of months.
	        monthNames.addAll(Arrays.asList(months));
	        
	        // Asigna los nombres de los meses como categorías para el eje horizontal.
	        xAxis.setCategories(monthNames);
	    }

	    /**
	     * Establece las personas que se mostrarán en las estadísticas.
	     * 
	     * @param persons
	     */
	    public void setPersonData(List<Persona> personas) {
	    	// Cuenta el número de personas que cumplen años en un mes específico.

	        int[] monthCounter = new int[12];
	        for (Persona p : personas) {
	            int month = p.getBirthday().getMonthValue() - 1;
	            monthCounter[month]++;
	        }

	        XYChart.Series<String, Integer> series = new XYChart.Series<>();
	        
	        // Crea un objeto XYChart para cada mes y lo agrega.
	        for (int i = 0; i < monthCounter.length; i++) {
	        	series.getData().add(new XYChart.Data<>(monthNames.get(i), monthCounter[i]));
	        }
	        
	        barChart.getData().add(series);
	    }
}

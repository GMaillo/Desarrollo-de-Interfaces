package Controlador;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import Modelo.ListaPersonaWrapper;
import Modelo.Persona;
import Util.BD;
import Vista.DialogoEditarPesonaControlador;
import Vista.EstadisticasCumpleanosControlador;
import Vista.OrganizadorRaizContralador;
import Vista.VistaPersonaControlador;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {
	private static Connection conn;

	private Stage primaryStage;
	private BorderPane rootLayout;
	/**
	 * Los datos como una lista observable de Personas.
	 */
	private ObservableList<Persona> personData = FXCollections.observableArrayList();

	/**
	 * Constructor
	 */
	public MainApp() {
		// Add some sample data

	}

	/**
	 * Devuelve los datos como una lista observable de personas.
	 * 
	 * @return personData
	 */
	public ObservableList<Persona> getPersonData() {
		return personData;
	}

	@Override
	public void start(Stage primaryStage) {

		// Establecemos conexión con base de datos indicando el nombre de usuario y la
		// contraseña
		try {
			conn = new BD("jdbc:mariadb://localhost:3306/contactos", "maviga", "1234").getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Agenda");
		this.primaryStage.getIcons().add(new Image("file:recursos/img/agenda.png"));

		initRootLayout();

		showPersonOverview();

	}

	/**
	 * Inicializa el root layout.
	 */
	public void initRootLayout() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../Vista/Raiz.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			OrganizadorRaizContralador controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Trata de cargar el último archivo de persona abierto

		File file = getPersonFilePath();
		if (file != null) {
			loadPersonDataFromFile(file);
		}
		personData.clear();
		try {
			personData.addAll(BD.getPersonas());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Muestra la descripción general de la persona dentro del root layout.
	 */
	public void showPersonOverview() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../Vista/VistaPersona.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);

			// Give the controller access to the main app.
			VistaPersonaControlador controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Devuelve a la pantalla principal
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Abre un cuadro de diálogo para editar los detalles de la persona
	 * especificada. Si el usuario clica en OK, los cambios se guardan en el objeto
	 * de persona proporcionado y devuelven true.
	 * 
	 * @param person el objeto persona a editar
	 * @return true si el usuario clica en OK
	 */
	public boolean showPersonEditDialog(Persona person) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../Vista/DialogoEditarPersona.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editar Persona");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			DialogoEditarPesonaControlador controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(person);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void showBirthdayStatistics() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("../Vista/EstadisticasCumpleanos.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Estadisticas Cumplea�os");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			EstadisticasCumpleanosControlador controller = loader.getController();
			controller.setPersonData(personData);

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Devuelve la preferencia del archivo de persona, es decir, el archivo que se
	 * abrió por última vez. La la preferencia se lee del registro específico del
	 * sistema operativo. Si tal preferencia no puede ser encontrada, devolverá un
	 * valor null
	 * 
	 * @return
	 */
	public File getPersonFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filePath", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Establece la ruta de archivo del archivo cargado actualmente. El camino se
	 * persiste en el registro específico del sistema operativo
	 * 
	 * @param file el archivo o nulo para eliminar la ruta
	 */
	public void setPersonFilePath(File file) {
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if (file != null) {
			prefs.put("filePath", file.getPath());

			primaryStage.setTitle("Agenda - " + file.getName());
		} else {
			prefs.remove("filePath");

			primaryStage.setTitle("Agenda");
		}
	}

	public void loadPersonDataFromFile(File file) {
		try {

			personData.clear();
			personData.addAll(ListaPersonaWrapper.getPersonas());

			setPersonFilePath(file);

		} catch (Exception e) {

		}
	}

	/**
	 * Guarda los datos de la persona actual en el archivo especificado
	 * 
	 * @param file
	 */
	public void savePersonDataToFile(File file) {

		try {
			BD.putPersonas(personData);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
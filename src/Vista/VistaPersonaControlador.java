package Vista;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import Controlador.MainApp;
import Modelo.Persona;
import Util.FechaUtil;

public class VistaPersonaControlador {
    @FXML
    private TableView<Persona> personTable;
    @FXML
    private TableColumn<Persona, String> firstNameColumn;
    @FXML
    private TableColumn<Persona, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    private MainApp mainApp;

    /**
     * El constructor es llamado antes del método initialize()
     */
    public VistaPersonaControlador() {
    }

    /**
     * Inicializa la clase de controlador. Este método se llama automáticamente
     * después de que se haya cargado el archivo fxml.
     */
    @FXML
    private void initialize() {

        // Inicializa la tabla de personas con las dos columnas.
        
        firstNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().lastNameProperty());

        showPersonDetails(null);

        // Escucha los cambios y muestra los detalles de la persona cuando se modifican

        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * Es llamado por la aplicación principal para devolver una referencia a sí mismo.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        personTable.setItems(mainApp.getPersonData());
    }
    
    /**
     * Rellena todos los campos de texto para mostrar detalles sobre la persona.
     * Si la persona especificada es nula, se borran todos los campos de texto.
     * 
     * @param person la persona o nullo
     */
    private void showPersonDetails(Persona persona) {
        if (persona != null) {

            // Llena las etiquetas con información del objeto persona.

            firstNameLabel.setText(persona.getFirstName());
            lastNameLabel.setText(persona.getLastName());
            streetLabel.setText(persona.getStreet());
            postalCodeLabel.setText(Integer.toString(persona.getPostalCode()));
            cityLabel.setText(persona.getCity());
            birthdayLabel.setText(FechaUtil.format(persona.getBirthday()));

        } else {
            // Persona es nula, elimina todo el texto.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }
    
    /**
     * Se llama cuando el usuario hace clic en el botón Eliminar.
     */
    @FXML
    private void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {

            // Nada seleccionado

        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Dialogo de Error");
        	alert.setHeaderText("No se puede borrar");
        	alert.setContentText("¡Selecciona una persona!");

        	alert.showAndWait();
        }
    }
    /**
     * Llamado cuando el usuario hace clic en el botón nuevo. Abre un cuadro de diálogo para editar
     * detalles para una nueva persona.
     */
    @FXML
    private void handleNewPerson() {
        Persona tempPerson = new Persona();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    /**
     * Se llama cuando el usuario hace clic en el botón editar. Abre un cuadro de diálogo para editar
     * detalles de la persona seleccionada.
     */
    @FXML
    private void handleEditPerson() {
        Persona selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }

        } else {
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("No hay persona seleccionada");
        	alert.setContentText("¡Selecciona una persona!");

        	alert.showAndWait();
        }
    }
}
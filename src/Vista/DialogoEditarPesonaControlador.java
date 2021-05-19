package Vista;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Modelo.Persona;
import Util.FechaUtil;



public class DialogoEditarPesonaControlador {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField birthdayField;


    private Stage dialogStage;
    private Persona persona;
    private boolean okClicked = false;

    /**
     * Inicializa la clase de controlador. Este método se llama automáticamente
     * después de que se haya cargado el archivo fxml.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Establece el escenario de este diálogo.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Establece la persona que se va a editar en el cuadro de diálogo.
     * 
     * @param person
     */
    public void setPerson(Persona persona) {
        this.persona = persona;

        firstNameField.setText(persona.getFirstName());
        lastNameField.setText(persona.getLastName());
        streetField.setText(persona.getStreet());
        postalCodeField.setText(Integer.toString(persona.getPostalCode()));
        cityField.setText(persona.getCity());
        birthdayField.setText(FechaUtil.format(persona.getBirthday()));
        birthdayField.setPromptText("dd.mm.yyyy");
    }

    /**
     * Devuelve true si el usuario clica OK, falso de lo contrario.-
     * 
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Llamado cuando el usuario clica en aceptar.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            persona.setFirstName(firstNameField.getText());
            persona.setLastName(lastNameField.getText());
            persona.setStreet(streetField.getText());
            persona.setPostalCode(Integer.parseInt(postalCodeField.getText()));
            persona.setCity(cityField.getText());
            persona.setBirthday(FechaUtil.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Llamado cuando el usuario clica cancelar.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Valida la entrada del usuario en los campos de texto.
     * 
     * @return true si la entrada es válida
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().length() == 0) {
            errorMessage += "No valid first name!\n"; 
        }
        if (lastNameField.getText() == null || lastNameField.getText().length() == 0) {
            errorMessage += "No valid last name!\n"; 
        }
        if (streetField.getText() == null || streetField.getText().length() == 0) {
            errorMessage += "No valid street!\n"; 
        }

        if (postalCodeField.getText() == null || postalCodeField.getText().length() == 0) {
            errorMessage += "No valid postal code!\n"; 
        } else {

            // Trata de parsear el código postal a un valor int

            try {
                Integer.parseInt(postalCodeField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid postal code (must be an integer)!\n"; 
            }
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage += "No valid city!\n"; 
        }

        if (birthdayField.getText() == null || birthdayField.getText().length() == 0) {
            errorMessage += "No valid birthday!\n";
        } else {
            if (!FechaUtil.validDate(birthdayField.getText())) {
                errorMessage += "No valid birthday. Use the format dd.mm.yyyy!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {

            // Muestra mensaje de error
            
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error Dialog");
        	alert.setHeaderText("Look, an Error Dialog");
        	alert.setContentText("Ooops, there was an error!");

        	alert.showAndWait();
            return false;
        }
    }
}

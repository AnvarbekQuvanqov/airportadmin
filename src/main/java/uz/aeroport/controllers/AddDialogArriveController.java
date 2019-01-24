package uz.aeroport.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.JSONObject;
import uz.aeroport.App;
import uz.aeroport.controllers.eventsController.AddDialogArriveEvent;
import uz.aeroport.controllers.eventsController.SendArriveEvent;
import uz.aeroport.utils.FxmlViews;
import uz.aeroport.utils.widgets.MyResourceBundle;
import uz.aeroport.utils.widgets.Wtransfer;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Jack on 21.12.2018.
 */
public class AddDialogArriveController implements Initializable
{


    @FXML
    private Label header;

    @FXML
    private Label ldate;

    @FXML
    private Label ltime;

    @FXML
    private Label lflight;

    @FXML
    private Label ldirection;

    @FXML
    private Label lstatus;

    @FXML
    private Label ltimes;

    @FXML
    private JFXButton saveit;

    @FXML
    private JFXButton cancel;

    @FXML
    private Label warn;

    @FXML
    private Label warn1;

    @FXML
    private Label warn2;

    @FXML
    private Label warn3;

    @FXML
    private Label warn4;

    @FXML
    private Label warn5;

    @FXML
    private DatePicker dateChooser;

    @FXML
    private JFXTextField timeField;

    @FXML
    private JFXTextField flightField;

    @FXML
    private JFXTextField destField;

    @FXML
    private JFXComboBox statusField;

    @FXML
    private JFXTextField statusTimeField;

    @FXML
    private JFXTextField destFieldR;

    @FXML
    private JFXTextField destFieldE;

    @FXML
    private Label warnR;

    @FXML
    private Label warnE;

    private boolean saveOrUpdate;

    private static MyResourceBundle myResourceBundle;
    private boolean arriveOrDepart;

    private JSONObject jsonObject;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        prepareForLabels();
        prepareMultiLanguage(resources);
        App.eventBus.addEventHandler(SendArriveEvent.ANY,event ->
        {
           this.jsonObject = event.getJsonObject();
           System.out.println(event.getJsonObject());
           fillWithData(jsonObject);
           saveOrUpdate = false;
        });
    }
    private void fillWithData(JSONObject jsonObject)
    {
        timeField.setText(jsonObject.getString("time"));
        flightField.setText(jsonObject.getString("flight"));
        destFieldE.setText(jsonObject.getString("destinationEng"));
        destFieldR.setText(jsonObject.getString("destinationRus"));
        destField.setText(jsonObject.getString("destinationUzb"));
        statusTimeField.setText(jsonObject.getString("statusTime"));
        if(jsonObject.getString("status").equals("schedule")){
            statusField.getSelectionModel().select(myResourceBundle.getString("Status1"));
        }
        if(jsonObject.getString("status").equals("expected")){
            statusField.getSelectionModel().select(myResourceBundle.getString("Status2"));
        }
        if(jsonObject.getString("status").equals("arrive")){
            statusField.getSelectionModel().select(myResourceBundle.getString("Status3"));
        }
        if(jsonObject.getString("status").equals("cancel")){
            statusField.getSelectionModel().select(myResourceBundle.getString("Status4"));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(jsonObject.getString("departDate"),formatter);
        dateChooser.setValue(localDate);
    }

    private void prepareMultiLanguage(ResourceBundle resources)
    {
        myResourceBundle = new MyResourceBundle(resources.getLocale(),"UTF-8");
        mulitLanguage();
        onClick(saveit,cancel,resources);
    }

    private void mulitLanguage()
    {
        header.setText(myResourceBundle.getString("AddDialog.header"));
        ldate.setText(myResourceBundle.getString("AddDialog.date"));
        ltime.setText(myResourceBundle.getString("AddDialog.timeF"));
        lflight.setText(myResourceBundle.getString("AddDialog.race"));
        ldirection.setText(myResourceBundle.getString("AddDialog.itenary"));
        lstatus.setText(myResourceBundle.getString("AddDialog.status"));
        ltimes.setText(myResourceBundle.getString("AddDialog.statusTime"));
        saveit.setText(myResourceBundle.getString("changePass.save"));
        cancel.setText(myResourceBundle.getString("changePass.cancel"));
        warn.setText(myResourceBundle.getString("AddDialog.warnings"));
        warn1.setText(myResourceBundle.getString("AddDialog.warnings"));
        warn2.setText(myResourceBundle.getString("AddDialog.warnings"));
        warn3.setText(myResourceBundle.getString("AddDialog.warnings"));
        warn4.setText(myResourceBundle.getString("AddDialog.warnings"));
        warn5.setText(myResourceBundle.getString("AddDialog.warnings"));
        List<String> statusWord = new ArrayList<>();
        statusWord.add(myResourceBundle.getString("Status1"));
        statusWord.add(myResourceBundle.getString("Status2"));
        statusWord.add(myResourceBundle.getString("Status3"));
        statusWord.add(myResourceBundle.getString("Status4"));
        statusField.getItems().addAll(statusWord);
    }

    private void onClick(JFXButton saveit, JFXButton cance,ResourceBundle resources)
    {
        cancel.setOnAction(event ->
        {
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            stage.close();
        });
        saveit.setOnAction(event ->
        {
            warn.setVisible(dateChooser.getEditor().getText().isEmpty());
            warn1.setVisible(timeField.getText().isEmpty());
            warn2.setVisible(flightField.getText().isEmpty());
            warn3.setVisible(destField.getText().isEmpty());
            if(statusField.getValue() == null){
                warn4.setVisible(true);
            }
            else{
                warn4.setVisible(false);
            }
            warn5.setVisible(statusTimeField.getText().isEmpty());
            warnE.setVisible(destFieldE.getText().isEmpty());
            warnR.setVisible(destFieldR.getText().isEmpty());

            if(!(warn.isVisible()
                    || warn1.isVisible()
                    || warn3.isVisible()
                    || warn2.isVisible()
                    || warn5.isVisible()
                    || warnR.isVisible()
                    || warnE.isVisible()
                    || warn4.isVisible()
            ))
            {

                if(jsonObject == null){
                    jsonObject = new JSONObject();
                    saveOrUpdate = true;
                }
                jsonObject.put("departDate",dateChooser.getValue());
                jsonObject.put("time",timeField.getText());
                jsonObject.put("flight",flightField.getText());
                jsonObject.put("destinationUzb", destField.getText());
                jsonObject.put("destinationEng",destFieldE.getText());
                jsonObject.put("destinationRus",destFieldR.getText());
                jsonObject.put("statusTime",statusTimeField.getText());
                if(myResourceBundle.getString("Status1").equals(statusField.getValue())){
                    jsonObject.put("status","schedule");
                }
                if(myResourceBundle.getString("Status2").equals(statusField.getValue())){
                    jsonObject.put("status","expected");
                }
                if(myResourceBundle.getString("Status3").equals(statusField.getValue())){
                    jsonObject.put("status","arrive");
                }
                if(myResourceBundle.getString("Status3").equals(statusField.getValue())){
                    jsonObject.put("status","cancel");
                }
                // arriveOrDepart bu yerda true bo`ladi
                arriveOrDepart = true;
                Wtransfer wtransfer = new Wtransfer();
                wtransfer.toGetController(FxmlViews.Addition.askedExit, resources.getLocale());
                ExitDialogController exitDialogController = (ExitDialogController)wtransfer.getController();
                exitDialogController.setLocaleToSave(resources.getLocale(),jsonObject , saveOrUpdate,arriveOrDepart);
                wtransfer.showAndWait();
                System.out.println(exitDialogController.success);
                if(exitDialogController.success)
                {
                    Stage stage =   (Stage)((Button)(event).getSource()).getScene().getWindow();
                    stage.close();
                    //Here where Event fired and will start in MainScreen update table
                    AddDialogArriveEvent addDialogArriveEvent = new AddDialogArriveEvent(AddDialogArriveEvent.ANY);
                    App.eventBus.fireEvent(addDialogArriveEvent);
                }

            }
        });
    }
    private void prepareForLabels() {
        warn.setVisible(false);
        warn1.setVisible(false);
        warn2.setVisible(false);
        warn3.setVisible(false);
        warn4.setVisible(false);
        warn5.setVisible(false);
        warnE.setVisible(false);
        warnR.setVisible(false);

    }

}

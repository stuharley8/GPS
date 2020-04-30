/*
 * Course: SE2800-031
 * Spring 2020
 * Lab: GPS
 * Author: Stuart Harley
 * Created: 4/11/2020
 */

package table;

import gps.Controller;
import gps.Track;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Controller Class for the Table.fxml window
 */
public class TableController {

    private static Track currentTrack;
    private static String trackName = "Track";
    private static String data = "Times at Various Speeds";

    @FXML
    private Menu trackSelectMenu;

    @FXML
    private Label titleLabel;

    @FXML
    private TableView<TableRow> table;

    @FXML
    private TableColumn<TableRow, String> column1;

    @FXML
    private TableColumn<TableRow, String> column2;

    @FXML
    private TableColumn<TableRow, String> column3;

    /**
     * Adds the loaded tracks to the select track menu dropdown
     * @param choices the list of track name that have been loaded in
     */
    @FXML
    public void loadTrackChoices(List<String> choices) {
        EventHandler<ActionEvent> handler = this::selectTrack;
        trackSelectMenu.getItems().clear();
        for(String track : choices) {
            trackSelectMenu.getItems().add(createMenuItem(track, handler));
        }
    }

    /**
     * Handles the user selecting a track to display data of
     * @param actionEvent the selection of a track
     */
    @FXML
    public void selectTrack(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        if(!menuItem.getText().equals("Track Select")) {  // Gets the menu as well so this checks for the menuItem
            trackName = menuItem.getText();
            setTitleText();
            for (int i = 0; i < Controller.getGPS().getNumTracks(); i++) {
                if (trackName.equals(Controller.getGPS().getTrack(i).getName())) {
                    currentTrack = Controller.getGPS().getTrack(i);
                    break;
                }
            }
            updateTable();
        }
    }

    private MenuItem createMenuItem(String text, EventHandler<ActionEvent> handler) {
        MenuItem result = new MenuItem(text);
        result.setOnAction(handler);
        return result;
    }

    @FXML
    private void setTitleText() {
        titleLabel.setText(trackName + " : " + data);
    }

    /**
     * Method deals with the User selecting the type of data to be displayed
     * @param actionEvent the actionEvent
     */
    @FXML
    public void selectData(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        data = menuItem.getText();
        setTitleText();
        updateTable();
    }

    @FXML
    private void updateTable() {
        table.getItems().clear();
        column1.setCellValueFactory(new PropertyValueFactory<>("col1"));
        column2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        column3.setCellValueFactory(new PropertyValueFactory<>("col3"));
        switch(data) {
            case "Times at Various Speeds":
                column1.setText("Speed");
                column2.setText("Time (min)");
                column3.setText("% Total Time");
                updateTimesAtVariousSpeeds();
                break;
            case "Times at Various Grades":
                column1.setText("Grade");
                column2.setText("Time (min)");
                column3.setText("% Total Time");
                updateTimesAtVariousGrades();
                break;
        }
    }

    private void updateTimesAtVariousSpeeds() {
        if(currentTrack != null && currentTrack.getNumPoints() >= 2) {
            TableSpeedsHandler tbh = new TableSpeedsHandler(currentTrack);
            table.getItems().add(new TableRow("<3mph", "" + tbh.getThreeLessMin(),
                    "" + tbh.getPercentThreeLess()));
            table.getItems().add(new TableRow("3-7mph", "" + tbh.getSevenLessMin(),
                    "" + tbh.getPercentSevenLess()));
            table.getItems().add(new TableRow("7-10mph", "" + tbh.getTenLessMin(),
                    "" + tbh.getPercentTenLess()));
            table.getItems().add(new TableRow("10-15mph", "" + tbh.getFifteenLessMin(),
                    "" + tbh.getPercentFifteenLess()));
            table.getItems().add(new TableRow("15-20mph", "" + tbh.getTwentyLessMin(),
                    "" + tbh.getPercentTwentyLess()));
            table.getItems().add(new TableRow(">20mph", "" + tbh.getTwentyGreaterMin(),
                    "" + tbh.getPercentTwentyGreater()));
        } else if(currentTrack != null && currentTrack.getNumPoints() < 2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Track Contains 1 Point");
            alert.setHeaderText(currentTrack.getName());
            alert.setContentText("Speed metrics cannot be calculated for a track with only 1 point");
            alert.showAndWait();
        }
    }

    private void updateTimesAtVariousGrades() {
        if(currentTrack != null && currentTrack.getNumPoints() >= 2) {
            TableTimesAtGradesHandler tgh = new TableTimesAtGradesHandler(currentTrack);
            table.getItems().add(new TableRow("<-5%", "" + tgh.getNegFiveLessMin(),
                    "" + tgh.getPercentNegFiveLess()));
            table.getItems().add(new TableRow("-5% to -1%", "" + tgh.getNegOneLessMin(),
                    "" + tgh.getPercentNegOneLess()));
            table.getItems().add(new TableRow("-1% to 1%", "" + tgh.getOneLessMin(),
                    "" + tgh.getPercentOneLess()));
            table.getItems().add(new TableRow("1% to 3%", "" + tgh.getThreeLessMin(),
                    "" + tgh.getPercentThreeLess()));
            table.getItems().add(new TableRow("3% to 5%", "" + tgh.getFiveLessMin(),
                    "" + tgh.getPercentFiveLess()));
            table.getItems().add(new TableRow(">5%", "" + tgh.getFiveGreaterMin(),
                    "" + tgh.getPercentFiveGreater()));
        } else if(currentTrack != null && currentTrack.getNumPoints() < 2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Track Contains 1 Point");
            alert.setHeaderText(currentTrack.getName());
            alert.setContentText("Grade metrics cannot be calculated for a track with only 1 point");
            alert.showAndWait();
        }
    }

    /**
     * Rounds a double to a specified number of decimal points
     * @param value the number to round
     * @param places the number of places to round to
     * @return the rounded number
     */
    public static double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}

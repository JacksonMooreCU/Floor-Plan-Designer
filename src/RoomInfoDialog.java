import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.util.Optional;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

/**
 * Created by Jacks on 2017-03-19.
 */
public class RoomInfoDialog extends Dialog <String[]> {

    public static final String[]    ROOM_COLORS = {"ORANGE", "YELLOW", "LIGHTGREEN", "DARKGREEN",
            "LIGHTBLUE", "BLUE", "CYAN", "DARKCYAN",
            "PINK", "DARKRED", "PURPLE", "GRAY"};

    private Building model;
    private int currentFloor;
    private int currentColour;
    private int row;
    private int col;
    private TextField occupant;
    private TextField position;
    private TextField number;
    private Button colour;


    public RoomInfoDialog(Building m,int cf,int cc,int r, int c){
        model = m;
        currentFloor = cf;
        currentColour = cc;
        row = r;
        col = c;

        setTitle("Login Dialog");
        setHeaderText(null);

        // Set the button types
        ButtonType okButtonType = new ButtonType("Ok", OK_DONE);
        getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Create the labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Add the labels
        Label label = new Label("Occupant:");
        grid.add(label,0,0);
        label.setMinHeight(30);
        label.setMinWidth(100);

        label = new Label("Position:");
        grid.add(label,0,1);
        label.setMinHeight(30);
        label.setMinWidth(100);

        label = new Label("Number:");
        grid.add(label,0,2);
        label.setMinHeight(30);
        label.setMinWidth(100);

        label = new Label("Floor:");
        grid.add(label,0,3);
        label.setMinHeight(30);
        label.setMinWidth(100);

        label = new Label("Size:");
        grid.add(label,0,4);
        label.setMinHeight(30);
        label.setMinWidth(100);

        //Add the room colour button
        colour = new Button();
        colour.setStyle("-fx-base: " + ROOM_COLORS[model.getFloorPlan(currentFloor).roomAt(row,col).getColorIndex()]  + ";");
        //colour.setDisable(true);
        colour.setFocusTraversable(false);
        colour.setMinHeight(30);
        colour.setMinWidth(150);
        grid.add(colour,2,2);

        //Add the text fields
        if (model.getFloorPlan(currentFloor).roomAt(row,col).getOccupant() == null){
            occupant = new TextField();
        }
        else {
            occupant = new TextField(model.getFloorPlan(currentFloor).roomAt(row,col).getOccupant());
        }
        occupant.setPromptText("Person who occupies this room");
        occupant.setMinHeight(30);
        occupant.setMinWidth(300);
        grid.add(occupant, 1, 0,2,1);

        if (model.getFloorPlan(currentFloor).roomAt(row,col).getPosition() == null){
            position = new TextField();
        }
        else {
            position = new TextField(model.getFloorPlan(currentFloor).roomAt(row,col).getPosition());
        }
        position.setPromptText("Job position/title of this person");
        position.setMinHeight(30);
        position.setMinWidth(300);
        grid.add(position, 1, 1,2,1);

        if (model.getFloorPlan(currentFloor).roomAt(row,col).getNumber() == null){
            number = new TextField();
        }
        else {
            number = new TextField(model.getFloorPlan(currentFloor).roomAt(row,col).getNumber());
        }
        number.setPromptText("The room number");
        number.setMinHeight(30);
        number.setMinWidth(150);
        grid.add(number, 1, 2,1,1);

        TextField floor = new TextField(""+model.getFloorPlan(currentFloor).getName());
        floor.setMinHeight(30);
        floor.setMinWidth(300);
        floor.setEditable(false);
        grid.add(floor, 1, 3,2,1);

        TextField size = new TextField(model.getFloorPlan(currentFloor).roomAt(row,col).getNumberOfTiles()+" Sq. Ft");
        size.setMinHeight(30);
        size.setMinWidth(300);
        size.setEditable(false);
        grid.add(size, 1, 4,2,1);

        getDialogPane().setContent(grid);

        //Convert the result to a string of each text box
        setResultConverter(new Callback<ButtonType, String[]>() {
            public String[] call(ButtonType param) {
                if (param == okButtonType){
                    String[] info = new String[3];
                    info[0] = occupant.getText();
                    info[1] = position.getText();
                    info[2] = number.getText();

                    return info;
                }
                return null;
            }
        });
        //Make the occupant have the focus
        Platform.runLater(() -> occupant.requestFocus());

        //Open the dialog box and get the result
        Optional<String[]> result = showAndWait();
        if (result.isPresent()){
            model.getFloorPlan(currentFloor).roomAt(row,col).setOccupant(result.get()[0]);
            model.getFloorPlan(currentFloor).roomAt(row,col).setPosition(result.get()[1]);
            model.getFloorPlan(currentFloor).roomAt(row,col).setNumber(result.get()[2]);

        }

    }
}

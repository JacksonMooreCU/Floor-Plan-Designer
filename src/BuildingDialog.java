import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * Created by Jacks on 2017-03-19.
 */
public class BuildingDialog extends Dialog {

    //name objects
    private Building model;
    private TextField numFloors;
    private TextField numExits;
    private TextField numRooms;
    private TextField totalSize;
    private Button directoryListing;
    private int totalRooms =0;
    private int totalTiles =0;
    private DirectoryDialog directoryDialog;

    //Create dialog
    public BuildingDialog(Building m) {

        model = m;

        setWidth(150);
        setHeight(500);

        setTitle("Building Overview");
        setHeaderText(null);

        //Set the button types
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        // Create the labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Add the labels
        Label label = new Label("Num Floors:");
        grid.add(label,0,0);
        label.setMinHeight(30);
        label.setMinWidth(50);

        label = new Label("Num Exits:");
        grid.add(label,0,1);
        label.setMinHeight(30);
        label.setMinWidth(50);

        label = new Label("Num Rooms:");
        grid.add(label,0,2);
        label.setMinHeight(30);
        label.setMinWidth(50);

        label = new Label("Total Size:");
        grid.add(label,0,3);
        label.setMinHeight(30);
        label.setMinWidth(50);

        //Add the button
        directoryListing = new Button("Directory Listing");
        directoryListing.setMinHeight(30);
        directoryListing.setMinWidth(200);
        grid.add(directoryListing,0,4,2,1);

        //Add the text fields
        numFloors = new TextField(model.getNumFloors()+"");
        numFloors.setMinHeight(30);
        numFloors.setMinWidth(50);
        grid.add(numFloors, 1, 0);

        numExits = new TextField(model.getNumExits()+"");
        numExits.setMinHeight(30);
        numExits.setMinWidth(50);
        grid.add(numExits, 1, 1);

        for (int eachFloor =0; eachFloor<model.getNumFloors(); eachFloor++){
            totalRooms += model.getFloorPlan(eachFloor).getNumberOfRooms();
        }
        numRooms = new TextField(totalRooms+"");
        numRooms.setMinHeight(30);
        numRooms.setMinWidth(50);
        grid.add(numRooms, 1, 2);

        for (int eachFloor =0; eachFloor<model.getNumFloors(); eachFloor++){
            totalTiles += (model.getFloorPlan(eachFloor).size()*model.getFloorPlan(eachFloor).size());
            for (int r=0; r<model.getFloorPlan(eachFloor).size(); r++)
                for (int c=0; c<model.getFloorPlan(eachFloor).size(); c++)
                    if(model.getFloorPlan(eachFloor).wallAt(r,c)){
                        totalTiles --;
                    }
        }
        totalSize = new TextField(totalTiles+" Sq. Ft.");
        totalSize.setMinHeight(30);
        totalSize.setMinWidth(50);
        grid.add(totalSize, 1, 3);

        //Handle the button being pressed
        directoryListing.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                directoryDialog = new DirectoryDialog(model,totalRooms);
            }
        });

        getDialogPane().setContent(grid);

        showAndWait();
    }
}

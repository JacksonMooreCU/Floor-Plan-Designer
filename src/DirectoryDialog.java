import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

/**
 * Created by Jacks on 2017-03-20.
 */
public class DirectoryDialog extends Dialog{

    private Building model;
    private ListView<String> directoryList;
    private String[] directory;
    private int totalRooms;
    private Button search;

    public DirectoryDialog(Building m,int t) {

        model = m;
        totalRooms = t;

        directory = new String[totalRooms];

        setWidth(150);
        setHeight(500);

        setTitle("Directory Listing");
        setHeaderText(null);

        //Set the button types
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);

        // Create the labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        //Adding each room to the directory
        directoryList = new ListView<String>();
        for (int eachFloor =0; eachFloor<model.getNumFloors(); eachFloor++) {
            //System.out.println(eachFloor);
            for (int eachRoom = 0; eachRoom <model.getFloorPlan(eachFloor).getNumberOfRooms(); eachRoom++) {
                //System.out.println(eachRoom);
                Room room =  model.getFloorPlan(eachFloor).getRooms()[eachRoom];
                //System.out.print(room.getNumber() +" - "+room.getOccupant()+" ("+room.getPosition()+")");
                directory[eachRoom] = room.getNumber() +" - "+room.getOccupant()+" ("+room.getPosition()+")";
            }
        }
        //Setting items to the list
        directoryList.setItems(FXCollections.observableArrayList(directory));
        directoryList.setPrefSize(300, 300);

        grid.add(directoryList,0,0);

        //creating the button
        search = new Button("Search");
        search.setMinHeight(30);
        search.setMinWidth(200);
        grid.add(search,0,1);

        //Handle when searching
        search.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {handleSearchPress();}
        });

        getDialogPane().setContent(grid);
        showAndWait();
    }

    //Handle when searching
    private void handleSearchPress(){
        TextInputDialog dialog = new TextInputDialog("Jackson");
        dialog.setTitle("Input Required");
        dialog.setHeaderText(null);
        dialog.setContentText("Please enter the full name of the person you are searching for: ");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            int[] results = searching(result.get());
            if(results[2] == 1){
                Room room =  model.getFloorPlan(results[0]).getRooms()[results[1]];
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Search Results");
                alert.setHeaderText(room.getOccupant()+" is our "+room.getPosition()+" add can be found on the"+
                        model.getFloorPlan(results[0]).getName()+" in room "+room.getNumber());
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Search Results");
                alert.setHeaderText("That name does not match anyone in our records, please try again.");
                alert.showAndWait();
            }
        }
    }
    public int[] searching(String name){
        int[] info = new int[3];
        for (int eachFloor =0; eachFloor<model.getNumFloors(); eachFloor++) {
            for (int eachRoom = 0; eachRoom < model.getFloorPlan(eachFloor).getNumberOfRooms(); eachRoom++) {
                Room room = model.getFloorPlan(eachFloor).getRooms()[eachRoom];
                System.out.println(room.getOccupant());
                if (name.equals(room.getOccupant())) {
                    info[0] = eachFloor;
                    info[1] = eachRoom;
                    info[2] = 1;
                    return info;
                }
            }
        }
        return null;
    }
}

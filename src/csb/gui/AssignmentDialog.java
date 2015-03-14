package csb.gui;

import csb.CSB_PropertyType;
import csb.data.Assignment;
import csb.data.Course;
import csb.data.ScheduleItem;
import static csb.gui.CSB_GUI.CLASS_HEADING_LABEL;
import static csb.gui.CSB_GUI.CLASS_PROMPT_LABEL;
import static csb.gui.CSB_GUI.PRIMARY_STYLE_SHEET;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class AssignmentDialog  extends Stage {
    // THIS IS THE OBJECT DATA BEHIND THIS UI
    Assignment assignment;
    
    // GUI CONTROLS FOR OUR DIALOG
    GridPane gridPane;
    Scene dialogScene;
    Label headingLabel;
    Label descriptionLabel;
    Label descriptionLabel2;
    TextField descriptionTextField;
    TextField descriptionTextField2;
    Label dateLabel;
    DatePicker datePicker;
    Label urlLabel;
    TextField urlTextField;
    Button completeButton;
    Button cancelButton;
    
    // THIS IS FOR KEEPING TRACK OF WHICH BUTTON THE USER PRESSED
    String selection;
    
    // CONSTANTS FOR OUR UI
    public static final String COMPLETE = "Complete";
    public static final String CANCEL = "Cancel";
    public static final String NAME_PROMPT = "Name: ";
    public static final String DATE_PROMPT = "Date";
    public static final String TOPIC_PROMPT = "Topics";
    public static final String ASSIGNMENT_HEADING = "Assignment Details";
    public static final String ADD_ASSIGNMENT_TITLE = "Add Assignment";
    public static final String EDIT_ASSIGNMENT_TITLE = "Edit Assignment";
    /**
     * Initializes this dialog so that it can be used for either adding
     * new schedule items or editing existing ones.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    public AssignmentDialog(Stage primaryStage, Course course,  MessageDialog messageDialog) {       
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        // FIRST OUR CONTAINER
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // PUT THE HEADING IN THE GRID, NOTE THAT THE TEXT WILL DEPEND
        // ON WHETHER WE'RE ADDING OR EDITING
        headingLabel = new Label(ASSIGNMENT_HEADING);
        headingLabel.getStyleClass().add(CLASS_HEADING_LABEL);
    
        // NOW THE DESCRIPTION 
        descriptionLabel = new Label(NAME_PROMPT);
        descriptionLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        descriptionTextField = new TextField();
        descriptionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            assignment.setName(newValue);
        });
        
        // AND THE DATE
        dateLabel = new Label(DATE_PROMPT);
        dateLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        datePicker = new DatePicker();
        datePicker.setOnAction(e -> {
            if (datePicker.getValue().isBefore(course.getStartingMonday())
                    || datePicker.getValue().isAfter(course.getEndingFriday())) {
                // INCORRECT SELECTION, NOTIFY THE USER
                PropertiesManager props = PropertiesManager.getPropertiesManager();
                messageDialog.show(props.getProperty(CSB_PropertyType.ILLEGAL_DATE_MESSAGE));
            }             
            else {
                assignment.setDate(datePicker.getValue());
                /*ObservableList<Assignment> list = course.getAssignments();
                boolean added = false;
                for (int i = 0; i < list.size(); i++)
                {
                    int c = assignment.compareTo(list.get(i));
                    if (c == -1)
                    {
                        for (int j = list.size()-1; j > i; j--)
                        {
                            Assignment temp = list.get(j);
                            list.set(j+1, temp);
                        }  
                        list.set(i, assignment);
                        added = true;
                        break;
                    } 
                    
                }
                
                if (!added)
                {
                    list.add(assignment);
                }*/
                  
            }
        });
        
        // AND THE URL
        descriptionLabel2 = new Label(TOPIC_PROMPT);
        descriptionLabel2.getStyleClass().add(CLASS_PROMPT_LABEL);
        descriptionTextField2 = new TextField();
        descriptionTextField2.textProperty().addListener((observable, oldValue, newValue) -> {
            assignment.setTopics(newValue);
        });
        
        // AND FINALLY, THE BUTTONS
        completeButton = new Button(COMPLETE);
        cancelButton = new Button(CANCEL);
        
        // REGISTER EVENT HANDLERS FOR OUR BUTTONS
        EventHandler completeCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            AssignmentDialog.this.selection = sourceButton.getText();
            AssignmentDialog.this.hide();
        };
        completeButton.setOnAction(completeCancelHandler);
        cancelButton.setOnAction(completeCancelHandler);

        // NOW LET'S ARRANGE THEM ALL AT ONCE
        gridPane.add(headingLabel, 0, 0, 2, 1);
        gridPane.add(descriptionLabel, 0, 1, 1, 1);
        gridPane.add(descriptionTextField, 1, 1, 1, 1);
        gridPane.add(dateLabel, 0, 2, 1, 1);
        gridPane.add(datePicker, 1, 2, 1, 1);
        gridPane.add(descriptionLabel2, 0, 3, 1, 1);
        gridPane.add(descriptionTextField2, 1, 3, 1, 1);      
        gridPane.add(completeButton, 0, 4, 1, 1);
        gridPane.add(cancelButton, 1, 4, 1, 1);

        // AND PUT THE GRID PANE IN THE WINDOW
        dialogScene = new Scene(gridPane);
        dialogScene.getStylesheets().add(PRIMARY_STYLE_SHEET);
        this.setScene(dialogScene);
    }
    
    /**
     * Accessor method for getting the selection the user made.
     * 
     * @return Either YES, NO, or CANCEL, depending on which
     * button the user selected when this dialog was presented.
     */
    public String getSelection() {
        return selection;
    }
    
    public Assignment getAssignment() { 
        return assignment;
    }
    
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * @param message Message to appear inside the dialog.
     */
    public Assignment showAddAssignmentDialog(LocalDate initDate) {
        // SET THE DIALOG TITLE
        setTitle(ADD_ASSIGNMENT_TITLE);
        
        // RESET THE SCHEDULE ITEM OBJECT WITH DEFAULT VALUES
        assignment = new Assignment();
        
        // LOAD THE UI STUFF
        descriptionTextField.setText(assignment.getName());
        datePicker.setValue(initDate);
        descriptionTextField2.setText(assignment.getTopics());
        
        // AND OPEN IT UP
        this.showAndWait();
        
        return assignment;
    }
    
    public void loadGUIData() {
        // LOAD THE UI STUFF
        descriptionTextField.setText(assignment.getName());
        datePicker.setValue(assignment.getDate());
        descriptionTextField2.setText(assignment.getTopics());     
    }
    
    public boolean wasCompleteSelected() {
        return selection.equals(COMPLETE);
    }
    
    public void showEditAssignmentDialog(Assignment itemToEdit) {
        // SET THE DIALOG TITLE
        setTitle(EDIT_ASSIGNMENT_TITLE);
        
        // LOAD THE SCHEDULE ITEM INTO OUR LOCAL OBJECT
        assignment = new Assignment();
        assignment.setName(itemToEdit.getName());
        assignment.setDate(itemToEdit.getDate());
        assignment.setTopics(itemToEdit.getTopics());
        
        // AND THEN INTO OUR GUI
        loadGUIData();
               
        // AND OPEN IT UP
        this.showAndWait();
    }
}
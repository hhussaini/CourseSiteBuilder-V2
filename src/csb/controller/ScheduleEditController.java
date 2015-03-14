package csb.controller;

import static csb.CSB_PropertyType.REMOVE_ITEM_MESSAGE;
import csb.data.Assignment;
import csb.data.Course;
import csb.data.CourseDataManager;
import csb.data.Lecture;
import csb.data.ScheduleItem;
import csb.gui.AssignmentDialog;
import csb.gui.CSB_GUI;
import csb.gui.MessageDialog;
import csb.gui.ScheduleItemDialog;
import csb.gui.YesNoCancelDialog;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import csb.gui.LectureDialog;
import java.time.DayOfWeek;
import java.util.List;
import javafx.collections.ObservableList;

/**
 *
 * @author McKillaGorilla
 */
public class ScheduleEditController {
    ScheduleItemDialog sid;
    LectureDialog ld;
    AssignmentDialog ad;
    MessageDialog messageDialog;
    YesNoCancelDialog yesNoCancelDialog;
    
    public ScheduleEditController(Stage initPrimaryStage, Course course, MessageDialog initMessageDialog, YesNoCancelDialog initYesNoCancelDialog) {
        sid = new ScheduleItemDialog(initPrimaryStage, course, initMessageDialog);
        ld = new LectureDialog(initPrimaryStage, course, initMessageDialog);
        ad = new AssignmentDialog(initPrimaryStage, course, initMessageDialog);
        messageDialog = initMessageDialog;
        yesNoCancelDialog = initYesNoCancelDialog;
    }

    // THESE ARE FOR SCHEDULE ITEMS
    
    public void handleAddScheduleItemRequest(CSB_GUI gui) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        sid.showAddScheduleItemDialog(course.getStartingMonday());
        
        // DID THE USER CONFIRM?
        if (sid.wasCompleteSelected()) {
            // GET THE SCHEDULE ITEM
            ScheduleItem si = sid.getScheduleItem();
            
            // AND ADD IT AS A ROW TO THE TABLE
            course.addScheduleItem(si);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }
    }
    
    public void handleEditScheduleItemRequest(CSB_GUI gui, ScheduleItem itemToEdit) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        sid.showEditScheduleItemDialog(itemToEdit);
        
        // DID THE USER CONFIRM?
        if (sid.wasCompleteSelected()) {
            // UPDATE THE SCHEDULE ITEM
            ScheduleItem si = sid.getScheduleItem();
            itemToEdit.setDescription(si.getDescription());
            itemToEdit.setDate(si.getDate());
            itemToEdit.setLink(si.getLink());
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }        
    }
    
    public void handleRemoveScheduleItemRequest(CSB_GUI gui, ScheduleItem itemToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_ITEM_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(YesNoCancelDialog.YES)) { 
            gui.getDataManager().getCourse().removeScheduleItem(itemToRemove);
        }
    }
    
    
    
    
    public void handleAddLectureRequest(CSB_GUI gui) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        ld.showAddLectureDialog(course.getStartingMonday());
        
        // DID THE USER CONFIRM?
        if (ld.wasCompleteSelected()) {
            // GET THE SCHEDULE ITEM
            Lecture li = ld.getLecture();
            
            // AND ADD IT AS A ROW TO THE TABLE
            course.addLecture(li);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }
    }
    
    
    
    
    public void handleEditLectureRequest(CSB_GUI gui, Lecture itemToEdit) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        ld.showEditLectureDialog(itemToEdit);
        
        // DID THE USER CONFIRM?
        if (ld.wasCompleteSelected()) {
            // UPDATE THE SCHEDULE ITEM
            Lecture li = ld.getLecture();
            itemToEdit.setTopic(li.getTopic());
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }        
    }
    
    
    
    public void handleRemoveLectureRequest(CSB_GUI gui, Lecture itemToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_ITEM_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(YesNoCancelDialog.YES)) { 
            gui.getDataManager().getCourse().removeLecture(itemToRemove);
        }
    }
    
    
    public void handleMoveUpLectureRequest(CSB_GUI gui, Lecture itemToMove) {
    
   
        ObservableList<Lecture> lectures = gui.getDataManager().getCourse().getLectures();
        
        int itemIndex = lectures.indexOf(itemToMove);
        Lecture temp = lectures.get(itemIndex);
        Lecture aboveItem = lectures.get(itemIndex-1);
        lectures.set(itemIndex, aboveItem);
        lectures.set(itemIndex-1, temp);
    }
    
    
    public void handleMoveDownLectureRequest(CSB_GUI gui, Lecture itemToMove) {
             
        ObservableList<Lecture> lectures = gui.getDataManager().getCourse().getLectures();
        
        int itemIndex = lectures.indexOf(itemToMove);
        Lecture temp = lectures.get(itemIndex);
        Lecture belowItem = lectures.get(itemIndex+1);
        lectures.set(itemIndex, belowItem);
        lectures.set(itemIndex+1, temp);
    }
    
    
    public void handleAddAssignmentRequest(CSB_GUI gui) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        ad.showAddAssignmentDialog(course.getStartingMonday());
        
        // DID THE USER CONFIRM?
        if (ad.wasCompleteSelected()) {
            // GET THE SCHEDULE ITEM
            Assignment ai = ad.getAssignment();
            
            // AND ADD IT AS A ROW TO THE TABLE
            course.addAssignment(ai);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }
    }
    
    public void handleEditAssignmentRequest(CSB_GUI gui, Assignment assignment) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        ad.showEditAssignmentDialog(assignment);
        
        // DID THE USER CONFIRM?
        if (ad.wasCompleteSelected()) {
            // UPDATE THE SCHEDULE ITEM
            Assignment ai = ad.getAssignment();
            assignment.setName(ai.getName());
            assignment.setDate(ai.getDate());
            assignment.setTopics(ai.getTopics());
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }        
    }
    
    public void handleRemoveAssignmentRequest(CSB_GUI gui, Assignment assignment) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_ITEM_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(YesNoCancelDialog.YES)) { 
            gui.getDataManager().getCourse().removeAssignment(assignment);
        }
    }
    
    
    
    
}
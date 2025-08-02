package lab6;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;


public class Main extends Application {

    private final ObservableList<String> tasks = FXCollections.observableArrayList();
    private ListView<String> taskListView;
    private TextField inputField;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("To-Do List");

        // a vertical box layout with 10px spacing between elements and 15px padding
        // in the edges of layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));



        //add new task to the list
        HBox inputBox = new HBox(10);
        inputField = new TextField();
        inputField.setPromptText("Enter a new task...");
        Button addButton = new Button("Add");
        addButton.setOnAction(this::handleAdd);
        inputBox.getChildren().addAll(inputField, addButton);

        // ListView to display tasks
        taskListView = new ListView<>(tasks);
        taskListView.setPrefHeight(200);

        // Button to remove selected task
        Button RemoveButton = new Button("Remove Selected");
        RemoveButton.setOnAction(this::handleRemove);



        // Button to save tasks to file
        Button SaveButton = new Button("Save");
        SaveButton.setOnAction(e -> saveTasksToFile());

        //ui elements
        root.getChildren().addAll(new Label("My Tasks:"), taskListView, inputBox, RemoveButton, SaveButton);

        // Set up and show the scene
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //add tasked when the add button is clicked
    private void handleAdd(ActionEvent event) {
        String task = inputField.getText().trim();
        if (!task.isEmpty()) {
            tasks.add(task);
            inputField.clear();
        }
    }

    //to remove the selected task
    private void handleRemove(ActionEvent event) {
        String selected = taskListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tasks.remove(selected);
        }
    }

    //file name for saving tasks
    private static final String FILE_NAME = "tasks.txt";

    private void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



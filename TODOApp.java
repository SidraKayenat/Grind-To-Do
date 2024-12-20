import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Task class
class Task {
    private static int idCounter = 0;
    private final int id;
    private String title;
    private String description;
    private String priority; // High, Medium, Low
    private String category;
    private String dueDate;

    public Task(String title, String description, String priority, String category, String dueDate) {
        this.id = ++idCounter;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s) Due: %s", priority, title, description, category, dueDate);
    }
}

public class TODOApp extends Application {

    private final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Grinder TODO Application");
    
        // Set the app to full screen
        primaryStage.setFullScreen(true);
    
        // Welcome Screen
        VBox welcomeRoot = new VBox(20);
        welcomeRoot.setStyle("-fx-alignment: center; -fx-background-color: #2b2b2b;");
    
        ImageView backgroundImage = new ImageView(new Image("file:background.jpg"));
        backgroundImage.setFitWidth(800);
        backgroundImage.setFitHeight(400);
        backgroundImage.setPreserveRatio(false);
    
        Button startButton = new Button("Let's Start");
        startButton.setStyle("-fx-background-color: #4da1a4; " +
                              "-fx-text-fill: white; " +
                              "-fx-font-size: 16px; " +
                              "-fx-font-weight: bold; " +   
                              "-fx-background-radius: 30px; " +   
                              "-fx-padding: 10px 50px; " +        
                              "-fx-alignment: center;");
    
        welcomeRoot.getChildren().addAll(backgroundImage, startButton);
    
        Scene welcomeScene = new Scene(welcomeRoot, 800, 400);
    
        // Main Application Screen
        HBox root = new HBox(20); // Use HBox for horizontal layout
        root.setPadding(new javafx.geometry.Insets(10));
        root.setStyle("-fx-background-color: black;");
    
        // Create the Banner/Navbar
        HBox navbar = new HBox(10);
        navbar.setStyle("-fx-background-color: #2b2b2b;");
    
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #4da1a4; " +
                              "-fx-text-fill: white; " +
                              "-fx-font-size: 16px; " +
                              "-fx-font-weight: bold; " +   
                              "-fx-background-radius: 30px; " +   
                              "-fx-padding: 5px 50px; " +        
                              "-fx-alignment: center;");
        backButton.setOnAction(e -> primaryStage.setScene(welcomeScene));
    
        // Add Image to Navbar
        ImageView navbarImage = new ImageView(new Image("file:bg2.png"));
        navbarImage.setFitHeight(642);
        navbarImage.setFitWidth(321);
        navbarImage.setPreserveRatio(false);

        navbar.getChildren().addAll(navbarImage, backButton);
    
        // Task list on the right side
        ListView<Task> taskListView = new ListView<>();
        taskListView.setStyle("-fx-control-inner-background: black; -fx-text-fill: white;");
    
        // Task input fields and buttons on the left side
        VBox inputFields = new VBox(10);
        inputFields.setStyle("-fx-background-color: #333; -fx-padding: 10px; -fx-border-color: #4da1a4; -fx-border-width: 2px;");
    
        TextField titleField = new TextField();
        titleField.setPromptText("Task Title");
    
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Task Description");
    
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("High", "Medium", "Low");
        priorityBox.setPromptText("Priority");
    
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
    
        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Due Date");
    
        Button addButton = new Button("Add Task");
        addButton.setStyle("-fx-background-color: #5CCD74; " +
                              "-fx-text-fill: white; " +
                              "-fx-font-size: 16px; " +
                              "-fx-font-weight: bold; " +   
                              "-fx-background-radius: 30px; " +   
                              "-fx-padding: 5px 60px; " +        
                              "-fx-alignment: center;");
        addButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String priority = priorityBox.getValue();
            String category = categoryField.getText();
            String dueDate = (dueDatePicker.getValue() != null) ? dueDatePicker.getValue().toString() : "Not Set";
    
            if (title.isEmpty() || priority == null || category.isEmpty()) {
                showAlert("Error", "Please fill in all fields!");
                return;
            }
    
            Task task = new Task(title, description, priority, category, dueDate);
            tasks.add(task);
            updateTaskList(taskListView);
    
            titleField.clear();
            descriptionField.clear();
            priorityBox.getSelectionModel().clearSelection();
            categoryField.clear();
            dueDatePicker.setValue(null);
        });
    
        Button deleteButton = new Button("Delete Task");
        deleteButton.setStyle("-fx-background-color: #FB7777; " +
                              "-fx-text-fill: white; " +
                              "-fx-font-size: 16px; " +
                              "-fx-font-weight: bold; " +   
                              "-fx-background-radius: 30px; " +   
                              "-fx-padding: 5px 50px; " +        
                              "-fx-alignment: center;");
        deleteButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                tasks.remove(selectedTask);
                updateTaskList(taskListView);
            } else {
                showAlert("Error", "No task selected!");
            }
        });
    
        inputFields.getChildren().addAll(titleField, descriptionField, priorityBox, categoryField, dueDatePicker, addButton, deleteButton);
    
        // Add navbar and task sections to the root HBox
        root.getChildren().addAll(navbar, inputFields, taskListView);
    
        Scene mainScene = new Scene(root, 800, 400);

    
        // Button to navigate to main screen
        startButton.setOnAction(e -> {
            
            primaryStage.setScene(mainScene);
            
            primaryStage.setFullScreen(true);
        });
    
        primaryStage.setScene(welcomeScene);
        primaryStage.show();
    }


    private void updateTaskList(ListView<Task> taskListView) {
        taskListView.getItems().clear();
        tasks.sort(Comparator.comparing(Task::getPriority));
        taskListView.getItems().addAll(tasks);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


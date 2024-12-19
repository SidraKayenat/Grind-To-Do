import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TODO List Application");

        // Layout
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(10));

        // Task List
        ListView<Task> taskListView = new ListView<>();

        // Input Fields
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

        // Buttons
        Button addButton = new Button("Add Task");
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
        deleteButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                tasks.remove(selectedTask);
                updateTaskList(taskListView);
            } else {
                showAlert("Error", "No task selected!");
            }
        });

        Button editButton = new Button("Edit Task");
        editButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                showEditTaskPopup(selectedTask, taskListView);
            } else {
                showAlert("Error", "No task selected!");
            }
        });

        Button openButton = new Button("Open Task");
        openButton.setOnAction(e -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                showTaskDetailsPopup(selectedTask);
            } else {
                showAlert("Error", "No task selected!");
            }
        });

        // Layout Arrangement
        HBox inputFields = new HBox(10, titleField, descriptionField, priorityBox, categoryField, dueDatePicker, addButton, deleteButton, editButton, openButton);
        root.getChildren().addAll(taskListView, inputFields);

        // Scene
        Scene scene = new Scene(root, 900, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateTaskList(ListView<Task> taskListView) {
        taskListView.getItems().clear();
        tasks.sort(Comparator.comparing(Task::getPriority));
        taskListView.getItems().addAll(tasks);
    }

    private void showEditTaskPopup(Task task, ListView<Task> taskListView) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Task");

        VBox editRoot = new VBox(10);
        editRoot.setPadding(new javafx.geometry.Insets(10));

        TextField titleField = new TextField(task.getTitle());
        TextField descriptionField = new TextField(task.getDescription());
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("High", "Medium", "Low");
        priorityBox.setValue(task.getPriority());
        TextField categoryField = new TextField(task.getCategory());
        DatePicker dueDatePicker = new DatePicker();
        if (!task.getDueDate().equals("Not Set")) {
            dueDatePicker.setValue(java.time.LocalDate.parse(task.getDueDate()));
        }

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            task.setTitle(titleField.getText());
            task.setDescription(descriptionField.getText());
            task.setPriority(priorityBox.getValue());
            task.setCategory(categoryField.getText());
            task.setDueDate((dueDatePicker.getValue() != null) ? dueDatePicker.getValue().toString() : "Not Set");
            updateTaskList(taskListView);
            editStage.close();
        });

        editRoot.getChildren().addAll(new Label("Title"), titleField, new Label("Description"), descriptionField, new Label("Priority"), priorityBox, new Label("Category"), categoryField, new Label("Due Date"), dueDatePicker, saveButton);

        Scene editScene = new Scene(editRoot, 300, 450);
        editStage.setScene(editScene);
        editStage.show();
    }

    private void showTaskDetailsPopup(Task task) {
        Stage detailsStage = new Stage();
        detailsStage.setTitle("Task Details");

        VBox detailsRoot = new VBox(10);
        detailsRoot.setPadding(new javafx.geometry.Insets(10));

        Label titleLabel = new Label("Title: " + task.getTitle());
        Label descriptionLabel = new Label("Description: " + task.getDescription());
        Label priorityLabel = new Label("Priority: " + task.getPriority());
        Label categoryLabel = new Label("Category: " + task.getCategory());
        Label dueDateLabel = new Label("Due Date: " + task.getDueDate());

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> detailsStage.close());

        detailsRoot.getChildren().addAll(titleLabel, descriptionLabel, priorityLabel, categoryLabel, dueDateLabel, closeButton);

        Scene detailsScene = new Scene(detailsRoot, 300, 250);
        detailsStage.setScene(detailsScene);
        detailsStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

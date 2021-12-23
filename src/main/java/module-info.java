module com.example.java_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.java_project to javafx.fxml;
    exports com.example.java_project;
}
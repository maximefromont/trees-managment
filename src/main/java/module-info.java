module com.example.java_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires opencsv;


    opens com.example.java_project to javafx.fxml;
    exports com.example.java_project;
}
module com.example.library2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.library2 to javafx.fxml;
    exports com.example.library2;
}
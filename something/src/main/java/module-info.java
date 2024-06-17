module com.example.java_dato_kuknishvili {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires static lombok;
    requires java.sql;

    opens com.example.java_dato_kuknishvili to javafx.fxml;
    exports com.example.java_dato_kuknishvili;
}
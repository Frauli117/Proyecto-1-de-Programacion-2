module proyecto.proyecto1kennethfrauli {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens proyecto.proyecto1kennethfrauli to javafx.fxml;
    exports proyecto.proyecto1kennethfrauli;
}

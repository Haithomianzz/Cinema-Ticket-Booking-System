module com.company {
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires java.naming;
    requires jdk.jshell;
    requires jdk.compiler;
    requires com.microsoft.sqlserver.jdbc;
    requires javatuples;
    requires org.controlsfx.controls;
    requires de.jensd.fx.glyphs.materialicons;
    requires java.xml.crypto;
    opens com.Frontend to javafx.fxml;

    opens com.Backend.Entities to javafx.base;
    opens com.Frontend.controllers to javafx.fxml;
    opens com.Frontend.controllers.Customer.Pages to javafx.fxml;
    opens com.Frontend.controllers.Admin.Pages to javafx.fxml;
    opens com.Frontend.controllers.Admin.Forms to javafx.fxml;
    opens com.Frontend.controllers.Customer.Cards to javafx.fxml; // Fix applied here
    opens com.Frontend.controllers.Customer.Forms to javafx.fxml;
    exports com.Backend.Dao;
    opens com.Backend.Dao to javafx.fxml;
    opens com.Backend to javafx.fxml;
    exports com.Frontend;
    exports com.Backend;

}
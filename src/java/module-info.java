module com.company {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires java.naming;
    requires jdk.jshell;
    requires jdk.compiler;
    requires com.microsoft.sqlserver.jdbc;
    requires java.xml;
    requires fontawesomefx;
    requires javatuples;
    requires org.controlsfx.controls;
    opens com.Frontend to javafx.fxml;

    opens com.Backend.Entities to javafx.base;
    opens com.Frontend.controllers to javafx.fxml;
    opens com.Frontend.controllers.Customer.Pages to javafx.fxml;
    opens com.Frontend.controllers.Customer.Cards to javafx.fxml; // Fix applied here
    exports com.Backend.Dao;
    opens com.Backend.Dao to javafx.fxml;
    opens com.Backend to javafx.fxml;
    exports com.Frontend;
    exports com.Backend;

}
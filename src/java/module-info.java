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
    requires com.google.zxing;
    requires com.google.zxing.javase;
    opens com.Frontend to javafx.fxml;
    opens com.Backend.Entities to javafx.base;
    opens com.Frontend.controllers to javafx.fxml,javafx.base;
    opens com.Frontend.controllers.Customer.Pages to javafx.fxml,javafx.base;
    opens com.Frontend.controllers.Admin.Pages to javafx.fxml,javafx.base;
    opens com.Frontend.controllers.Admin.Forms to javafx.fxml,javafx.base;
    opens com.Frontend.controllers.Customer.Cards to javafx.fxml,javafx.base; // Fix applied here
    opens com.Frontend.controllers.Customer.Forms to javafx.fxml,javafx.base;

    exports com.Backend.Dao;
    opens com.Backend.Dao to javafx.fxml;
    opens com.Backend to javafx.fxml;
    exports com.Frontend;
    exports com.Backend;

}
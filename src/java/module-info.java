module com.company {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires java.naming;
    requires jdk.jshell;
    requires jdk.compiler;
    requires com.microsoft.sqlserver.jdbc;
    requires javatuples;


    opens com.Frontend to javafx.fxml;
    opens com.Backend.Entities to javafx.base;

    exports com.Frontend;
    exports com.Backend.Dao;
    opens com.Backend.Dao to javafx.fxml;
    exports com.Backend;
    opens com.Backend to javafx.fxml;
}
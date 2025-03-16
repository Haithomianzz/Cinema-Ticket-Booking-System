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


    opens com.client to javafx.fxml;
    opens com.Entities to javafx.base;

    exports com.client;
}
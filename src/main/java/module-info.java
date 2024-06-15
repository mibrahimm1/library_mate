module com.cargologix.library_mate {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires com.jfoenix;

    opens com.cargologix.library_mate to javafx.fxml;

    exports com.cargologix.library_mate;
    exports com.cargologix.library_mate.controllers;
    opens com.cargologix.library_mate.controllers to javafx.fxml;
    exports com.cargologix.library_mate.extra;
    opens com.cargologix.library_mate.extra to javafx.fxml;
}
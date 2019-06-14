module fxformgenerator {
    requires javafx.controls;

    requires java.desktop; // for java.beans
    requires java.validation; // automatic module name of validation-api, to be found in MANIFEST.MF
    requires javax.el.api;

    exports org.fxformgenerator.core;
    exports org.fxformgenerator.core.readonly;

    // has to be commented-in if deep reflection is needed by users of FXFormGenerator
    opens org.fxformgenerator.core;
    opens org.fxformgenerator.core.readonly;


    // allow execution of samples, hide the related packages when FXFormGenerator is used as a library
    opens org.fxformgenerator.samples;
    opens org.fxformgenerator.samples.models;

}
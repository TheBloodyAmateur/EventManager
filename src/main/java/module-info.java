module com.github.thebloodyamateur.eventmanager {
    requires java.base;
    requires com.fasterxml.jackson.databind;
    requires static lombok;

    exports com.github.eventmanager;
    exports com.github.eventmanager.filehandlers;
    exports com.github.eventmanager.formatters;
}
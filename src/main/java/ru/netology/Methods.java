package ru.netology;

public enum Methods {
        GET("GET"),
        POST("POST"),
        DELETE("DELETE");

        private final String nameMethods;

        public String getNameMethods() {
                return nameMethods;
        }

        Methods(String nameMethods) {
                this.nameMethods = nameMethods;
        }
}
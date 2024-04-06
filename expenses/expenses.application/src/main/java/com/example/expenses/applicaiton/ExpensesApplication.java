package com.example.expenses.applicaiton;

public class ExpensesApplication {
    /**
     * ```
     * javac module-info.java src/main/java/com/example/expenses/applicaiton/ExpensesApplication.java -d target
     * jar --create --file=expenses.jar --main-class=com.example.expenses.applicaiton.ExpensesApplication --verbose -C target .
     * java --module-path expenses.jar --module expenses.application/com.example.expenses.applicaiton.ExpensesApplication
     *
     * jar tf
     * jar xf expenses.jar META-INF/MANIFEST.MF // to verify main class
     *
     *
     * // in root where master pom.xml, build both modules and run the app
     * mvn package
     * java --module-path ./expenses.application/target/expenses.application-1.0.jar:expenses.readers/target/expenses.readers-1.0.jar \
     *      --module expenses.application/com.example.expenses.applicaiton.ExpensesApplication
     * ```
     */
    public static void main(String[] args) {
        System.out.println("YAY");
    }
}

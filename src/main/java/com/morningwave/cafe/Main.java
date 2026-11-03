package com.morningwave.cafe;

import com.morningwave.cafe.db.DBConnection;
import com.morningwave.cafe.db.JpaUtil;
import com.morningwave.cafe.demo.CrudDemo;
import com.morningwave.cafe.demo.InheritanceDemo;
import com.morningwave.cafe.demo.JdbcDemo;
import com.morningwave.cafe.demo.JpaDemo;
import com.morningwave.cafe.exception.MenuException;
import com.morningwave.cafe.ui.ConsoleMenuApplication;

public class Main {

    public static void main(String[] args) {
        DBConnection database = DBConnection.getInstance();
        try {
            database.initializeDatabase();
            String mode = args.length == 0 ? "" : args[0];
            switch (mode) {
                case "--inheritance-demo" -> InheritanceDemo.run();
                case "--crud-demo" -> CrudDemo.run();
                case "--jdbc-demo" -> JdbcDemo.run();
                case "--hibernate-demo" -> runHibernateDemo();
                default -> new ConsoleMenuApplication().run();
            }
        } catch (MenuException e) {
            throw new IllegalStateException("Could not run the application", e);
        } finally {
            database.close();
        }
    }

    private static void runHibernateDemo() throws MenuException {
        JpaUtil jpa = JpaUtil.getInstance();
        try {
            JpaDemo.run();
        } finally {
            jpa.close();
        }
    }
}

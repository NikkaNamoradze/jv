package com.example.java_dato_kuknishvili;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Getter
public class DBUtils {
    private static DBUtils instance;
    private Connection connection;

    private DBUtils() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/books";
        String user = "root";
        String password = "";
        connection = DriverManager.getConnection(url, user, password);
    }

    public static DBUtils getInstance() throws SQLException {
        if (instance == null) {
            instance = new DBUtils();
        } else if (instance.getConnection().isClosed()) {
            instance = new DBUtils();
        }
        return instance;
    }

    public void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = "jdbc:mysql://localhost:3306/books";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public void insertProduct(String bookName, String bookAuthor, int bookPrice) throws SQLException {
        String sql = "INSERT INTO books_table (book_name, book_author, book_price) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, bookName);
            pstmt.setString(2, bookAuthor);
            pstmt.setInt(3, bookPrice);
            pstmt.executeUpdate();
        }
    }

    public ResultSet getAllBooks() throws SQLException {
        String sql = "SELECT * FROM books_table";
        return connection.createStatement().executeQuery(sql);
    }

    public void removeProduct(String bookId) throws SQLException {
        String sql = "DELETE FROM books_table WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            pstmt.executeUpdate();
        }
    }
}
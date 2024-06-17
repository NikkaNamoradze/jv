package com.example.java_dato_kuknishvili;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            DBUtils.getInstance().openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Label formLabel = new Label("Add Product");
        formLabel.setStyle("-fx-font-size: 25; -fx-font-weight: bold");

        Label bookNameLabel = new Label("Book Name");
        Label bookAuthorLabel = new Label("Book Author");
        Label bookPriceLabel = new Label("Price");

        TextField nameField = new TextField();
        nameField.setStyle("-fx-min-width: 200; -fx-min-height: 40");
        TextField authorField = new TextField();
        authorField.setStyle("-fx-min-width: 200; -fx-min-height: 40");
        TextField priceField = new TextField();
        priceField.setStyle("-fx-min-width: 200; -fx-min-height: 40");

        Label feedbackLabel = new Label();
        feedbackLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold");

        Button addButton = new Button("Add Product");
        addButton.setStyle("-fx-text-fill: #fff; -fx-background-color: #0033ff; -fx-min-height: 40; -fx-cursor: hand");
        addButton.setMaxWidth(Double.MAX_VALUE);

        VBox bookListBox = new VBox();
        bookListBox.setSpacing(10);
        try {
            bookListBox.getChildren().addAll(createBookList(bookListBox));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        addButton.setOnAction(actionEvent -> {
            String bookName = nameField.getText();
            String bookAuthor = authorField.getText();
            String priceText = priceField.getText();

            if (bookName.isEmpty() || bookAuthor.isEmpty() || priceText.isEmpty()) {
                feedbackLabel.setText("All fields are required!");
                feedbackLabel.setStyle("-fx-text-fill: red");
                return;
            }

            try {
                int bookPrice = Integer.parseInt(priceText);
                DBUtils.getInstance().insertProduct(bookName, bookAuthor, bookPrice);
                feedbackLabel.setText("Product added successfully!");
                feedbackLabel.setStyle("-fx-text-fill: green");
                nameField.clear();
                authorField.clear();
                priceField.clear();
                bookListBox.getChildren().clear();
                bookListBox.getChildren().addAll(createBookList(bookListBox));
            } catch (NumberFormatException e) {
                feedbackLabel.setText("Price must be a valid number!");
                feedbackLabel.setStyle("-fx-text-fill: red");
            } catch (SQLException e) {
                feedbackLabel.setText("Failed to add product!");
                feedbackLabel.setStyle("-fx-text-fill: red");
                e.printStackTrace();
            }
        });

        VBox bookNameBox = new VBox(8, bookNameLabel, nameField);
        VBox bookAuthorBox = new VBox(8, bookAuthorLabel, authorField);
        VBox bookPriceBox = new VBox(8, bookPriceLabel, priceField);

        VBox formFields = new VBox(12, bookNameBox, bookAuthorBox, bookPriceBox);

        VBox form = new VBox(30, formLabel, formFields, addButton, feedbackLabel);
        form.setPadding(new Insets(40, 150, 0, 150));
        form.setStyle("-fx-start-margin: 50");
        VBox mainLayout = new VBox(50, form);

        ScrollPane scrollView = new ScrollPane(bookListBox);
        scrollView.setFitToWidth(true);

        mainLayout.getChildren().add(scrollView);

        Scene scene = new Scene(mainLayout, 900, 800);
        stage.setTitle("Books!");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createBookList(VBox bookListBox) throws SQLException {
        VBox bookList = new VBox();
        ResultSet rs = DBUtils.getInstance().getAllBooks();

        while (rs.next()) {
            String bookId = rs.getString("id");
            String bookName = rs.getString("book_name");
            String bookAuthor = rs.getString("book_author");
            int bookPrice = rs.getInt("book_price");

            Label bookLabel = new Label("Name: " + bookName + " | Author: " + bookAuthor + " | Price: " + bookPrice);
            Button removeButton = new Button("Remove");
            removeButton.setStyle("-fx-background-color: #CE0D47FF; -fx-text-fill: #FFF");
            removeButton.setOnAction(actionEvent -> {
                try {
                    DBUtils.getInstance().removeProduct(bookId);
                    bookListBox.getChildren().clear();
                    bookListBox.getChildren().addAll(createBookList(bookListBox));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            HBox bookItem = new HBox(10, bookLabel, removeButton);
            bookItem.setPadding(new Insets(10));
            bookItem.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 10; -fx-alignment: center; -fx-border-width: 1;");

            bookList.getChildren().add(bookItem);
        }

        return bookList;
    }

    public static void main(String[] args) {
        launch();
    }
}
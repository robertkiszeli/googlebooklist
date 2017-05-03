package com.robertkiszelirk.googlebooksearch;

import java.util.ArrayList;

class Book {

    private final ArrayList<String> bookAuthor;
    private final String bookTitle;
    private final String bookPublishedDate;
    private final String bookThumbnail;
    private final String bookLink;

    Book(ArrayList<String> bookAuthor,
         String bookTitle,
         String bookPublishedDate,
         String bookThumbnail,
         String bookLink) {
        this.bookAuthor = bookAuthor;
        this.bookTitle = bookTitle;
        this.bookPublishedDate = bookPublishedDate;
        this.bookThumbnail = bookThumbnail;
        this.bookLink = bookLink;
    }

    ArrayList<String> getBookAuthor() {
        return bookAuthor;
    }

    String getBookTitle() {
        return bookTitle;
    }

    String getBookPublishedDate() {
        return bookPublishedDate;
    }

    String getBookUrl() {
        return bookThumbnail;
    }

    String getBookLink() {
        return bookLink;
    }
}

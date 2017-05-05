package com.robertkiszelirk.googlebooksearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

class BookAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<Book> booksList;

    // Set base data
    public BookAdapter(Context context, ArrayList<Book> booksList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.booksList = booksList;
    }

    // Get list size
    @Override
    public int getCount() {
        return booksList.size();
    }

    // Get current item
    @Override
    public Object getItem(int position) {
        return booksList.get(position);
    }

    // Get item position
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Creaete row view
        View rowView = layoutInflater.inflate(R.layout.search_list_item_row, parent, false);

        TextView bookAuthor = (TextView) rowView.findViewById(R.id.search_list_item_book_author);
        TextView bookTitle = (TextView) rowView.findViewById(R.id.search_list_item_book_title);
        TextView bookPublisedDate = (TextView) rowView.findViewById(R.id.search_list_item_book_published_date);
        ImageView bookImage = (ImageView) rowView.findViewById(R.id.search_list_item_book_image);
        // Create book data
        Book book = (Book) getItem(position);
        // Set parameters
        String authors = "";
        for (int i = 0; i < book.getBookAuthor().size(); i++) {
            authors = authors + book.getBookAuthor().get(i);
        }
        bookAuthor.setText(authors);
        bookTitle.setText(book.getBookTitle());
        bookPublisedDate.setText(book.getBookPublishedDate());
        if (book.getBookUrl() != null) {
            Glide.with(context).load(book.getBookUrl()).into(bookImage);
        }
        // Return view
        return rowView;
    }
}

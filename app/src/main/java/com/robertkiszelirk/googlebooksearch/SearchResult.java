package com.robertkiszelirk.googlebooksearch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {

    private static final String LOG_TAG = SearchResult.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);
        // Set list view list
        ListView searchResultList = (ListView) findViewById(R.id.search_result_list);
        // Extract books from json
        final ArrayList<Book> booksList = extractBooksFromJson(getIntent().getStringExtra("json"));
        // If has book
        if(booksList != null) {
            // Create book adapter
            BookAdapter bookAdapter = new BookAdapter(getBaseContext(), booksList);
            // Populate list view
            searchResultList.setAdapter(bookAdapter);
            // Handl onClick
            searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(booksList.get(position).getBookLink()));
                    startActivity(i);
                }
            });
        }else{
            // If there is no book
            Toast.makeText(this, R.string.empty_search, Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Book> extractBooksFromJson(String json) {
        // Books List
        ArrayList<Book> booksJsonList = new ArrayList<>();
        try{
            JSONObject mainJsonObject = new JSONObject(json);
            // Check if it has books
            int totalItems = mainJsonObject.getInt(getString(R.string.total_items));
            if(totalItems == 0){return null;}

            JSONArray bookItems = mainJsonObject.getJSONArray(getString(R.string.items));
            // Get books
            for(int i = 0; i < bookItems.length(); i++){
                JSONObject volumeInfo = bookItems.getJSONObject(i).getJSONObject(getString(R.string.volume_info));
                ArrayList<String> bookAuthors = new ArrayList<>();
                // Check if volumeInfo has authors
                if(volumeInfo.has(getString(R.string.authors))) {
                    JSONArray authors = volumeInfo.getJSONArray(getString(R.string.authors));
                    for (int j = 0; j < authors.length(); j++) {
                        bookAuthors.add(authors.get(j).toString());
                    }
                }
                // Get book title
                String bookTitle = volumeInfo.getString(getString(R.string.title));
                // Get publis date
                String bookPublishedDate = volumeInfo.getString(getString(R.string.publish_date));
                // Get image link
                String bookThumbnail;
                if(volumeInfo.has(getString(R.string.image_links))){
                    bookThumbnail = volumeInfo.getJSONObject(getString(R.string.image_links)).getString(getString(R.string.thumbnail));
                }else{
                    bookThumbnail = null;
                }
                // Get book link
                String bookLink;
                if(volumeInfo.has(getString(R.string.book_link))){
                    bookLink = volumeInfo.getString(getString(R.string.book_link));
                }else{
                    bookLink = null;
                }
                // Create and add current book
                Book book = new Book(bookAuthors,bookTitle,bookPublishedDate,bookThumbnail,bookLink);
                booksJsonList.add(book);
            }
        }catch(JSONException e){
            Log.e(LOG_TAG,getString(R.string.json_error),e);
        }
        return booksJsonList;
    }
}

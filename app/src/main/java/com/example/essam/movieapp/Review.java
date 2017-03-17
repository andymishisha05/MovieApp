package com.example.essam.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by essam on 12/2/2016.
 */

public class Review implements Parcelable {

    private String id;
    private String author;
    private String content;
    private String url;
   // public Review(String Id, String Author, String Content, String Url) {
      //  this.id = Id;
       // this.author = Author;
       // this.content = Content;
        //this.url = Url;
   // }

    public Review() {

    }
    protected Review(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };


    public Review(JSONObject trailer) throws JSONException {
        this.id = trailer.getString("id");
        this.author = trailer.getString("author");
        this.content = trailer.getString("content");
    }

    public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }

    public String getAuthor() { return this.author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getUrl() { return this.url; }

    public void setUrl(String url) { this.url = url; }
    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }
    public static class RevieweResult {
        private List<Review> results;

        public List<Review> getResults() {
            return results;
        }
    }

}






package edu.calstatela.cs594.rickbennett.newsapp;

/**
 * Created by Rick on 7/5/17.
 */
public class NewsItem {
    private String author, description, title, url, urlToImage, timePublished;

    public NewsItem (String author, String description, String title, String url, String urlToImage, String timePublished) {
        this.author = author;
        this.description = description;
        this.title = title;
        this.url = url;
        this.urlToImage = urlToImage;
        this.timePublished = timePublished;
    }

    public String getAuthor() { return author; }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getTimePublished() {
        return timePublished;
    }

    public void setTimePublished(String timePublished) {
        this.timePublished = timePublished;
    }
}

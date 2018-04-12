package gravityfalls.library.objects;

public class Book {
    private String author;
    private String country;
    private String imageLink;
    private String language;
    private String link;
    private int pages;
    private String title;
    private String year;

    public Book(){

    }

    public Book(String author, String country, String imageLink, String language, String link, int pages, String title, String year) {
        this.author = author;
        this.country = country;
        this.imageLink = imageLink;
        this.language = language;
        this.link = link;
        this.pages = pages;
        this.title = title;
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public String getCountry() {
        return country;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getLanguage() {
        return language;
    }

    public String getLink() {
        return link;
    }

    public int getPages() {
        return pages;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }
}

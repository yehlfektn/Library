package gravityfalls.library.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String author;
    private boolean available;
    private String country;
    private String imageLink;
    private String language;
    private String link;
    private int pages;
    private String short_description;
    private String title;
    private String year;
    private String onUser;
    private String category;
    private int position;
    private String date_taken;

    public Book(){

    }

    public Book(String author, boolean available, String country, String imageLink, String language, String link, int pages, String short_description, String title, String year, String onUser, String category, int position, String date_taken) {
        this.author = author;
        this.available = available;
        this.country = country;
        this.imageLink = imageLink;
        this.language = language;
        this.link = link;
        this.pages = pages;
        this.short_description = short_description;
        this.title = title;
        this.year = year;
        this.onUser = onUser;
        this.category = category;
        this.position = position;
        this.date_taken = date_taken;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDate_taken() {
        return date_taken;
    }

    public int getPosition() {
        return position;
    }

    public void setDate_taken(String date_taken) {
        this.date_taken = date_taken;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setOnUser(String onUser) {
        this.onUser = onUser;
    }

    public String getOnUser() {
        return onUser;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(String year) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeString(this.country);
        dest.writeString(this.imageLink);
        dest.writeString(this.language);
        dest.writeString(this.link);
        dest.writeInt(this.pages);
        dest.writeString(this.short_description);
        dest.writeString(this.title);
        dest.writeString(this.year);
        dest.writeString(this.onUser);
        dest.writeString(this.category);
    }

    protected Book(Parcel in) {
        this.author = in.readString();
        this.available = in.readByte() != 0;
        this.country = in.readString();
        this.imageLink = in.readString();
        this.language = in.readString();
        this.link = in.readString();
        this.pages = in.readInt();
        this.short_description = in.readString();
        this.title = in.readString();
        this.year = in.readString();
        this.onUser = in.readString();
        this.category = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}

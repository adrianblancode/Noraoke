package co.adrianblan.noraoke;

/**
 * Created by Adrian on 2015-03-27.
 */
public class SongItem {

    private String title;
    private String artist;
    private String image;

    public SongItem(){
        this.title = "";
        this.artist = "";
        this.image = "";
    };

    public SongItem(String title, String artist){
        this.title = title;
        this.artist = artist;
        this.image = "";
    };

    public SongItem(String title, String artist, String image){
        this.title = title;
        this.artist = artist;
        this.image = image;
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

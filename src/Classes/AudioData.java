package Classes;

/**
 *Audiovizuális anyag osztálya
 */
public class AudioData {
    /**
    *Mesterséges ID
    */
    int Id;
    /**
     *Anyag neve
     */
    String Name;
    /**
     *Előadó neve
     */
    String Artist;
    /**
     *Kiadás éve
     */
    int ReleaseYear;
    /**
     *Anyag zenei stílusa
     */
    String Style;
    /**
     *Anyag tárolási típusa
     */
    Enums.Audiotype Type;
    /**
     *Kölcsönözhető-e
     */
    Boolean Borrowable;
    /**
     *Kölcsönző neve
     */
    MemberData Borrower;
    /**
     *Van-e borítókép
     */
    Boolean HasCover;
    /**
     *Van-e hanganyag tárolva hozzá
     */
    Boolean HasAudio;

    /**
     * getterek
     */
    public int getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getArtist() {
        return Artist;
    }

    public int getReleaseYear() {
        return ReleaseYear;
    }

    public String getStyle() {
        return Style;
    }

    public Enums.Audiotype getType() {
        return Type;
    }

    public boolean getBorrowable() {
        return Borrowable;
    }


    public MemberData getBorrower() {
        return Borrower;
    }

    /**
     * setterek
     */

    public void setName(String name) {
        Name = name;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public void setReleaseYear(int releaseYear) {
        ReleaseYear = releaseYear;
    }

    public void setStyle(String style) {
        Style = style;
    }

    public void setType(Enums.Audiotype type) {
        Type = type;
    }

    public void setBorrowable(boolean borrowable) {
        Borrowable = borrowable;
    }


    public void setBorrower(MemberData borrower) {
        Borrower = borrower;
    }

    public AudioData(int id, String name, String artist, Integer releaseyear, String style, Enums.Audiotype type, Boolean borrowable, MemberData borrower){
        Id=id;
        Name=name;
        Artist=artist;
        ReleaseYear=releaseyear;
        Style=style;
        Type=type;
        Borrowable=borrowable;
        Borrower=borrower;
    }
    public AudioData(String name, String artist, Integer releaseyear, String style, Enums.Audiotype type){
        Id=0;
        Name=name;
        Artist=artist;
        ReleaseYear=releaseyear;
        Style=style;
        Type=type;
        Borrowable=false;
        Borrower=null;
    }
}

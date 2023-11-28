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
     *Kölcsönző IDja
     */
    int BorrowerId;
    /**
     *Kölcsönző neve
     */
    String BorrowerName;
    /**
     *Van-e borítókép
     */
    Boolean HasCover;
    /**
     *Van-e hanganyag tárolva hozzá
     */
    Boolean HasAudio;

    /**
     * konstruktor
     */
    public AudioData(){
        Borrowable = true;
    }

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

    public boolean isBorrowable() {
        return Borrowable;
    }

    public int getBorrowerId() {
        return BorrowerId;
    }

    public String getBorrowerName() {
        return BorrowerName;
    }

    public boolean hasCover() {
        return HasCover;
    }

    public boolean hasAudio() {
        return HasAudio;
    }


    /**
     * setterek
     */
    public void setId(int id) {
        Id = id;
    }

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

    public void setBorrowerId(int borrowerId) {
        BorrowerId = borrowerId;
    }

    public void setBorrowerName(String borrowerName) {
        BorrowerName = borrowerName;
    }

    public void setHasCover(boolean hasCover) {
        HasCover = hasCover;
    }

    public void setHasAudio(boolean hasAudio) {
        HasAudio = hasAudio;
    }

    public AudioData(int id, String name, String artist, Integer releaseyear, String style, Enums.Audiotype type, Boolean borrowable){
        Id=id;
        Name=name;
        Artist=artist;
        ReleaseYear=releaseyear;
        Style=style;
        Type=type;
        Borrowable=borrowable;
    }
}

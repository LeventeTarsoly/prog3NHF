package classes;

/**
 * Audiovizuális anyag osztálya
 */
public class AudioData {
    /**
     * Mesterséges ID
     */
    final int Id;
    /**
     * Anyag neve
     */
    String Name;
    /**
     * Előadó neve
     */
    String Artist;
    /**
     * Kiadás éve
     */
    int ReleaseYear;
    /**
     * Anyag zenei stílusa
     */
    String Style;
    /**
     * Anyag tárolási típusa
     */
    Enums.Audiotype Type;
    /**
     * Kölcsönözhető-e
     */
    Boolean Borrowable;
    /**
     * Kölcsönző neve
     */
    MemberData Borrower;

    /**
     * Mesterséges ID gettere
     *
     * @return ID
     */
    public int getId() {
        return Id;
    }

    /**
     * Anyag nevének gettere
     *
     * @return név
     */
    public String getName() {
        return Name;
    }

    /**
     * Előadó nevének gettere
     *
     * @return Előadó neve
     */
    public String getArtist() {
        return Artist;
    }

    /**
     * Kiadás évének gettere
     *
     * @return Kiadás éve
     */
    public int getReleaseYear() {
        return ReleaseYear;
    }

    /**
     * Anyag stílusának gettere
     *
     * @return Anyag stílusa
     */
    public String getStyle() {
        return Style;
    }

    /**
     * Anyag típusának gettere
     *
     * @return Anyag típusa
     */
    public Enums.Audiotype getType() {
        return Type;
    }

    /**
     * Kölcsönözhetőség gettere
     *
     * @return Kölcsönözhetőség
     */
    public boolean getBorrowable() {
        return Borrowable;
    }


    /**
     * Kölcsönző MemberData gettere
     *
     * @return MemberDataként a kölcsönző
     */
    public MemberData getBorrower() {
        return Borrower;
    }

    /**
     * Anyag címének settere
     *
     * @param name az új cím
     */
    public void setName(String name) {
        Name = name;
    }

    /**
     * Előadó nevének settere
     *
     * @param artist az új előadó neve
     */
    public void setArtist(String artist) {
        Artist = artist;
    }

    /**
     * Kiadás évének settere
     *
     * @param releaseYear az új kiadási év
     */
    public void setReleaseYear(int releaseYear) {
        ReleaseYear = releaseYear;
    }

    /**
     * Anyag stílusának settere
     *
     * @param style az új stílus
     */
    public void setStyle(String style) {
        Style = style;
    }

    /**
     * Anyag típusának settere
     *
     * @param type az új típus
     */
    public void setType(Enums.Audiotype type) {
        Type = type;
    }

    /**
     * Kölcsönözhetőség settere
     *
     * @param borrowable az új kölcsönözhetőség
     */
    public void setBorrowable(boolean borrowable) {
        Borrowable = borrowable;
    }


    /**
     * Kölcsönző MemberData settere
     *
     * @param borrower az új kölcsönző MemberDatája
     */
    public void setBorrower(MemberData borrower) {
        Borrower = borrower;
    }

    /**
     * AudioData konstruktora
     *
     * @param id          az ID
     * @param name        a cím
     * @param artist      a előadó
     * @param releaseyear a kiadás éve
     * @param style       a stílus
     * @param type        a típus
     * @param borrowable  kölcsönözhetőség
     * @param borrower    kölcsönző
     */
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
}

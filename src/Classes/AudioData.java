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
}

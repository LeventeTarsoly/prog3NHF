package models;

import classes.AudioData;
import classes.MemberData;

import javax.swing.tree.*;
import java.util.List;

/**
 * A kölcsönzésekhez tartozó JTree Modelje
 */
public class BorrowModel extends DefaultTreeModel {
    /**
     * A fa gyökere
     */
    final DefaultMutableTreeNode root;
    /**
     * A tagok
     */
    final List<MemberData> members;
    /**
     * Az anyagok
     */
    final List<AudioData> audios;

    /**
     * Konstruktor
     *
     * @param root    a gyökér
     * @param members a tagok listája
     * @param audios  az anyagok lstája
     */
    public BorrowModel(DefaultMutableTreeNode root, List<MemberData> members, List<AudioData> audios) {
        super(root);
        this.root = root;
        this.members=members;
        this.audios = audios;
    }

    public DefaultMutableTreeNode getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        //ha a parent a gyökér, akkor az indexedik member kell
        if (parent == root)
            return members.get(index);
            //ha a parent member, akkor azt kell megkeresni, és annak az indexedik audioja kell
        else {
            for (MemberData member : members) {
                if (parent == member) {
                    for (AudioData audio:audios) {
                        if(audio.getId()==member.getBorroweds().get(index))
                            return audio.getName();
                    }
                }
            }
        }
        //3. eset, ha audio, de annak nincs gyereke
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        //ha a parent a gyökér, akkor a member lista hossza
        if (parent == root)
            return members.size();
            //ha a parent member, akkor azt kell megkeresni, és annak a jelenlegi kölcsönzések listája
        else {
            for (MemberData member : members) {
                if (parent == member) {
                    return member.getBorroweds().size();
                }
            }
        }
        //3. eset, ha audio, de annak nincs gyereke, tehát 0
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        //ha AudioData, akkor levél
        if(node.getClass()== AudioData.class)
            return true;
        //ha MemberData, akkor csak akkor levél, ha nincs kölcsönzése -> kölcsönzések lista üres
        return node.getClass() == MemberData.class && ((MemberData) node).getBorroweds().isEmpty();
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        //ha a parent a gyökér, akkor a member listában kell megnézni hanyadik
        if (parent == root)
            members.indexOf((MemberData) child);
            //ha a parent member, akkor azt kell megkeresni, és annak a jelenlegi
            // kölcsönzések listájában kell megnézni hanyadik
        else {
            for (MemberData member : members) {
                if (parent == member) {
                    for (AudioData audio: audios) {
                        if(audio.equals(child))
                            return member.getBorroweds().indexOf(audio.getId());
                    }
                }
            }
        }
        //3. eset, ha audio, de annak nincs gyereke, tehát 0
        return 0;
    }
}

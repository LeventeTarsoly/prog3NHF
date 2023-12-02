package Models;

import Classes.AudioData;
import Classes.MemberData;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;
import java.util.List;

public class BorrowModel extends DefaultTreeModel {
    DefaultMutableTreeNode root;
    List<MemberData> members;
    List<AudioData> audios;

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
        if (parent == root)
            return members.get(index);
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
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (parent == root)
            return members.size();
        else {
            for (MemberData member : members) {
                if (parent == member) {
                    return member.getBorroweds().size();
                }
            }
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        if(node.getClass()== AudioData.class)
            return true;
        if(node.getClass()== MemberData.class && ((MemberData) node).getBorroweds().isEmpty())
            return true;
        return false;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == root)
            members.indexOf((MemberData) child);
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
        return 0;
    }
}

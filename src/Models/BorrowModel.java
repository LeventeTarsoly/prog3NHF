package Models;

import Classes.AudioData;
import Classes.MemberData;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.*;
import java.util.List;

public class BorrowModel extends DefaultTreeModel {
    TreeNode root;
    List<MemberData> members;

    public BorrowModel(TreeNode root) {
        super(root);
        this.root = root;
    }

    public void setMembers(List<MemberData> members){this.members=members;}
    @Override
    public Object getRoot() {
        return root;
    }
//todo
    @Override
    public Object getChild(Object parent, int index) {
        if (parent == root)
            return members.get(index);
        else {
            for (MemberData member : members) {
                if (parent == member) {
                    return member.getBorroweds().get(index);
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
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }
}
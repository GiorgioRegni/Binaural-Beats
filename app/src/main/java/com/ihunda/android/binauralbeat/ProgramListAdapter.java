package com.ihunda.android.binauralbeat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProgramListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<CategoryGroup> groups;
    private LayoutInflater inflater;

    public ProgramListAdapter(Context context, ArrayList<CategoryGroup> groupes) {
        this.context = context;
        this.groups = groupes;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    public Object getChild(int gPosition, int cPosition) {
        return groups.get(gPosition).getObjets().get(cPosition);
    }

    public Program getChildProgram(int gPosition, int cPosition) {
        return groups.get(gPosition).getProgram().get(cPosition);
    }

    public long getChildId(int gPosition, int cPosition) {
        return cPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ProgramMeta objet = (ProgramMeta) getChild(groupPosition, childPosition);
//		Program p = DefaultProgramsBuilder.getProgram(objet);
        Program p = getChildProgram(groupPosition, childPosition);

        ChildViewHolder childViewHolder;

        if (convertView == null) {
            childViewHolder = new ChildViewHolder();

            convertView = inflater.inflate(R.layout.presetlist_group_child, null);

            childViewHolder.textViewChild = (TextView) convertView.findViewById(R.id.TVChild);
            childViewHolder.textViewChildDescription = (TextView) convertView.findViewById(R.id.TVChildDescription);
            childViewHolder.textViewChildTime = (TextView) convertView.findViewById(R.id.TVChildTime);

            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }


        childViewHolder.textViewChild.setText(p.getName());
        childViewHolder.textViewChildDescription.setText(p.getDescription());
        childViewHolder.textViewChildTime.setText(String.format("%sH%s",
                formatTimeNumberwithLeadingZero(p.getLength() / 60 / 60),
                formatTimeNumberwithLeadingZero((p.getLength() / 60) % 60)));

        return convertView;
    }

    public int getChildrenCount(int gPosition) {
        return groups.get(gPosition).getObjets().size();
    }

    public Object getGroup(int gPosition) {
        return groups.get(gPosition);
    }

    public int getGroupCount() {
        return groups.size();
    }

    public long getGroupId(int gPosition) {
        return gPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder gholder;

        CategoryGroup group = (CategoryGroup) getGroup(groupPosition);

        if (convertView == null) {
            gholder = new GroupViewHolder();

            convertView = inflater.inflate(R.layout.presetlist_group_row, null);

            gholder.textViewGroup = (TextView) convertView.findViewById(R.id.TVGroup);

            convertView.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag();
        }

        gholder.textViewGroup.setText(group.getNiceName());
        
       /* ExpandableListView eLV = (ExpandableListView) parent;
        if (eLV.isGroupExpanded(groupPosition) == false) {
        	eLV.expandGroup(groupPosition);
        }
        */
        return convertView;
    }

    public boolean hasStableIds() {
        return true;
    }

    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    class GroupViewHolder {
        public TextView textViewGroup;
    }

    class ChildViewHolder {
        public TextView textViewChild;
        public TextView textViewChildDescription;
        public TextView textViewChildTime;
    }

    private String formatTimeNumberwithLeadingZero(int t) {
        if (t > 9) {
            return String.format("%2d", t);
        } else {
            return String.format("0%1d", t);
        }
    }
}


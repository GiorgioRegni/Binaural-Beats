package com.ihunda.android.binauralbeat;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

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

	public long getChildId(int gPosition, int cPosition) {
		return cPosition;
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final ProgramMeta objet = (ProgramMeta) getChild(groupPosition, childPosition);
		
		ChildViewHolder childViewHolder;
		
        if (convertView == null) {
        	childViewHolder = new ChildViewHolder();
        	
            convertView = inflater.inflate(R.layout.presetlist_group_child, null);
            
            childViewHolder.textViewChild = (TextView) convertView.findViewById(R.id.TVChild);
            
            convertView.setTag(childViewHolder);
        } else {
        	childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        
        childViewHolder.textViewChild.setText(objet.getName());
        
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
        gholder.textViewGroup.getBackground().setAlpha(120);
        
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
	}

}


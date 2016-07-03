package com.abraxas.spps.alma;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.List;

public class GradeExpandableListAdapter extends BaseExpandableListAdapter {

    private String cookie;
    private Context context;
    private List<Assignment> assignments;

    public GradeExpandableListAdapter(Context context, List<Assignment> assignments, String cookie) {
        this.context = context;
        this.assignments = assignments;
        this.cookie = cookie;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return assignments.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.fragment_grade_list_item_layout, null);


        TextView taskDetail = (TextView) convertView.findViewById(R.id.grade_taskDetail);
        TextView feedback = (TextView) convertView.findViewById(R.id.grade_feedback);

        taskDetail.setText(assignments.get(groupPosition).getTaskDetail());

        ViewGroup viewGroup = (ViewGroup) convertView.findViewById(R.id.grade_link_insertion_point);
        if (assignments.get(groupPosition).getFile().size()!=0) {
            List<String> files = assignments.get(groupPosition).getFile();
            convertView.findViewById(R.id.grade_attachmentView).setVisibility(View.VISIBLE);
            for (int i = 0; i < files.size(); i++) {
                TextView fileLink = new TextView(this.context);
                String str = files.get(i);
                fileLink.setText(str.split(";")[0]);
                fileLink.setHint(str.split(";")[1]);
                fileLink.setOnClickListener(new ELAOnClickListener(str, cookie));
                fileLink.setClickable(true);
                fileLink.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                viewGroup.addView(fileLink, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        } else {
            convertView.findViewById(R.id.grade_attachmentView).setVisibility(View.GONE);
        }

        if (assignments.get(groupPosition).getTaskGrade() != null) {
            String feedbackString = assignments.get(groupPosition).getTaskGrade().getFeedback();

            if (feedbackString != null) {
                convertView.findViewById(R.id.grade_feedbackView).setVisibility(View.VISIBLE);
                feedback.setText(feedbackString);
            } else {
                convertView.findViewById(R.id.grade_feedbackView).setVisibility(View.GONE);
            }
        } else {
            convertView.findViewById(R.id.grade_feedbackView).setVisibility(View.GONE);
        }

        return convertView;
    }

    private String gregorianToString(GregorianCalendar gcal){
        return (gcal.get(GregorianCalendar.MONTH)+1)+"/"+gcal.get(GregorianCalendar.DATE)+"/"+(gcal.get(GregorianCalendar.YEAR)%2000);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.assignments.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.assignments.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment_grade_list_group_layout, null);
        }

        TextView dueDate = (TextView) convertView.findViewById(R.id.grade_dueDate);
        TextView taskTitle = (TextView) convertView.findViewById(R.id.grade_taskTitle);
        TextView taskCategoryTitle = (TextView) convertView.findViewById(R.id.grade_taskCategoryTitle);
        TextView statusMsg = (TextView) convertView.findViewById(R.id.grade_statusMsg);
        TextView weight = (TextView) convertView.findViewById(R.id.grade_weight);
        TextView taskGrade = (TextView) convertView.findViewById(R.id.grade_taskGrade);


        taskGrade.setText("-");
        if(assignments.get(groupPosition).getWeight()==-1){
            weight.setText("-");
        } else {
            taskGrade.setText(String.format("%.1f%%", assignments.get(groupPosition).getTaskGrade().getPercent()));
            weight.setText(String.format("%.1f%%", assignments.get(groupPosition).getWeight()));
        }

        dueDate.setText(gregorianToString(assignments.get(groupPosition).getDueDate()));
        taskTitle.setText(assignments.get(groupPosition).getTaskTitle());
        taskCategoryTitle.setText(assignments.get(groupPosition).getTaskCategory());
        statusMsg.setText(assignments.get(groupPosition).getStatusMsg());
        taskTitle.setWidth(context.getResources().getDisplayMetrics().widthPixels - (dueDate.getWidth() + convertView.findViewById(R.id.grade_extraInfo).getWidth()));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

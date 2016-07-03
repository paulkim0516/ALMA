package com.abraxas.spps.alma;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Course> classList;
    private int semester;
    private ArrayList<List<Assignment>> arrayList;
    private String cookie;

    public ExpandableListAdapter(Context context, ArrayList<Course> classList, int semester, String cookie) {
        Log.d("tag", "ELA");
        this.context = context;
        this.classList = classList;
        this.semester = semester;
        this.arrayList = new ArrayList<>();
        this.cookie = cookie;

        List<Assignment> assignments;
        List<Assignment> assignmentArrayList;

        for(int groupPosition = 0; groupPosition<this.classList.size(); groupPosition++) {
            assignmentArrayList = new ArrayList<>();
            if (semester == Course.FIRST_SEMESTER) {
                assignments = this.classList.get(groupPosition).getFirstSemester().getAssignmentList();

            } else {
                assignments = this.classList.get(groupPosition).getSecondSemester().getAssignmentList();

            }


            for (int i = assignments.size()-1; i>=0; i--) {
                if (!isAfter(Calendar.getInstance(), assignments.get(i).getDueDate())){
                    assignmentArrayList.add(assignments.get(i));
                }
            }
            arrayList.add(groupPosition, assignmentArrayList);
        }
    }

    private boolean isAfter(Calendar firstDate, Calendar secondDate){
        int y1 = firstDate.get(Calendar.YEAR);
        int y2 = secondDate.get(Calendar.YEAR);
        int m1 = firstDate.get(Calendar.MONTH);
        int m2 = secondDate.get(Calendar.MONTH);
        int d1 = firstDate.get(Calendar.DATE);
        int d2 = secondDate.get(Calendar.DATE);

        if (y1>y2){
            return true;
        } else if (y1==y2) {
            if (m1 > m2) {
                return true;
            } else if(m1==m2) {
                if (d1 > d2) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(semester==Course.FIRST_SEMESTER)
            return this.classList.get(groupPosition).getFirstSemester()
                    .getAssignmentList().get(childPosition);
        else
            return this.classList.get(groupPosition).getSecondSemester()
                    .getAssignmentList().get(childPosition);
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
        convertView = infalInflater.inflate(R.layout.fragment_main_list_item_layout, null);

        TextView dueDate = (TextView) convertView.findViewById(R.id.dueDate);
        TextView taskTitle = (TextView) convertView.findViewById(R.id.taskTitle);
        TextView taskCategoryTitle = (TextView) convertView.findViewById(R.id.taskCategoryTitle);
        TextView taskDetail = (TextView) convertView.findViewById(R.id.taskDetail);

        dueDate.setText(GregorianToString(arrayList.get(groupPosition).get(childPosition).getDueDate()));
        taskTitle.setText(arrayList.get(groupPosition).get(childPosition).getTaskTitle());
        taskCategoryTitle.setText(arrayList.get(groupPosition).get(childPosition).getTaskCategory());
        taskDetail.setText(arrayList.get(groupPosition).get(childPosition).getTaskDetail());

        ViewGroup viewGroup = (ViewGroup) convertView.findViewById(R.id.link_insertion_point);
        if (arrayList.get(groupPosition).get(childPosition).getFile().size()!=0) {
            List<String> files = arrayList.get(groupPosition).get(childPosition).getFile();
            convertView.findViewById(R.id.attachmentView).setVisibility(View.VISIBLE);
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
            convertView.findViewById(R.id.attachmentView).setVisibility(View.GONE);
        }
        return convertView;
    }

    private String GregorianToString(GregorianCalendar gcal){
        return (gcal.get(GregorianCalendar.MONTH)+1)+"/"+gcal.get(GregorianCalendar.DATE)+"/"+(gcal.get(GregorianCalendar.YEAR)%2000);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return arrayList.get(groupPosition).size()>3?3:arrayList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.classList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.classList.size();
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
            convertView = infalInflater.inflate(R.layout.fragment_main_list_group_layout, null);
        }

        TextView classPeriod = (TextView) convertView.findViewById(R.id.classPeriod);
        TextView classTime = (TextView) convertView.findViewById(R.id.classTime);
        TextView classTitle = (TextView) convertView.findViewById(R.id.classTitle);
        TextView classRoom = (TextView) convertView.findViewById(R.id.classRoom);
        TextView classTeacher = (TextView) convertView.findViewById(R.id.classTeacher);
        TextView classScore = (TextView) convertView.findViewById(R.id.classScore);

        if(semester==Course.FIRST_SEMESTER){
            classPeriod.setText(this.classList.get(groupPosition).getFirstSemester().getClassPeriod());
            classTime.setText(this.classList.get(groupPosition).getFirstSemester().getClassTime());
            classTitle.setText(this.classList.get(groupPosition).getFirstSemester().getClassTitle());
            classRoom.setText(this.classList.get(groupPosition).getFirstSemester().getRoomNumber());
            classTeacher.setText(this.classList.get(groupPosition).getFirstSemester().getTeacherName().toString());
            classScore.setText(this.classList.get(groupPosition).getFirstSemester().getOverallGrade().getLetterGrade());
        } else {
            classPeriod.setText(String.format("%d",this.classList.get(groupPosition).getSecondSemester().getClassPeriod()));
            classTime.setText(this.classList.get(groupPosition).getSecondSemester().getClassTime());
            classTitle.setText(this.classList.get(groupPosition).getSecondSemester().getClassTitle());
            classRoom.setText(this.classList.get(groupPosition).getSecondSemester().getRoomNumber());
            classTeacher.setText(this.classList.get(groupPosition).getSecondSemester().getTeacherName().toString());
            classScore.setText(this.classList.get(groupPosition).getSecondSemester().getOverallGrade().getLetterGrade());
        }

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

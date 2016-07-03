package com.abraxas.spps.alma;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GradeFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener {

    private ArrayList<Course> classObject;
    private int semester;
    private ExpandableListView exView;
    private OnFragmentInteractionListener mListener;
    private String cookie;
    private TextView overallGrade;
    private ArrayList<String> classTitleList;
    private ViewGroup categoryGradeList;
    private Spinner spinner;
    private TextView nothingToShow;
    private LinearLayout content;
    int lastIndex;
    private ArrayList<Integer> expandedGroups = new ArrayList<>();

    // TODO: Rename and change types and number of parameters
    public static GradeFragment newInstance(ArrayList<Course> param1, int semester) {
        GradeFragment fragment = new GradeFragment();
        Bundle args = new Bundle();
        args.putInt(LoginActivity.SEMESTER,semester);
        args.putSerializable(LoginActivity.SCHOOL_CLASS,param1);
        fragment.setArguments(args);
        return fragment;
    }

    public GradeFragment() {
        // Required empty public constructor
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            classObject = (ArrayList<Course>) getArguments().getSerializable(LoginActivity.SCHOOL_CLASS);
            semester = getArguments().getInt(LoginActivity.SEMESTER);
            cookie = getArguments().getString(LoginActivity.COOKIE);
            classTitleList = new ArrayList<>();
            for(int i=0; i<classObject.size(); i++){
                classTitleList.add(semester == Course.FIRST_SEMESTER ? classObject.get(i).getFirstSemester().getClassTitle() : classObject.get(i).getSecondSemester().getClassTitle());
            }
        }
        lastIndex = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        if(classObject.size()!=0) {
            exView = (ExpandableListView) view.findViewById(R.id.assignmentListView);
            overallGrade = (TextView) view.findViewById(R.id.overallGradeView);
            spinner = (Spinner) view.findViewById(R.id.classSpinner);
            nothingToShow = (TextView) view.findViewById(R.id.nothingToShow);
            content = (LinearLayout) view.findViewById(R.id.gradeContent);
            categoryGradeList = (ViewGroup) view.findViewById(R.id.categoryGradeView);

            ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, classTitleList);
            mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(mArrayAdapter);
            spinner.setOnItemSelectedListener(this);

            updateContents(lastIndex);
        } else {
            View gradeFragmentView = view.findViewById(R.id.gradeFragmentView);
            View tv = view.findViewById(R.id.textView2);
            gradeFragmentView.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        expandedGroups.clear();
        updateContents(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @SuppressLint("DefaultLocale")
    private void updateContents(int pos){
        SchoolClass sc;
        List<Assignment> assignments;
        List<Category> categories;

        categoryGradeList.removeAllViews();

        if(semester==Course.FIRST_SEMESTER){
            sc = classObject.get(pos).getFirstSemester();
        } else {
            sc = classObject.get(pos).getSecondSemester();
        }

        categories = sc.getCategory();

        LayoutInflater infalInflater = (LayoutInflater) this.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(Category category:categories) {
            View convertView = infalInflater.inflate(R.layout.grade_category_layout, null);
            TextView categoryProportion = (TextView) convertView.findViewById(R.id.categoryProportion);
            TextView categoryTitle = (TextView) convertView.findViewById(R.id.categoryTitle);
            TextView categoryScore = (TextView) convertView.findViewById(R.id.categoryScore);

            categoryProportion.setText(String.format("%.1f%% of Final Grade", category.getPercent()));
            categoryTitle.setText(category.getCategoryTitle());
            categoryScore.setText(String.format("%.1f%%", category.getGrade().getPercent()));
            categoryGradeList.addView(convertView);
        }



        assignments = sc.getAssignmentList();
        if(assignments.size()!=0) {
            content.setVisibility(View.VISIBLE);
            nothingToShow.setVisibility(View.GONE);
            exView.setAdapter(new GradeExpandableListAdapter(this.getContext(), assignments, cookie));
            overallGrade.setText(sc.getOverallGrade().getLetterGrade());
        } else {
            content.setVisibility(View.GONE);
            nothingToShow.setVisibility(View.VISIBLE);
        }
        lastIndex = pos;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

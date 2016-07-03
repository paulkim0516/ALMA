package com.abraxas.spps.alma;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ExpandableListView;

import com.roomorama.caldroid.*;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends android.support.v4.app.Fragment {
    private ArrayList<Course> classObject;
    private int semester;
    private String cookie;
    private ExpandableListView exView;
    private ArrayList<Integer> expandedGroups = new ArrayList<>();
    private SchoolCalendar schoolCalendar;
    private CalendarView calendarView;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(ArrayList<Course> param1, int semester, String cookie) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(LoginActivity.SEMESTER,semester);
        args.putSerializable(LoginActivity.SCHOOL_CLASS, param1);
        args.putString(LoginActivity.COOKIE, cookie);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarFragment() {
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
            schoolCalendar = (SchoolCalendar) getArguments().getSerializable(LoginActivity.CALENDAR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);

        calendarView.setShowWeekNumber(false);
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
        

        return view;
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

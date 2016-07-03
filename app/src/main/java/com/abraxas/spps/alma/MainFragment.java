package com.abraxas.spps.alma;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends android.support.v4.app.Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
    private ArrayList<Course> classObject;
    private int semester;
    private String cookie;
    private ExpandableListView exView;
    private ArrayList<Integer> expandedGroups = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(ArrayList<Course> param1, int semester) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(LoginActivity.SEMESTER,semester);
        args.putSerializable(LoginActivity.SCHOOL_CLASS,param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        exView = (ExpandableListView) view.findViewById(R.id.schedule_table);
        if(classObject.size()!=0) {
            exView.setAdapter(new ExpandableListAdapter(this.getContext(), classObject, semester, cookie));
        } else {
            exView.setVisibility(View.GONE);
            View tv = view.findViewById(R.id.textView);
            tv.setVisibility(View.VISIBLE);
        }
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

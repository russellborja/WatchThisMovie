package com.example.russellborja.watchthismovie.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.russellborja.watchthismovie.R;
import com.example.russellborja.watchthismovie.Utils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectionFragment.OnSelectionClickedListener} interface
 * to handle interaction events.
 */
public class SelectionFragment extends Fragment {

    private OnSelectionClickedListener mListener;
    private final String LOG_TAG = "SelectionFragment";
    public final int SELECTION_NUMBER = 4; //number of selection boxes
    //private boolean[] isBlank = new boolean[SELECTION_NUMBER];

    public SelectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        // Set button click listeners
        Button reset = (Button) getActivity().findViewById(R.id.reset_button);
        reset.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.listview_selected);
                int childViewcount = linearLayout.getChildCount();

                // find first available empty container
                for(int i=childViewcount; i >= 0; i--){
                    View view = linearLayout.getChildAt(i);
                    if(view instanceof ImageView){
                        view.setTag("blank");
                        ((ImageView) view).setImageResource(R.drawable.draganddrop);

                    }
                }
                Utils.removeAllSelections();
            }
        });
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSelectionClicked(uri);
        }
    }

    // displays movie poster in empty containers
    public void updateSelectionContainers(Bitmap poster){
        ImageView emptyImageView = (ImageView) getAvailableView();
        if (emptyImageView != null){
            if(poster == null){
                emptyImageView.setImageResource(R.drawable.poster_not_available);
            }
            else {
                emptyImageView.setImageBitmap(poster);
            }
            emptyImageView.setTag("filled");
        }

        else{
            Log.v(LOG_TAG, "Already filled");
            Toast.makeText(getActivity().getApplicationContext(), "Already filled", Toast.LENGTH_LONG).show();
        }
    }

    // retrieves the first empty selection container
    public View getAvailableView(){
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.listview_selected);
        int childViewcount = linearLayout.getChildCount();
        View availView = null;

        // find first available empty container
        for(int i=childViewcount; i >= 0; i--){
            View v = linearLayout.getChildAt(i);
            if(v instanceof ImageView && v.getTag().equals("blank")){
                availView = v;
            }
        }
        return availView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnSelectionClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
    public interface OnSelectionClickedListener {
        // TODO: Update argument type and name
        public void onSelectionClicked(Uri uri);
    }

}

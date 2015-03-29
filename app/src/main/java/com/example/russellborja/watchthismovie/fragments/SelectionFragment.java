package com.example.russellborja.watchthismovie.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.russellborja.watchthismovie.R;
import com.example.russellborja.watchthismovie.Utils;

import java.util.Random;


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
        Button randomize = (Button) getActivity().findViewById(R.id.randomize_button);

        // clears all selection boxes
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

        // randomly chooses one of the selected movies and displays it in a popup window
        randomize.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(Utils.getIntSelected() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please select movies to randomize", Toast.LENGTH_SHORT).show();
                }
                else{
                    //initialize random title, main activity view, popup view
                    int randNum = generateRandomNumber();
                    String randTitle = Utils.selectedViews[randNum];
                    ImageView selectionBox = null;
                    Bitmap randPoster;
                    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getBaseContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final FrameLayout mainActivityView = (FrameLayout) getActivity().findViewById(R.id.main_frame_layout);
                    View popupView = layoutInflater.inflate(R.layout.popup_movie,null);
                    ImageView popupImage = (ImageView) popupView.findViewById(R.id.chosen_movie_poster);
                    TextView popupText = (TextView) popupView.findViewById(R.id.chosen_movie_title);

                    // determine which bitmap poster to use
                    switch(randNum){
                        case 0:
                            selectionBox = (ImageView) getActivity().findViewById(R.id.first_selection);
                            break;
                        case 1:
                            selectionBox = (ImageView) getActivity().findViewById(R.id.second_selection);
                            break;
                        case 2:
                            selectionBox = (ImageView) getActivity().findViewById(R.id.third_selection);
                            break;
                        case 3:
                            selectionBox = (ImageView) getActivity().findViewById(R.id.fourth_selection);
                            break;
                        default:
                            break;
                    }
                    randPoster = ((BitmapDrawable) selectionBox.getDrawable()).getBitmap();

                    // load popup image and title
                    popupImage.setImageBitmap(randPoster);
                    popupText.setText(randTitle);

                    // generate popup window
                    final PopupWindow popupWindow = new PopupWindow(popupView, ActionBar.LayoutParams.MATCH_PARENT,
                            ActionBar.LayoutParams.MATCH_PARENT);
                    // initialize close button
                    Button btnDismiss = (Button)popupView.findViewById(R.id.close_popup);
                    btnDismiss.setOnClickListener(new Button.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            mainActivityView.getForeground().setAlpha(0);
                            popupWindow.dismiss();
                        }
                    });

                    // dim background and show popup
                    mainActivityView.getForeground().setAlpha(220);
                    popupWindow.showAtLocation(v, Gravity.CENTER, 0,0);

                }
            }
        });
    }


    // random number between 0 and (number of movies selected -1)
    public int generateRandomNumber(){
        int numberSelected = Utils.getIntSelected();
        Random rand = new Random();
        return rand.nextInt(numberSelected);
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


    public interface OnSelectionClickedListener {
        // TODO: Update argument type and name
        public void onSelectionClicked(Uri uri);
    }

}

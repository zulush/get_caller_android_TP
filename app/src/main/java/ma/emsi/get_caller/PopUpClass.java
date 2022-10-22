package ma.emsi.get_caller;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


public class PopUpClass implements View.OnClickListener {

    private final MainActivity mainActivity;
    private Contact contact;
    private Button call;
    private Button sms;
    private PopupWindow popupWindow;
    private TextView popupText;


    public PopUpClass(MainActivity mainActivity, Contact contact) {
        this.contact = contact;
        this.mainActivity = mainActivity;
    }

    //PopupWindow display method

    public void showPopupWindow(final View view) {


        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popupwindow, null);
        popupText = popupView.findViewById(R.id.popuptext);
        popupText.setText("Nom : "+ contact.getNom()+"\n Tel : " + contact.getTel());


        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;

        //Create a window with our parameters
        popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler
        call = popupView.findViewById(R.id.call);
        sms = popupView.findViewById(R.id.sms);


        call.setOnClickListener(this);
        sms.setOnClickListener(this);




        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == call){
            mainActivity.call(contact.getTel());
        } else if (v == sms){
            mainActivity.sms(contact.getTel());
        }
    }
}
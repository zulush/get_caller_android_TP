package ma.emsi.get_caller;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button searchBtn;
    private CountryCodePicker ccp;
    private RadioButton tel;
    private RadioButton nom;
    private TextView data;

    public void call(String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel));
        startActivity(intent);
    }

    public void sms(String tel) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+tel));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBtn = this.findViewById(R.id.searchBtn);
        ccp = this.findViewById(R.id.ccp);
        tel = (RadioButton) findViewById(R.id.tel);
        nom = (RadioButton) findViewById(R.id.nom);
        data = this.findViewById(R.id.data);
        nom.setChecked(true);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
        List<String> newContactString = loadContact(this.getContentResolver());
        String newName = newContactString.get(0).split(": ")[0];
        String newPhone = newContactString.get(0).split(": ")[1];
        Log.d("New Contact = ",newName + " / " + newPhone);
        Service s = new Service(this);
        s.add(newName, newPhone, "212");
    }




    public void getData(Contact contact) {

        if(contact != null){
            popupwindow(contact);
        } else {
            Toast.makeText(this, "Aucun enregistrement trouvé", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendRequest() {
        Log.d("contry:", ccp.getSelectedCountryCode());
        Log.d("Tel:", data.getText().toString());

        Service s = new Service(this);

        if(this.tel.isChecked()){
            s.getByName(data.getText().toString(), "phone", ccp.getSelectedCountryCode().toString());
        } else {
            s.getByName(data.getText().toString(), "name", ccp.getSelectedCountryCode().toString());
        }


    }
    private void popupwindow(Contact contact) {
        PopUpClass popUpClass = new PopUpClass(this, contact);
        popUpClass.showPopupWindow(this.searchBtn);
    }

    public List<String> loadContact (ContentResolver cr){
        List<String> contacts = new ArrayList<>();
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add (name +" : "+ phoneNumber);
        }
        phones.close();
        return contacts;
    }
}
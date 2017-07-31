package by.epamtest2.kazun;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        String fakeID = getIntent().getExtras().getString("itemText");

        TextView infoTextView = (TextView) findViewById(R.id.infoTextView);
        if (fakeID != null){
            infoTextView.setText(fakeID);
        }
    }

}

package p222.tp3.projet222;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbh = new DatabaseHelper(this.getBaseContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_upload);


        Button bUp = findViewById(R.id.btnUpload);
        bUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeFichier f = new TypeFichier("test", "fake/path");
                dbh.addFichier(f);
            }
        });
    }



}

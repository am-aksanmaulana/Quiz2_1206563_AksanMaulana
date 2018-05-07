package am.aksanmaulana.gmail.com.quiz2_1206563_aksanmaulana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CekSaldoActivity extends AppCompatActivity {

    private TextView tvSaldo;
    private Button btnBack;
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cek_saldo);

        Intent intent = getIntent();
        String strSaldo = intent.getStringExtra("saldo");

        //sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        //ed = sp.edit();
        //String strID = sp.getString("id", "");
        //String strPasswd = sp.getString("passwd", "");

        tvSaldo = (TextView) findViewById(R.id.tvSaldo);
        tvSaldo.setText("Jumlah saldo = Rp. " + strSaldo);
        /*
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new AmbilData().execute("http://mobprog.yuliadi.pro:5000/cek_saldo?id="+strID+"&passwd="+strPasswd); //url jadi parameter
        } else {
            // tampilkan error
            Toast t = Toast.makeText( getApplicationContext(), "Tidak ada koneksi!",Toast.LENGTH_LONG);
            t.show();
        }
        */
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

package am.aksanmaulana.gmail.com.quiz2_1206563_aksanmaulana;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // NIM - 1206563
    // NAMA - AKSAN MAULANA

    private Button btnDaftar, btnCekSaldo, btnTransferSaldo;
    public static final String PREFS_NAME = "Authentification";
    String strID, strPasswd;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    TextView tvID, tvAsumsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ed = sp.edit();

        tvAsumsi = (TextView) findViewById(R.id.tvAsumsi);
        tvAsumsi.setText("Karena tidak ada API untuk Login, jadi saya buat alur programnya :\n" +
                "1. Kalo tekan tombol DAFTAR, maka akan membuat akun baru.\n" +
                "2. Kalo tekan tombol CEK SALDO, maka perlu masukkan ID dan Password si pengguna, tapi " +
                "sudah otomatis terisi (karena tersimpan di shared preferences).\n" +
                "3. Kalo tekan tombol TRANSFER, maka perlu masukkan ID dan Password si pengirim/pengguna dahulu, tapi " +
                "sudah otomatis terisi (karena tersimpan di shared preferences).\n" +
                "\nJadi, kalau mau masuk/ganti akun, perlu masuk ke tombol Cek Saldo/Transfer terlebih dahulu. atau melewati tombol daftar " +
                "untuk melakukan pendaftaran akun baru lalu menyimpan ID dan Password di shared preferences.\n" +
                "\nText ID bisa ditekan untuk mereload halaman utama, karena kalo sudah masuk ke page Cek Saldo dengan akun lain, atau page " +
                "Transfer, lalu menekan tombol back android, maka text ID belum terganti. Kecuali, setelah masuk ke page Cek Saldo, lalu menekan " +
                "tombol back yang ada di page itu. atau setelah masuk ke page Transfer, lalu menyelesaikan proses Transaksi.\n" +
                "\nShared preferences terhapus jika menekan tombol DAFTAR");

        tvID = (TextView) findViewById(R.id.tvID);
        strID = sp.getString("id", "");
        Log.i("check", "id = " + strID);
        strPasswd = sp.getString("passwd", "");
        tvID.setText("ID Pengguna : " + strID);
        tvID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnDaftar = (Button) findViewById(R.id.btnFormLogin);
        btnCekSaldo = (Button) findViewById(R.id.btnCekSaldo);
        btnTransferSaldo = (Button) findViewById(R.id.btnTransferSaldo);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(), DaftarActivity.class);
                intent.putExtra("button", "Daftar");
                intent.putExtra("title", "Daftar");
                intent.putExtra("state", "daftar");
                startActivity(intent);
            }
        });

        btnTransferSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(), DaftarActivity.class);
                intent.putExtra("button", "Masuk");
                intent.putExtra("title", "Autentifikasi");
                intent.putExtra("state", "transfer");
                startActivity(intent);
            }
        });

        btnCekSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(), DaftarActivity.class);
                intent.putExtra("button", "Masuk");
                intent.putExtra("title", "Autentifikasi");
                intent.putExtra("state", "cekSaldo");
                startActivity(intent);
            }
        });

    }
}

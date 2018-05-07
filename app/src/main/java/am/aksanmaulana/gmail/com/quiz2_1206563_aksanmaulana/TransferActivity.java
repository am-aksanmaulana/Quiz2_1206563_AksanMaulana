package am.aksanmaulana.gmail.com.quiz2_1206563_aksanmaulana;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static am.aksanmaulana.gmail.com.quiz2_1206563_aksanmaulana.MainActivity.PREFS_NAME;

public class TransferActivity extends AppCompatActivity {

    private EditText etIdTujuan, etJumlah;
    private Button btnTransfer;
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ed = sp.edit();

        final String strID = sp.getString("id", "");
        final String strPasswd = sp.getString("passwd", "");

        btnTransfer = (Button) findViewById(R.id.btnTansfer);
        etIdTujuan = (EditText) findViewById(R.id.etIdTujuan);
        etJumlah = (EditText) findViewById(R.id.etJumlah);

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idTujuan = etIdTujuan.getText().toString();
                String strJumlah = etJumlah.getText().toString();

                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission") NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new AmbilData().execute("http://mobprog.yuliadi.pro:5000/transfer_coin?id="+strID+"&passwd="+strPasswd+"&id_tujuan="+idTujuan+"&jumlah="+strJumlah); //url jadi parameter
                } else {
                    // tampilkan error
                    Toast t = Toast.makeText( getApplicationContext(), "Tidak ada koneksi!",Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });

    }

    private class AmbilData extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... strUrl) {
            Log.v("yw", "mulai ambil data");
            String result = null;
            StringBuffer sb = new StringBuffer();
            InputStream is = null;
            try {
                URL url = new URL(strUrl[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //timeout
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);

                conn.setRequestMethod("POST");
                conn.connect();

                try {
                    is = new BufferedInputStream(conn.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String inputLine = "";
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    result = sb.toString();
                }
                catch (Exception e) {
                    Log.i("yw", "Error reading InputStream");
                    result = null;
                }
                finally {
                    if (is != null) {
                        try {
                            is.close();
                        }
                        catch (IOException e) {
                            Log.i("yw", "Error closing InputStream");
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {
            String id_tetangga, event_desc, id_events;
            Log.i("check", result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                String respon = jsonObj.getString("respon");

                //Log.i("check", saldo);
                //tvSaldo.setText("Jumlah saldo = Rp. " + saldo);
                if(respon.equals("OK")){
                    Toast t = Toast.makeText( getApplicationContext(), "Berhasil terkirim!",Toast.LENGTH_LONG);
                    t.show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }else if(respon.equals("Error, id pengirim dan/atau password salah")){
                    Toast t = Toast.makeText( getApplicationContext(), "" + respon,Toast.LENGTH_LONG);
                    t.show();

                    Intent intent = new Intent(getApplicationContext(), DaftarActivity.class);
                    intent.putExtra("button", "Masuk");
                    intent.putExtra("title", "Autentifikasi");
                    intent.putExtra("state", "transfer");
                    startActivity(intent);
                }else{
                    Toast t = Toast.makeText(getApplicationContext(), "" + respon, Toast.LENGTH_LONG);
                    t.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

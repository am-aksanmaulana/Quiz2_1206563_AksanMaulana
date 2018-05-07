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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class DaftarActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    EditText etID, etPasswd;
    String strID, strPasswd;
    TextView tvTitle;
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        final Intent intent = getIntent();
        String buttonText = intent.getStringExtra("button");
        String titleText = intent.getStringExtra("title");
        final String strState = intent.getStringExtra("state");
        sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        ed = sp.edit();

        strID = sp.getString("id", "");
        strPasswd = sp.getString("passwd", "");

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(titleText);

        etID = (EditText) findViewById(R.id.etIdTujuan);
        etPasswd = (EditText) findViewById(R.id.etPassword);
        if(strState.equals("daftar")) {
            ed.remove("id");
            ed.remove("passwd");
            ed.commit();

            etID.setText("");
            etPasswd.setText("");
        }else{
            etID.setText(strID);
            etPasswd.setText(strPasswd);
        }

        Button btnFormLogin = (Button) findViewById(R.id.btnFormLogin);
        btnFormLogin.setText(buttonText);

        btnFormLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strID = etID.getText().toString();
                strPasswd = etPasswd.getText().toString();

                //checking if email and passwords are empty
                if (TextUtils.isEmpty(strID)) {
                    Toast.makeText(getApplicationContext(), "Tolong isi kolom ID", Toast.LENGTH_LONG).show();
                    return;
                }

                if (TextUtils.isEmpty(strPasswd)) {
                    Toast.makeText(getApplicationContext(), "Tolong isi kolom password", Toast.LENGTH_LONG).show();
                    return;
                }

                if(strID != null && strPasswd != null) {
                    if(strState.equals("daftar")){
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            new AmbilDataBuatAccount().execute("http://mobprog.yuliadi.pro:5000/buat_account?id=" + strID + "&passwd=" + strPasswd); //url jadi parameter
                        } else {
                            // tampilkan error
                            Toast t = Toast.makeText(getApplicationContext(), "Tidak ada koneksi!", Toast.LENGTH_LONG);
                            t.show();
                        }
                    }else if(strState.equals("cekSaldo")){
                        /*strID = etID.getText().toString();
                        strPasswd = etPasswd.getText().toString();

                        ed.putString("id", strID);
                        ed.putString("passwd", strPasswd);
                        ed.commit();
                        Intent intentCekSaldo = new Intent(getApplicationContext(), CekSaldoActivity.class);
                        startActivity(intentCekSaldo);*/
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            new AmbilDataCekSaldo().execute("http://mobprog.yuliadi.pro:5000/cek_saldo?id="+strID+"&passwd="+strPasswd); //url jadi parameter
                        } else {
                            // tampilkan error
                            Toast t = Toast.makeText(getApplicationContext(), "Tidak ada koneksi!", Toast.LENGTH_LONG);
                            t.show();
                        }
                    }else{
                        strID = etID.getText().toString();
                        strPasswd = etPasswd.getText().toString();

                        ed.putString("id", strID);
                        ed.putString("passwd", strPasswd);
                        ed.commit();
                        Intent intentTransfer = new Intent(getApplicationContext(), TransferActivity.class);
                        startActivity(intentTransfer);
                    }

                }
            }
        });
    }

    private class AmbilDataCekSaldo extends AsyncTask<String, Integer, String> {

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

                conn.setRequestMethod("GET");
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
            Log.i("check", result);
            try {
                ed.putString("id", strID);
                ed.putString("passwd", strPasswd);
                ed.commit();

                JSONObject jsonObj = new JSONObject(result);
                String saldo = jsonObj.getString("saldo");
                Intent intentCekSaldo = new Intent(getApplicationContext(), CekSaldoActivity.class);
                intentCekSaldo.putExtra("saldo", saldo);
                startActivity(intentCekSaldo);
                // Log.i("check", "saldo" + saldo);
                //tvSaldo.setText("Jumlah saldo = Rp. " + saldo);

            } catch (JSONException e) {
                //e.printStackTrace();
                Toast t = Toast.makeText( getApplicationContext(), "id dan/atau password salah!",Toast.LENGTH_LONG);
                t.show();
            }
        }
    }

    private class AmbilDataBuatAccount extends AsyncTask<String, Integer, String> {

        protected String doInBackground(String... strUrl) {
            Log.v("check", "mulai ambil data");
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
                    Toast t = Toast.makeText( getApplicationContext(), "Akun baru berhasil dibuat!",Toast.LENGTH_LONG);
                    t.show();

                    //tulis
                    ed.putString("id", strID);
                    ed.putString("passwd", strPasswd);
                    ed.commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

package tads.eaj.com.sharkbehavior;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginBolsistaActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextPassword;

    public static final String USER_NAME = "USERNAME";

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_bolsista);

        editTextUserName = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
    }

    public void clickLogin(View view) throws IOException {
        username = editTextUserName.getText().toString();
        password = editTextPassword.getText().toString();

        new bolsistaLogin().execute(username,password);
    }

    private class bolsistaLogin extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String username = arg0[0];
                String password = arg0[1];

                /*
                    A URL especificada vai variar de acordo com o servidor. 10.0.2.2 é usado pois no emulador android
                    localhost ou 127.0.0.1 significa ele mesmo.

                 */

                String link = "http://192.168.137.1/tubaraoproject/loginbolsista.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(!s.isEmpty()){
                Intent i = new Intent(LoginBolsistaActivity.this,RealizaObservacaoActivity.class);
                Bundle b = new Bundle();
                b.putString("id_bolsista", s);
                i.putExtras(b);
                startActivity(i);
            }else{
                Toast.makeText(LoginBolsistaActivity.this, "Login ou senha inválidos.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickFechar(View v){
        finish();
    }
}

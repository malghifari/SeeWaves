package com.sah.seewaves;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SendEmail  extends AppCompatActivity {
    GmailSender sender;
    String user = "ahwa.dev@gmail.com";
    String password = "seewaves_pbd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_email);
        sender = new GmailSender(user, password);
    }

    public void send_email(View view) {
        try
        {
            AsyncClass l=new AsyncClass();
            l.execute();  //sends the email in background
            Toast.makeText(this, l.get(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }

    }

    class AsyncClass extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {

                sender.sendMail("EMERGENCY MESSAGE - SEE WAVES",
                        "This is Body of testing mail","Lala",
                        "haniahwafa@gmail.com")                   ;

            } catch (Exception e) {
                Log.e("error", e.getMessage(), e);
                return "Email Not Sent";
            }
            return "Email Sent";
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("LongOperation",result+"");
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}

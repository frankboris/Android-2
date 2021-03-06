package com.marwa.eltayeb.tweetme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.marwa.eltayeb.tweetme.LoginActivity.PREFS_NAME;

public class MainActivity extends AppCompatActivity {

    private UserAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String queryLink = "http://192.168.1.7/learn/query.php";

        List<User> userList = new ArrayList<>();
        userList.add(new User("Amr","Silicon valley"));
        ListView userListView = findViewById(R.id.userListView);
        adapter = new UserAdapter(this,userList);
        userListView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.delete_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuID = item.getItemId();

        switch (menuID) {

            case R.id.logout_menu:
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("logged");
                editor.apply();
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;

            case R.id.delete_menu:
                SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
                String email = sharedPreferences.getString("mail", "No Mail");

                SharedPreferences.Editor preferenceEditor = sharedPreferences.edit();
                preferenceEditor.remove("logged");
                preferenceEditor.apply();

                //Delete user From Database
                new DeleteAsyncTask().execute("http://192.168.1.8/learn/delete.php?email=" + email);
                Intent intent = new Intent(this, SignupActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the loginActivity
        moveTaskToBack(true);
    }

    private static class DeleteAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String urlLink = strings[0];
            try {
                new URL(urlLink).openStream();
            } catch (Exception e) {
            }
            return null;
        }
    }

    private static class DeleteAsyncTask2 extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            QueryUtils.readDataFromApi("http://192.168.1.7/learn/query.php");
            return null;
        }
    }
}

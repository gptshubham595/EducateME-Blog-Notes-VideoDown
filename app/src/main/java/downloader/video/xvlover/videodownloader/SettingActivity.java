package downloader.video.xvlover.videodownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class SettingActivity  extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().show();
        getSupportActionBar().setTitle("Settings");
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

    }
}
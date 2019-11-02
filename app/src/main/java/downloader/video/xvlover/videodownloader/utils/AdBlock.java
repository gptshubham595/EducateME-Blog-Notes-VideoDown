package downloader.video.xvlover.videodownloader.utils;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AdBlock {
    private static final String FILE = "hosts.txt";
    private static final Set<String> hosts = new HashSet<>();
    private static final List<String> whitelist = new ArrayList<>();
    private static final Locale locale = Locale.getDefault();
    private static final String TAG = "ADBLOCK";

    private static void loadHosts(final Context context) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                AssetManager manager = context.getAssets();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(manager.open(FILE)));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        hosts.add(line.toLowerCase(locale));
                    }
                } catch (IOException i) {}
            }
        });
        thread.start();
    }


    private static String getDomain(String url) throws URISyntaxException {
        url = url.toLowerCase(locale);

        int index = url.indexOf('/', 8); // -> http://(7) and https://(8)
        if (index != -1) {
            url = url.substring(0, index);
        }

        URI uri = new URI(url);
        String domain = uri.getHost();
        if (domain == null) {
            return url;
        }
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }

    private Context context;

    public AdBlock(Context context) {
        this.context = context;

        if (hosts.isEmpty()) {
            loadHosts(context);
        }
    }



    public  boolean isAd(String url) {
        String domain;
        try {
            domain = getDomain(url);
        } catch (URISyntaxException u) {
            return false;
        }

        Log.d(TAG, domain.toLowerCase(locale));
        return hosts.contains(domain.toLowerCase(locale));
    }



}
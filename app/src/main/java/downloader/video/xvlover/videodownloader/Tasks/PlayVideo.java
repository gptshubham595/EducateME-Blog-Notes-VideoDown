package downloader.video.xvlover.videodownloader.Tasks;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.shubham.iitg.R;

import downloader.video.xvlover.videodownloader.constants.iConstants;

public class PlayVideo implements iConstants {
    public  static  Dialog dialog;




        public static  void play(Context context , String url , String  title , String ext){
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.video_dialogdown);
            dialog.setTitle("Title...");

            VideoView vid = (VideoView) dialog.findViewById(R.id.video_view);
            vid.setVideoURI(Uri.parse(url));
            MediaController media = new MediaController(context);

            
            vid.setMediaController(media);
            vid.start();
             dialog.show();

            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        }

}

package yy.yjbo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
/**
 * Vitamio “维他蜜”视频播放
 * @author yjbo
 * @time 2017/4/17 10:17
 * @mail 1457521527@qq.com
 */
public class MainActivity extends AppCompatActivity {

    private TextView percentTv;
    private TextView netSpeedTv;
    private long bufTime = 0;//同一进度记录访问的时间；
    private long bufTimeDafult = 10*1000;//同一进度记录访问的时间设置；
    private int bufPercent = 0;//当前缓存的百分比（没有百分号，int型）
    private boolean isBuf = false;//是否进行缓存提示；false 没有缓存
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //显示缓冲百分比的TextView
        percentTv = (TextView) findViewById(R.id.buffer_percent);
        //显示下载网速的TextView
        netSpeedTv = (TextView) findViewById(R.id.net_speed);
        if (Vitamio.initialize(this)) {
            final VideoView videoView = (VideoView) findViewById(R.id.vitamio);
            videoView.setVideoURI(Uri.parse("http://video.89mc.com/89mc/knowledge/video/df366ce1-1f48-40ae-a805-6837b824c08c.mp4"));
//            videoView.setVideoURI(Uri.parse("http://112.253.22.157/17/z/z/y/u/zzyuasjwufnqerzvyxgkuigrkcatxr/hc.yinyuetai.com/D046015255134077DDB3ACA0D7E68D45.flv"));
            MediaController controller = new MediaController(this);
            videoView.setMediaController(controller);
            videoView.start();
            videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    if (!isBuf) {//如果不缓存
                        if (bufPercent == percent) {//缓存的进度总是一样；
                            if (bufTime == 0) {
                                System.out.println("缓存的进度01：");
                                bufTime = System.currentTimeMillis();
                            }
                            if (System.currentTimeMillis() - bufTime > bufTimeDafult) {//在一定时间内，缓存的进度总不变化；
                                System.out.println("缓存的进度02：");
                                percentTv.setText("当前网络不好，请查看网络！" + percent + "%");
                                Toast.makeText(MainActivity.this, "当前网络不好，请查看网络！", Toast.LENGTH_SHORT).show();
                                isBuf = true;
                                bufTime = System.currentTimeMillis();
                            } else {//还没到额定时间，缓存的进度总不变化；
//                            bufTime = System.currentTimeMillis();
                                System.out.println("缓存的进度03：");
                            }
                            System.out.println("缓存的进度04：");
                        } else {//缓存的进度不一样；
                            System.out.println("缓存的进度05：");
                            bufTime = System.currentTimeMillis();
                        }
                        ////防止下一次显示的缓存大小小于上一次的；此时就不要显示;这种情况只能发生在视频刚刚开始缓存阶段；
                        if (bufPercent > percent){
//                            isBuf = true;
                        }else {
//                            if ()
//                            isBuf = true;
                            percentTv.setText("已缓冲：" + percent + "%");
                            System.out.println("已缓冲：" + percent + "%");
                        }
                        System.out.println("缓存的进度06：" + "===" + bufPercent + "===" + percent);
                        bufPercent = percent;
                    }else {
                        percentTv.setVisibility(View.GONE);
                        netSpeedTv.setVisibility(View.GONE);
                    }
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    percentTv.setText("出错了：" + what + "==="+extra);
                    System.out.println("出错了：" + what + "==="+extra);
                    return false;
                }
            });
            videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    Log.e("视频播放的处理2： " + what+"===="+extra+"kb-----"
                            +mp.isBuffering()+"====="+videoView.isBuffering());
                    switch (what) {
                        //开始缓冲
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START://701
                            percentTv.setVisibility(View.VISIBLE);
                            netSpeedTv.setVisibility(View.VISIBLE);
                            netSpeedTv.setText("开始缓存：" + extra + "kb/s");
                            percentTv.setText("已缓冲：" + 0 + "%");
                            mp.pause();
                            break;
                        //缓冲结束
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END://702
                            isBuf = false;
                            percentTv.setVisibility(View.GONE);
                            netSpeedTv.setVisibility(View.GONE);
                            mp.start();
                            break;
                        //正在缓冲
                        case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED://901
                            netSpeedTv.setText("当前网速:" + extra + "kb/s");
                            System.out.println("当前网速:" + extra + "kb/s");
                            break;
                    }
                    return true;
                }
            });
        }
    }
}

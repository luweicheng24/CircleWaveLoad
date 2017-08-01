package ruanrong.com.loadingcircle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
   private WaveLoadCircle waveLoadCircle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          waveLoadCircle = (WaveLoadCircle) findViewById(R.id.wave);
          waveLoadCircle.setWaveHeightRatio(0.8f);
    }
}

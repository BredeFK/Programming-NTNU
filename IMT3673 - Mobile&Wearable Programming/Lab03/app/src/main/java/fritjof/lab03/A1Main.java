package fritjof.lab03;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.ImageView;

import static java.lang.System.exit;

public class A1Main extends AppCompatActivity implements SensorEventListener {

    private static int deviceFlat = 98;
    private ImageView ball;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;
    private float ballX, ballY;
    private float axisX, axisY;
    private float velX, velY;
    private float maxBallX, maxBallY;
    private int axisZ;
    private Point size;
    private Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_a1_main);
        initialize();
    }


    public void onBackPressed() {
        exit(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.sensorManager.registerListener(this, this.sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        this.axisZ = (int) (sensorEvent.values[2] * 10);

        if (axisZ == deviceFlat) {// If device is flat on a surface
            this.axisX = 0;
            this.axisY = 0;
        } else {
            this.axisX = -(sensorEvent.values[0] / 10);
            this.axisY = sensorEvent.values[1] / 10;
        }

        //    Log.d("VALUES", "X = " + this.axisX + "   Y = " + this.axisY + "   Z = " + this.axisZ);

        this.velX += this.axisX;
        this.velY += this.axisY;

        this.ballX += this.velX;
        this.ballY += this.velY;

        if (this.ballX < 0) {
            this.ballX = 0;
            this.velX = -this.velX * 0.5f;
            //      Log.d("CRASH", "ballX = " + this.ballX + "    ballY = " + this.ballY);
            collision();
        } else if (this.ballX > this.maxBallX) {
            this.ballX = this.maxBallX;
            this.velX = -this.velX * 0.5f;
            //    Log.d("CRASH", "ballX = " + this.ballX + "    ballY = " + this.ballY);
            collision();
        }

        if (this.ballY < 0) {
            this.ballY = 0;
            this.velY = -this.velY * 0.5f;
            //  Log.d("CRASH", "ballX = " + this.ballX + "    ballY = " + this.ballY);
            collision();
        } else if (this.ballY > this.maxBallY) {
            this.ballY = this.maxBallY;
            this.velY = -this.velY * 0.5f;
            //  Log.d("CRASH", "ballX = " + this.ballX + "    ballY = " + this.ballY);
            collision();
        }

        this.ball.setX(this.ballX);
        this.ball.setY(this.ballY);
    }


    public void initialize() {
        this.ball = findViewById(R.id.IDball);
        this.size = new Point();
        this.display = getWindowManager().getDefaultDisplay();
        this.display.getSize(size);

        this.maxBallX = (float) size.x - getDpToPx(30); // ball width 10 + margin left 10 + margin right 10 = 30
        this.maxBallY = (float) size.y - getDpToPx(30) - getDpToPx(32) - getNavigationHeight();  // ball height 10 + margin top 10 + margin bottom + 10 + navigationbar + actionbar 32

        this.axisX = this.axisY = this.velX = this.velY = 0;
        this.axisZ = 0;
        this.ballX = this.maxBallX / 2;
        this.ballY = this.maxBallY / 2;

        this.ball.setX(this.ballX);
        this.ball.setY(this.ballY);

        this.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        this.sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        this.mediaPlayer = MediaPlayer.create(A1Main.this, R.raw.pop);
    }

    public void collision() {
        this.mediaPlayer.start();
        this.vibrator.vibrate(100);
    }

    // Not my work
    // https://stackoverflow.com/questions/20264268/how-do-i-get-the-height-and-width-of-the-android-navigation-bar-programmatically
    public int getNavigationHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    // Converts dp to pixels
    public float getDpToPx(int dp) {
        return (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}

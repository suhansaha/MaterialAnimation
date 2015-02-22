package in.suhan.materialanimation;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class SubActivity2 extends ActionBarActivity {
    final String PACKAGE = "SUHAN";
    int mLeftDelta, mTopDelta;
    float mWidthScale, mHeightScale;
    private ImageView imageView;
    private TextView textView;
    Drawable mBackground;
    RelativeLayout topLevelLayout;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_activity2);

        Bundle bundle = getIntent().getExtras();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        bitmap = BitmapFactory.decodeResource(getResources(), bundle.getInt(PACKAGE + ".resourceId"), options);
        final int thTop = bundle.getInt(PACKAGE+".top");
        final int thLeft = bundle.getInt(PACKAGE+".left");
        final int thWidth = bundle.getInt(PACKAGE+".width");
        final int thHeight = bundle.getInt(PACKAGE+".height");
        final int orientation = bundle.getInt(PACKAGE+".orientation");

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        imageView.setImageBitmap(bitmap);

        textView.setText(bundle.getString(PACKAGE+".description"));

        mBackground = new ColorDrawable(Color.BLACK);
        topLevelLayout = (RelativeLayout)findViewById(R.id.topLevelLayout);
        topLevelLayout.setBackground(mBackground);

        ViewTreeObserver observer = imageView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);
                mLeftDelta = thLeft - screenLocation[0];
                mTopDelta = thTop - screenLocation[1];
                mWidthScale = (float) thWidth / imageView.getWidth();
                mHeightScale = (float) thHeight / imageView.getHeight();

                runEnterAnimation();

                return true;
            }
        });

    }

    public void runEnterAnimation(){
        final long duration = 1000;
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        imageView.setScaleX(mWidthScale);
        imageView.setScaleY(mHeightScale);
        imageView.setTranslationX(mLeftDelta);
        imageView.setTranslationY(mTopDelta);
        textView.setAlpha(0);

        imageView.animate().setDuration(duration).scaleX(1).scaleY(1).translationX(0).translationY(0)
                .setInterpolator(new AccelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                textView.setTranslationY(-textView.getHeight());
                textView.animate().setDuration(duration/2).translationY(0).alpha(1).setInterpolator(new DecelerateInterpolator());
            }
        });

        ObjectAnimator bgAnim;
        bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
        bgAnim.setDuration(duration);
        bgAnim.start();

        ObjectAnimator colorizer = ObjectAnimator.ofFloat(SubActivity2.this, "saturation", 0 ,1);
        colorizer.setDuration(duration);
        colorizer.start();
    }
    public void runExitAnimation(){
        final long duration = 1000;
        imageView.setPivotX(0);
        imageView.setPivotY(0);
        textView.setAlpha(0);


        imageView.animate().setDuration(duration).scaleX(mWidthScale).scaleY(mHeightScale).translationX(mLeftDelta).translationY(mTopDelta)
                .setInterpolator(new AccelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });

        ObjectAnimator bgAnim;
        bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 255, 0);
        bgAnim.setDuration(duration);
        bgAnim.start();

        ObjectAnimator colorizer = ObjectAnimator.ofFloat(SubActivity2.this, "saturation", 1 ,0);
        colorizer.setDuration(duration);
        colorizer.start();

        return;
    }

    public void setSaturation(float value){
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(value);
        ColorMatrixColorFilter colorizerFilter = new ColorMatrixColorFilter(colorMatrix);
        imageView.setColorFilter(colorizerFilter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.default_activity_button){
            runExitAnimation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);

        Log.d("MaterialAnimationTrace","Finishing activity");
    }

    @Override
    public void onBackPressed() {
        Log.d("MaterialAnimationTrace","Run exit animation now");


        runExitAnimation();

    }
}

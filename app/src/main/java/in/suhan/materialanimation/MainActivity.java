package in.suhan.materialanimation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transitions.everywhere.ChangeBounds;
import android.transitions.everywhere.Fade;
import android.transitions.everywhere.Scene;
import android.transitions.everywhere.Transition;
import android.transitions.everywhere.TransitionManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;



public class MainActivity extends ActionBarActivity {
    final String PACKAGE = "SUHAN";
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setExitTransition(new Explode());

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);


        //final Button btn2 = (Button) findViewById(R.id.button2);
        //btn2.setAlpha(0);

        ColorMatrix grayMatrix = new ColorMatrix();
        grayMatrix.setSaturation(0);
        ColorMatrixColorFilter grayscaleFilter = new ColorMatrixColorFilter(grayMatrix);

        GridLayout mGridLayout = (GridLayout)findViewById(R.id.gridLayout);
        mGridLayout.setColumnCount(3);
        mGridLayout.setUseDefaultMargins(true);

        Resources resources = getResources();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        for(int i = 0 ; i < 9 ; i++){
            ImageView imageView = new ImageView(this);
            imageView.setAdjustViewBounds(true);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.width = 300;
            imageView.setLayoutParams(layoutParams);
            imageView.setImageBitmap(BitmapFactory.decodeResource(resources, (i % 2 == 0 ? R.drawable.photo2 : R.drawable.photo5), options));
            imageView.setColorFilter(grayscaleFilter);
            imageView.setOnClickListener(thumbClickListener);
            mGridLayout.addView(imageView);
        }

    }

    private View.OnClickListener thumbClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("MaterialAnimationTrace","Thumbnail Image Clicked");
            int[] screenLocation = new int[2];
            v.getLocationOnScreen(screenLocation);

            Intent subActivity = new Intent(MainActivity.this,SubActivity2.class);

            int orientation = getResources().getConfiguration().orientation;
            subActivity.putExtra(PACKAGE+".orientation",orientation).
                    putExtra(PACKAGE+".resourceId",R.drawable.photo2).
                    putExtra(PACKAGE+".left",screenLocation[0]).
                    putExtra(PACKAGE+".top",screenLocation[1]).
                    putExtra(PACKAGE+".width",v.getWidth()).
                    putExtra(PACKAGE+".height",v.getHeight()).
                    putExtra(PACKAGE+".description","This is a dummy text");

            startActivity(subActivity);
            overridePendingTransition(0,0);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.action_navigation) {
            Intent intent = new Intent(this,SubActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
   private boolean mState = true;
    public void doTransition(View v){
        Scene mAScene, mBScene;
        ViewGroup mSceneRoot;

        mSceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        mAScene = Scene.getSceneForLayout(mSceneRoot, R.layout.a_scene, this);
        mBScene = Scene.getSceneForLayout(mSceneRoot, R.layout.b_scene, this);

        Transition mFadeTransition = new ChangeBounds();
        if(mState) {
            TransitionManager.go(mBScene, mFadeTransition);
            mState = false;
        }else{
            TransitionManager.go(mAScene, mFadeTransition);
            mState = true;
        }
    }
    public void doTransition2(View v){
        ViewGroup mSceneRoot;

        mSceneRoot = (ViewGroup) findViewById(R.id.master_layout);
        TextView mLabelText = new TextView(this);
        mLabelText.setText("Hello Animation");

        Transition mFade = new Fade();
        mFade.setDuration(1000);
        TransitionManager.beginDelayedTransition(mSceneRoot, mFade);

        mSceneRoot.addView(mLabelText);

    }

    @Override
   public void onEnterAnimationComplete() {

        final Button btn1 = (Button) findViewById(R.id.button);

        final Button btn2 = (Button) findViewById(R.id.button2);
        float pos = btn2.getTranslationX();
        btn2.setTranslationX(800);

        PropertyValuesHolder valueX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X,pos);
        PropertyValuesHolder valueAlpha = PropertyValuesHolder.ofFloat(View.ALPHA,1);

        ObjectAnimator translateAnimation = ObjectAnimator.ofPropertyValuesHolder(btn2,valueX,valueAlpha);

        translateAnimation.setStartDelay(3000);
        //translateAnimation.setRepeatCount(1);
        //translateAnimation.setRepeatMode(ValueAnimator.REVERSE);
        translateAnimation.start();
        super.onEnterAnimationComplete();
    }
}

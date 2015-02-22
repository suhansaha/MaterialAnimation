package in.suhan.materialanimation;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class SubActivity extends ActionBarActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Data> dataList = new ArrayList<Data>();

        int[] img = {R.drawable.ic_no1_icon,R.drawable.ic_no2_icon,R.drawable.ic_no3_icon,R.drawable.ic_no4_icon};
        String[] text = getResources().getStringArray(R.array.second_list);
        for(int i = 0 ; i < 100 ; i++){
            Data item = new Data();
            item.setText(text[ i % text.length ]);
            item.setImgSrc(img[ i % img.length ]);
            dataList.add(item);
        }

        adapter = new CardAdapter(this, dataList);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                adapter.resetScrollDelay();
                super.onScrolled(recyclerView, dx, dy);
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.cardList);
        recyclerView.setOnScrollListener(scrollListener);
        recyclerView.setAdapter(adapter);
        recyclerView.getItemAnimator().setAddDuration(1000);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub, menu);
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

        return super.onOptionsItemSelected(item);
    }

}

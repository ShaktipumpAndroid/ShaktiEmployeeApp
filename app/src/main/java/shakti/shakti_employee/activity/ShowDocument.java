package shakti.shakti_employee.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import shakti.shakti_employee.R;


public class ShowDocument extends BaseActivity {

    Context context;
    ImageView imageView;

    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_document);

        context = this;
        imageView =findViewById(R.id.imageView);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("Image Display");

        if (getIntent().getExtras() != null) {
            if (getIntent().getStringExtra("image_path") != null && !getIntent().getStringExtra("image_path").isEmpty()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(getIntent().getStringExtra("image_path"));
                imageView.setImageBitmap(myBitmap);
            }
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

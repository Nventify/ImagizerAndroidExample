package com.nventfiy.imagizerandroidexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nventfiy.imagizerandroid.ImagizerAndroidClient;
import java.util.ArrayList;

public class CompareActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "CompareActivity";

    private ImagizerAndroidClient client;
    private ImageView[] imageViews = new ImageView[9];
    private ToggleButton enableImagizerSwitch;
    private ToggleButton webpSwitch;
    private Spinner qualitySpinner;
    private TextView totalOriginalSize;
    private TextView totalFileSize;
    private TextView reductionPercent;
    private ImageLoader imageLoader;
    private ProgressBar progressbar;
    private String[] images = {
            "/img911/zPCsEi.jpg",
            "/img538/RVVemq.jpg",
            "/img540/r6EjeE.jpg",
            "/img538/BQREbF.jpg",
            "/img829/0azo.jpg",
            "/img840/h2j6.jpg",
            "/img540/0vSeM2.jpg",
            "/img743/7kxUOc.jpg",
            "/img673/0NzJNW.jpg"
    };
    private ArrayList<String> imageUrls = new ArrayList<>();

    /* On image load complete add up the total image sizes and calculate bandwidth reduction */
    private ImageLoadingListener imageLoadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            imageUrls.add(imageUri);
            Log.i(TAG, "Downloaded: " + imageUri);

            long totalImageSize = DownloadStorageSizes.sumImageSizesForImageUrl(imageUri);
            long totalOriginalImageSize = DownloadStorageSizes.sumOriginalImageSizeForImageUrl(imageUri);
            int percent = DownloadStorageSizes.getPercent();

            totalOriginalSize.setText(String.format("%s kb", String.valueOf((int) Math.ceil(totalOriginalImageSize / 1024))));
            totalFileSize.setText(String.format("%s kb", String.valueOf((int) Math.ceil(totalImageSize / 1024))));
            reductionPercent.setText(String.format("%s %%", String.valueOf(percent)));

            int progressValue = (int) Math.ceil((1.0 / imageViews.length) * 100.0);
            progressbar.setProgress(progressbar.getProgress() + progressValue);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        setContentView(R.layout.activity_compare);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        imageLoader = ImageLoader.getInstance();
        imageLoader.setDefaultLoadingListener(imageLoadingListener);

        webpSwitch = (ToggleButton) findViewById(R.id.switch2);
        enableImagizerSwitch = (ToggleButton) findViewById(R.id.switch1);
        qualitySpinner = (Spinner) findViewById(R.id.spinner);
        totalOriginalSize = (TextView) findViewById(R.id.originalSizeValue);
        totalFileSize = (TextView) findViewById(R.id.imagizerSizeValue);
        reductionPercent = (TextView) findViewById(R.id.sizeReduction);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);

        // Initialize the Imagizer client
        // Using the default initializer which points to the Imagizer Engine Demo service
        // demo.imagizercdn.com
        client = new ImagizerAndroidClient(this);

        // Since we are using Imagizer Engine Demo Service
        // we'll need to specify our Image storage origin
        // Imagizer will fetch your images from this endpoint
        client.setOriginHost("demo-images.imagizercdn.com");

        // Enable Auto device pixel ratio setting.
        // Device pixel ratio will now be detected
        // and automatically applied to image urls
        // http://demo.imagizercdn.com/doc/#dpr-device-pixel-ratio
        client.setAutoDpr(true);

        // Set global image quality (compression)
        client.setQuality(60);

        // Set image format to be returned from Imagizer
        // Use jpeg as default
        client.setFormat("jpeg");

        // Define our image views
        imageViews[0] = (ImageView) findViewById(R.id.imageView1);
        imageViews[1] = (ImageView) findViewById(R.id.imageView2);
        imageViews[2] = (ImageView) findViewById(R.id.imageView3);
        imageViews[3] = (ImageView) findViewById(R.id.imageView4);
        imageViews[4] = (ImageView) findViewById(R.id.imageView5);
        imageViews[5] = (ImageView) findViewById(R.id.imageView6);
        imageViews[6] = (ImageView) findViewById(R.id.imageView7);
        imageViews[7] = (ImageView) findViewById(R.id.imageView8);
        imageViews[8] = (ImageView) findViewById(R.id.imageView9);

        loadSpinner();
        loadSwitches();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Intent intent = new Intent(this, FeaturesActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cleanUp();
    }

    private void loadSwitches() {
        enableImagizerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (buttonView.isPressed()) {
                    loadImages();
                }
            }
        });

        webpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (buttonView.isPressed()) {

                    if (webpSwitch.isChecked()) {
                        client.setFormat("webp");
                    } else {
                        client.setFormat("jpeg");
                    }

                    loadImages();
                }
            }
        });
    }

    private void disableImagizer() {
        qualitySpinner.setEnabled(false);
        qualitySpinner.setVisibility(View.GONE);
        findViewById(R.id.qualityLabel).setVisibility(View.GONE);
        findViewById(R.id.imagizerSizeValue).setVisibility(View.GONE);
        findViewById(R.id.sizeReduction).setVisibility(View.GONE);
        findViewById(R.id.textView2).setVisibility(View.GONE);
        findViewById(R.id.textView3).setVisibility(View.GONE);
        findViewById(R.id.switch2).setVisibility(View.GONE);
    }

    private void enableImagizer() {
        qualitySpinner.setEnabled(true);
        qualitySpinner.setVisibility(View.VISIBLE);
        findViewById(R.id.qualityLabel).setVisibility(View.VISIBLE);
        findViewById(R.id.imagizerSizeValue).setVisibility(View.VISIBLE);
        findViewById(R.id.sizeReduction).setVisibility(View.VISIBLE);
        findViewById(R.id.textView2).setVisibility(View.VISIBLE);
        findViewById(R.id.textView3).setVisibility(View.VISIBLE);
        findViewById(R.id.switch2).setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadImages();
    }

    private void loadImages() {
        cleanUp();

        if (enableImagizerSwitch.isChecked()) {
            loadImagizerImages();
            enableImagizer();
        } else {
            loadOriginalImages();
            disableImagizer();
        }
    }

    private void loadSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quality_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualitySpinner.setAdapter(adapter);
        qualitySpinner.setSelection(4);
        qualitySpinner.setOnItemSelectedListener(this);
        qualitySpinner.setEnabled(enableImagizerSwitch.isChecked());
    }

    private void loadImagizerImages() {
        Log.i(TAG, "load Imagizer optimized images");

        String url;

        int i = 0;
        for (String image : images) {

            // Setup first image
            url = client.buildUrl(image)

                    // lets request the image to be scaled and cropped
                    // to fit perfectly into the image view
                    .fitToView(imageViews[i])

                    // return the URL in string format
                    .toString();

            // Load the image from Imagizer into the image view
            Log.i(TAG, "load image " + url);

            imageLoader.displayImage(url, imageViews[i]);

            i++;
        }
    }

    /* Load full size images. These are not using Imagizer */
    private void loadOriginalImages() {
        Log.i(TAG, "load original images");

        String url;

        int i = 0;
        for (String image : images) {
            url = "http://demo-images.imagizercdn.com" + image;
            imageLoader.displayImage(url, imageViews[i]);

            i++;
        }
    }

    @SuppressLint("SetTextI18n")
    private void cleanUp() {
        progressbar.setProgress(0);
        totalOriginalSize.setText("0 kb");
        totalFileSize.setText("0 kb");
        reductionPercent.setText("0 %");

        DownloadStorageSizes.clearAll();

        for (String image: imageUrls) {
            MemoryCacheUtils.removeFromCache(image, imageLoader.getMemoryCache());
            DiskCacheUtils.removeFromCache(image, imageLoader.getDiskCache());
        }

        for (ImageView imageView : imageViews) {
            if (imageView != null) {
                imageLoader.cancelDisplayTask(imageView);
                imageView.setImageResource(android.R.color.transparent);
            }
        }

        imageUrls.clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        client.setQuality(Integer.valueOf(String.valueOf(parent.getSelectedItem())));
        cleanUp();

        if (enableImagizerSwitch.isChecked()) {
            loadImagizerImages();
        } else {
            loadOriginalImages();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}

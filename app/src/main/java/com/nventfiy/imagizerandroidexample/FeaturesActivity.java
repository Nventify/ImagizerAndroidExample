package com.nventfiy.imagizerandroidexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nventfiy.imagizerandroid.ImagizerAndroidClient;

public class FeaturesActivity extends AppCompatActivity {

    private static final String TAG = "FeaturesActivity";

    private ImageLoader imageLoader;
    private ImagizerAndroidClient client;

    private ImageView[] imageViews = new ImageView[25];

    // a list of images we'll use here to demonstrate the Imagizer Engine
    private String[] images = {
            "/img911/zPCsEi.jpg",
            "/img911/GwSXw0.jpg",
            "/img855/772w.jpg",
            "/img855/772w.jpg",
            "/img924/lLg2JC.jpg",
            "/img811/d4r2.jpg",
            "/img707/5062459.jpg",
            "/img674/jloeiY.jpg",
            "/img674/8083cb.jpg",
            "/img537/550e65.jpg",
            "/img909/I61iCD.jpg",
            "/img909/oYhUY5.jpg"
    };

    /* On image load complete add up the total image sizes and calculate bandwidth reduction */
    private ImageLoadingListener imageLoadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            Log.i(TAG, "Downloaded: " + imageUri);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        defineImageViews();

        // Grab an instance of the Android Universal Image Loader
        imageLoader = ImageLoader.getInstance();

        // Set a default download listener. This is used for logging the image url fetches
        imageLoader.setDefaultLoadingListener(imageLoadingListener);

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

        // Compress our images by setting the quality to 75
        client.setQuality(75);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

        loadImages();
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
            Intent intent = new Intent(this, CompareActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /* Define all our image views */
    protected void defineImageViews() {
        imageViews[0] = (ImageView) findViewById(R.id.featuresImage1);
        imageViews[1] = (ImageView) findViewById(R.id.featuresImage2);
        imageViews[2] = (ImageView) findViewById(R.id.featuresImage3);
        imageViews[3] = (ImageView) findViewById(R.id.featuresImage4);
        imageViews[4] = (ImageView) findViewById(R.id.featuresImage5);
        imageViews[5] = (ImageView) findViewById(R.id.featuresImage6);
        imageViews[6] = (ImageView) findViewById(R.id.featuresImage7);
        imageViews[7] = (ImageView) findViewById(R.id.featuresImage8);
        imageViews[8] = (ImageView) findViewById(R.id.featuresImage9);
        imageViews[9] = (ImageView) findViewById(R.id.featuresImage10);
        imageViews[10] = (ImageView) findViewById(R.id.featuresImage11);
        imageViews[11] = (ImageView) findViewById(R.id.featuresImage12);
        imageViews[12] = (ImageView) findViewById(R.id.featuresImage13);
        imageViews[13] = (ImageView) findViewById(R.id.featuresImage14);
        imageViews[14] = (ImageView) findViewById(R.id.featuresImage15);
        imageViews[15] = (ImageView) findViewById(R.id.featuresImage16);
        imageViews[16] = (ImageView) findViewById(R.id.featuresImage17);
        imageViews[17] = (ImageView) findViewById(R.id.featuresImage18);
        imageViews[18] = (ImageView) findViewById(R.id.featuresImage19);
        imageViews[19] = (ImageView) findViewById(R.id.responsiveImage1);
        imageViews[20] = (ImageView) findViewById(R.id.responsiveImage2);
        imageViews[21] = (ImageView) findViewById(R.id.responsiveImage3);
    }

    /**
     * Using ImagizerEngine client and Android Universal Image Loader
     * to load examples of processed images.
     *
     * Here we we will try a variety of different image processing
     */
    protected void loadImages() {
        String url;

        // Scale the image by width.
        // The height will be calculated by the aspect ratio of the source image.
        // http://demo.imagizercdn.com/doc/#scale
        url = client.buildUrl(images[0])
                .addParam("width", 300)
                .toString(); // return the URL in string format

        imageLoader.displayImage(url, imageViews[0]); // Load the image from Imagizer into the image view

        // Scale the image by height.
        // The width will be calculated by the aspect ratio of the source image.
        url = client.buildUrl(images[1])
                .addParam("height", 300)
                .toString();

        imageLoader.displayImage(url, imageViews[1]);

        // Crops any excess image data outside
        // the width and height boundaries after scaling.
        // The output image will match exactly the dimensions given.
        // http://demo.imagizercdn.com/doc/#crop
        url = client.buildUrl(images[2])
                .addParam("height", 300)
                .addParam("width", 300)
                .addParam("crop", "fit")
                .toString();

        imageLoader.displayImage(url, imageViews[2]);

        // Scale an image to the given dimensions
        // and fills the extra space with pad_color. The default color is white.
        url = client.buildUrl(images[3])
                .addParam("height", 300)
                .addParam("width", 300)
                .addParam("crop", "pad")
                .toString();

        imageLoader.displayImage(url, imageViews[3]);

        // Custom crop allows for specifying a sub-region of the source image
        // to be returned. The value is 4 numbers
        // representing x, y, width, and height separated using commas
        url = client.buildUrl(images[4])
                .addParam("width", 300)
                .addParam("crop", "65,190,300,300")
                .toString();

        imageLoader.displayImage(url, imageViews[4]);

        // Responsive images will be scaled and cropped automatically
        // to fit perfectly into the image view no matter the device or screen size
        url = client.buildUrl(images[0])
                .fitToView(imageViews[19])
                .toString();

        imageLoader.displayImage(url, imageViews[19]);

        url = client.buildUrl(images[1])
                .fitToView(imageViews[20])
                .toString();

        imageLoader.displayImage(url, imageViews[20]);

        url = client.buildUrl(images[2])
                .fitToView(imageViews[21])
                .toString();

        imageLoader.displayImage(url, imageViews[21]);

        // Crop out image data around any faces detected in the image
        url = client.buildUrl(images[5])
                .addParam("crop", "face")
                .toString();

        imageLoader.displayImage(url, imageViews[5]);

        // Entropy crop will find
        // and return the most interesting features of the source image,
        // cropping out less important areas.
        url = client.buildUrl(images[6])
                .addParam("crop", "entropy")
                .toString();

        imageLoader.displayImage(url, imageViews[6]);

        // Add padding around an image with a given pad_color as a hex string.
        // The default color is white.
        // http://demo.imagizercdn.com/doc/#pad
        url = client.buildUrl(images[4])
                .addParam("width", 300)
                .addParam("pad", 20)
                .addParam("pad_color", "000")
                .toString();

        imageLoader.displayImage(url, imageViews[7]);

        // Rotate an image with the angle param
        // The meaningful values are 90, 180, and 270 degrees.
        // http://demo.imagizercdn.com/doc/#rotate
        url = client.buildUrl(images[1])
                .addParam("width", 300)
                .addParam("angle", 90)
                .toString();

        imageLoader.displayImage(url, imageViews[8]);


        // The quality parameter allows for specifying the quality of the image.
        // Decreasing the quality parameter will reduce the file size.
        // Quality value may be set from 1 to 100 and the default value is 90.
        // http://demo.imagizercdn.com/doc/#quality
        url = client.buildUrl(images[7])
                .addParam("width", 300)
                .addParam("quality", 90) // use high quality here
                .toString();

        imageLoader.displayImage(url, imageViews[9]);

        url = client.buildUrl(images[7])
                .addParam("width", 300)
                .addParam("quality", 10) // use lower value here
                .toString();

        imageLoader.displayImage(url, imageViews[10]);

        // Control image sharpness
        // http://demo.imagizercdn.com/doc/#sharpening
        url = client.buildUrl(images[8])
                .addParam("width", 300)
                .addParam("sharp_amount", 0) // use default here
                .toString();

        imageLoader.displayImage(url, imageViews[11]);

        url = client.buildUrl(images[8])
                .addParam("width", 300)
                .addParam("sharp_amount", 100) // use highest value here
                .toString();

        imageLoader.displayImage(url, imageViews[12]);

        // Blur an image
        // http://demo.imagizercdn.com/doc/#blur
        url = client.buildUrl(images[9])
                .addParam("width", 300)
                .addParam("blur", 0) // use default here
                .toString();

        imageLoader.displayImage(url, imageViews[13]);

        url = client.buildUrl(images[9])
                .addParam("width", 300)
                .addParam("blur", 30) // blurry here
                .toString();

        imageLoader.displayImage(url, imageViews[14]);

        // Image filters
        // Filters apply various effects to images
        // Imagizer offers 20 different filters
        // http://demo.imagizercdn.com/doc/#filter
        url = client.buildUrl(images[10])
                .addParam("width", 300)
                .addParam("filter", 2)
                .toString();

        imageLoader.displayImage(url, imageViews[15]);

        url = client.buildUrl(images[10])
                .addParam("width", 300)
                .addParam("filter", 3) // try filter #3
                .toString();

        imageLoader.displayImage(url, imageViews[16]);

        url = client.buildUrl(images[10])
                .addParam("width", 300)
                .addParam("filter", 21) // try filter #21 (grey scale)
                .toString();

        imageLoader.displayImage(url, imageViews[17]);

        // Watermarking
        // Pass the watermark image path and a few spatial parameters
        // http://demo.imagizercdn.com/doc/#watermarking
        url = client.buildUrl(images[10])
                .addParam("width", 300)
                .addParam("mark", "/img921/KlViNj.png")
                .addParam("mark_scale", 60)
                .addParam("mark_pos", "bottom,right")
                .addParam("mark_offset", 2)
                .toString();

        imageLoader.displayImage(url, imageViews[18]);
    }

    public void clearLocalImageCache() {
        for (ImageView imageView : imageViews) {
            if (imageView != null) {
                imageLoader.cancelDisplayTask(imageView);
                imageView.setImageResource(android.R.color.transparent);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        clearLocalImageCache();
    }
}

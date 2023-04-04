package shakti.shakti_employee.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import shakti.shakti_employee.R;



/**
 * Created by parkjisun on 2017. 3. 15..
 */

public class ImageManager {

    //private static final int MINIMUM_HEIGHT = 480;
    private static final int MINIMUM_HEIGHT = 300;

    public static final String GALLERY_DIRECTORY_NAME = "ShaktiKusum";

    public static void adjustCameraParameters(Context context, Camera camera, String pictureSizeStr) {

        Camera.Parameters parameters = camera.getParameters();


        if (pictureSizeStr == null) {
            // 4:3 비율을 가진 해상도의 사진 사이즈만 추려 저장
            List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
            ArrayList<Camera.Size> validPictureSizeList = new ArrayList<>();

            for (Camera.Size size : pictureSizeList) {
                if (size.height > MINIMUM_HEIGHT) {
                    if (size.width / 4 * 3 == size.height) {
                        validPictureSizeList.add(size);

                    }
                }
            }

            // 사진 사이즈 리스트 저장되어 있지 않을 경우에만 저장
            if (Config.getSharedPreferenceString(context, Config.PREF_KEY_PICTURE_SIZE_LIST).isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (Camera.Size size : validPictureSizeList) {
                    sb.append(Integer.toString(size.width)).append("x").append(Integer.toString(size.height)).append("|");
                }
                Config.putSharedPreference(context, Config.PREF_KEY_PICTURE_SIZE_LIST, sb.toString());
            }

        // 최초에는 가장 작은 사이즈 설정
            Camera.Size currentSize = parameters.getPictureSize();
            String currentSizeStr = Config.getSharedPreferenceString(context, Config.PREF_KEY_PICTURE_SIZE);
            int newWidth, newHeight;
            if (currentSizeStr.isEmpty()) {
//            Camera.Size newSize = validPictureSizeList.get(validPictureSizeList.size() - 1);
                Camera.Size newSize = validPictureSizeList.get(0); // FIXME jisun-test : 가장 큰 사이즈로 설정
                newWidth = newSize.width;
                newHeight = newSize.height;
            } else {
                String[] temp = currentSizeStr.split("x");
                newWidth = Integer.parseInt(temp[0]);
                newHeight = Integer.parseInt(temp[1]);
            }
            if ((currentSize.width != newWidth) || (currentSize.height != newHeight)) {
                parameters.setPictureSize(newWidth, newHeight);
            }
            Config.putSharedPreference(context, Config.PREF_KEY_PICTURE_SIZE, newWidth + "x" + newHeight);
            Log.e("jisunLog", "new picture size ] " + newWidth + "x" + newHeight);
        } else {
            String[] temp = pictureSizeStr.split("x");
            parameters.setPictureSize(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
            Config.putSharedPreference(context, Config.PREF_KEY_PICTURE_SIZE, pictureSizeStr);
        }

        // 4:3으로 preview 비율 설정
        List<Camera.Size> list = parameters.getSupportedPreviewSizes();
        int targetWidth = 0, targetHeight = 0;
        for (Camera.Size size : list) {
           Log.e("jisunLog", "previewSize " + size.width + "x" + size.height);
            if (size.width / 4 * 3 == size.height) {
                // 해당 비율을 가진 가장 높은 preview 값 찾기
                targetWidth = size.width;
                targetHeight = size.height;
                break;
            }
        }
        Log.e("jisunLog", "target " + targetWidth + "x" + targetHeight);
        parameters.setPreviewSize(targetWidth, targetHeight);

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);

        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);

        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);

        parameters.setExposureCompensation(0);

        parameters.setPictureFormat(ImageFormat.JPEG);

        parameters.setJpegQuality(100);

       // parameters.setRotation(270);
        // TODO jisun : 전면 카메라이면 parameter 설정시 죽음
        camera.setParameters(parameters);
    }

    public void changePictureSize(Context context,Camera camera, String newSizeStr) {
        Log.e("jisunLog", "newSizeStr " + newSizeStr);
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size currentSize = parameters.getPictureSize();
        if (!getSizeString(currentSize).equals(newSizeStr)) {
            String[] temp = newSizeStr.split("x");
            parameters.setPictureSize(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));

            List<Camera.Size> list = parameters.getSupportedPreviewSizes();
            if (!list.isEmpty()) {
                parameters.setPreviewSize(list.get(0).width, list.get(0).height);
            }
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

            try {
                camera.setParameters(parameters);
                // parameter 변경 성공하면 shared preferences도 변경
                Config.putSharedPreference(context, Config.PREF_KEY_PICTURE_SIZE, newSizeStr);
            } catch (RuntimeException e) {
                Log.e("jisunLog", "RuntimeException ] " + e.getLocalizedMessage());
                // setParameter시 죽는 단말기 존재(Nexus5)
                Toast.makeText(context, "사진 사이즈 변경 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getSizeString(Camera.Size size) {
        return size.width + "x" + size.height;
    }

    /**
     * Create a path for saving an image
     */
    public static String getMediaFilePath(String type,String name,String enq_doc) {

       // File root = android.os.Environment.getExternalStorageDirectory();

      //  File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),GALLERY_DIRECTORY_NAME);
        File root = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Utility.GALLERY_DIRECTORY_NAME_COMMON);

        File dir = new File(root.getAbsolutePath() + "/SKAPP/"+ type + enq_doc); //it is my root directory

        try {
            if (!dir.exists()) {
                dir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        // Create a media file name
        return dir.getPath() + File.separator + "IMG_"+ name + "." + "jpg";
    }

    public static Bitmap saveImageWithTimeStamp(Context context, byte data[], int offset, int length, float textSize,String lat,String lng,String cust_name) {

        Bitmap src = BitmapFactory.decodeByteArray(data, offset, length);

        src = rotateBitmap(src, 90);
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);


        SimpleDateFormat sdf = new SimpleDateFormat(Config.TIME_STAMP_FORMAT_DATE, Locale.getDefault());
        SimpleDateFormat sdf1 = new SimpleDateFormat(Config.TIME_STAMP_FORMAT_TIME, Locale.getDefault());
        String date = sdf.format(new Date());
        String time = sdf1.format(new Date());

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float scaledTextSize = src.getWidth() * textSize / displayMetrics.widthPixels;


        Canvas cs = new Canvas(dest);
        Paint paint = new Paint();
        paint.setTextSize(110);
        paint.setFakeBoldText(true);
        int color = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        cs.drawBitmap(src, 0f, 0f, null);

        // 가운데 정렬을 위해 텍스트 시작 시점 계산
        float height = paint.measureText("yY");
        float width = paint.measureText("Date: "+date+"\n"+"Time: "+time);
        float startXPosition = (src.getWidth() - width);
        float startYPosition = (src.getHeight() - height);

        String text = "Latitude: "+lat;
        String text1 = "Longitude: "+lng;
        String text2 = "Date: "+date;
        String text3 = "Time: "+time;

        String text4 = "Customer Name: "+cust_name;

        cs.drawText(text , startXPosition - 1250, startYPosition - 600, paint);
        cs.drawText(text1, startXPosition - 1250, startYPosition - 450, paint);
        cs.drawText(text2+" "+text3 , startXPosition - 1250, startYPosition - 300, paint);
        cs.drawText(text4, startXPosition - 1250, startYPosition - 150, paint);

        return dest;
    }

    public static File saveFile(Bitmap bitmap,String type,String name,String enq_doc) {
        File file = new File(Objects.requireNonNull(getMediaFilePath(type,name,enq_doc)));
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, new FileOutputStream(file));
        } catch (FileNotFoundException e) {

            file = null;
        }
        return file;
    }

    private static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


}

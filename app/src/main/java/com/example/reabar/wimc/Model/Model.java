package com.example.reabar.wimc.Model;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.reabar.wimc.FilesManagerHelper;
import com.example.reabar.wimc.MyApplication;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reabar on 28/06/2016.
 */
public class Model {
    private static Model ourInstance = new Model();
    private Bitmap image;
    private ModelFirebase modelFirebase;
    private ModelCloudinary modelCloudinary;
    private ModelSql modelSql;
    private User currentUser;
    private static boolean imageBool;
    private FilesManagerHelper fileManager;

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
        modelFirebase = new ModelFirebase();
        modelSql = new ModelSql();
        modelCloudinary = new ModelCloudinary(MyApplication.getAppContext());
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void signupUser(final User user, final String password, final SyncListener listener){
        modelFirebase.signupUser(user, password, listener);
    }

    public void signInUser(User user, String password, final SyncListener listener){
        modelFirebase.signInUser(user, password, listener);
    }

    public void logoutUser(){
        modelFirebase.logoutUser();
    }

    public User getCurrentUser(){
       modelFirebase.getCurrentUser();
        return currentUser;
    }

    public String getCurrentUserID(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void resetPassword(String email, final SyncListener listener){
        modelFirebase.resetPassword(email, listener);
    }

    public void updatePassword(String newPassword, final SyncListener listener){
        modelFirebase.updatePassword(newPassword, listener);
    }

    public List<String> getUsersList(Model.SyncListener listener){
        return modelFirebase.getUsersList(listener);
    }

    public void getOwnedCars(String uId,SyncListener listener){
        modelFirebase.getOwnedCars(uId,listener);
    }

    public void getListOfSharedCars(String uId, SyncListener listener){
        modelFirebase.getListOfSharedCars(uId,listener);
    }

    public void addCarToDB(final Car car, final SyncListener listener){
        modelFirebase.addCarToDB(car, listener);
    }

    public void updateCar(Car car,Model.SyncListener listener){
        modelFirebase.updateCar(car,listener);
    }

    public void getAllCars(SyncListener listener){
        modelFirebase.getListOfAllCarsInDB(listener);
    }

    public void parkCar(Parking parking, SyncListener listener){
        modelFirebase.parkCar(parking,listener);
    }

    public void getMyUnparkedCars(String uid, SyncListener listener){
        modelFirebase.getMyUnparkedCars(uid, listener);
    }

    public void getAllMyParkedCars(SyncListener listener){
        modelFirebase.getAllMyParkedCars(listener);
    }

    public void getAllMyParkingSpots(SyncListener listener){
        modelFirebase.getAllMyParkingSpots(listener);
    }

    public void stopParking(Parking parking){
        modelFirebase.stopParking(parking);
    }

    public void stopParking(Car car){
        modelFirebase.stopParking(car);
    }

    //--- Listeners ---- //
    public interface SyncListener{
        void isSuccessful(boolean success);
        void failed(String message);
        void PassData(Object data);
    }





    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >() {
            @Override
            protected Bitmap doInBackground(String... params) {
                //first try to fin the image on the device
                Bitmap bmp = fileManager.loadImageFromFile(imageName);

                if (bmp == null) {
                    bmp = modelCloudinary.loadImage(imageName);
                    //save the image locally for next time
                    if (bmp != null)
                        fileManager.saveImageToFile(bmp,imageName);
                }
                return bmp;
            }
            @Override
            protected void onPostExecute(Bitmap result) {
                listener.onResult(result);
            }
        };
        task.execute();
    }


    public interface LoadImageListener{
        public void onResult(Bitmap imageBmp);
    }
  /*  public void SetLocalBitmap(Bitmap image)
    {
        this.image = image;
        for(int i=0; i<data.size(); i++)
            data.get(i).setImageProduct(image);

        modelCloudinary.uploadImage(data.get(0).getImageProductLink(), data.get(0).getImageProduct());
    }
*/

/*
    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        saveImageToFile(imageBitmap,imageName); // synchronously save image locally
        Thread d = new Thread(new Runnable() {  // asynchronously save image to parse
            @Override
            public void run() {
                modelCloudinary.saveImage(imageBitmap,imageName);
            }
        });
        d.start();
    }

    public interface LoadImageListener{
        public void onResult(Bitmap imageBmp);
    }

    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >() {
            @Override
            protected Bitmap doInBackground(String... params) {
                Bitmap bmp = loadImageFromFile(imageName);              //first try to fin the image on the device
                if (bmp == null) {                                      //if image not found - try downloading it from parse
                    bmp = modelCloudinary.loadImage(imageName);
                    if (bmp != null) saveImageToFile(bmp,imageName);    //save the image locally for next time
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                listener.onResult(result);
            }
        };
        task.execute();
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        FileOutputStream fos;
        OutputStream out = null;
        try {
            //File dir = context.getExternalFilesDir(null);
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //add the picture to the gallery so we dont need to manage the cache size
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
            Log.d("tag","add image to cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        String str = null;
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);

            //File dir = context.getExternalFilesDir(null);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

*/


}

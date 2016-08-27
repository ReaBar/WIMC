package com.example.reabar.wimc.Model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.reabar.wimc.FilesManagerHelper;
import com.example.reabar.wimc.MyApplication;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
        fileManager = new FilesManagerHelper(MyApplication.getAppContext());
        modelFirebase = new ModelFirebase();
        modelSql = new ModelSql();
        modelCloudinary = new ModelCloudinary(MyApplication.getAppContext());
        getAPIVersion();
        modelFirebase.getListOfAllCarsInDB(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                for (Car car:(List<Car>)data) {
                    modelSql.addCar(car);
                }
                modelSql.updateCarsDbTime(System.currentTimeMillis());
            }
        });

        modelFirebase.getMyParkingSpots(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                for (Parking parking:(List<Parking>)data) {
                    modelSql.parkCar(parking);
                }
                modelSql.updateParkingDbTime(System.currentTimeMillis());
            }
        });

        modelFirebase.getUsersList(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                for (User user:(List<User>)data) {
                    modelSql.addUser(user);
                }
                modelSql.updateUsersDbTime(System.currentTimeMillis());
            }
        });
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void signupUser(final User user, final String password, final SyncListener listener){
        modelFirebase.signupUser(user, password, listener);
        modelSql.addUser(user);
        updateUsersDbTime();
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

    public void getUsersList(final Model.SyncListener listener){
        final String lastUpdateDate = modelSql.getUsersLastUpdateTime();
        final List<User> usersList = new ArrayList<>();
        modelFirebase.getUsersDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if(data == null ||lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0){
                    modelFirebase.getUsersList(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            if(data != null){
                                for (User user: (List<User>)data) {
                                    modelSql.addUser(user);
                                }
                                modelSql.updateUsersDbTime(System.currentTimeMillis());

                                listener.passData(data);
                            }
                        }
                    });
                    if(data == null){
                        modelFirebase.updateUsersDbTime(System.currentTimeMillis());
                    }
                }
                else{
                    modelSql.getUsersList(listener);
                }
            }
        });
    }

    public void getOwnedCars(final String uId, final SyncListener listener){
        final String lastUpdateDate = modelSql.getCarLastUpdate();
        final List<Car> carsList = new ArrayList<>();
        modelFirebase.getCarDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if(data == null){
                    listener.passData(carsList);
                }

                else if (lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getOwnedCars(uId, new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            if(data != null){
                                for (Car car:(List<Car>)data) {
                                    if (modelSql.getCarById(car.getCarId()) == null) {
                                        modelSql.addCar(car);
                                    }
                                }
                                modelSql.updateCarsDbTime(System.currentTimeMillis());
                                listener.passData(data);
                            }
                        }
                    });
                }

                else{
                    modelSql.getOwnedCars(uId,listener);
                }
            }
        });
    }

    public void getListOfSharedCars(final String uId,final SyncListener listener){
        final String lastUpdateDate = modelSql.getCarLastUpdate();
        final List<Car> carsList = new ArrayList<>();
        modelFirebase.getCarDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if(data == null){
                    listener.passData(carsList);
                }

                else if (lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getListOfSharedCars(uId, new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            if(data != null){
                                for (Car car:(List<Car>)data) {
                                    if (modelSql.getCarById(car.getCarId()) == null) {
                                        modelSql.addCar(car);
                                    }
                                }
                                modelSql.updateCarsDbTime(System.currentTimeMillis());
                                listener.passData(data);
                            }
                        }
                    });
                }

                else{
                    modelSql.getListOfSharedCars(uId,listener);
                }
            }
        });
    }

    public void addCarToDB(final Car car, final SyncListener listener){
        modelFirebase.addCarToDB(car, listener);
        modelSql.addCar(car);
        updateCarDbTime();
    }

    public void updateCar(Car car,Model.SyncListener listener){
        modelFirebase.updateCar(car, listener);
        modelSql.updateCar(car);
        updateCarDbTime();
    }

    public void getAllCars(final SyncListener listener){
        final String lastUpdateDate = modelSql.getCarLastUpdate();
        final ArrayList<Car> carList= new ArrayList<>();
        modelFirebase.getCarDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if(data == null){
                    listener.passData(carList);
                }

                else if (lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            for (Car car : (ArrayList<Car>) data) {
                                if (modelSql.getCarById(car.getCarId()) == null) {
                                    modelSql.addCar(car);
                                }
                            }
                            modelSql.updateCarsDbTime(System.currentTimeMillis());
                            listener.passData(data);
                        }
                    });
                } else {
                    modelSql.getAllCars(listener);
                }
            }
        });
    }

    public void parkCar(Parking parking, SyncListener listener){
        modelFirebase.parkCar(parking, listener);
        modelSql.parkCar(parking);
        updateParkingDbTime();
    }

    public void getMyUnparkedCars(final String uId,final SyncListener listener){
        final String lastUpdateDate = modelSql.getParkingLastUpdate();
        final ArrayList<Car> unparkedCars= new ArrayList<>();
        modelFirebase.getParkingDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                 if (data == null || lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getMyUnparkedCars(uId,new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if(data != null){
                                        for (Car car:(List<Car>)data) {
                                            if(modelSql.getCarById(car.getCarId()) == null){
                                                modelSql.addCar(car);
                                            }
                                        }
                                        modelSql.updateCarsDbTime(System.currentTimeMillis());
                                    }
                                }
                            });

                            modelFirebase.getMyParkingSpots(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if(data != null){
                                        for (Parking parking: (List<Parking>)data) {
                                            modelSql.parkCar(parking);
                                        }
                                        modelSql.updateParkingDbTime(System.currentTimeMillis());
                                    }
                                }
                            });
                            if(data == null){
                                modelFirebase.updateParkingDbTime(System.currentTimeMillis());
                            }
                            listener.passData(data);
                        }
                    });
                } else {
                    modelSql.getMyUnparkedCars(listener);
                }
            }
        });
    }

    public void getMyParkedCars(final SyncListener listener){
        final String lastUpdateDate = modelSql.getParkingLastUpdate();
        final ArrayList<Car> parkedCars= new ArrayList<>();
        modelFirebase.getParkingDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if(data == null){
                    listener.passData(parkedCars);
                }

                else if (lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getMyParkedCars(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if(data != null){
                                        for (Car car:(List<Car>)data) {
                                            if(modelSql.getCarById(car.getCarId()) == null){
                                                modelSql.addCar(car);
                                            }
                                        }
                                        modelSql.updateCarsDbTime(System.currentTimeMillis());
                                    }
                                }
                            });

                            modelFirebase.getMyParkingSpots(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if(data != null){
                                        for (Parking parking: (List<Parking>)data) {
                                            modelSql.parkCar(parking);
                                        }
                                        modelSql.updateParkingDbTime(System.currentTimeMillis());
                                    }
                                }
                            });
                            listener.passData(data);
                        }
                    });
                } else {
                    modelSql.getMyParkedCars(listener);
                }
            }
        });
    }

    public void getMyParkingSpots(final SyncListener listener){
        final String lastUpdateDate = modelSql.getParkingLastUpdate();
        final ArrayList<Parking> parkingSpots= new ArrayList<>();
        modelFirebase.getParkingDbTime(new SyncListener() {
            @Override
            public void isSuccessful(boolean success) {

            }

            @Override
            public void failed(String message) {

            }

            @Override
            public void passData(Object data) {
                if(data == null){
                    listener.passData(parkingSpots);
                }

                else if (lastUpdateDate == null || data.toString().compareTo(lastUpdateDate) > 0) {
                    modelFirebase.getMyParkingSpots(new SyncListener() {
                        @Override
                        public void isSuccessful(boolean success) {

                        }

                        @Override
                        public void failed(String message) {

                        }

                        @Override
                        public void passData(Object data) {
                            modelFirebase.getListOfAllCarsInDB(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if(data != null){
                                        for (Car car:(List<Car>)data) {
                                            if(modelSql.getCarById(car.getCarId()) == null){
                                                modelSql.addCar(car);
                                            }
                                        }
                                        modelSql.updateCarsDbTime(System.currentTimeMillis());
                                    }
                                }
                            });

                            modelFirebase.getMyParkingSpots(new SyncListener() {
                                @Override
                                public void isSuccessful(boolean success) {

                                }

                                @Override
                                public void failed(String message) {

                                }

                                @Override
                                public void passData(Object data) {
                                    if(data != null){
                                        for (Parking parking: (List<Parking>)data) {
                                            modelSql.parkCar(parking);
                                        }
                                        modelSql.updateParkingDbTime(System.currentTimeMillis());
                                    }
                                }
                            });
                            listener.passData(data);
                        }
                    });
                } else {
                    modelSql.getMyParkingSpots(listener);
                }
            }
        });
    }

    public void stopParking(Parking parking){
        modelFirebase.stopParking(parking);
        modelSql.stopParking(parking);
        updateParkingDbTime();
    }

    public void stopParking(Car car){
        modelFirebase.stopParking(car);
        modelSql.stopParking(car);
        updateParkingDbTime();
    }

    public void updateCarDbTime(){
        long currentTime = System.currentTimeMillis();
        modelFirebase.updateCarDbTime(currentTime);
        modelSql.updateCarsDbTime(currentTime);
    }

    public void updateParkingDbTime(){
        long currentTime = System.currentTimeMillis();
        modelFirebase.updateParkingDbTime(currentTime);
        modelSql.updateParkingDbTime(currentTime);
    }

    public void updateUsersDbTime(){
        long currentTime = System.currentTimeMillis();
        modelFirebase.updateUsersDbTime(currentTime);
        modelSql.updateUsersDbTime(currentTime);
    }

    public float getAPIVersion() {

        float f=1f;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f= Float.valueOf(strBuild.toString());
            Log.d("deviceVersion","device OS version is: " + f);
        } catch (NumberFormatException e) {
            Log.e("deviceVersion", "error retrieving api version" + e.getMessage());
        }

        return f;
    }

    public List<Address> getLatandLong(String locationName){
        List<Address> result = new ArrayList<>();
        try {
            result = new Geocoder(MyApplication.getAppContext()).getFromLocationName(locationName, 5);
            return result;
        } catch (IOException e) {
            Log.e("location",e.getMessage());
        }
        return null;

    }

    //--- Listeners ---- //
    public interface SyncListener{
        void isSuccessful(boolean success);
        void failed(String message);
        void passData(Object data);
    }





    public void loadImage(final String imageName, final LoadImageListener listener) {
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >() {
            @Override
            protected Bitmap doInBackground(String... params) {
                //Bitmap bmp = modelCloudinary.loadImage(imageName);
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







    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        saveImageToFile(imageBitmap,imageName); // synchronously save image locally
        Thread d = new Thread(new Runnable() {  // asynchronously save image to parse
            @Override
            public void run() {
                modelCloudinary.uploadImage(imageName, imageBitmap);
            }
        });
        d.start();
    }


    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        FileOutputStream fos;
        OutputStream out = null;
        try {
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
            MyApplication.getAppContext().sendBroadcast(mediaScanIntent);
            Log.d("tag", "add image to cache: " + imageFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

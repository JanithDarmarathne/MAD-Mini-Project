package com.example.dashboard.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.dashboard.Models.Comment;
import com.example.dashboard.Models.ProductUser;
import com.example.dashboard.Models.User;



import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ShopDB {
    //user
    public static final String KEY_ID = "Id";
    public static final String KEY_Name = "Name";
    public static final String KEY_Email = "Email";
    public static final String KEY_Password = "Password";
    public static final String KEY_Address = "Addresss";
    public static final String KEY_Image = "Image";
    //product
    public static final String KEY_ProductName = "Name";
    public static final String KEY_ProductCategory = "ProductCategory";
    //Comment
    public  static final String KEY_Comment = "Comments";

    private final String DATABASE_NAME = "ShopDB";
    private final String DATABASE_UserTable = "User";
    private final String DATABASE_ProductTable = "Product";
    private final String DATABASE_CommentTable = "CommentTable";
    private final int DATABASE_VERSION = 1;

    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabse;
    User user;
    ArrayList<ProductUser> list;
    ArrayList<Comment> list1;

    private ByteArrayOutputStream object;
    private byte[] imageInBytes;
    Bitmap objectBitmap;

    public ShopDB(Context context){
        ourContext=context;
    }


    public class DBHelper  extends SQLiteOpenHelper {

        public DBHelper(Context context){

            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            String sqlCode= "CREATE TABLE "+DATABASE_UserTable+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_Name+" TEXT NOT NULL, "+KEY_Email+" TEXT NOT NULL,"+KEY_Address+" TEXT NOT NULL,"+KEY_Password+" TEXT NOT NULL,"+KEY_Image+" BLOB);";
            String sqlCode1= "CREATE TABLE "+DATABASE_ProductTable+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_ProductName+" TEXT NOT NULL, "+KEY_ProductCategory+" TEXT NOT NULL);";
            String sqlCode2= "CREATE TABLE "+DATABASE_CommentTable+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+KEY_Comment+" TEXT NOT NULL);";

            db.execSQL(sqlCode);
            db.execSQL(sqlCode1);
            db.execSQL(sqlCode2);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_UserTable);
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_ProductTable);
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_CommentTable);

            onCreate(db);

        }



    }

    public ShopDB open() throws SQLException {

        ourHelper = new DBHelper(ourContext);
        ourDatabse = ourHelper.getWritableDatabase();
        return this;
    }

    public void close(){

        ourHelper.close();


    }

    public long CreateUser(String name, String email, String address, String password, Bitmap imagebit){

        if(imagebit != null){

            Bitmap imagetoStorebitmap = imagebit;
            object = new ByteArrayOutputStream();
            imagetoStorebitmap.compress(Bitmap.CompressFormat.JPEG,100,object);

            imageInBytes = object.toByteArray();

            ContentValues cv = new ContentValues();
            cv.put(KEY_Name,name);
            cv.put(KEY_Email,email);
            cv.put(KEY_Address,address);
            cv.put(KEY_Password,password);
            cv.put(KEY_Image,imageInBytes);

            return  ourDatabse.insert(DATABASE_UserTable,null,cv);
        }

        return 0;
    }

    public long CreateProduct(String productName,String productCategory){

        ContentValues cv = new ContentValues();
        cv.put(KEY_ProductName,productName);
        cv.put(KEY_ProductCategory,productCategory);


        return  ourDatabse.insert(DATABASE_ProductTable,null,cv);


    }

    public long addComment(String Comment){

        ContentValues cv = new ContentValues();
        cv.put(KEY_Comment,Comment);

        return  ourDatabse.insert(DATABASE_CommentTable,null,cv);


    }

    public User getUser(String email){

        String [] columns = {KEY_ID,KEY_Name,KEY_Email,KEY_Address};
        Cursor c = ourDatabse.rawQuery("Select * FROM "+DATABASE_UserTable+" Where Email= ?",new String[]{email});

        String results="";

        int iROWID = c.getColumnIndex(KEY_ID);
        int iNAME = c.getColumnIndex(KEY_Name);
        int iEmail = c.getColumnIndex(KEY_Email);
        int iAddress = c.getColumnIndex(KEY_Address);


        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            //  results = c.getString(iROWID)+": "+c.getString(iNAME)+" "+c.getString(iCELL)+"\n"+"\n";
             user = new User(c.getString(iNAME),c.getString(iEmail),c.getString(iAddress));
        }

        c.close();


        return user;
    }


    public int getSelectedUser(String email, String password){

        String [] columns = {KEY_ID,KEY_Email,KEY_Password};
        Cursor c = ourDatabse.rawQuery("Select "+KEY_ID+", "+KEY_Email+" FROM "+DATABASE_UserTable+" Where Password= ? AND Email= ?",new String[]{password, email});
        int result = 0;
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            //  results = c.getString(iROWID)+": "+c.getString(iNAME)+" "+c.getString(iCELL)+"\n"+"\n";
           result++;
        }

        c.close();


        return result;
    }

    public Bitmap getUserImage(String email, String name){

        String [] columns = {KEY_ID,KEY_Email,KEY_Password};
        Cursor c = ourDatabse.rawQuery("Select "+KEY_Image+" FROM "+DATABASE_UserTable+" Where Name= ? AND Email= ?",new String[]{name, email});
        int result = 0;

        int iImage = c.getColumnIndex(KEY_Image);

        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            byte[] imageByte = c.getBlob(iImage);

            objectBitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.length);

            result++;
        }

        c.close();


        return  objectBitmap;
    }

    public ArrayList<ProductUser> getProduct(){

        String results="";
        list = new ArrayList<>();

        String [] columns = {KEY_ID,KEY_ProductName,KEY_ProductCategory};
        Cursor c = ourDatabse.query(DATABASE_ProductTable,columns,null,null,null,null,null);
        int iROWID = c.getColumnIndex(KEY_ID);
        int iNAME = c.getColumnIndex(KEY_ProductName);
        int iProductCategory = c.getColumnIndex(KEY_ProductCategory);
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
           // Product product = ;

           // results += c.getString(iROWID)+": "+c.getString(iNAME)+" "+c.getString(iProductCategory)+"\n"+"\n";
         //   list.add(new Product(c.getInt(iROWID),c.getString(iNAME),c.getString(iProductCategory)));

        }

        c.close();


        return list;
    }


    public ArrayList<Comment> getComment(){

        String results="";
        list1 = new ArrayList<>();

        String [] columns = {KEY_ID,KEY_Comment};
        Cursor c = ourDatabse.query(DATABASE_CommentTable,columns,null,null,null,null,null);
        int iROWID = c.getColumnIndex(KEY_ID);
        int iNAME = c.getColumnIndex(KEY_Comment);
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){

            list1.add(new Comment(c.getInt(iROWID),c.getString(iNAME)));

        }

        c.close();


        return list1;
    }

    public long deleteUser(String rowID){
        return ourDatabse.delete(DATABASE_UserTable,KEY_ID+"=?",new String[]{rowID});

    }

    public void  deleteProduct(String rowID){

         ourDatabse.delete(DATABASE_ProductTable,KEY_ID+"=?",new String[]{rowID});

    }

    public long updateUser(String rowID, String name, String email,String address){

        ContentValues cv = new ContentValues();
        cv.put(KEY_ID,rowID);
        cv.put(KEY_Name,name);
        cv.put(KEY_Email,email);
        cv.put(KEY_Address,address);

        return ourDatabse.update(DATABASE_UserTable,cv,KEY_ID+"=?",new String[]{rowID});

    }

    public long updateProduct(String rowID, String name, String category){

        ContentValues cv = new ContentValues();
        cv.put(KEY_ID,rowID);
        cv.put(KEY_ProductName,name);
        cv.put(KEY_ProductCategory,category);

        return ourDatabse.update(DATABASE_ProductTable,cv,KEY_ID+"=?",new String[]{rowID});

    }
}

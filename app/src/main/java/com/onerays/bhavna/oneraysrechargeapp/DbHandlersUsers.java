package com.onerays.bhavna.oneraysrechargeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

//Most important class of all!!!
public class DbHandlersUsers {

    DatabaseHelper helper;
    Context context;

    public DbHandlersUsers(Context context) {
        helper = new DatabaseHelper(context);
        this.context = context;
    }

    //USER TABLE
    //Adding User to the USERS table

    long addUser(long mobile, String email, String password, String name, String city, String date, String time) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DatabaseHelper.MOBILE, mobile);
        values.put( DatabaseHelper.EMAIL, email);
        values.put( DatabaseHelper.PASSWORD, password);
        values.put( DatabaseHelper.NAME, name);
        values.put( DatabaseHelper.CITY, city);
        values.put( DatabaseHelper.REG_DATE, date);
        values.put( DatabaseHelper.REG_TIME, time);

        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_USERS, null, values);
        } catch (Exception e) {
            System.out.println(""+e);
        }
        return id;
    }

    //USER TABLE
    //Getting data from the Users table

    public String[] getData(long mobile) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.MOBILE, DatabaseHelper.EMAIL, DatabaseHelper.NAME, DatabaseHelper.CITY, DatabaseHelper.REG_DATE, DatabaseHelper.REG_TIME};
        Cursor cursor = db.query( DatabaseHelper.TABLE_USERS, columns, DatabaseHelper.MOBILE + "='" + mobile + "'", null, null, null, null, null);

        int index1 = cursor.getColumnIndex( DatabaseHelper.MOBILE);
        int index2 = cursor.getColumnIndex( DatabaseHelper.EMAIL);
        int index3 = cursor.getColumnIndex( DatabaseHelper.NAME);
        int index4 = cursor.getColumnIndex( DatabaseHelper.CITY);
        int index5 = cursor.getColumnIndex( DatabaseHelper.RE_TIME);
        int index6 = cursor.getColumnIndex( DatabaseHelper.RE_DATE);

        String[] values = new String[5];
        while (cursor.moveToNext()) {
            long s1 = cursor.getLong(index1);
            String s2 = cursor.getString(index2);
            String s3 = cursor.getString(index3);
            String s4 = cursor.getString(index4);
            String s5 = cursor.getString(index5);
            String s6 = cursor.getString(index6);

            if (s1 == mobile) {
                values[0] = s2;
                values[1] = s3;
                values[2] = s4;
                values[3] = s5;
                values[4] = s6;
                return values;
            }
        }
        cursor.close();
        return values;
    }



    //USERS TABLE
    //Getting usersName and password for validation during login

    public String checkMobile(long mobile) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String column[] = {DatabaseHelper.MOBILE, DatabaseHelper.PASSWORD};
        Cursor cursor = db.query( DatabaseHelper.TABLE_USERS, column, DatabaseHelper.MOBILE + "='" + mobile + "'", null, null, null, null);

        int index1 = cursor.getColumnIndex( DatabaseHelper.PASSWORD);
        int index2 = cursor.getColumnIndex( DatabaseHelper.MOBILE);

        StringBuilder builder = new StringBuilder();
        while (cursor.moveToNext()) {
            String s1 = cursor.getString(index1);
            long s2 = cursor.getLong(index2);
            if (s2 == mobile) {
                builder.append(s1);
                return builder.toString();
            }
        }

        return builder.toString();
    }

    //Checking whether mobile already exist
    //USER TABLE

    public boolean checkMobileExist(long mobile) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] column = {DatabaseHelper.MOBILE};
        Cursor cursor = db.query( DatabaseHelper.TABLE_USERS, column, DatabaseHelper.MOBILE + "='" + mobile + "'", null, null, null, null);

        int index = cursor.getColumnIndex( DatabaseHelper.MOBILE);
        cursor.moveToPosition(-1);
        while (cursor.moveToNext()) {
            long s = cursor.getLong(index);
            System.out.println(s);
            if (mobile == s) {
                cursor.close();
                return TRUE;            // mobile Exists
            }
        }
        cursor.close();
        return FALSE;               // mobile Not Exists
    }

    //USERS TABLE
    //getting the columns from user table

   public String[] getColumnUsers(){
//        //Order will depend on the order of how you are displaying values in your list item
//        // i.e. the values in String []to array in the listview activity
        String[]a={DatabaseHelper.MOBILE, DatabaseHelper.EMAIL, DatabaseHelper.PASSWORD, DatabaseHelper.NAME, DatabaseHelper.CITY};
       return a;
    }

    //USERS TABLE
    //getting the cursor and sending all the columns
    public Cursor getAllColumnsUsers(){
       SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns={DatabaseHelper.MOBILE, DatabaseHelper.EMAIL, DatabaseHelper.PASSWORD, DatabaseHelper.NAME, DatabaseHelper.CITY};

        Cursor cursor=db.query( DatabaseHelper.TABLE_USERS,columns,null,null,null,null,null);
        return cursor;
   }

    // Before Registering check for blank db
   public boolean statusOfUserTable(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String count = "SELECT count(*) FROM users";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
       // There is Data hence check for already existing MobNo
       return icount > 0;
    }

    //Recharge TABLE
    //Adding Recharges Made By Users

    long addRecharge(long usermobile, long remobile, String mode, int amount, String operator, String state, String date, String time, long cardno) {

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( DatabaseHelper.USER_MOBILE_ID, usermobile);
        values.put( DatabaseHelper.RECHARGE_MOBILE_NO, remobile);
        values.put( DatabaseHelper.RECHARGE_MODE, mode);
        values.put( DatabaseHelper.AMOUNT, amount);
        values.put( DatabaseHelper.OPERATOR, operator);
        values.put( DatabaseHelper.STATE, state);
        values.put( DatabaseHelper.RE_DATE, date);
        values.put( DatabaseHelper.RE_TIME, time);
        values.put( DatabaseHelper.RE_CARD, cardno);

        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_RECHARGE, null, values);
        } catch (Exception e) {
            System.out.println(""+e);
        }
        return id;
    }

    //RECHARGE TABLE
    //Getting data from the Recharge table

   public String[] getRechargeData(long mobile) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.USER_MOBILE_ID, DatabaseHelper.RECHARGE_MOBILE_NO, DatabaseHelper.RECHARGE_MODE, DatabaseHelper.AMOUNT, DatabaseHelper.OPERATOR, DatabaseHelper.STATE, DatabaseHelper.RE_DATE, DatabaseHelper.RE_TIME, DatabaseHelper.RE_CARD};
        Cursor cursor = db.query( DatabaseHelper.TABLE_RECHARGE, columns, DatabaseHelper.USER_MOBILE_ID +"='"+mobile+"'", null, null, null, null, null);

        int index1 = cursor.getColumnIndex( DatabaseHelper.USER_MOBILE_ID);
        int index2 = cursor.getColumnIndex( DatabaseHelper.RECHARGE_MOBILE_NO);
        int index3 = cursor.getColumnIndex( DatabaseHelper.RECHARGE_MODE);
        int index4 = cursor.getColumnIndex( DatabaseHelper.AMOUNT);
        int index5 = cursor.getColumnIndex( DatabaseHelper.OPERATOR);
        int index6 = cursor.getColumnIndex( DatabaseHelper.STATE);
        int index7 = cursor.getColumnIndex( DatabaseHelper.RE_DATE);
        int index8 = cursor.getColumnIndex( DatabaseHelper.RE_TIME);
        int index9 = cursor.getColumnIndex( DatabaseHelper.RE_CARD);


        String[]values = new String[8];
        while (cursor.moveToNext()) {
           long s1 = cursor.getLong(index1);
            String s2 = cursor.getString(index2);
            String s3 = cursor.getString(index3);
            String s4 = cursor.getString(index4);
            String s5 = cursor.getString(index5);
            String s6 = cursor.getString(index6);
            String s7 = cursor.getString(index7);
            String s8 = cursor.getString(index8);
            String s9 = cursor.getString(index9);

           if(s1==mobile){
                values[0]=s2;
                values[1]=s3;
                values[2]=s4;
                values[3]=s5;
                values[4]=s6;
                values[5]=s7;
                values[6]=s8;
                values[7]=s9;
                return  values;
            }
        }
        return values;
    }

    /* DTH Table - Adding DTH */
    //Adding Recharges Made By Users

    long addDth(long userno, long dthno, int rdth, String dthprovider, String date, String time, long cardno) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( DatabaseHelper.DTH_MOB_USERID, userno);
        values.put( DatabaseHelper.DTH_NO, dthno);
        values.put( DatabaseHelper.DTH_RECHARGE_AMOUNT, rdth);
        values.put( DatabaseHelper.DTH_PROVIDER, dthprovider);
        values.put( DatabaseHelper.DTH_DATE, date);
        values.put( DatabaseHelper.DTH_TIME, time);
        values.put( DatabaseHelper.DTH_CARD, cardno);

        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_DTH, null, values);
        } catch (Exception e) {
            System.out.println(""+e);
        }
        return id;
    }

    //USER TABLE
    //Getting data from the Recharge table

   public String[] getDTHData(long mobile) {
       SQLiteDatabase db = helper.getWritableDatabase();
       String[] columns = {DatabaseHelper.DTH_MOB_USERID, DatabaseHelper.DTH_NO, DatabaseHelper.DTH_RECHARGE_AMOUNT, DatabaseHelper.DTH_PROVIDER, DatabaseHelper.DTH_DATE, DatabaseHelper.DTH_TIME, DatabaseHelper.DTH_CARD};
       Cursor cursor = db.query( DatabaseHelper.TABLE_DTH, columns, DatabaseHelper.DTH_MOB_USERID +"='"+mobile+"'",null ,null, null, null);

       int index1 = cursor.getColumnIndex( DatabaseHelper.DTH_MOB_USERID);
       int index2 = cursor.getColumnIndex( DatabaseHelper.DTH_NO);
        int index3 = cursor.getColumnIndex( DatabaseHelper.DTH_RECHARGE_AMOUNT);
        int index4 = cursor.getColumnIndex( DatabaseHelper.DTH_PROVIDER);
        int index5 = cursor.getColumnIndex( DatabaseHelper.DTH_DATE);
        int index6 = cursor.getColumnIndex( DatabaseHelper.DTH_TIME);
       int index7 = cursor.getColumnIndex( DatabaseHelper.DTH_CARD);

        String[]values=new String[6];
        while (cursor.moveToNext()) {
              long s1 = cursor.getLong(index1);
            String s2 = cursor.getString(index2);
            String s3 = cursor.getString(index3);
            String s4 = cursor.getString(index4);
            String s5 = cursor.getString(index5);
            String s6 = cursor.getString(index6);
            String s7 = cursor.getString(index7);

            if(s1==mobile){
                values[0]=s2;
                values[1]=s3;
                values[2]=s4;
                values[3]=s5;
                values[4]=s6;
                values[5]=s7;
                return  values;
            }
       }

        return values;
   }

    /* Landline Table - Adding Landline */
    //Adding Recharges Made By Users

    long addLandline(long userno, long landno, int rland, String landprovider, String date, String time, long cardno) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( DatabaseHelper.LAND_MOB_USERID, userno);
        values.put( DatabaseHelper.LAND_NO, landno);
        values.put( DatabaseHelper.LAND_RECHARGE_AMOUNT, rland);
        values.put( DatabaseHelper.LAND_PROVIDER, landprovider);
        values.put( DatabaseHelper.LAND_DATE, date);
        values.put( DatabaseHelper.LAND_TIME, time);
        values.put( DatabaseHelper.LAND_CARD, cardno);

        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_LANDLINE, null, values);
        } catch (Exception e) {
            System.out.println(""+e);
        }
        return id;
    }

    //Getting data from the Landline table

    public String[] getLandlineData(long mobile) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {DatabaseHelper.LAND_MOB_USERID, DatabaseHelper.LAND_NO, DatabaseHelper.LAND_RECHARGE_AMOUNT, DatabaseHelper.LAND_PROVIDER, DatabaseHelper.LAND_DATE, DatabaseHelper.LAND_TIME, DatabaseHelper.LAND_CARD};
        Cursor cursor = db.query( DatabaseHelper.TABLE_LANDLINE, columns, DatabaseHelper.LAND_MOB_USERID +"='"+mobile+"'",null ,null, null, null, null);

       int index1 = cursor.getColumnIndex( DatabaseHelper.LAND_MOB_USERID);
       int index2 = cursor.getColumnIndex( DatabaseHelper.LAND_NO);
       int index3 = cursor.getColumnIndex( DatabaseHelper.LAND_RECHARGE_AMOUNT);
       int index4 = cursor.getColumnIndex( DatabaseHelper.LAND_PROVIDER);
       int index5 = cursor.getColumnIndex( DatabaseHelper.LAND_DATE);
       int index6 = cursor.getColumnIndex( DatabaseHelper.LAND_TIME);
       int index7 = cursor.getColumnIndex( DatabaseHelper.LAND_CARD);

        String[]values = new String[6];
       while (cursor.moveToNext()) {
           long s1 = cursor.getLong(index1);
            String s2 = cursor.getString(index2);
            String s3 = cursor.getString(index3);
            String s4 = cursor.getString(index4);
            String s5 = cursor.getString(index5);
            String s6 = cursor.getString(index6);
            String s7 = cursor.getString(index7);
            if(s1==mobile){
                values[0]=s2;
                values[1]=s3;
                values[2]=s4;
                values[3]=s5;
                values[4]=s6;
                values[5]=s7;
                return  values;
            }
        }

        return values;
    }

/* BankDetails Table - Adding BankDetails */

    long addBankDetails(long cardno, String expiry, String cvv, long userno, String cUsername, String bankname, String date, String time) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DatabaseHelper.CARD_NO, cardno);
        values.put( DatabaseHelper.CARD_EXP, expiry);
        values.put( DatabaseHelper.CARD_CVV, cvv);
        values.put( DatabaseHelper.BANK_MOB_USERID, userno);
        values.put( DatabaseHelper.CARD_USERNAME, cUsername);
        values.put( DatabaseHelper.BANK_NAME, bankname);
        values.put( DatabaseHelper.BANK_DATE, date);
        values.put( DatabaseHelper.BANK_TIME, time);

        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_BANKDETAILS, null, values);
        } catch (Exception e) {
            System.out.println(""+e);
        }
        return id;
    }

    //Getting data from the Bank table

    public String[] getBankData(long cardno) {

        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DatabaseHelper.CARD_NO, DatabaseHelper.CARD_EXP, DatabaseHelper.CARD_CVV, DatabaseHelper.BANK_MOB_USERID, DatabaseHelper.CARD_USERNAME, DatabaseHelper.BANK_NAME, DatabaseHelper.BANK_BALANCE, DatabaseHelper.BANK_DATE, DatabaseHelper.BANK_TIME};
        Cursor cursor = db.query( DatabaseHelper.TABLE_BANKDETAILS, columns, DatabaseHelper.CARD_NO +"='"+cardno+"'",null ,null, null, null, null);

       int index1 = cursor.getColumnIndex( DatabaseHelper.CARD_NO);
       int index2 = cursor.getColumnIndex( DatabaseHelper.CARD_EXP);
       int index3 = cursor.getColumnIndex( DatabaseHelper.CARD_CVV);
       int index4 = cursor.getColumnIndex( DatabaseHelper.BANK_MOB_USERID);
       int index5 = cursor.getColumnIndex( DatabaseHelper.CARD_USERNAME);
       int index6 = cursor.getColumnIndex( DatabaseHelper.BANK_NAME);
       int index7 = cursor.getColumnIndex( DatabaseHelper.BANK_BALANCE);
       int index8 = cursor.getColumnIndex( DatabaseHelper.BANK_DATE);
       int index9 = cursor.getColumnIndex( DatabaseHelper.BANK_TIME);

       String[]values = new String[8];
        while (cursor.moveToNext()) {
            long s1 = cursor.getLong(index1);
            String s2 = cursor.getString(index2);
            String s3 = cursor.getString(index3);
            String s4 = cursor.getString(index4);
            String s5 = cursor.getString(index5);
            String s6 = cursor.getString(index6);
            String s7 = cursor.getString(index7);
            String s8 = cursor.getString(index8);
           String s9 = cursor.getString(index9);

            if(s1 == cardno){
                values[0]=s2;
                values[1]=s3;
                values[2]=s4;
                values[3]=s5;
                values[4]=s6;
                values[5]=s7;
                values[6]=s8;
                values[7]=s9;
                return  values;
            }
        }

        return values;
    }

    // check whether Bank table contains data or not
    public boolean BankDetailsCount(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String count = "SELECT count(*) FROM bankdetails";
        Cursor mcursor = db.rawQuery(count, null);
        int icount = mcursor.getCount();
        mcursor.close();
        // There is Data hence check for already existing MobNo
        return icount > 0;
    }

    // Before performing any transactions check whether User added any card or not
    public boolean CardDetailsUser(long mobile){
        System.out.print("CardDetailsUser Called");
        SQLiteDatabase db = helper.getWritableDatabase();
        String count = "SELECT * FROM bankdetails WHERE userno = '" + mobile + "' ";
        Cursor cursor = db.rawQuery(count, null);
        int icount = cursor.getCount();
        cursor.close();
        // There is Data hence User Already Added Card
        return icount > 0;
    }

    // Get All the Cards that a User has saved
    public ArrayList<CardBean> getUserCards(long mobile) {

        ArrayList<CardBean> cd = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();

            String[] columns = {DatabaseHelper.BANK_ID, DatabaseHelper.CARD_NO, DatabaseHelper.BANK_NAME, DatabaseHelper.BANK_BALANCE};
            Cursor cursor = db.query( DatabaseHelper.TABLE_BANKDETAILS, columns, DatabaseHelper.BANK_MOB_USERID +"='"+mobile+"'",null ,null, null, null, null);
          String query = "SELECT id,cardno,bankname,balance FROM bankdetails WHERE userno = '" + mobile + "' ";
         // Cursor cursor = db.rawQuery(query, null);
        cursor.moveToPosition(-1);
        while(cursor.moveToNext()) {
            int index = cursor.getColumnIndex( DatabaseHelper.BANK_ID);
            int index1 = cursor.getColumnIndex( DatabaseHelper.CARD_NO);
            int index2 = cursor.getColumnIndex( DatabaseHelper.BANK_NAME);
            int index3 = cursor.getColumnIndex( DatabaseHelper.BANK_BALANCE);

            String s1 = cursor.getString(index);
            String s2 = cursor.getString(index1);
            String s3 = cursor.getString(index2);
            String s4 = cursor.getString(index3);

            CardBean cb = new CardBean( Integer.parseInt(s1), Long.parseLong(s2),s3, Integer.parseInt(s4));
            cd.add(cb);

        }
        cursor.close();
        return cd;
    }


    // Insert the User Images

    long insertImage(long mobile, byte[] imageBytes) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( DatabaseHelper.IMG_USER_MOBILE, mobile);
        values.put( DatabaseHelper.IMAGE, imageBytes);

        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_USERIMAGES, null, values);
        } catch (Exception e) {
            System.out.println(""+e);
        }
        return id;
    }

    // Get the image from Database

    public byte[] retreiveImageFromDB(long mobile) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DatabaseHelper.IMG_USER_MOBILE, DatabaseHelper.IMAGE};
        Cursor cursor = db.query( DatabaseHelper.TABLE_USERIMAGES, columns, DatabaseHelper.IMG_USER_MOBILE +"='"+mobile+"'",null,null,null,null);

        int index1 = cursor.getColumnIndex( DatabaseHelper.IMG_USER_MOBILE);
        int index2 = cursor.getColumnIndex( DatabaseHelper.IMAGE);

        while (cursor.moveToNext()) {
            long s1 = cursor.getLong(index1);
            byte[] s2 = cursor.getBlob(index2);

            if (s1 == mobile) {
                return s2;
            }
        }
        cursor.close();
        return null;
    }

    /* Transactions Table */

    long addTrans(long cardno, String serName, long serNo , int amount, int balance, long userno, String date, String time) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( DatabaseHelper.TRANS_CARDNO, cardno);
        values.put( DatabaseHelper.TRANS_SERNAME, serName);
        values.put( DatabaseHelper.TRANS_SERNO, serNo);
        values.put( DatabaseHelper.TRANS_AMOUNT,amount);
        values.put( DatabaseHelper.TRANS_BALANCE,balance);
        values.put( DatabaseHelper.TRANS_MOB_USERID, userno);
        values.put( DatabaseHelper.TRANS_DATE, date);
        values.put( DatabaseHelper.TRANS_TIME, time);

        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_TRANSACTIONS, null, values);
        } catch (Exception e) {
            System.out.println(""+e);
        }
        return id;
    }

    public ArrayList<TransBean> getTrans(long mobile) {

        ArrayList<TransBean> cd = new ArrayList<>();
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DatabaseHelper.TRANS_ID, DatabaseHelper.TRANS_CARDNO, DatabaseHelper.TRANS_SERNAME, DatabaseHelper.TRANS_SERNO, DatabaseHelper.TRANS_AMOUNT, DatabaseHelper.TRANS_BALANCE, DatabaseHelper.TRANS_DATE, DatabaseHelper.TRANS_TIME};
        Cursor cursor = db.query( DatabaseHelper.TABLE_TRANSACTIONS, columns, DatabaseHelper.TRANS_MOB_USERID +"='"+mobile+"'",null ,null, null, DatabaseHelper.TRANS_ID + " DESC");
        cursor.moveToPosition(-1);
        while(cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex( DatabaseHelper.TRANS_ID);
            int index2 = cursor.getColumnIndex( DatabaseHelper.TRANS_CARDNO);
            int index3 = cursor.getColumnIndex( DatabaseHelper.TRANS_SERNAME);
            int index4 = cursor.getColumnIndex( DatabaseHelper.TRANS_SERNO);
            int index5 = cursor.getColumnIndex( DatabaseHelper.TRANS_AMOUNT);
            int index6 = cursor.getColumnIndex( DatabaseHelper.TRANS_BALANCE);
            int index7 = cursor.getColumnIndex( DatabaseHelper.TRANS_DATE);
            int index8 = cursor.getColumnIndex( DatabaseHelper.TRANS_TIME);

            String s1 = cursor.getString(index1);
            String s2 = cursor.getString(index2);
            String s3 = cursor.getString(index3);
            String s4 = cursor.getString(index4);
            String s5 = cursor.getString(index5);
            String s6 = cursor.getString(index6);
            String s7 = cursor.getString(index7);
            String s8 = cursor.getString(index8);

            TransBean cb = new TransBean( Integer.parseInt(s1), Long.parseLong(s2),s3, Long.parseLong(s4), Integer.parseInt(s5), Integer.parseInt(s6),s7,s8);
            cd.add(cb);

        }
        cursor.close();
        return cd;
    }



    public Cursor getAllUserImages(){

        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DatabaseHelper.IMAGE};
        Cursor cursor = db.query( DatabaseHelper.TABLE_USERIMAGES,columns,null,null,null,null,null);
        return cursor;
    }




    //Adding bank data of the Users. Since we dont have access to the bank's original data, we are creating our own data
    //BANK TABLE

    public long addBankData(long cardNumber, double balance, long mobile, String expiry, int cvv) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put( DatabaseHelper.CARD_NUMBER, cardNumber);
        values.put( DatabaseHelper.BALANCE, balance);
        values.put( DatabaseHelper.MOBILE, mobile);
        values.put( DatabaseHelper.EXPIRY, expiry);
        values.put( DatabaseHelper.CVV, cvv);

        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_CUSTOMERS, null, values);
        } catch (Exception e) {
            System.out.print(""+e);
        }
        return id;
    }


    //Getting a single element from Bank Users Table
    //BANK TABLE
    public String getSingleCustomer() {
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] columns = {DatabaseHelper.CARD_NUMBER, DatabaseHelper.BALANCE, DatabaseHelper.MOBILE, DatabaseHelper.EXPIRY, DatabaseHelper.CVV};
        Cursor cursor = db.query( DatabaseHelper.TABLE_CUSTOMERS, columns, null, null, null, null, null, null);

        StringBuilder sb = new StringBuilder();

        int index1 = cursor.getColumnIndex( DatabaseHelper.CARD_NUMBER );
        int index7 = cursor.getColumnIndex( DatabaseHelper.BALANCE );
        int index8 = cursor.getColumnIndex( DatabaseHelper.EXPIRY );
        int index9 = cursor.getColumnIndex( DatabaseHelper.CVV );
        int index10=cursor.getColumnIndex( DatabaseHelper.CARD_NUMBER );

        cursor.moveToNext();
        long a = cursor.getLong(index1);
        double g = cursor.getDouble(index7);
        String h = cursor.getString(index8);
        int i = cursor.getInt(index9);
        long j=cursor.getLong(index10);

        sb.append(a + " " + g + " " + h + " " + i +" "+j+" \n");

        return sb.toString();
    }



    //Checking for correct Card Number, Expiry and CVV
    //BANK TABLE

    public int checkBalance(long cardNumber){

        SQLiteDatabase db = helper.getWritableDatabase();
        String[]columns= {DatabaseHelper.BANK_BALANCE};
        Cursor cursor = db.query( DatabaseHelper.TABLE_BANKDETAILS,columns, DatabaseHelper.CARD_NO +"='"+cardNumber+"'",null,null,null,null);

        int index1 = cursor.getColumnIndex( DatabaseHelper.BANK_BALANCE);

        int bal = 0 ;
        cursor.moveToPosition(-1);

        while(cursor.moveToNext()){

            bal = cursor.getInt(index1);
            cursor.close();
            return bal;

        }
        cursor.close();
        return bal;
    }


    //BANK TABLE
    //Check if the newly added card already exist

    public boolean checkDuplicateCard (long cardNumber){
        SQLiteDatabase db=helper.getWritableDatabase();

        String[]columns = {DatabaseHelper.CARD_NO};
        Cursor cursor=db.query( DatabaseHelper.TABLE_BANKDETAILS,columns, DatabaseHelper.CARD_NO +"='"+cardNumber+"'",null,null,null,null);

        int index1=cursor.getColumnIndex( DatabaseHelper.CARD_NO);
        cursor.moveToPosition(-1);
        while(cursor.moveToNext()){

            long num = cursor.getLong(index1);
            if(num == cardNumber){
                cursor.close();
                return TRUE ;               // Duplicate Exists
            }
        }
        cursor.close();
        return FALSE;                       // Duplicate Doesnt Exists
    }


    //BANK TABLE
    //Update the balance after the recharge is done

    public boolean updateBalance(long cardNumber , int amount){

        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put( DatabaseHelper.BANK_BALANCE, amount);

        String[]updated={""+cardNumber};

        long id = db.update( DatabaseHelper.TABLE_BANKDETAILS,cv, DatabaseHelper.CARD_NO +" =? ",updated);

        if(id < 0){
            Toast.makeText(this.context, "Balance Not Updated !", Toast.LENGTH_SHORT).show();
            return FALSE;  // Balance Not Updated
        }else{
            Toast.makeText(this.context, "Balance Updated Successfully!", Toast.LENGTH_SHORT).show();
            return TRUE;   // Balance Updated
        }
    }





    public String getbalance(long mobile) {

       SQLiteDatabase db = helper.getWritableDatabase();
       String[] columns = {DatabaseHelper.BANK_BALANCE};
       Cursor cursor = db.query( DatabaseHelper.TABLE_BANKDETAILS, columns, DatabaseHelper.BANK_MOB_USERID +"='"+mobile+"'",null ,null, null, null, null);

      int index = cursor.getColumnIndex( DatabaseHelper.BANK_BALANCE);

       String values = new String();
        cursor.moveToPosition(-1);

        while (cursor.moveToNext()) {
            String s1 = cursor.getString(index);

                values = s1;
                cursor.close();
                return values;
            }
       cursor.close();
        return values;
   }


    //BANK TABLE
   //getting the required column names from the BANK Customer table
    public String[] getColumnBank(){
       //Order will depend on the order of how you are displaying values in your list item
//        // i.e. the values in String []to array in the listview activity
       String[] a={DatabaseHelper.BANK_MOB_USERID, DatabaseHelper.CARD_NO, DatabaseHelper.CARD_USERNAME, DatabaseHelper.BANK_BALANCE, DatabaseHelper.CARD_EXP};
        return a;
   }


    //BANK TABLE
    //Getting the columns for the Accounts Activity
    public String[] getColumnBank1(){
        String[]a={DatabaseHelper.CARD_NUMBER, DatabaseHelper.EXPIRY, DatabaseHelper.CVV};
        return a;
    }


    //BANK TABLE
    //getting all the columns
    public Cursor getAllColumnsBank(){

        SQLiteDatabase db = helper.getWritableDatabase();
       String[] columns={DatabaseHelper.CARD_NO, DatabaseHelper.CARD_USERNAME, DatabaseHelper.CARD_EXP, DatabaseHelper.BANK_MOB_USERID, DatabaseHelper.BANK_BALANCE};
       Cursor cursor=db.query( DatabaseHelper.TABLE_BANKDETAILS,columns,null,null,null,null,null);
       return cursor;
    }

    public Cursor getAllTransaction(Cursor cursor){
        SQLiteDatabase db = helper.getWritableDatabase();
       String[] columns;

       return cursor;
    }



    //BANK TABLE
    //Getting the cursor for only the match mobile number
    public Cursor getAllColumnsBank1(long mobile){
        SQLiteDatabase db=helper.getWritableDatabase();

        String[]columns={DatabaseHelper.CARD_NUMBER, DatabaseHelper.BALANCE, DatabaseHelper.MOBILE, DatabaseHelper.EXPIRY, DatabaseHelper.CVV};

        Cursor cursor=db.query( DatabaseHelper.TABLE_CUSTOMERS,columns, DatabaseHelper.MOBILE +"='"+mobile+"'",null,null,null,null);
        return cursor;
    }

    //Adding data for the transaction history MOBILE
    //MOBILE TABLE
    public long addRechargeInfo(long mobile, double amount, String date, String time, String provider, String tag){
        SQLiteDatabase db=helper.getWritableDatabase();

        String[]columns={DatabaseHelper.MOBILE, DatabaseHelper.PROVIDER, DatabaseHelper.RECHARGE_AMOUNT};

        ContentValues cv=new ContentValues();
        cv.put( DatabaseHelper.MOBILE,mobile);
        cv.put( DatabaseHelper.RECHARGE_AMOUNT,amount);
        cv.put( DatabaseHelper.DATE,date);
        cv.put( DatabaseHelper.TIME,time);
        cv.put( DatabaseHelper.PROVIDER,provider);
        cv.put( DatabaseHelper.TAG,tag);


        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_MOBILE, null, cv);
        } catch (Exception e) {
            System.out.print(""+e);
        }
        return id;
    }

    //getting data from
    //MOBILE Table
    public String getAllMobile(){
        SQLiteDatabase db=helper.getWritableDatabase();

        String[]columns={DatabaseHelper.MOBILE, DatabaseHelper.PROVIDER, DatabaseHelper.RECHARGE_AMOUNT};
        Cursor cursor=db.query( DatabaseHelper.TABLE_MOBILE,columns,null,null,null,null,null);

        StringBuilder sb=new StringBuilder();

        int index1=cursor.getColumnIndex( DatabaseHelper.MOBILE );
        int index2=cursor.getColumnIndex( DatabaseHelper.PROVIDER );
        int index3=cursor.getColumnIndex( DatabaseHelper.RECHARGE_AMOUNT );
        int index4=cursor.getColumnIndex( DatabaseHelper.DATE );
        int index5=cursor.getColumnIndex( DatabaseHelper.TIME );

        while(cursor.moveToNext()){
            long a=cursor.getLong(index1);
            String b=cursor.getString(index2);
            double c=cursor.getDouble(index3);
            String d=cursor.getString(index4);
            String e=cursor.getString(index5);

            sb.append(a+" "+b+" "+c+" "+d+" "+e+"\n");
        }
        return sb.toString();
    }

    //deleting all history after the user logouts
    //MOBILE TABLE
    public void deleteAllData(){
        SQLiteDatabase db=helper.getWritableDatabase();
        db.execSQL("delete from "+ DatabaseHelper.TABLE_MOBILE );
    }

    //Getting all the columns Names for mobile history
    //MOBILE TABLE
    public String[] getColumns(){
        //Order will depend on the order of how you are displaying values in your list item
        // i.e. the values in String []to array in the listview activity
        String[]a={DatabaseHelper.MOBILE, DatabaseHelper.TAG, DatabaseHelper.PROVIDER, DatabaseHelper.RECHARGE_AMOUNT, DatabaseHelper.DATE, DatabaseHelper.TIME};
        return a;
    }

    //Getting details from mobile table
    //MOBILE TABLE
    public Cursor getHistory(){
        SQLiteDatabase db=helper.getWritableDatabase();
        String[]columns={DatabaseHelper.MOBILE, DatabaseHelper.MOBILE, DatabaseHelper.RECHARGE_AMOUNT, DatabaseHelper.DATE, DatabaseHelper.TIME, DatabaseHelper.PROVIDER, DatabaseHelper.TAG};

        Cursor c=db.query( DatabaseHelper.TABLE_MOBILE,columns,null,null,null,null,null,null);

        return c;
    }

    //CARD TABLE
    //Inserting the value into the table from AddNewCard class
   public long addCardDetails(long number, String expiry, String cvv){
        SQLiteDatabase db=helper.getWritableDatabase();

        ContentValues cv=new ContentValues();
        cv.put( DatabaseHelper.CARD_NUMBER_CARD,number);
        cv.put( DatabaseHelper.CARD_EXPIRY_CARD,expiry);
        cv.put( DatabaseHelper.CARD_CVV_CARD,cvv);

        long id = 0;
        try {
            id = db.insert( DatabaseHelper.TABLE_CARD, null, cv);
        } catch (Exception e) {
            System.out.print(""+e);
        }
        return id;
    }

    //CARD TABLE
    //getting the required row for the list view adapter
    public String[] getColumnsCard(){
        String[]a={DatabaseHelper.CARD_NUMBER_CARD, DatabaseHelper.CARD_EXPIRY_CARD, DatabaseHelper.CARD_CVV_CARD};
        return a;
    }

    //CARD TABLE
    //getting the Cursor with all the columns for the list adapter
    public Cursor getColumnCardCursor(){
        SQLiteDatabase db=helper.getWritableDatabase();
        String[]columns={DatabaseHelper.CARD_NUMBER_CARD, DatabaseHelper.EXPIRY, DatabaseHelper.CVV};

        Cursor cursor=db.query( DatabaseHelper.TABLE_CARD,columns,null,null,null,null,null);
        return cursor;
    }



    class DatabaseHelper extends SQLiteOpenHelper {

        public static final String CARD_NUMBER ="cardno" ;
        public static final String DATE = "date";
        public static final String CARD_NUMBER_CARD ="cardnumber" ;
        public static final String CARD_EXPIRY_CARD = "cardexpiry";
        public static final String CARD_CVV_CARD = "cvv";
        public static final String TABLE_CARD ="card" ;
        public static final String EXPIRY ="expiry" ;
        public static final String RECHARGE_AMOUNT ="amount" ;
        public static final String PROVIDER ="provider" ;
        public static final String TIME ="time" ;
        public static final String TABLE_MOBILE ="mobile" ;
        public static final String CVV ="cvv" ;
        public static final String TAG ="tag" ;
        public static final String BALANCE ="balance" ;
        public static final String TABLE_CUSTOMERS ="customer" ;
        Context context;

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "Quadplay";
        private static final String TABLE_USERS = "users";
        private static final String TABLE_RECHARGE = "recharge";
        private static final String TABLE_DTH = "dth";
        private static final String TABLE_LANDLINE = "landline";
        private static final String TABLE_BANKDETAILS = "bankdetails";
        private static final String TABLE_USERIMAGES = "images";
        private static final String TABLE_TRANSACTIONS = "transactions";

        /*Details about Users table columns*/
        private static final String USER_ID = "id";
        private static final String MOBILE = "mobno";
        private static final String EMAIL = "email";
        private static final String PASSWORD = "password";
        private static final String NAME = "name";
        private static final String CITY = "city";
        private static final String REG_DATE = "date";
        private static final String REG_TIME = "time";

        /*Details about Recharge Table columns*/
        private static final String RID = "id";
        private static final String USER_MOBILE_ID = "userno";
        private static final String RECHARGE_MOBILE_NO = "rno";
        private static final String RECHARGE_MODE = "mode";
        private static final String AMOUNT = "ramount";
        private static final String OPERATOR = "operator";
        private static final String STATE = "state";
        private static final String RE_DATE = "date";
        private static final String RE_TIME = "time";
        private static final String RE_CARD = "cardno";

        /*Details about DTH Details tracking Table*/
        private static final String DTHID = "id";
        private static final String DTH_MOB_USERID = "userno";
        private static final String DTH_NO = "dthno";
        private static final String DTH_RECHARGE_AMOUNT = "rdth";
        private static final String DTH_PROVIDER = "dthprovider";
        private static final String DTH_DATE = "date";
        private static final String DTH_TIME = "time";
        private static final String DTH_CARD = "cardno";

        /*Details about Landline Details tracking Table*/
        private static final String LAND_ID = "id";
        private static final String LAND_MOB_USERID = "userno";
        private static final String LAND_NO = "landno";
        private static final String LAND_RECHARGE_AMOUNT = "rland";
        private static final String LAND_PROVIDER = "landprovider";
        private static final String LAND_DATE = "date";
        private static final String LAND_TIME = "time";
        private static final String LAND_CARD = "cardno";

        /*Details about Bank Details tracking Table*/
        private static final String BANK_ID = "id";
        private static final String CARD_NO ="cardno";
        private static final String CARD_EXP ="expiry";
        private static final String CARD_CVV ="cvv";
        private static final String BANK_MOB_USERID = "userno";
        private static final String CARD_USERNAME = "cardname";
        private static final String BANK_NAME = "bankname";
        private static final String BANK_BALANCE = "balance";
        private static final String BANK_DATE = "date";
        private static final String BANK_TIME = "time";

        /*Details about Transaction Details tracking Table*/
        private static final String TRANS_ID = "id";
        private static final String TRANS_CARDNO ="cardno";
        private static final String TRANS_SERNAME ="name";
        private static final String TRANS_SERNO ="no";
        private static final String TRANS_AMOUNT ="amount";
        private static final String TRANS_BALANCE ="balance";
        private static final String TRANS_MOB_USERID = "userno";
        private static final String TRANS_DATE = "date";
        private static final String TRANS_TIME = "time";

        /*Details about User Images Table*/
        private static final String IMAGE_ID ="id";
        private static final String IMG_USER_MOBILE ="imobile";
        private static final String IMAGE ="image";

        /* Create Tables */

        private static final String CREATE_TABLE_USERS = "CREATE TABLE "
                + TABLE_USERS + "("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + MOBILE + " INTEGER, "
                + EMAIL + " VARCHAR(30), "
                + PASSWORD + " VARCHAR(30), "
                + NAME + " VARCHAR(40), "
                + CITY + " VARCHAR(40),"
                + REG_DATE + " VARCHAR(20),"
                + REG_TIME + " VARCHAR(20)); ";

        private static final String CREATE_TABLE_RECHARGE = "CREATE TABLE "
                + TABLE_RECHARGE + "("
                + RID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_MOBILE_ID + " INTEGER, "
                + RECHARGE_MOBILE_NO + " INTEGER, "
                + RECHARGE_MODE + " VARCHAR(20), "
                + AMOUNT + " INTEGER , "
                + OPERATOR + " VARCHAR(20), "
                + STATE + " VARCHAR(20), "
                + RE_DATE + " VARCHAR(20),"
                + RE_TIME + " VARCHAR(20),"
                + RE_CARD + " VARCHAR(30) ); ";

        private static final String CREATE_TABLE_DTH = "CREATE TABLE "
                + TABLE_DTH + "("
                + DTHID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DTH_MOB_USERID + " INTEGER, "
                + DTH_NO + " VARCHAR(30), "
                + DTH_RECHARGE_AMOUNT + " INTEGER, "
                + DTH_PROVIDER + " VARCHAR(30) , "
                + DTH_DATE + " VARCHAR(20),"
                + DTH_TIME + " VARCHAR(20),"
                + DTH_CARD + " VARCHAR(30) ); " ;

        private static final String CREATE_TABLE_LANDLINE = "CREATE TABLE "
                + TABLE_LANDLINE + "("
                + LAND_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LAND_MOB_USERID + " INTEGER, "
                + LAND_NO + " INTEGER, "
                + LAND_RECHARGE_AMOUNT + " INTEGER, "
                + LAND_PROVIDER + " VARCHAR(30) , "
                + LAND_DATE + " VARCHAR(20),"
                + LAND_TIME + " VARCHAR(20),"
                + LAND_CARD + " VARCHAR(30) ); " ;

        private static final String CREATE_TABLE_BANKDETAILS = "CREATE TABLE "
                + TABLE_BANKDETAILS + "("
                + BANK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + CARD_NO + " VARCHAR(30) , "
                + CARD_EXP + " INTEGER, "
                + CARD_CVV+ " INTEGER, "
                + BANK_MOB_USERID + " INTEGER, "
                + BANK_NAME + " VARCHAR(50) , "
                + CARD_USERNAME + " VARCHAR(50), "
                + BANK_BALANCE + " INTEGER DEFAULT 1000, "
                + BANK_DATE + " VARCHAR(20),"
                + BANK_TIME + " VARCHAR(20) ); " ;

        private static final String CREATE_TABLE_TRANS = "CREATE TABLE "
                + TABLE_TRANSACTIONS + "("
                + TRANS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + TRANS_CARDNO + " VARCHAR(30) , "
                + TRANS_SERNAME + " VARCHAR(30), "
                + TRANS_SERNO + "VARCHAR(30), "
                + TRANS_AMOUNT+ " INTEGER, "
                + TRANS_BALANCE+ " INTEGER, "
                + TRANS_MOB_USERID + " INTEGER, "
                + TRANS_DATE + " VARCHAR(20),"
                + TRANS_TIME + " VARCHAR(20) ); " ;

        private static final String CREATE_TABLE_IMAGES = "CREATE TABLE "
                + TABLE_USERIMAGES + "("
                + IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IMG_USER_MOBILE + " INTEGER, "
                + IMAGE + " BLOB NOT NULL ); " ;


        /* DATABASE HELPER */

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
                sqLiteDatabase.execSQL(CREATE_TABLE_RECHARGE);
                sqLiteDatabase.execSQL(CREATE_TABLE_DTH);
                sqLiteDatabase.execSQL(CREATE_TABLE_LANDLINE);
                sqLiteDatabase.execSQL(CREATE_TABLE_BANKDETAILS);
                sqLiteDatabase.execSQL(CREATE_TABLE_TRANS);
                sqLiteDatabase.execSQL(CREATE_TABLE_IMAGES);

            } catch (SQLException e) {
                System.out.println(""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
                try {
                    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
                    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RECHARGE);
                    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DTH);
                    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LANDLINE);
                    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BANKDETAILS);
                    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
                    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERIMAGES);
                    onCreate(sqLiteDatabase);
                } catch (SQLException e) {
                    Toast.makeText(context, " " + e, Toast.LENGTH_LONG).show();
                }
        }

    }

}

/*Copyright [2018] [Jurgen Emanuels]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package za.co.samtakie.samtakieradio.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressWarnings("unused")
public class DbHelper extends SQLiteOpenHelper {

    /* Set the database name. */
    private static final String DATABASE_NAME = "samtakie_radio.db";

    /* Set the database version number, always update the version number if you change
    * the database scheme */
    private static final int DATABASE_VERSION = 1;

    /* Set the table values for the online_radio Table */
    private static final String SQL_CREATE_ONLINE_RADIO_TABLE = " CREATE TABLE " +
            Contract.RadioEntry.TABLE_NAME + " (" +
            Contract.RadioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " INTEGER NOT NULL," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME + " TEXT NOT NULL," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK + " TEXT NOT NULL," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE + " TEXT NOT NULL" + ");";

    /* Set the table values for the online_radio_fav Table */
    private static final String SQL_CREATE_ONLINE_RADIO_FAV_TABLE = " CREATE TABLE " +
            Contract.RadioEntry.TABLE_NAME_FAV + " (" +
            Contract.RadioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " INTEGER NOT NULL," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME + " TEXT NOT NULL," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK + " TEXT NOT NULL," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE + " TEXT NOT NULL" + ");";

    /* Set the table values for the online_radio_fav Table */
    private static final String SQL_CREATE_ONLINE_RADIO_NEWS_TABLE = " CREATE TABLE " +
            Contract.RadioEntry.TABLE_NAME_NEWS + " (" +
            Contract.RadioEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_TITLE + " TEXT NOT NULL," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_DATE + " INTEGER NOT NULL," +
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_MESSAGE + " TEXT NOT NULL" + ");";


    private static final String SQL_DELETE_ONLINE_RADIO = "DROP TABLE IF EXISTS " +
            Contract.RadioEntry.TABLE_NAME;

    private static final String SQL_DELETE_ONLINE_FAV_RADIO = "DROP TABLE IF EXISTS " +
            Contract.RadioEntry.TABLE_NAME_FAV;

    private static final String SQL_DELETE_ONLINE_NEWS_RADIO = "DROP TABLE IF EXISTS " +
            Contract.RadioEntry.TABLE_NAME_NEWS;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ONLINE_RADIO_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ONLINE_RADIO_FAV_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_ONLINE_RADIO_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /* Upgrade the database by using an if statement for the version number
         * At the moment no update is being carried out. Check web page for a best practice
         * on using onUpgrade: https://thebhwgroup.com/blog/how-android-sqlite-onupgrade */

        //noinspection StatementWithEmptyBody
        if(oldVersion < 6){
            //Add updated code here to be execute in case of an upgrade in the database
        }
    }
}
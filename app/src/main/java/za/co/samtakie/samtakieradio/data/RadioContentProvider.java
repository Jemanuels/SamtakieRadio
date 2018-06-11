package za.co.samtakie.samtakieradio.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class RadioContentProvider extends ContentProvider {

    public static final int RADIO = 100;
    public static final int RADIO_ID = 101;

    public static final int RADIO_FAV = 200;
    public static final int RADIO_FAV_ID = 201;

    private final static UriMatcher sUriMatcher = buildUriMatcher();

    private DbHelper mDbHelper;



    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_ONLINE_RADIO, RADIO);

        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_ONLINE_RADIO + "/#", RADIO_ID);

        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_ONLINE_RADIO_FAV, RADIO_FAV);

        uriMatcher.addURI(Contract.AUTHORITY, Contract.PATH_ONLINE_RADIO_FAV + "/#", RADIO_FAV_ID);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new DbHelper(getContext());

        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch(sUriMatcher.match(uri)){
            case RADIO:
                db.beginTransaction();
                int rowInserted = 0;
                try{
                    for(ContentValues value : values){
                        Log.d("Content provider ", value.toString());
                        long _id = db.insert(Contract.RadioEntry.TABLE_NAME, null, value);
                        if(_id != -1){
                            rowInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }

                if(rowInserted > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowInserted;
        }

        return super.bulkInsert(uri, values);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArg, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)){
            case RADIO_ID:{
                String[] selectionArguments = new String[]{uri.getLastPathSegment()};

                cursor = mDbHelper.getReadableDatabase().query(
                        Contract.RadioEntry.TABLE_NAME,
                        projection,
                        Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;



            }

            case RADIO:{
                cursor = mDbHelper.getReadableDatabase().query(
                        Contract.RadioEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArg,
                        null,
                        null,
                        sortOrder);

                break;
            }


            case RADIO_FAV_ID:{
                String[] selectionArguments = new String[]{uri.getLastPathSegment()};

                cursor = mDbHelper.getReadableDatabase().query(
                        Contract.RadioEntry.TABLE_NAME_FAV,
                        projection,
                        Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;



            }

            case RADIO_FAV:{
                cursor = mDbHelper.getReadableDatabase().query(
                        Contract.RadioEntry.TABLE_NAME_FAV,
                        projection,
                        selection,
                        selectionArg,
                        null,
                        null,
                        sortOrder);

                break;
            }
            default:
              throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case RADIO:
                long id = db.insert(Contract.RadioEntry.TABLE_NAME, null, contentValues);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;

            case RADIO_FAV:
                long idFav = db.insert(Contract.RadioEntry.TABLE_NAME_FAV, null, contentValues);
                if(idFav > 0){
                    returnUri = ContentUris.withAppendedId(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV, idFav);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;



                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;

        if(null == selection){
            selection = "1";
        }

        switch (sUriMatcher.match(uri)){
            case RADIO:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        Contract.RadioEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            case RADIO_FAV:
                numRowsDeleted = mDbHelper.getWritableDatabase().delete(
                        Contract.RadioEntry.TABLE_NAME_FAV,
                        selection,
                        selectionArgs);

                break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(numRowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}

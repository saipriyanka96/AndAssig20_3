package com.example.layout.assig20_3;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Pri on 10/23/2017.
 */

public class ContactHelper {
    public static int deleteContact(ContentResolver contactHelper, String number) {
//method to delete contact
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        String contactID = String.valueOf(getContactID(contactHelper, number));

        Log.d("Contact ID: ", contactID);

        String[] args = new String[]{contactID};
        ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection(ContactsContract.RawContacts.CONTACT_ID + "=?", args).build());
        try {
            ContentProviderResult[] contentProviderResult = contactHelper.applyBatch(ContactsContract.AUTHORITY, ops);
            Log.d("CP: ", String.valueOf(contentProviderResult[0].count));
            return contentProviderResult[0].count;
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();

        }

        return -1;
    }

    private static long getContactID(ContentResolver contactHelper, String number) {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String[] projection = {ContactsContract.PhoneLookup._ID};
        Cursor cursor = null;

        try {
            cursor = contactHelper.query(contactUri, projection, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    long personID = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                    return personID;
                    //return th id
                }
                cursor.close();
                //cursor is closed
            } else {
                Log.d("Contact ID: ", "null");
                //otherwise id with info
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                //not null then close the cursor
                cursor = null;
            }
        }
        return -1;
    }
}
/**
 * getColumnIndexOrThrow-Returns the zero-based index for the given column name, or throws IllegalArgumentException if the column doesn't exist. If you're not sure if a column will exist or not use getColumnIndex(String) and check for -1, which is more efficient than catching the exceptions.

 Parameters
 columnName	String: the name of the target column.
 Returns
 int	the zero-based column index for the given column name
 Throws
 IllegalArgumentException	if the column does not exist
 * getLong-long getLong (int columnIndex)
 Returns the value of the requested column as a long.

 * uri.encode()-Translates a string into x-www-form-urlencoded format.
 * This method uses the platform's default encoding as the encoding scheme to obtain the bytes for unsafe characters.
 * ContactsContract.PhoneLookup-A table that represents the result of looking up a phone number
 * Creates a new Uri by appending an already-encoded path segment to a base Uri.

 Parameters
 baseUri	Uri: Uri to append path segment to
 pathSegment	String: encoded path segment to append
 Returns
 Uri	a new Uri based on baseUri with the given segment appended to the path

 * contentProviderOperations-Represents a single operation to be performed as part of a batch of operations.
 *  contentProviderOperations are set into an arraylist
 *  newUpdate-Create a ContentProviderOperation.Builder suitable for building an updateContentProviderOperation.
 Parameters
 uri	Uri: The Uri that is the target of the insert.
 ContactsContract.RawContacts.CONTENT_URI-Constants for the raw contacts table, which contains one row of contact information for each person in each synced account.
 Sync adapters and contact management apps are the primary consumers of this API.
 withValue()-A value to insert or update. This value may be overwritten by the corresponding value specified by withValueBackReference(String, int). This can only be used with builders of type insert, update, or assert.
 Parameters
 key	String: the name of this value
 value	Object: the value itself. the type must be acceptable for insertion by put(String, byte[])
 withValueBackReference-Add a ContentValues back reference. A column value from the back references takes precedence over a value specified in withValues(ContentValues). This can only be used with builders of type insert, update, or assert.
 Parameters
 key	String
 previousResult	int
 ContactsContract.CommonDataKinds.Structured-A data kind representing the contact's proper name. You can use all columns defined for ContactsContract.Data as well as the following aliases.
 ContactsContract.RawContacts-Constants for the raw contacts table, which contains one row of contact information for each person in each synced account. Sync adapters and contact management apps are the primary consumers of this API.
 getApplicationContext-Return the context of the single, global Application object of the current process.
 getContentResolver-Return a ContentResolver instance for your application's package.
 apply()-Commit your preferences changes back from this Editor to the SharedPreferences object it is editing.
 ContactsContract.AUTHORITY-The authority for the contacts provider
 ContactsContract.Data.MIMETYPE-The MIME type of the item represented by this row
 ContactsContract.Data.CONTENT_URI:The content:// style URI for this table, which requests a directory of data rows matching the selection criteria.
 CursorQuery the given URI, returning a Cursor over the result set.
 Parameters
 uri	Uri: The URI, using the content:// scheme, for the content to retrieve.
 This value must never be null.

 projection	String: A list of which columns to return. Passing null will return all columns, which is inefficient.
 selection	String: A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given URI.
 selectionArgs	String: You may include ?s in selection, which will be replaced by the values from selectionArgs, in the order that they appear in the selection. The values will be bound as Strings.
 This value may be null.

 sortOrder	String: How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
 Returns
 Cursor	A Cursor object, which is positioned before the first entry, or null

 */

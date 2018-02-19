package com.example.layout.assig20_3;
//Package objects contain version information about the implementation and specification of a Java package
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //public keyword is used in the declaration of a class,method or field;public classes,method and fields can be accessed by the members of any class.
//extends is for extending a class. implements is for implementing an interface
//AppCompatActivity is a class from e v7 appcompat library. This is a compatibility library that back ports some features of recent versions of
// Android to older devices.
    private TextView contactId;
    private final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;
    //no of contacts can that can we read

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Variables, methods, and constructors, which are declared protected in a superclass can be accessed only by the subclasses
        // in other package or any class within the package of the protected members class.
        //void is a Java keyword.  Used at method declaration and definition to specify that the method does not return any type,
        // the method returns void.
        //onCreate Called when the activity is first created. This is where you should do all of your normal static set up: create views,
        // bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously frozen state,
        // if there was one.Always followed by onStart().
        //Bundle is most often used for passing data through various Activities.
// This callback is called only when there is a saved instance previously saved using onSaveInstanceState().
// We restore some state in onCreate() while we can optionally restore other state here, possibly usable after onStart() has
// completed.The savedInstanceState Bundle is same as the one used in onCreate().
        // call the super class onCreate to complete the creation of activity like the view hierarchy
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //R means Resource
        //layout means design
        //  main is the xml you have created under res->layout->main.xml
        //  Whenever you want to change your current Look of an Activity or when you move from one Activity to another .
        // The other Activity must have a design to show . So we call this method in onCreate and this is the second statement to set
        // the design
        ///findViewById:A user interface element that displays text to the user.

        contactId = (TextView) findViewById(R.id.contact_id);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //ContextCompat.checkSelfPermission-Determine whether you have been granted a particular permission.
            //Parameters context	Context
            // permission	String: The name of the permission being checked

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
//Gets whether you should show UI with rationale for requesting a permission.
                //Parameters activity	Activity: The target activity.
                // permission	String: A permission your app wants to request.
            } else {

                //  request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                //Requests permissions to be granted to this application.
                /**Parameters
                 activity	Activity: The target activity.
                 permissions	String: The requested permissions. Must me non-null and not empty.
                 requestCode	int: Application specific request code to match with a result reported to onRequestPermissionsResult(int, String[], int[]). Should be >= 0.**/
            }
        }else{
            //contactId.setText(String.valueOf(getContactID(getContentResolver(), "8285385442")));
            contactId.setOnClickListener(new View.OnClickListener() {
                //on click of the button the contact id will be deleted
                //here we insert the contacts on click of the button
                /**Register a callback to be invoked when this view is clicked. If this view is not clickable, it becomes clickable.

                 Parameters
                 l	View.OnClickListener: The callback that will run
                 This value may be null.
                 onClick-Called when a view has been clicked.

                 Parameters
                 v	View: The view that was clicked.**/
                @Override
                public void onClick(View view) {
                    deleteContact();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Callback for the result from requesting permissions.
        /**Parameters
         requestCode	int: The request code passed in requestPermissions(android.app.Activity, String[], int)
         permissions	String: The requested permissions. Never null.
         grantResults	int: The grant results for the corresponding permissions which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
         **/
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
// permission was granted,  Do the
                    // contacts-related task you need to do.
                    // Since reading contacts takes more time, let's run it on a separate thread.
                    contactId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteContact();
                        }
                    });
                } else {

                    // permission denied, Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "You've denied the required permission.", Toast.LENGTH_LONG);
                   /** Make a standard toast that just contains a text view.

                    Parameters
                    context	Context: The context to use. Usually your Application or Activity object.
                    text	CharSequence: The text to show. Can be formatted text.
                    duration	int: How long to display the message. Either LENGTH_SHORT or LENGTH_LONG**/
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void deleteContact() {

        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.delete_contact_form, null);

        final EditText phone = view.findViewById(R.id.input_contact_phone);

        builder.setView(view).setPositiveButton(R.string.delete_contact, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String contactPhone = phone.getText().toString();

                        // Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contactPhone));
                        // grantUriPermission("com.compkerworld.playingwithcontacts.utils", contactUri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                        int deleteStatus = ContactHelper.deleteContact(getContentResolver(), contactPhone);

                        if (deleteStatus == 1) {
                            Toast.makeText(getApplicationContext(), "Deleted successfully.", Toast.LENGTH_LONG).show();
                        } else if(deleteStatus == 0){
                            Toast.makeText(getApplicationContext(), "No such number exists.", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getApplicationContext(), "Failed to delete.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_contact, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.getDialog().cancel();
                    }
                });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

}



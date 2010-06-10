package ru.kmerenkov.people;

import java.io.InputStream;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;

public class ContactInfo {
	private Long _id;
	private String _name;
	private String _lookup_key;
	
	public ContactInfo(Long contactId, String name, String lookupKey) {
		_id = contactId;
		_name = name;
		_lookup_key = lookupKey;
	}
	
	public Long getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}
	
	public String getLookupKey() {
		return _lookup_key;
	}
	
	public Uri getPhotoUri() {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, _id);
		Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
		return photoUri;
	}

	public Bitmap getPhoto(ContentResolver contentResolver) {
		Long contactId = _id;
	    Uri contactPhotoUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);

	    // contactPhotoUri --> content://com.android.contacts/contacts/1557

	    InputStream photoDataStream = Contacts.openContactPhotoInputStream(contentResolver, contactPhotoUri); // <-- always null
	    Bitmap photo = BitmapFactory.decodeStream(photoDataStream);
	    return photo;
	}
}

package ru.kmerenkov.people;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;


public class PeopleWidget extends AppWidgetProvider {
	private static String TAG = "PeopleWidget";
	public static final String ACTION_QUICK_CONTACT = "com.android.contacts.action.QUICK_CONTACT";
	public static final String EXTRA_TARGET_RECT = "target_rect";
	public static final String EXTRA_MODE = "mode";
	public static final String EXTRA_EXCLUDE_MIMES = "exclude_mimes";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}
	
	private int getCorrespondingImageId(int position) {
		int viewId = 0;
		switch (position) {
		case 1:
			viewId = R.id.ImageButton01; break;
		case 2:
			viewId = R.id.ImageButton02; break;
		case 3:
			viewId = R.id.ImageButton03; break;
		case 4:
			viewId = R.id.ImageButton04; break;
		case 5:
			viewId = R.id.ImageButton05; break;
		case 6:
			viewId = R.id.ImageButton06; break;
		case 7:
			viewId = R.id.ImageButton07; break;
		case 8:
			viewId = R.id.ImageButton08; break;
		case 9:
			viewId = R.id.ImageButton09; break;
		}
		return viewId;
	}
	
	private Intent showQuickContact(ContactInfo contactInfo) {
		final Rect rect = new Rect();
		rect.left = 0;
		rect.top = 0;
		rect.right = 0;
		rect.bottom = 0;
		final Intent intent = new Intent(ACTION_QUICK_CONTACT);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setData(ContactsContract.Contacts.getLookupUri(contactInfo.getId(), Contacts.LOOKUP_KEY));
        intent.putExtra(EXTRA_TARGET_RECT, rect);
        intent.putExtra(EXTRA_MODE, ContactsContract.QuickContact.MODE_MEDIUM);
        intent.putExtra(EXTRA_EXCLUDE_MIMES, new String[]{});
        return intent;
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Uri uri = Contacts.CONTENT_URI;
		String[] projection = new String[] {Contacts._ID, Contacts.DISPLAY_NAME, Contacts.LOOKUP_KEY};
		String selection = Contacts.STARRED;
		String[] selectionArgs = new String[]{};
		String sortOrder = Contacts.DISPLAY_NAME + " ASC"; // Contacts.LAST_TIME_CONTACTED + " DESC";

		Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

		//Log.i(TAG, "Cursor rows count: " + cursor.getCount());

		List<ContactInfo> contacts = new ArrayList<ContactInfo>(9); // how the hell do I LIMIT?

		int ci = 1;
		while (cursor.moveToNext()) {
			if (ci == 9) {
				cursor.close();
				break;
			}
			Long id = cursor.getLong(0);
			String name = cursor.getString(1);
			String lookupKey = cursor.getString(2);
			contacts.add(new ContactInfo(id, name, lookupKey));
			ci++;
		}

		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

		int ccount = contacts.size();
		Log.d(TAG, "Contacts count: " + Integer.toString(ccount));
		for (int i = 0; i < 9; i++) {
			int viewId = getCorrespondingImageId(i+1);
			if (i < ccount) {
				ContactInfo c = contacts.get(i);
				Log.d(TAG, "Showing view: " + Integer.toString(viewId));
				views.setImageViewUri(viewId, c.getPhotoUri());
				views.setViewVisibility(viewId, View.VISIBLE);
				views.setOnClickPendingIntent(viewId, PendingIntent.getActivity(context, 0, showQuickContact(c), PendingIntent.FLAG_UPDATE_CURRENT));
			}
			else {
				Log.d(TAG, "Hiding view: " + Integer.toString(viewId));
				views.setViewVisibility(viewId, View.GONE);
			}
		}

        ComponentName thisWidget = new ComponentName(context, PeopleWidget.class);
        appWidgetManager.updateAppWidget(thisWidget, views);
	}

}

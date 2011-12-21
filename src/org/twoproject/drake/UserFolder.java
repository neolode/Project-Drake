package org.twoproject.drake;


import java.util.ArrayList;
import java.util.List;
import org.twoproject.drake.R;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Folder which contains applications or shortcuts chosen by the user.
 *
 */
public class UserFolder extends Folder implements DropTarget {
    public UserFolder(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    /**
     * Creates a new UserFolder, inflated from R.layout.user_folder.
     *
     * @param context The application's context.
     *
     * @return A new UserFolder.
     */
    static UserFolder fromXml(Context context) {
        return (UserFolder) LayoutInflater.from(context).inflate(R.layout.user_folder, null);
    }

    public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset,
            Object dragInfo) {
        final ItemInfo item = (ItemInfo) dragInfo;
        final int itemType = item.itemType;
        return (itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION ||
                itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT) && item.container != mInfo.id;
    }
    
    public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo, Rect recycle) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo) {
        final ApplicationInfo item = (ApplicationInfo) dragInfo;
        //noinspection unchecked
        ((ArrayAdapter<ApplicationInfo>) mContent.getAdapter()).add((ApplicationInfo) dragInfo);
        LauncherModel.addOrMoveItemInDatabase(mLauncher, item, mInfo.id, 0, 0, 0);
    }

    public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo) {
    }

    public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo) {
    }

    public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset, Object dragInfo) {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onDropCompleted(View target, boolean success) {
        if (success) {
            //noinspection unchecked
            ArrayAdapter<ApplicationInfo> adapter =
                    (ArrayAdapter<ApplicationInfo>) mContent.getAdapter();
            adapter.remove(mDragItem);
        }
    }

    void bind(FolderInfo info) {
        super.bind(info);
        //setContentAdapter(new ApplicationsAdapter(mLauncher, ((UserFolderInfo) info).contents));
        setContentAdapter(new FolderAdapter(mLauncher, ((UserFolderInfo) info).contents));
    }

    // When the folder opens, we need to refresh the GridView's selection by
    // forcing a layout
    @Override
    void onOpen() {
        super.onOpen();
        requestFocus();
    }
    private class FolderAdapter extends ArrayAdapter<ApplicationInfo> {
    	private LayoutInflater mInflater;
    	private Drawable mBackground;
    	private int mTextColor = 0;
    	private boolean useThemeTextColor = false;
        private Typeface themeFont=null;
    	
		public FolderAdapter(Context context, ArrayList<ApplicationInfo> icons) {
			super(context, 0,icons);
			mInflater=LayoutInflater.from(context);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ApplicationInfo info = getItem(position);

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.application_boxed, parent,
						false);
			}

			if (!info.filtered) {
				info.icon = Utilities.createIconThumbnail(info.icon, getContext());
				info.filtered = true;
			}

			final TextView textView = (TextView) convertView;
			textView.setCompoundDrawablesWithIntrinsicBounds(null, info.icon, null,
					null);
			textView.setText(info.title);
			if (useThemeTextColor) {
				textView.setTextColor(mTextColor);
			}
			//ADW: Custom font
			if(themeFont!=null) textView.setTypeface(themeFont);
			// so i'd better not use it, sorry themers
			if (mBackground != null)
				convertView.setBackgroundDrawable(mBackground);
			return convertView;
		}
    	
    }
}

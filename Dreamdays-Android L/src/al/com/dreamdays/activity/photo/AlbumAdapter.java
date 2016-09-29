package al.com.dreamdays.activity.photo;

import java.util.ArrayList;
import java.util.List;

import al.com.dreamdays.base.BaseApplication;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guxiu.dreamdays.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author Fatty
 *
 */
public class AlbumAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<AlbumEntity> mAlbums;

	public class ViewHolder {
		ImageView cameraRollImageView;
		TextView cameraRollextView;
	}

	public AlbumAdapter(Context ctx) {
		mAlbums = new ArrayList<AlbumEntity>(0);
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setAlbumsList(List<AlbumEntity> albums) {
		mAlbums.clear();
		mAlbums.addAll(albums);
		notifyDataSetChanged();
	}

	public void addAlbums(List<AlbumEntity> albums) {
		mAlbums.addAll(albums);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mAlbums.size();
	}

	public void clear() {
		mAlbums.clear();
		this.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		if (mAlbums.isEmpty() || position >= mAlbums.size()) {
			return null;
		}
		return mAlbums.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.fatty_camera_roll_item_layout,viewGroup, false);
			holder = new ViewHolder();
			holder.cameraRollImageView = (ImageView) view.findViewById(R.id.cameraRollImageView);
			holder.cameraRollextView = (TextView) view.findViewById(R.id.cameraRollextView);
			holder.cameraRollextView.setTypeface(BaseApplication.typeface_medium);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
			holder.cameraRollImageView.setImageDrawable(null);
			holder.cameraRollImageView.setImageBitmap(null);
		}

		AlbumEntity album = mAlbums.get(position);
		holder.cameraRollextView.setText(album.getmName());
		ImageLoader.getInstance().displayImage(	"file://" + album.getmCoverUrl(), holder.cameraRollImageView);
		return view;
	}
}

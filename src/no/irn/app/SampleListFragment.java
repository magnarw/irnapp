package no.irn.app;





import no.irn.app.about.AboutSnmsFragment;
import no.irn.app.halal.HalalList;
import no.irn.app.hijri.HijriList;
import no.irn.app.news.BuildProjectListFragment;
import no.irn.app.news.EventListFragment;
import no.irn.app.news.NewsListFragment;
import no.irn.app.preylist.PreyOverviewFragment;
import no.irn.app.qibla.QiblaFragment;
import no.irn.app.settings.SettingsFragment;
import no.snms.app.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SampleListFragment extends ListFragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SampleAdapter adapter = new SampleAdapter(getActivity());
		adapter.add(new SampleItem("Hjem", R.drawable.ic_apphome));
	    adapter.add(new SampleItem("Månedstabell", R.drawable.ic_news));
	    adapter.add(new SampleItem("Halal", R.drawable.ic_donation));
	    adapter.add(new SampleItem("Hijri", R.drawable.ic_events));
	    adapter.add(new SampleItem("Qibla", R.drawable.ic_qibla));
	   
	 
	    adapter.add(new SampleItem("Om IRN", R.drawable.ic_logo));
	    
	    adapter.add(new SampleItem("Innstillinger", R.drawable.ic_settings));
	
		setListAdapter(adapter);
	}

	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent1 = null;
		Fragment newContent2 = null;
		switch (position) {
		case 0:
			newContent1 = new PreyOverviewFragment();
			break;
		case 1:
			newContent1 = new PrayCalenderListFragment();
			break;
		case 2:
			newContent1 = new HalalList();
			break;	
		case 3:
			newContent1 = new HijriList();
			break;	
		case 4:
			newContent1 = new QiblaFragment();
			break;	

		
		case 5:
			newContent1 = new AboutSnmsFragment();
			break;		
		
		case 6:
			newContent1 = new SettingsFragment();
			break;	
		}
		
		if (newContent1 != null)
			switchFragment(newContent1,newContent2);
	}
	

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}

	}
	
	private void switchFragment(Fragment fragment1, Fragment fragment2) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof BaseActivity) {
			BaseActivity fca = (BaseActivity) getActivity();
			fca.switchContent(fragment1, fragment2);
		} 
	}
	
}

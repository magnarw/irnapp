package no.irn.app.preylist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import no.irn.app.BaseActivity;
import no.irn.app.MainApplication;
import no.irn.app.PrayCalenderListFragment;
import no.irn.app.PreyCountDownTimer;
import no.irn.app.PreyOverView;
import no.irn.app.alarm.Alarm;
import no.irn.app.alarm.AlarmChangeListner;
import no.irn.app.alarm.AlarmDialogFragment;
import no.irn.app.alarm.AlarmUtilities;
import no.irn.app.database.SnmsDAO;
import no.irn.app.domain.NewsItem;
import no.irn.app.domain.PreyItem;
import no.irn.app.halal.HalalList;
import no.irn.app.hijri.HijriList;
import no.irn.app.images.ImageCacheManager;
import no.irn.app.jumma.JummaAdaptor;
import no.irn.app.jumma.JummaListner;
import no.irn.app.network.RequestManager;
import no.irn.app.news.BuildProjectListFragment;
import no.irn.app.news.EventListFragment;
import no.irn.app.news.NewsDetailsFragment;
import no.irn.app.news.NewsListFragment;
import no.irn.app.news.NewsManager;
import no.irn.app.news.NewsListFragment.NewsListAdapter;
import no.irn.app.qibla.QiblaFragment;
import no.irn.app.settings.SettingsFragment;
import no.irn.app.utils.SnmsPrayTimeAdapter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import no.snms.app.R;
import no.snms.app.R.id;
import no.snms.app.R.layout;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PreyOverviewFragment extends Fragment implements OnClickListener,
		AlarmChangeListner,
		android.app.DatePickerDialog.OnDateSetListener {

	private DateTime currentDate;
	private DateTime timeCurrentlyUsedInPreyOverView;
	private int newsPage;
	private int newsOffset;
	private int filter;
	private List<PreyItem> preyTimes;
	private TextView currentDay;
	private ImageButton nextDay;
	private ImageButton prevDay;
	private ImageButton nextNews;
	private ImageButton prevNews;

	private Button montlyCalenderShortCut;
	private Button hijriShortcut;
	private Button settingsShortCut;
	private Button halalShortCut;
	private Button qiblaShortcut;
	private Button hijriShorcut;

	private TextView calender;
	private DateTime currentDateTime;
	public static final String DATEPICKER_TAG = "datepicker";
	private CountDownTimer preyCountDownTimer;
	private LinearLayout preyRowContainer;
	private LinearLayout shortCutContainer;
	private LinearLayout latestNewsContainer;
	private Map<String, View> preyNamePreyRowMap;
	private Map<String, ImageView> alarmButtonNameMap = new HashMap<String, ImageView>();
	private ProgressBar newsSpinnerProgress2;
	private HorizontalScrollView latesetNewsScrollView;
	private Button shortCuts;
	private Button latestNews;

	private NetworkImageView newsImage1;
	private TextView newsText1;

	private NetworkImageView newsImage2;
	private TextView newsText2;

	private final Integer NUMBER_OF_PRAYS = 6;

	private final String[] PREY_LABLES = { "Fajr", "Soloppgang", "Dhuhr",
			"Asr", "Maghrib", "Isha" };
	protected NewsItem currentNewsItem2;
	protected NewsItem currentNewsItem1;
	Intent intent = new Intent(getAppContext(), PreyOverviewFragment.class);

	AlarmUtilities Util;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.preyoverview, null);
		preyRowContainer = (LinearLayout) root
				.findViewById(R.id.preyRowContainer);
		addPreyRowsToContainerAndStoreInMap(inflater);
		currentDay = (TextView) root.findViewById(R.id.prey_current_day);
		currentDay.setOnClickListener(this);
		nextDay = (ImageButton) root.findViewById(R.id.prey_next_day);
		nextDay.setOnClickListener(this);
		prevDay = (ImageButton) root.findViewById(R.id.prey_prev_day);
		latesetNewsScrollView = (HorizontalScrollView) root
				.findViewById(R.id.latesetNewsScrollView);
		shortCutContainer = (LinearLayout) root
				.findViewById(R.id.shortCutContainer);

		montlyCalenderShortCut = (Button) root.findViewById(R.id.monthlyCalenderShortCut);
		hijriShortcut = (Button) root.findViewById(R.id.hijriShortCut);
		settingsShortCut = (Button) root.findViewById(R.id.settingsShortCut);
		halalShortCut = (Button) root
				.findViewById(R.id.halalDonationShortCut);
		settingsShortCut = (Button) root.findViewById(R.id.settingsShortCut);
		qiblaShortcut = (Button) root.findViewById(R.id.qiblaShortcut);
		hijriShorcut = (Button) root.findViewById(R.id.hijriShortCut);
		montlyCalenderShortCut.setOnClickListener(this);
		settingsShortCut.setOnClickListener(this);

		halalShortCut.setOnClickListener(this);
		qiblaShortcut.setOnClickListener(this);
		hijriShorcut.setOnClickListener(this);
	
		latestNewsContainer = (LinearLayout) root
				.findViewById(R.id.latestNewsContainer);
		prevDay.setOnClickListener(this);
		currentDate = new DateTime();
		timeCurrentlyUsedInPreyOverView = currentDate;

		final Calendar calendar = Calendar.getInstance();
		currentDay = (TextView) root.findViewById(R.id.prey_current_day);
	
		Util = new AlarmUtilities(((PreyOverView) getActivity()).getDAO());
		return root;
	}

	private void addPreyRowsToContainerAndStoreInMap(LayoutInflater inflater) {
			preyNamePreyRowMap = new HashMap<String, View>();
			for (int i = 0; i < NUMBER_OF_PRAYS; i++) {
				View row = inflater.inflate(R.layout.prey_row,
						preyRowContainer, false);
				preyRowContainer.addView(row);
				preyNamePreyRowMap.put(PREY_LABLES[i], row);
				
				if(i!=1) {
					ImageView alarmIcon = (ImageView) row
							.findViewById(R.id.alarmclock_inactive);
					alarmButtonNameMap.put(PREY_LABLES[i], alarmIcon);
					alarmIcon.setOnClickListener(this);
					preyNamePreyRowMap.put(PREY_LABLES[i], row);
				} else {
					ImageView alarmIcon = (ImageView) row
							.findViewById(R.id.alarmclock_inactive);
					alarmIcon.setOnClickListener(this);
					 alarmIcon.setImageResource(Color.GRAY);
					 alarmIcon.setBackgroundResource(R.drawable.snmsnoneactivealarm);
				}
			
			}
			View row = inflater.inflate(R.layout.prey_row, preyRowContainer,
					false);
			ImageView alarmIcon = (ImageView) row
					.findViewById(R.id.alarmclock_inactive);
			 alarmIcon.setImageResource(Color.GRAY);
			 alarmIcon.setBackgroundResource(R.drawable.snmsnoneactivealarm);
		

	}

	@Override
	public void onResume() {
		super.onResume();

		cancelAllPreviousRequest();
		preyTimes = loadPrayTimes(new DateTime());
		setUpCurrentDay();
		
		// mheaderView.setPadding(0, 0, 0, 0);
		renderPreyList();
		renderAlarmState();

		

	}

	private void cancelAllPreviousRequest() {
		RequestManager.getRequestQueue().cancelAll(
				new RequestQueue.RequestFilter() {
					@Override
					public boolean apply(Request<?> request) {
						return true;
					}
				});
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void renderAlarmState() {
		for (String key : alarmButtonNameMap.keySet()) {
			ImageView alarmIcon = alarmButtonNameMap.get(key);
			if (Util.hasAlarm(key) == true) {
				// alarmIcon.setBackgroundColor(R.color.snmsActiveGreen);
				alarmIcon.setSelected(true);

				// alarmIcon.setImageResource(R.drawable.ic_alarm_clock_active);
			} else {
				alarmIcon.setSelected(false);
				 alarmIcon.setImageResource(R.drawable.ic_alarm_clock);
			}
		}

	}

	public void renderPreyList() {

		if (this.preyCountDownTimer != null) {
			this.preyCountDownTimer.cancel();
			this.preyCountDownTimer = null;
		}

		if (currentDateUsedInAppIsMoreThanOneDayAgo()) {
			renderAllPreysAsPassed();
		}
		if (currentDateUsedInAppIsMoreThanOneInthaFuture()) {
			renderAllPreysAsFuturu();
		}
		try {
			for (PreyItem item : preyTimes) {

				View preyRow = this.preyNamePreyRowMap.get(item.getName());
				TextView title = (TextView) preyRow
						.findViewById(R.id.row_title);
				TextView status = (TextView) preyRow
						.findViewById(R.id.row_status);
				TextView time = (TextView) preyRow.findViewById(R.id.row_time);
				ImageView image = (ImageView) preyRow
						.findViewById(R.id.alarmclock_inactive);

				if (isPassed(item)) {
					renderPassed(item, preyRow, title, time, status, image);
				}
				if (isFuture(item)) {
					renderFuture(item, preyRow, title, time, status, image);
				}
				if (isActive(item)) {
					renderActive(item, preyRow, title, time, status, image);
				}
				if (isNext(item)) {
					renderNext(item, preyRow, title, time, status, image);
					// TODO: Trigger a new count down
				}
			}
		} catch (Exception e) {
			Log.e("ERROR", "Could not render this prey");
		}

	}

	private void renderAllPreysAsFuturu() {
		for (PreyItem item : preyTimes) {
			View preyRow = this.preyNamePreyRowMap.get(item.getName());
			TextView title = (TextView) preyRow.findViewById(R.id.row_title);
			TextView status = (TextView) preyRow.findViewById(R.id.row_status);
			TextView time = (TextView) preyRow.findViewById(R.id.row_time);
			ImageView image = (ImageView) preyRow
					.findViewById(R.id.alarmclock_inactive);
			renderFuture(item, preyRow, title, time, status, image);
			// TODO: Trigger a new count down
		}

	}

	private void renderAllPreysAsPassed() {
		for (PreyItem item : preyTimes) {
			View preyRow = this.preyNamePreyRowMap.get(item.getName());
			TextView title = (TextView) preyRow.findViewById(R.id.row_title);
			TextView status = (TextView) preyRow.findViewById(R.id.row_status);
			TextView time = (TextView) preyRow.findViewById(R.id.row_time);
			ImageView image = (ImageView) preyRow
					.findViewById(R.id.alarmclock_inactive);
			renderPassed(item, preyRow, title, time, status, image);
		}

	}

	private boolean currentDateUsedInAppIsMoreThanOneInthaFuture() {
		return timeCurrentlyUsedInPreyOverView.getDayOfYear() < DateTime.now()
				.getDayOfYear();
	}

	private boolean currentDateUsedInAppIsMoreThanOneDayAgo() {
		return timeCurrentlyUsedInPreyOverView.getDayOfYear() < DateTime.now()
				.getDayOfYear();
	}

	private boolean isActive(PreyItem preyItem) {
		List<PreyItem> candidates = new ArrayList<PreyItem>();
		if (!preyItem.getTime().isBeforeNow())
			return false;
		for (PreyItem candiate : preyTimes) {
			if (candiate.getTime().isBeforeNow()) {
				candidates.add(candiate);
			}
		}
		Collections.sort(candidates);
		return candidates.get(candidates.size() - 1).equals(preyItem)
				&& (preyItem.getTime().getDayOfMonth() == DateTime.now()
						.getDayOfMonth() && preyItem.getTime().getMonthOfYear() == DateTime
						.now().getMonthOfYear());
	}

	private boolean isNext(PreyItem preyItem) {
		if (!preyItem.getTime().isAfterNow())
			return false;
		if (preyItem.getTime().getDayOfYear() == currentDate.getDayOfYear()) {
			List<PreyItem> candidates = new ArrayList<PreyItem>();
			for (PreyItem candiate : preyTimes) {
				if (candiate.getTime().isAfterNow()) {
					candidates.add(candiate);
				}
			}
			Collections.sort(candidates);
			return candidates.get(0).equals(preyItem);
		} else {
			return false;
		}
	}

	private boolean isPassed(PreyItem key) {
		return key.getTime().isBeforeNow();
	}

	private boolean isFuture(PreyItem key) {
		return key.getTime().isAfterNow();
	}

	private void renderActive(PreyItem key, View container, TextView title,
			TextView time, TextView status, ImageView image) {
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (7 * scale + 0.5f);
		title.setTextColor(Color.BLACK);
		time.setTextColor(Color.BLACK);
		container.setBackgroundResource(R.drawable.border_active_pray);
		// container.setPadding(0, pixels, 0, pixels);
		status.setTextColor(Color.BLACK);
	}

	private void renderPassed(PreyItem key, View container, TextView title,
			TextView time, TextView status, ImageView image) {
		View preyRow = this.preyNamePreyRowMap.get(key.getName());
		renderGeneralProperties(key, preyRow, title, time, status, image);
		title.setTextColor(Color.LTGRAY);
		time.setTextColor(Color.LTGRAY);

		status.setTextColor(Color.LTGRAY);
	}

	private void renderNext(PreyItem key, View container, TextView title,
			TextView time, TextView status, ImageView image) {

		DateTimeZone zone = DateTimeZone.forID(TimeZone.getDefault().getID());

		String keyTime = key.getTime().toString();
		String us = DateTime.now(zone).toString();

		DateTime delta = key.getTime().minus(DateTime.now(zone).getMillis());
		Long deltaMilis = key.getTime().getMillis()
				- DateTime.now(zone).getMillis();

		preyCountDownTimer = new PreyCountDownTimer(deltaMilis, 1000, status,
				this);
		preyCountDownTimer.start();
	}

	private void renderFuture(PreyItem key, View container, TextView title,
			TextView time, TextView status, ImageView image) {
		title.setTextColor(Color.BLACK);
		status.setTextColor(Color.BLACK);
		time.setTextColor(Color.BLACK);
		renderGeneralProperties(key, container, title, time, status, image);
	}

	private void renderGeneralProperties(PreyItem item, View convertView,
			TextView title, TextView time, TextView status, ImageView image) {
		// indicator.setBackgroundDrawable(background);
		String ZeroPlusHour = Integer.toString(item.getTime().getHourOfDay());
		if (item.getTime().getHourOfDay() < 10) {
			ZeroPlusHour = "0" + ZeroPlusHour;
		}
		String ZeroPlusMin = Integer.toString(item.getTime().getMinuteOfHour());
		if (item.getTime().getMinuteOfHour() < 10) {
			ZeroPlusMin = "0" + ZeroPlusMin;
		}
		time.setText(ZeroPlusHour + ":" + ZeroPlusMin);
		title.setText(item.getName());
		status.setText("");
		convertView.setBackgroundResource(R.drawable.border_none_active_pray);
	}

	private void setUpCurrentDay() {
		int dayOfWeek = timeCurrentlyUsedInPreyOverView.getDayOfWeek();
		String day;
		switch (dayOfWeek) {
		case 1:
			day = "Mandag";
			break;
		case 2:
			day = "Tirsdag";
			break;
		case 3:
			day = "Onsdag";
			break;
		case 4:
			day = "Torsdag";
			break;
		case 5:
			day = "Fredag";
			break;
		case 6:
			day = "Lørdag";
			break;
		case 7:
			day = "Søndag";
			break;
		default:
			day = "Ukjent";
			break;

		}
		day += " " + timeCurrentlyUsedInPreyOverView.getDayOfMonth() + "."
				+ timeCurrentlyUsedInPreyOverView.getMonthOfYear() + "."
				+ timeCurrentlyUsedInPreyOverView.getYear();
		currentDay.setText(day);
	}

	public List<PreyItem> loadPrayTimes(DateTime dateTime) {
		SnmsPrayTimeAdapter prayTimeAdapter = new SnmsPrayTimeAdapter(
				getActivity().getAssets(),
				((PreyOverView) getActivity()).getDAO());
		DateTime midnight = dateTime.minusHours(dateTime.getHourOfDay())
				.minusMinutes(dateTime.getMinuteOfHour())
				.minusSeconds(dateTime.getSecondOfMinute());
		return prayTimeAdapter.getPrayListForDate(midnight);

	}

	@Override
	public void onClick(View v) {
		if (v.equals(currentDay)) {
			android.app.DatePickerDialog pika = new android.app.DatePickerDialog(
					this.getActivity(), this, DateTime.now().getYear(),
					DateTime.now().getMonthOfYear()-1, DateTime.now().getDayOfMonth());

			pika.show();
		}
		if (v.equals(nextDay)) {
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView
					.plusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
			renderPreyList();
		
			
		
		}
		if (v.equals(prevDay)) {
			timeCurrentlyUsedInPreyOverView = timeCurrentlyUsedInPreyOverView
					.minusDays(1);
			setUpCurrentDay();
			preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
			renderPreyList();
			

		}
		
		if (v.equals(shortCuts)) {
			shortCuts.setSelected(true);
			latestNews.setSelected(false);
			shortCutContainer.setVisibility(View.VISIBLE);
			latesetNewsScrollView.setVisibility(View.GONE);
		}
		if (v.equals(latestNews)) {
			latestNews.setSelected(true);
			shortCuts.setSelected(false);
			shortCutContainer.setVisibility(View.GONE);
			latesetNewsScrollView.setVisibility(View.VISIBLE);
		}

		if (v.equals(newsImage1)) {
			NewsDetailsFragment myDetailFragment = new NewsDetailsFragment(
					currentNewsItem1);
			switchFragment(myDetailFragment, null);
		}
		if (v.equals(newsImage2)) {
			NewsDetailsFragment myDetailFragment = new NewsDetailsFragment(
					currentNewsItem2);
			switchFragment(myDetailFragment, null);
		}
		if (v.equals(montlyCalenderShortCut)) {
			PrayCalenderListFragment myDetailFragment = new PrayCalenderListFragment();
			switchFragment(myDetailFragment, null);
		}
		if (v.equals(hijriShortcut)) {
			HijriList myDetailFragment = new HijriList();
			switchFragment(myDetailFragment, null);
		}
		if (v.equals(settingsShortCut)) {
			SettingsFragment myDetailFragment = new SettingsFragment();
			switchFragment(myDetailFragment, null);
		}
		if (v.equals(qiblaShortcut)) {
			QiblaFragment myDetailFragment = new QiblaFragment();
			switchFragment(myDetailFragment, null);
		}
		if (v.equals(hijriShorcut)) {
			HijriList myDetailFragment = new HijriList();
			switchFragment(myDetailFragment, null);
		}
		if (v.equals(halalShortCut)) {
			HalalList myDetailFragment = new HalalList();
			switchFragment(myDetailFragment, null);
		}

		for (RelativeLayout key : newsMap.keySet()) {
			if (v.equals(key)) {
				NewsDetailsFragment myDetailFragment = new NewsDetailsFragment(
						newsMap.get(key));
				
				   Bundle args = new Bundle();
				   args.putSerializable("newsItem", newsMap.get(key));
				   
				   myDetailFragment.setArguments(args);
				switchFragment(myDetailFragment, null);
			}
		}
		try {
		for (String key : alarmButtonNameMap.keySet()) {
			if (!key.equals("Soloppgang") && v.equals(alarmButtonNameMap.get(key))) {
				if (!Util.hasAlarm(key)) {
					for (PreyItem preyItem : preyTimes) {
						if (preyItem.getName().equals(key)) {
							//create a standard android dialog
							

					
							FragmentTransaction ft = getActivity()
									.getSupportFragmentManager()
									.beginTransaction(); // endre dette til å
															// bruke
															// setReapeting
							AlarmDialogFragment newFragment = AlarmDialogFragment
									.newInstance(preyItem,(ImageButton) v); // var key
							newFragment.show(ft, "dialog");
							Bundle args = new Bundle();
							args.putString("prey", key);
							newFragment.setArguments(args);

						}
					}

				} else {

					AlarmUtilities Util = new AlarmUtilities(
							((PreyOverView) getActivity()).getDAO());
					Alarm alarm = Util.getAlarm(key);
					Util.RemoveAlarm(alarm.getId(), getAppContext(),
							alarm.getPrey());
					
					int duration = Toast.LENGTH_SHORT;
					Toast toast = null;
						toast = Toast.makeText(getActivity(), "Alarm deaktivert" , duration);
					toast.show();
					renderAlarmState();
				}

			}
		}
		} catch (Exception e){
			System.out.println("blah");
		}
	}

	public static Context getAppContext() {
		return MainApplication.getAppContext();
	}

	private void switchFragment(Fragment fragment1, Fragment fragment2) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof BaseActivity) {
			BaseActivity fca = (BaseActivity) getActivity();
			fca.switchContent(fragment1, fragment2);
		}
	}

	private Response.ErrorListener createErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO : Log error and get prey times from local storage
				// error.getStackTrace();
				newsSpinnerProgress2.setVisibility(View.GONE);
				Log.e("error", error.toString());
			}
		};
	}

	HashMap<RelativeLayout, NewsItem> newsMap = new HashMap<RelativeLayout, NewsItem>();

	void setListener(RelativeLayout latestNews) {
		latestNews.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	private Response.Listener<NewsItem[]> createSuccessListener() {
		return new Response.Listener<NewsItem[]>() {

			@Override
			public void onResponse(NewsItem[] response) {
				// newsSpinnerProgress.setVisibility(View.GONE);
				LayoutInflater inflater = getActivity().getLayoutInflater();
				for (NewsItem item : response) {
					RelativeLayout latestNews = (RelativeLayout) inflater
							.inflate(R.layout.recent_news, null, false);
					TextView newsText = (TextView) latestNews
							.findViewById(R.id.newsText);
					NetworkImageView newsimage = (NetworkImageView) latestNews
							.findViewById(R.id.newsImage);
					newsText.setText(item.getTitle());
					newsimage.setImageUrl(getVersion("medium",item.getImgUrl()),
							ImageCacheManager.getInstance().getImageLoader());
					latestNewsContainer.addView(latestNews);
					newsMap.put(latestNews, item);
					setListener(latestNews);

				}

			}
		};
	}

		public void setAlarm(View v) {
		v.setVisibility(View.INVISIBLE);
	}

	@Override
	public void alarmChanged(String alarm, Boolean value) {
		
		//Alarm aktivert
		
		int duration = Toast.LENGTH_SHORT;
		Toast toast = null;
		if(value) 
			toast = Toast.makeText(getActivity(), "Alarm aktivert" , duration);
		else 
			toast = Toast.makeText(getActivity(), "Alarm deaktivert" , duration);
		renderAlarmState();
		toast.show();

	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if (preyCountDownTimer != null) {
			preyCountDownTimer.cancel();
			preyCountDownTimer = null;
		}
		timeCurrentlyUsedInPreyOverView = new DateTime(year, monthOfYear + 1,
				dayOfMonth, 0, 0);
		setUpCurrentDay();
		preyTimes = loadPrayTimes(timeCurrentlyUsedInPreyOverView);
		renderPreyList();
		

	}
	
	private String getVersion(String version,String imageUrl){
		
		String [] temp = imageUrl.split("\\.");
		
		String toReturn = "";
		for(int i = 0; i<temp.length;i++){
			if(i == temp.length-2)
				toReturn +=  temp[i] + "_" + version + ".";
			else if(i == temp.length -1) 
				toReturn+= temp[i];
			else 
				toReturn+= temp[i] + ".";
		}
		
		return toReturn;
	}

}

package no.irn.app.alarm;

import no.irn.app.domain.PreyItem;
import android.content.Context;
import android.content.Intent;

public interface AlarmChangeListner {
	
	

	public void alarmChanged(String name, Boolean value); 
	
}

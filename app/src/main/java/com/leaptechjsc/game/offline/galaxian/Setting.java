package com.leaptechjsc.game.offline.galaxian;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.leaptechjsc.game.offline.galaxian.R;

public class Setting extends PreferenceActivity 
{
	//Option names and default values
	private static final String OPT_MUSIC_BACKGROUND= "music_background";
	private static final boolean OPT_MUSIC_BACKGROUND_DEF= true;
	private static final String OPT_MUSIC_SOUND= "music_sound";
	private static final boolean OPT_MUSIC_SOUND_DEF= true;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.setting);
	}
	
	/** Get the current value of the music background option */
	public static boolean getMusic_Background(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getBoolean(OPT_MUSIC_BACKGROUND, OPT_MUSIC_BACKGROUND_DEF);
	}
	/** Get the current value of the music sound option */
	public static boolean getMusic_Sound(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context)
		.getBoolean(OPT_MUSIC_SOUND, OPT_MUSIC_SOUND_DEF);
	}
}

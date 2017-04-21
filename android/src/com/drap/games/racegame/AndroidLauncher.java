package com.drap.games.racegame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.drap.games.racegame.Game;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AndroidLauncher extends AndroidApplication implements PlayServices{
	private GameHelper gameHelper;
	private final static int requestCode = 1;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		gameHelper.enableDebugLog(false);

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInFailed(){ }

			@Override
			public void onSignInSucceeded(){ }
		};

		gameHelper.setup(gameHelperListener);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock=true;
		initialize(new Game(this), config);
	}
	@Override
	protected void onStart()
	{
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void signIn()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.beginUserInitiatedSignIn();
				}
			});
			Gdx.app.log("MainActivity", "Log in succes.");
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void signOut()
	{
		try
		{
			runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					gameHelper.signOut();
				}
			});
			Gdx.app.log("MainActivity", "Log out succes.");
		}
		catch (Exception e)
		{
			Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
		}
	}

	@Override
	public void rateGame()
	{
		String str = "https://play.google.com/store/apps/details?id=com.drap.games.racegame";
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
	}

	@Override
	public void unlockAchievement(String achievement)
	{
		Games.Achievements.unlock(gameHelper.getApiClient(),
				getString(getResources().getIdentifier(achievement, "string", this.getPackageName())));
	}

	@Override
	public void submitScore(int nLap,int highScore)
	{
		if (isSignedIn() == true)
		{
			if(nLap==3)
				Games.Leaderboards.submitScore(gameHelper.getApiClient(),
						getString(R.string.leaderboard_3_laps), highScore);
			else if(nLap==5)
				Games.Leaderboards.submitScore(gameHelper.getApiClient(),
						getString(R.string.leaderboard_5_laps), highScore);
		}
	}

	@Override
	public void showAchievement()
	{
		if (isSignedIn() == true)
		{
			startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
		}
		else {
			signIn();
		}
	}

	@Override
	public void showScore(int nLap)
	{
		if (isSignedIn() == true)
		{
			if(nLap==3)
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
						getString(R.string.leaderboard_3_laps)), requestCode);
			else if(nLap==5)
				startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
						getString(R.string.leaderboard_5_laps)), requestCode);
		}
		else
		{
			signIn();
		}
	}

	@Override
	public boolean isSignedIn()
	{
		return gameHelper.isSignedIn();
	}

	@Override
	public String getPlayerName() {
		return Games.Players.getCurrentPlayer(gameHelper.getApiClient()).getDisplayName();
	}

	@Override
	public void getPlayerBanner() {
		Uri imgUri=Games.Players.getCurrentPlayer(gameHelper.getApiClient()).getBannerImagePortraitUri();
		savefile(imgUri,"display.png");
		/*Bitmap bitmap;
		com.badlogic.gdx.scenes.scene2d.utils.Drawable drawable;
		final Drawable[] drawable_android = {null};
		//try {
			//InputStream inputStream = getContentResolver().openInputStream(imgUri);
			ImageManager imageManager=ImageManager.create(this.getContext());
			imageManager.loadImage(new ImageManager.OnImageLoadedListener() {
				@Override
				public void onImageLoaded(Uri uri, Drawable drawable, boolean b) {
					drawable_android[0] =drawable;
				}
			},imgUri);
			/*bitmap = BitmapFactory.decodeStream(inputStream);
			Texture tex = new Texture(bitmap.getWidth(), bitmap.getHeight(), Pixmap.Format.RGBA8888);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex.getTextureObjectHandle());
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			bitmap.recycle();
			return tex;
		} catch (FileNotFoundException e) {
			return null;
		}*/
	}
	void savefile(Uri sourceuri,String name)
	{
		String sourceFilename= sourceuri.getPath();
		String destinationFilename = name;

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFilename));
			bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
			byte[] buf = new byte[1024];
			bis.read(buf);
			do {
				bos.write(buf);
			} while(bis.read(buf) != -1);
		} catch (IOException e) {

		} finally {
			try {
				if (bis != null) bis.close();
				if (bos != null) bos.close();
			} catch (IOException e) {

			}
		}
	}
}

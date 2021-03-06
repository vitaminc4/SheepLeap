package com.game.sheepleap;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import com.game.sheepleap.scenes.MainMenuScene;
import com.game.sheepleap.scenes.SplashScene;

import android.view.KeyEvent;

public class GameActivity extends BaseGameActivity {

	private ZoomCamera mZoomCamera;
    
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) 
    {
        return new LimitedFPSEngine(pEngineOptions, 60);
    }


	@Override
	public EngineOptions onCreateEngineOptions() {
		mZoomCamera = new ZoomCamera(0, 0, 800, 480);
		// TODO maybe handle phone resolutions/ratios a bit more gracefully than stretching... esp since this is a relatively low resolution
		//EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(800, 480), this.mZoomCamera);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), this.mZoomCamera);
	    engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
	    engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
	    return engineOptions;
	}


	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		ResourcesManager.prepareManager(mEngine, this, mZoomCamera, getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();	
	}


	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		SplashScene.displayNew();
		pOnCreateSceneCallback.onCreateSceneFinished(SceneManager.getInstance().getCurrentScene());
		//SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
	}


	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
	    mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() 
	    {
	            @Override
				public void onTimePassed(final TimerHandler pTimerHandler) 
	            {
	                mEngine.unregisterUpdateHandler(pTimerHandler);
	                //SceneManager.getInstance().createMenuScene();
	                MainMenuScene.displayNew();
	            }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
		
	}
	

	@Override
	protected void onDestroy()
	{
	    super.onDestroy();
	        
	    if (this.isGameLoaded())
	    {
	        System.exit(0);    
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
	    if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
	        SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
	    }
	    return false; 
	}
    
}

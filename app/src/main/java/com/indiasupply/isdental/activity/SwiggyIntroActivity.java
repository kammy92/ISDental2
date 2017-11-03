package com.indiasupply.isdental.activity;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.Constants;
import com.stephentuso.welcome.BasicPage;
import com.stephentuso.welcome.ParallaxPage;
import com.stephentuso.welcome.WelcomeActivity;
import com.stephentuso.welcome.WelcomeConfiguration;

public class SwiggyIntroActivity extends WelcomeActivity {
    
    @Override
    protected WelcomeConfiguration configuration () {
//        Window window = getWindow ();
//        if (Build.VERSION.SDK_INT >= 21) {
//            window.clearFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        }
        
        return new WelcomeConfiguration.Builder (this)
                .defaultTitleTypefacePath (Constants.font_name)
                .defaultHeaderTypefacePath (Constants.font_name)
                .defaultDescriptionTypefacePath (Constants.font_name)
                .doneButtonTypefacePath (Constants.font_name)
                .skipButtonTypefacePath (Constants.font_name)
                .animateButtons (true)
                .canSkip (false)
                .swipeToDismiss (false)
                .page (new BasicPage (R.drawable.ic_home_featured,
                        "Welcome",
                        "An Android library for onboarding, instructional screens, and more")
                        .background (R.color.material_deep_orange))
                .page (new BasicPage (R.drawable.ic_home_services,
                        "Simple to use",
                        "Add a welcome screen to your app with only a few lines of code.")
                        .background (R.color.material_red))
                .page (new ParallaxPage (R.layout.parallax_example,
                        "Easy parallax",
                        "Supply a layout and parallax effects will automatically be applied")
                        .lastParallaxFactor (1.0f)
                        .background (R.color.material_deep_purple))
                .page (new BasicPage (R.drawable.ic_home_events,
                        "Customizable",
                        "All elements of the welcome screen can be customized easily.")
                        .background (R.color.md_material_blue_800))
                .exitAnimation (R.anim.slide_out_left)
                .build ();
    }
}
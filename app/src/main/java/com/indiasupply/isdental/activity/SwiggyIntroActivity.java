package com.indiasupply.isdental.activity;

import com.indiasupply.isdental.R;
import com.indiasupply.isdental.utils.Constants;
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
                .page (new ParallaxPage (R.layout.intro_screen_1,
                        "Offers",
                        "Offers Description")
                        .lastParallaxFactor (0.5f)
                        .headerColorResource (this, R.color.primary_text2)
                        .descriptionColorResource (this, R.color.primary_text2)
                        .background (R.color.intro_screen_1))
                .page (new ParallaxPage (R.layout.intro_screen_2,
                        "Events",
                        "Events Description")
                        .lastParallaxFactor (0.5f)
                        .headerColorResource (this, R.color.primary_text2)
                        .descriptionColorResource (this, R.color.primary_text2)
                        .background (R.color.intro_screen_2))
                .page (new ParallaxPage (R.layout.intro_screen_3,
                        "Contacts",
                        "Contacts Description")
                        .lastParallaxFactor (0.5f)
                        .headerColorResource (this, R.color.primary_text2)
                        .descriptionColorResource (this, R.color.primary_text2)
                        .background (R.color.intro_screen_3))
                .page (new ParallaxPage (R.layout.intro_screen_4,
                        "Service",
                        "Service Description")
                        .lastParallaxFactor (0.5f)
                        .headerColorResource (this, R.color.primary_text2)
                        .descriptionColorResource (this, R.color.primary_text2)
                        .background (R.color.intro_screen_4))
                .exitAnimation (R.anim.slide_out_left)
                .build ();
    }
}
package com.soco.SoCoClient.common.util;

import android.widget.Button;

import com.soco.SoCoClient.events.model.Event;

/**
 * Created by David_WANG on 11/09/2015.
 */
public class LikeUtil {
    public static void updateLikeButtonStatus(Button button,Event event, boolean doLike){
        if(doLike) {
            //update status
            initialLikeButton(button, true);
            //update counter
            int originalNumber = event.getNumber_of_likes();
            event.setNumber_of_likes(originalNumber + 1);
            button.setText(Integer.toString(event.getNumber_of_likes()));
            event.setIsLikedEvent(true);
            return;
        }else{
            //update status
            initialLikeButton(button, false);
            //update counter
            int originalNumber = event.getNumber_of_likes();
            event.setNumber_of_likes(originalNumber - 1);
            button.setText(Integer.toString(event.getNumber_of_likes()));
            event.setIsLikedEvent(false);
            return;
        }
    }
    public static void initialLikeButton(Button button, boolean liked){
        if(liked) {
//            button.setEnabled(false);
//            button.setClickable(false);
            button.setActivated(true);
        }else{
//            button.setEnabled(true);
//            button.setClickable(true);
            button.setActivated(false);
        }
    }
}

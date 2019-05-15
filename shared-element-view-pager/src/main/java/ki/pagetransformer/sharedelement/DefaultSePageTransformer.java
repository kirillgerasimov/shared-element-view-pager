package ki.pagetransformer.sharedelement;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

/**
 * PageTransformer that allows you to do shared element transitions between pages in ViewPager.
 * It requires view pager sides match screen sides to function properly. I.e. ViewPager page width
 * must be equal to screen width. <br/>
 * Usage:<br/>
 * <code>
 * sharedElementPageTransformer.addTransition(R.id.FirstPageTextView, R.id.SecondPageTextView)</code>
 * </code>
 */
public class DefaultSePageTransformer extends AbstractSePageTransformer {

    /**
     * @param activity       activity that hosts view pager
     * @param fragments      fragment that are in view pager in the SAME ORDER
     * @param viewPager
     */
    public DefaultSePageTransformer(Activity activity, List<Fragment> fragments, ViewPager viewPager) {
        super(activity, fragments, viewPager);
    }

    @Override
    protected void modifyPositions(View fromView, View toView, View fromPage, View toPage, View currentPage, float position, boolean slideToTheRight) {
        // saving shared element position on the screen
        float fromX = fromView.getX() - fromView.getTranslationX();
        float fromY = fromView.getY() - fromView.getTranslationY();
        float toX = toView.getX() - toView.getTranslationX();
        float toY = toView.getY() - toView.getTranslationY();
        float deltaX = toX - fromX;
        float deltaY = toY - fromY;

        // scaling
        float fromWidth = fromView.getWidth();
        float fromHeight = fromView.getHeight();
        float toWidth = toView.getWidth();
        float toHeight = toView.getHeight();
        float deltaWidth = toWidth - fromWidth;
        float deltaHeight = toHeight - fromHeight;


        int fromId = fromView.getId();
        int toId = toView.getId();

        if (position <= -1) {

        } else if (position < 1) {

            float pageWidth = getPageWidth();
            float sign = slideToTheRight ? 1 : -1;

            float translationY = (deltaY + deltaHeight / 2) * sign * (-position);
            float translationX = (deltaX + sign * pageWidth + deltaWidth / 2) * sign * (-position);

            if (currentPage.findViewById(fromId) != null) {
                fromView.setTranslationX(translationX);
                fromView.setTranslationY(translationY);

                float scaleX = (fromWidth == 0) ? 1 : (fromWidth + deltaWidth * sign * (-position)) / fromWidth;
                float scaleY = (fromHeight == 0) ? 1 : (fromHeight + deltaHeight * sign * (-position)) / fromHeight;

                fromView.setScaleX(scaleX);
                fromView.setScaleY(scaleY);
            }
            if (currentPage.findViewById(toId) != null) {

                toView.setTranslationX(translationX);
                toView.setTranslationY(translationY);
                float scaleX = (toWidth == 0) ? 1 : (toWidth + deltaWidth * sign * (-position)) / toWidth;
                float scaleY = (toHeight == 0) ? 1 :(toHeight + deltaHeight * sign * (-position)) / toHeight;

                toView.setScaleX(scaleX);
                toView.setScaleY(scaleY);
            }


        } else {
        }


    }
}
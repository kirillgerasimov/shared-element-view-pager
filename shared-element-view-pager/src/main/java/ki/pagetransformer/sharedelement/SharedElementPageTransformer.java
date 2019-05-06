package ki.pagetransformer.sharedelement;

import android.app.Activity;
import android.graphics.Point;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;

/**
 * PageTransformer that allows you to do shared element transitions between pages in ViewPager.
 * It requires view pager sides match screen sides to function properly. I.e. ViewPager page width
 * must be equal to screen width. <br/>
 *     Usage:<br/>
 *         <code>
 *                  sharedElementPageTransformer.addSharedTransition(R.id.FirstPageTextView, R.id.SecondPageTextView)</code>
 *         </code>
 *
 *
 */
public class SharedElementPageTransformer implements ViewPager.PageTransformer, ViewPager.OnPageChangeListener {
    /** Android need the correction while view scaling for some reason*/
    private static float MAGICAL_ANDROID_RENDERING_SCALE = 1;
//    private static float MAGICAL_ANDROID_RENDERING_SCALE = 0.995f;

    // External variables

    private final Activity activity;
    List<Fragment> fragments;
    private Set<Pair<Integer,Integer>> sharedElementIds = new HashSet<>();



    //Internal variables

    private List<View> pages;
    private Map<View, Integer> pageToNumber = new HashMap<>();

    private Integer fromPageNumber = 0;
    private Integer toPageNumber;

    /** current view pager position */
    private int position;

    /**
     * @param activity activity that hosts view pager
     * @param fragments fragment that are in view pager in the SAME ORDER
     */
    public SharedElementPageTransformer(Activity activity, List<Fragment> fragments) {
        this.activity = activity;
        this.fragments = fragments;
    }


    @Override
    public void transformPage(@NonNull View page, float position) {
        updatePageCache();
        if (fromPageNumber == null || toPageNumber == null) return;

        for (Pair<Integer,Integer> idPair : sharedElementIds) {
            Integer fromViewId = idPair.first;
            Integer toViewId = idPair.second;

            View fromView = activity.findViewById(fromViewId);
            View toView = activity.findViewById(toViewId);

            if (fromView != null && toView != null) {
                //Looking if current Shared element transition matches visible pages

                View fromPage = pages.get(fromPageNumber);
                View toPage = pages.get(toPageNumber);

                if(fromPage != null && toPage != null) {
                    fromView = fromPage.findViewById(fromViewId);
                    toView = toPage.findViewById(toViewId);


                    // if both views are on pages user drag between apply transformation
                    if (
                            fromView != null
                                    && toView != null
                    ) {
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

                        boolean slideToTheRight = toPageNumber > fromPageNumber;

                        if (position <= -1) {

                        } else if (position < 1) {

                            float pageWidth = getSceenWidth();
                            float sign = slideToTheRight ? 1 : -1;

                            float translationY = (deltaY + deltaHeight / 2) * sign * (-position);
                            float translationX = (deltaX + sign * pageWidth + deltaWidth / 2) * sign * (-position);

                            if (page.findViewById(fromId) != null) {
                                fromView.setTranslationX(translationX);
                                fromView.setTranslationY(translationY);

                                float scaleX = (fromWidth == 0) ? 1 : (fromWidth + deltaWidth * sign * (-position)) / fromWidth;
                                float scaleY = (fromHeight == 0) ? 1 : (fromHeight + deltaHeight * sign * (-position)) / fromHeight;

                                fromView.setScaleX(scaleX);
                                fromView.setScaleY(scaleY * MAGICAL_ANDROID_RENDERING_SCALE);
                            }
                            if (page.findViewById(toId) != null) {

                                toView.setTranslationX(translationX);
                                toView.setTranslationY(translationY);
                                float scaleX = (toWidth == 0) ? 1 : (toWidth + deltaWidth * sign * -position) / toWidth;
                                float scaleY = (toHeight == 0) ? 1 :(toHeight + deltaHeight * sign * -position) / toHeight;

                                toView.setScaleX(scaleX);
                                toView.setScaleY(scaleY);
                            }


                        } else {
                        }

                    }
                }
            }
        }
    }

    private float getSceenWidth() {
        Point outSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(outSize);
        return outSize.x;
    }

    /**
     * Creating page cache array to determine if shared element on
     * currently visible page
     */
    private void updatePageCache() {
        pages = new ArrayList<>();

        for (int i = 0; i < fragments.size(); i++) {
            View pageView = fragments.get(i).getView();
            pages.add(pageView);
            pageToNumber.put(pageView, i);

        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Set<Integer> visiblePages = new HashSet<>();

        visiblePages.add(position);
        visiblePages.add(positionOffset >= 0 ? position + 1 : position - 1);
        visiblePages.remove(fromPageNumber);

        toPageNumber = visiblePages.iterator().next();

        if (pages == null || toPageNumber >= pages.size()) toPageNumber = null;
    }


    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) {
            fromPageNumber = position;
            resetViewPositions();
        }

    }

    private void resetViewPositions() {
            for (Pair<Integer, Integer> idPair : sharedElementIds) {
                View sharedElement = activity.findViewById(idPair.first);
                if(sharedElement != null) {
                    sharedElement.setTranslationX(0);
                    sharedElement.setTranslationY(0);
                    sharedElement.setScaleX(1);
                    sharedElement.setScaleY(1);
                }
                sharedElement = activity.findViewById(idPair.second);
                if(sharedElement != null) {

                    sharedElement.setTranslationX(0);
                    sharedElement.setTranslationY(0);
                    sharedElement.setScaleX(1);
                    sharedElement.setScaleY(1);
                }
            }

    }

    /**
     * Set up shared element transition from element with <code>fromViewId</code> to
     * element with <code>toViewId</code>. Note that you can setup each transition
     * direction separately. e.g. <br/>
     * <code>addSharedTransition(R.id.FirstPageTextView, R.id.SecondPageTextView)</code><br/>
     * and<br/>
     * <code>addSharedTransition(R.id.SecondPageTextView, R.id.FirstPageTextView)</code><br/>
     * are different.
     * @param fromViewId
     * @param toViewId
     */
    public void addSharedTransition(int fromViewId, int toViewId) {
        sharedElementIds.add(new Pair<>(fromViewId, toViewId));
    }

    /**
     * In case there is "ladder" appears between while transition.
     * You may try to tune that magical scale to get rid of it.
     * @param magicalAndroidRenderingScale float between 0 and infinity. Typically very close to 1.0
     */
    public static void setMagicalAndroidRenderingScale(float magicalAndroidRenderingScale) {
        MAGICAL_ANDROID_RENDERING_SCALE = magicalAndroidRenderingScale;
    }
}
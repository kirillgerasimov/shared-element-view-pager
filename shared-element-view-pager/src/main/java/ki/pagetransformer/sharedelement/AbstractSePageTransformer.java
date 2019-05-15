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

public abstract class AbstractSePageTransformer implements SePageTransformer {

    // External variables

    protected final Activity activity;
    protected final ViewPager viewPager;

    List<Fragment> fragments;
    protected Set<Pair<Integer, Integer>> sharedElementIds = new HashSet<>();

    //Internal variables

    protected List<View> pages = new ArrayList<>();
    protected Map<View, Integer> pageToNumber = new HashMap<>();

    protected Integer fromPageNumber = 0;
    protected Integer toPageNumber;

    protected Map<Integer, Float> idToAbsX = new HashMap<>();
    protected Map<Integer, Float> idToAbsY = new HashMap<>();

    /**
     * current view pager position
     */
    private int position;

    /**
     * @param activity  activity that hosts view pager
     * @param fragments fragment that are in view pager in the SAME ORDER
     */
    public AbstractSePageTransformer(Activity activity, List<Fragment> fragments, ViewPager viewPager) {
        this.activity = activity;
        this.fragments = fragments;
        this.viewPager = viewPager;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
        updatePageCache();
        if (fromPageNumber == null || toPageNumber == null) return;

        for (Pair<Integer, Integer> idPair : sharedElementIds) {
            Integer fromViewId = idPair.first;
            Integer toViewId = idPair.second;

            View fromView = activity.findViewById(fromViewId);
            View toView = activity.findViewById(toViewId);

            if (fromView != null && toView != null) {
                //Looking if current Shared element transition matches visible pages

                View fromPage = pages.get(fromPageNumber);
                View toPage = pages.get(toPageNumber);

                if (fromPage != null && toPage != null) {
                    fromView = fromPage.findViewById(fromViewId);
                    toView = toPage.findViewById(toViewId);

                    // if both views are on pages user drag between apply transformation
                    if (
                            fromView != null
                                    && toView != null
                    ) {

                        boolean slideToTheRight = toPageNumber > fromPageNumber;
                        modifyPositions(fromView, toView, fromPage, toPage, page, position, slideToTheRight);

                    }
                }
            }
        }
    }

    protected void modifyPositions(View fromView, View toView, View fromPage, View toPage, View currentPage, float position, boolean slideToTheRight) {}

    protected float getPageWidth() {
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
        }
    }


    /**
     * Set up shared element transition from element with <code>fromViewId</code> to
     * element with <code>toViewId</code>. Note that you can setup each transition
     * direction separately. e.g. <br/>
     * <code>addTransition(R.id.FirstPageTextView, R.id.SecondPageTextView)</code><br/>
     * and<br/>
     * <code>addTransition(R.id.SecondPageTextView, R.id.FirstPageTextView)</code><br/>
     * are different.
     *
     * @param fromViewId
     * @param toViewId
     */
    public void addTransition(int fromViewId, int toViewId) {
        addTransition(fromViewId, toViewId, true);
    }

    /**
     * Set up shared element transition from element with <code>fromViewId</code> to
     * element with <code>toViewId</code>. Note that you can setup each transition
     * direction separately. e.g. <br/>
     * <code>addTransition(R.id.FirstPageTextView, R.id.SecondPageTextView)</code><br/>
     * and<br/>
     * <code>addTransition(R.id.SecondPageTextView, R.id.FirstPageTextView)</code><br/>
     * are different.
     *
     * @param fromViewId
     * @param toViewId
     * @param bothDirections to include backward transition from toViewId to fromViewId aswell
     */
    public void addTransition(int fromViewId, int toViewId, boolean bothDirections) {
        sharedElementIds.add(new Pair<>(fromViewId, toViewId));
        if (bothDirections) {
            sharedElementIds.add(new Pair<>(toViewId, fromViewId));
        }
    }

    @Override
    public void removeTransition(int fromViewId, int toViewId, boolean bothDirections) {
        sharedElementIds.remove(new Pair<>(fromViewId, toViewId));
        if (bothDirections) {
            sharedElementIds.remove(new Pair<>(toViewId, fromViewId));
        }
    }

    @Override
    public void removeTransition(int fromViewId, int toViewId) {
        removeTransition(fromViewId, toViewId, true);
    }


    public void clearAllTransitions() {
        sharedElementIds.clear();
    }



}

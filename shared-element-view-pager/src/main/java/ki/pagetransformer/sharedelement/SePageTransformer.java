package ki.pagetransformer.sharedelement;

import android.util.Pair;

import androidx.viewpager.widget.ViewPager;

/**
 * SePageTransformer stands for Shared Element Page Transformer
 */
public interface SePageTransformer extends ViewPager.PageTransformer, ViewPager.OnPageChangeListener {

    /**
     * Set up shared element transition from element with <code>fromViewId</code> to
     * element with <code>toViewId</code> and vice versa. Note that you can setup each transition
     * direction separately using
     * <code>addTransition(int fromViewId, int toViewId, boolean bothDirections)</code>. e.g. <br/>
     * <code>addTransition(R.id.FirstPageTextView, R.id.SecondPageTextView, false)</code><br/>
     * and<br/>
     * <code>addTransition(R.id.SecondPageTextView, R.id.FirstPageTextView, false)</code><br/>
     * are different.
     *
     * @param fromViewId
     * @param toViewId
     */
    void addTransition(int fromViewId, int toViewId);

    /**
     * Set up shared element transition from element with <code>fromViewId</code> to
     * element with <code>toViewId</code> and vice versa. Note that you can setup each transition
     * direction separately using
     * <code>addTransition(int fromViewId, int toViewId, boolean bothDirections)</code>. e.g. <br/>
     * <code>addTransition(R.id.FirstPageTextView, R.id.SecondPageTextView, false)</code><br/>
     * and<br/>
     * <code>addTransition(R.id.SecondPageTextView, R.id.FirstPageTextView, false)</code><br/>
     * are different.
     *
     * @param fromViewId
     * @param toViewId
     * @param bothDirections to include backward transition from toViewId to fromViewId aswell
     */
    void addTransition(int fromViewId, int toViewId, boolean bothDirections);

    void removeTransition(int fromViewId, int toViewId, boolean bothDirections);

    void removeTransition(int fromViewId, int toViewId);


    void clearAllTransitions();
}

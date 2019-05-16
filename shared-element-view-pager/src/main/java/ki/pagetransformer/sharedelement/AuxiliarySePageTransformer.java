package ki.pagetransformer.sharedelement;

import android.app.Activity;
import android.os.Build;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PageTransformer that allows you to do shared element transitions between pages in ViewPager.
 * It works only with ImageViews atm. And requires viewpager to be displayed on relative layout.
 * Usage:<br/>
 * <code>
 * sharedElementPageTransformer.addTransition(R.id.FirstPageTextView, R.id.SecondPageTextView)</code>
 * </code>
 */
public class AuxiliarySePageTransformer extends AbstractSePageTransformer {

    private Map<Pair<Integer, Integer>, View> intermediateViews = new HashMap<>();
    
    private final Object relativeLayout;

    /**
     * @param activity       activity that hosts view pager
     * @param fragments      fragment that are in view pager in the SAME ORDER
     * @param viewPager
     * @param relativeLayout
     */
    public AuxiliarySePageTransformer(Activity activity, List<Fragment> fragments, ViewPager viewPager, Object relativeLayout) {
        super(activity, fragments, viewPager);
        this.relativeLayout = relativeLayout;
    }

    @Override
    protected void modifyPositions(View fromView, View toView, View fromPage, View toPage, View currentPage, float position, boolean slideToTheRight) {
        Integer fromViewId = fromView.getId();
        Integer toViewId = toView.getId();

        Pair<Integer, Integer> idPair = Pair.create(fromView.getId(), toView.getId());

        if (fromView instanceof ImageView && toView instanceof ImageView) {
            if (!intermediateViews.containsKey(idPair)) {
//                for debug purposes
//                TextView imageView = new TextView(activity);
//                imageView.setText(idPair.first + " - " + idPair.second);
//                imageView.setTextSize(40);
                ImageView imageView = new ImageView(activity);
//                imageView.setBackgroundColor(0xFF000000);
                imageView.setImageDrawable(((ImageView) fromView).getDrawable());
                imageView.setMaxWidth(fromView.getWidth());
                imageView.setMinimumWidth(fromView.getWidth());
                imageView.setMaxHeight(fromView.getHeight());
                imageView.setMinimumHeight(fromView.getHeight());
                imageView.setScaleType(((ImageView) fromView).getScaleType());


                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(fromView.getWidth(), fromView.getHeight());
                int[] fromViewPosition = new int[2];
                fromView.getLocationInWindow(fromViewPosition);
                int[] layoutPosition = getLayoutPosition();

                params.leftMargin = fromViewPosition[0] - layoutPosition[0];
                params.topMargin = fromViewPosition[1] - layoutPosition[1];

                intermediateViews.put(idPair, imageView);


                ((RelativeLayout) relativeLayout).addView(imageView, params);
            }


            // saving shared element position on the screen

            float fromX = idToAbsX.get(fromViewId) != null ? idToAbsX.get(fromViewId) : fromView.getX() - fromView.getTranslationX();
            float fromY = idToAbsY.get(fromViewId) != null ? idToAbsY.get(fromViewId) : fromView.getY() - fromView.getTranslationY();

            float toX = idToAbsX.get(toViewId) != null ? idToAbsX.get(toViewId) : toView.getX() - toView.getTranslationX();
            float toY = idToAbsY.get(toViewId) != null ? idToAbsY.get(toViewId) : toView.getY() - toView.getTranslationY();
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
                View intermediateView = intermediateViews.get(idPair);
                if (intermediateView != null && currentPage.findViewById(fromId) != null) {
                    intermediateView.setVisibility(View.INVISIBLE);

                }

            } else if (position < 1) {


                float sign = slideToTheRight ? 1 : -1;

                View intermediateView = intermediateViews.get(idPair);
                if (intermediateView != null && currentPage.findViewById(fromId) != null) {

                    float translationXx = (deltaX + deltaWidth / 2) * sign * (-(position));
                    float translationYy = (deltaY + deltaHeight / 2) * sign * (-(position));

                    Float fromViewX = idToAbsX.get(fromId);
                    Float fromViewY = idToAbsY.get(fromId);

                    if (intermediateView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams && fromViewX != null) {
                        ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) intermediateView.getLayoutParams();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            //TranslationX and Y is not working properly so I have to use margin
                            int[] layoutPosition = getLayoutPosition();
                            int marginX = (int) (translationXx + fromViewX - layoutPosition[0]);
                            int marginY = (int) (translationYy + fromViewY - layoutPosition[1]);
                            p.setMarginStart(marginX);
                            p.topMargin = marginY;

                        }
//                    intermediateView.setTranslationX(translationXx);
//                    intermediateView.setTranslationY(translationYy);
                        intermediateView.requestLayout();
                    }

                    float scaleX = (fromWidth == 0) ? 1 : (fromWidth + deltaWidth * sign * (-position)) / fromWidth;
                    float scaleY = (fromHeight == 0) ? 1 : (fromHeight + deltaHeight * sign * (-position)) / fromHeight;

                    intermediateView.setScaleX(scaleX);
                    intermediateView.setScaleY(scaleY);
//                intermediateView.setVisibility(View.VISIBLE);
//                ImageView imageView = intermediateViews.get(Pair.create(idPair.second, idPair.first));
//                if (imageView != null) {
//                    imageView.setVisibility(View.INVISIBLE);
//                }
                }


                if (currentPage.findViewById(fromId) != null) {
                    if (slideToTheRight) {
                        if ((position > -1) && (position < 0)) {
                            fromView.setVisibility(View.INVISIBLE);
                            intermediateView.setVisibility(View.VISIBLE);
                        } else {
                            fromView.setVisibility(View.VISIBLE);
                            intermediateView.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if ((position > 0) && (position < 1)) {
                            fromView.setVisibility(View.INVISIBLE);
                            intermediateView.setVisibility(View.VISIBLE);
                        } else {
                            fromView.setVisibility(View.VISIBLE);
                            intermediateView.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                if (currentPage.findViewById(toId) != null) {
                    if (slideToTheRight) {
                        if ((position > 0) && (position < 1)) {
                            toView.setVisibility(View.INVISIBLE);
                            intermediateView.setVisibility(View.VISIBLE);
                        } else {
                            toView.setVisibility(View.VISIBLE);
                            intermediateView.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        if ((position > -1) && (position < 0)) {
                            toView.setVisibility(View.INVISIBLE);
                            intermediateView.setVisibility(View.VISIBLE);
                        } else {
                            toView.setVisibility(View.VISIBLE);
                            intermediateView.setVisibility(View.INVISIBLE);
                        }
                    }


                }
// if (currentPage.findViewById(toId) != null) {
//                if(slideToTheRight) {
//                    if (position >= 0 || position <= -1) {
//                        toView.setVisibility(View.VISIBLE);
////                        intermediateView.setVisibility(View.INVISIBLE);
//                    } else {
//                        toView.setVisibility(View.INVISIBLE);
////                        intermediateView.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    if (position <= 0 || position >= 1) {
//                        toView.setVisibility(View.VISIBLE);
////                        intermediateView.setVisibility(View.INVISIBLE);
//                    } else {
//                        toView.setVisibility(View.INVISIBLE);
////                        intermediateView.setVisibility(View.VISIBLE);
//                    }
//                }
//
//
//
//            }


            } else {
                View intermediateView = intermediateViews.get(idPair);
                if (intermediateView != null && currentPage.findViewById(fromId) != null) {
                    intermediateView.setVisibility(View.INVISIBLE);

                }
            }
        }
    }

    private int[] getLayoutPosition() {
        int[] layoutPosition = new int[2];
        ((View) relativeLayout).getLocationInWindow(layoutPosition);
        return layoutPosition;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);

        float pageWidth = getPageWidth();

        for (Pair<Integer, Integer> idPair : sharedElementIds) {
            //TODO: get rid of this loop
            for (int i = 0; i < pages.size(); i++) {
                View page = pages.get(i);
                if (page == null) {
                    return;
                }

                View firstView = page.findViewById(idPair.first);

                if (firstView != null) {
                    float x = getX(firstView) - ((float) i - (position + positionOffset)) * pageWidth;
                    idToAbsX.put(firstView.getId(), x);
                    idToAbsY.put(firstView.getId(), (float) getY(firstView));
//                    System.out.println("onPageScrolled id = " + firstView.getId() + "  x = " + x + " y = " + getY(firstView));
                }

                View secondView = page.findViewById(idPair.second);

                if (secondView != null) {
                    float x = getX(secondView) - ((float) i - (position + positionOffset)) * pageWidth;
                    idToAbsX.put(secondView.getId(), x);
                    idToAbsY.put(secondView.getId(), (float) getY(secondView));
//                    System.out.println("onPageScrolled id = " + secondView.getId() + "  x = " + x + " y = " + getY(secondView));
                }

            }
        }

    }


    @Override
    public void removeTransition(int fromViewId, int toViewId, boolean bothDirections) {
        super.removeTransition(fromViewId, toViewId, bothDirections);

        Pair<Integer, Integer> idPair = Pair.create(fromViewId, toViewId);
        View view = intermediateViews.get(idPair);
        ((RelativeLayout) relativeLayout).removeView(view);
        intermediateViews.remove(idPair);
        if(bothDirections) {
            idPair = Pair.create(toViewId, fromViewId);
            view = intermediateViews.get(idPair);
            ((RelativeLayout) relativeLayout).removeView(view);
            intermediateViews.remove(idPair);

        }

    }

    @Override
    public void removeTransition(int fromViewId, int toViewId) {
        removeTransition(fromViewId, toViewId, true);
        super.removeTransition(fromViewId, toViewId);
    }

    @Override
    public void clearAllTransitions() {
        super.clearAllTransitions();
        for (Pair<Integer, Integer> idPair : intermediateViews.keySet()) {
            removeTransition(idPair.first, idPair.second, true);
        }


    }

    public int getX(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position[0];
    }

    public int getY(View view) {
        int[] position = new int[2];
        view.getLocationOnScreen(position);
        return position[1];
    }

}
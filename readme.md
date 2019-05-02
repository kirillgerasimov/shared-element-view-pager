# Shared Element View Pager
This library allows you to perform somewhat similar to Shared Element Transition **between** ViewPager pages.
**It's an alpha version** so feel free to create pull requests or modify it by your own.

![](images/shared-element-demo.gif)

### Contents.
- **demo** - directory with demo android app that. Shows Shared Element Transition between ViewPager pages.
share
- **shared-element-view-pager** - the library itself

### Usage.

- Add all fragment from from your ViewPager to the List in the same order.
```
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(hello_fragment);
        fragments.add(small_picture_fragment);
```
<br/>

- Create *SharedElementPageTransformer* presumably in *Activity.onCreate()*. <br/>
*this* refers to activity:
```
        SharedElementPageTransformer transformer =
                new SharedElementPageTransformer(this,  fragments);
```
<br/>

- Add shared transition by passing pairs of view ids, that need to be linked together 

```
        transformer.addSharedTransition(R.id.smallPic_image_cat, R.id.bigPic_image_cat);
        transformer.addSharedTransition(R.id.bigPic_image_cat, R.id.smallPic_image_cat);

        transformer.addSharedTransition(R.id.smallPic_text_label, R.id.bigPic_text_label);
        transformer.addSharedTransition(R.id.bigPic_text_label, R.id.smallPic_text_label);

        transformer.addSharedTransition(R.id.third_text, R.id.smallPic_text_label);
        transformer.addSharedTransition(R.id.smallPic_text_label, R.id.third_text);
```
<br/>

- Set our *transformer* to ViewPager pageTransformer **AND** onPageChangeListener.
```
        viewPager.setPageTransformer(false, transformer);
        viewPager.addOnPageChangeListener(transformer);
```

Look at *ki.pagetransformer.sharedelement.demo.MainActivity* and compile the demo for more details. 
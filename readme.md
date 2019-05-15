# Shared Element View Pager
This library allows you to perform Shared Element Transition **between** ViewPager pages.
It's an alpha version so feel free to create pull requests or modify it by your own.
<p align="center">


<img src="images/shared-element-demo.gif"/>
</p>


## Contents
- **demo** - directory with demo android app that shows Shared Element Transition between ViewPager pages.

- **shared-element-view-pager** - the library itself

## Usage
### Adding library
1. Make sure you have jcenter() in you repositories
```        
allprojects {
    repositories {
        google()
        jcenter() //All android studio projects have this repo by default
    }
}
```
<br/>
<br/>


2. Add library to module dependencies
```   
dependencies {
    //...     
    implementation 'com.github.kirillgerasimov:shared-element-view-pager:0.0.3-alpha'
}
```
<br/>
<br/>
<br/>



### Creating shared element transitions

1. Add all fragments from your ViewPager to the List in the same order.
```
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(hello_fragment);
        fragments.add(small_picture_fragment);
```
<br/>
<br/>


2. Create *SharedElementPageTransformer* presumably in *Activity.onCreate()*. <br/>
*this* refers to activity:
```
     SePageTransformer transformer = new DefaultSePageTransformer(this,  fragments, viewPager);
```
<br/>
<br/>


3. Add shared transition by passing pairs of view ids, that need to be linked together 

```
    transformer.addTransition(R.id.smallPic_image_cat2, R.id.bigPic_image_cat);


```
<br/>
<br/>


4. Set our *transformer* to ViewPager's pageTransformer **AND** onPageChangeListener.
```
    viewPager.setPageTransformer(false, transformer);
    viewPager.addOnPageChangeListener(transformer);
```
<br/>
<br/>



Look at *ki.pagetransformer.sharedelement.demo.MainActivity* and compile the demo for more details.
<br/>
<br/>


### Restrictions
Note that  *DefaultSePageTransformer* requires page width to be equal to screen to work properly.  This transformer does not 
create any intermediate extra views to perform animation. That's why it only drown within parent View (that's how drawing works in android). 
Fast animation may look fine in that case too though.

To work with nested views you need to: 
 1. create intermediate view in root layout
 2. Place it on top of animated view
 3. addTransition(R.id.intermediateViewId, R.id.targetViewId)
 4. manage view visibility

Also I created experimental AuxiliarySePageTransformer, that do the following steps for you. 
If you animate Recycler View, you still need to provide unique Ids. You could use the following 
example:

```
  @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView imageView = getImageView(holder);
        imageView.setId(generateThumbnailId(position));         
    
    }            
    
    //...       
    private ImageView getImageView(ViewHolder holder) {
        return (ImageView) holder.frameLayout.getChildAt(0);
    }
    public int generateThumbnailId(int itemPosition) {
        return 1000000 + itemPosition;
    }
    //...
     v.setOnClickListener(view -> {
        transformer.addTransition(generateThumbnailId(itemPosition), R.id.targetViewId);
        activity.viewPager.setCurrentItem(2);
    }
```

AuxiliarySePageTransformer doesn't require View Pager to occupy whole screen. 
It works only with Image Views and relative root layout at the moment.  
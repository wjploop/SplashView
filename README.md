#3 继承ViewGroup自定义的View，来模仿一个简化的ViewPager

主要技术实现：
* `GeastureDetector`监听滑动事件，实现滑动功能，监听Fling事件，自动翻页
* 使用了`mScroller`来实现平滑滚动，滑动回弹
* 暴露一个onPageScrollListener接口，暴露滚动事件


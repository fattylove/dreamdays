 2014-12-30
 1、修复widget入口问题-进主页
 2、增加播放声音的光感应效果
 3、增加Home页面长按删除
 4、switch点击区域放大
 5、feedback 标题修改
 6、增加Home页面Delete模式切换
 7、长按删除
 
 
 
 2014-12-31
 1、录音逻辑与IOS一致
 
 
 
 2015-1-4
 1、Widget数据优化与刷新
 2、文案整理
 3、AlphaImageView，event为1的时候进入详情崩溃
 
 
 
 2015-1-7
 1、修复录音时间过短闪退的bug
 2、Add event和 edit event设置backgroud layout线的问题
 3、增加了Lite2.0.0版本
 
 
 2015-1-8
 1、优化wallpaper缓存问题
 2、优化拍照图片压缩
 3、OOM问题解决
 		options.inPreferredConfig = Bitmap.Config.RGB_565; 
		options.inPurgeable = true;  
		options.inInputShareable = true;  
		
		final int minSideLength = Math.min(Constant.width, Constant.height);
		options.inSampleSize = computeSampleSize(options, minSideLength , Constant.width * Constant.height);
		
		
		
2015-1-9
1、Category分类删除的bug
2、Iap频繁点Done导致Crash
3、Home页面列表无Events，滑动事件失效		
4、录音问题导致back键错误



2015-1-12
1、修复通知进入无背景图的Bug
2、通知与Repeat时间均修改为9：30提醒



2015-1-16
1、no cover event上无事件去掉黑色大饼
2、修改部分字体
3、缩小主页按钮
4、Wallpaper列表展示
5、分享取图的时候，隐藏top上的4个button
6、Iap更新新版lib
7、全局异常处理



2015-1-19
1、主页面TitleBar的背景色根据CoverEvent平均RGB来选择
2、2.0.1版本以上的图片压缩处理
3、在编辑页面取消ProgressBar - 计算图片平均RGB算法放在主线程



2015-1-20
1、修复详情页面因滑动误操作录音按钮，导致的Nullpoint异常
2、恢复限免功能
3、增加4.0 - 5.0之间的Dialog



2015-1-21
1、修改了Sign in 、Sign up里面的字体
2、修改所有title的字体
3、Switch适配5.0(取消)
4、添加cover event点Done导致国产机屏幕卡死问题








	
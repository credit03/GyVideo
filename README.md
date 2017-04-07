# GyVideo
一个纯属为了学习MVP+RXJAVA+Retrofit开发播放电影软件
### 参考 微影，一款纯粹的在线视频App，基于Material Design + MVP + RxJava + Retrofit + Realm + Glide 开发
https://github.com/GeekGhost/Ghost
### 界面方面直接复制原作者的界面
### 把作者RxJava1升级为RxJava2，EventBus修改为RxBus2，添加电视播放，删除作者的侧滑菜单等等

 ![image](https://github.com/credit03/GyVideo/blob/master/gif/vi.gif)
 
 
 
由于fm.jiecao:jiecaovideoplayer:5.5.2 底层修改使用android提供的媒体播放器，5.5.2 对rtsp视频不兼容，播放电影有问题
而fm.jiecao:jiecaovideoplayer:4.7.1_preview  基于ijkplayer, 支持hls,rtsp
但不支持上一次播放位置记录，也不支持调节亮度，所以修改4.7.1_preview，添加位置记录，调节亮度功能

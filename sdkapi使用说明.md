#SDK接入说明

* [一. SDK接入配置](#100)
* [二. SDK api说明](#101)
	* [1. 实例SDK接口IStarpy对象](#1)
	* [2. Activity生命周期调用](#2)
	* [3. 初始化sdk](#3)
	* [4. 设置SDK语言版本](#4)
	* [5. 设置角色信息](#5)
	* [6. 登录接口](#6)
	* [7. 充值接口](#7)
	* [8. 客服接口](#8)
	* [9. SDK客户端分享](#9)
	* [10. SDK网页分享活动](#10)
	* [11. 打开平台页面](#11)
	* [12. Google play game services接口](#12)
		* [12.1 Google game services成就解锁](#12.1)
		* [12.2 Google game services显示成就](#12.2)
		* [12.3 Google game services提交排行榜分数](#12.3)
		* [12.4 Google game services显示排行榜](#12.4)
	* [13. Firebase接入](#13)
		* [13.1 首先，向您工程的根级 build.gradle 文件添加规则，以纳入 google-services 插件和 Google 的 Maven 代码库](#13.1)
		* [13.2 在您的APP模块 Gradle 文件（通常是 app/build.gradle）中，在文件的最底部添加apply plugin 代码行，以启用 Gradle 插件](#13.2)
		* [13.3 添加Firebase配置的google-services.json文件，将此文件复制到项目的模块module文件夹（通常是 app/）](#13.3)
		


----------------

* <h2 id="100">SDK接入配置</h2> 
说明：由于Google 2015年6月不再支持Eclipse，https://developer.android.com/studio/tools/sdk/eclipse-adt.html
，Android 24之后的版本SDK和Google play servcie库，google support v4 v7库，还有很多第三方库都已经不支持Eclipse，固本SDK不支持Eclipse，请使用AndroidStudio接入。

	*******SDK接入demo请查看https://github.com/gan00000/StarpySdkDemo**********
	
	*******SDK接入demo请查看https://github.com/gan00000/StarpySdkDemo**********


* 在您的项目中，打开 your_app | Gradle Scripts | build.gradle (Project) 并添加以下位置添加下面的代码，以便从maven仓库下载 SDK：

	```
	//添加以下配置
	allprojects {
	    repositories {
	        jcenter()
	        google()
	        maven {
	            url "http://maven.starpy168.com:8081/repository/maven-public/"
	            credentials {
	                username "cp_dev"
	                password "cpdev1688"
	            }
	        }
	    }
	}
		
	```
* 在您的项目中，打开 your_app | Gradle Scripts | build.gradle (Module: app) 并添加以下编译语句，以便编译SDK:

	```
	configurations.all {
	    // 采用动态版本声明的依赖缓存 时间
	    resolutionStrategy.cacheDynamicVersionsFor 2*60, 'seconds'
	}

	dependencies {
	    compile fileTree(include: ['*.jar'], dir: 'libs')
	
	    //SDK   添加以下配置以依赖SDK,具体版本对接的时候与SDK人员沟通确定
	    compile 'com.starpy.sdk:starpy-sdk:2.+'

	}		
	```
* 添加游戏配置文件，在您的项目中，打开 your_app | assets下创建starpy目录并添加 starpy\_game\_config 配置文件（该文件SDK对接人员提供）

* 在您的项目中，打开 your_app | Gradle Scripts | build.gradle 中productFlavors内添加以下配置，以动态设置游戏配置

	```
	productFlavors {

        xxx {
            minSdkVersion 18
            applicationId 'xxxx'   //值我方提供
            signingConfig signingConfigs.brmmdsign //签名我方提供
            targetSdkVersion 22  //值我方提供
            versionCode 1
            versionName '1.0'
            flavorDimensions "1"

            resValue "string", "facebook_app_name", "xxx"  //值我方提供
            resValue "string", "facebook_app_id", "xxx"  //值我方提供
            resValue "string", "facebook_authorities", "xxxx"  //值我方提供
            resValue "string", "google_app_id", "xxxx"  //值我方提供
            
            ndk {
                abiFilters "armeabi", "armeabi-v7a", "x86"  // 根据游戏so指定要ndk需要兼容的架构(游戏的so有什么类型就设置什么)
            }

        }

    }
	```
------------------------------

<h2 id="101">SDK api说明</h2> 
以下为sdk api使用示例,具体请查看SDK demo 

* <h3 id="1">实例SDK接口IStarpy对象</h3>  
`iStarpy = StarpyFactory.create(); ` 
 
* <h3 id="2">Activity生命周期调用</h3> 

	```java
	游戏Activity以下相应的声明周期方法（必须调用）:  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		iStarpy = StarpyFactory.create();
	    //初始化sdk
	    iStarpy.initSDK(this);
		//在游戏Activity的onCreate生命周期中调用
	    iStarpy.onCreate(this);
	}
   @Override
    protected void onResume() {
        super.onResume();
        PL.i("activity onResume");
        iStarpy.onResume(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        iStarpy.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        iStarpy.onPause(this);
        PL.i("activity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        PL.i("activity onStop");
        iStarpy.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PL.i("activity onDestroy");
        iStarpy.onDestroy(this);
    } 
    
     @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        iStarpy.onWindowFocusChanged(this,hasFocus);
    }
    
     /**
     * 6.0以上系统权限授权回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { 
    	super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      	PL.i("activity onRequestPermissionsResult");
      	iStarpy.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }  
```
 
* <h3 id="3">初始化sdk</h3>
```
	游戏启动的时候调用，通常在游戏activity oncreate中调用
	
	iStarpy.initSDK(this);
```

* <h3 id="4">设置SDK语言版本</h3> 
	
 ```
 根据游戏的语言，设置相应的SDK语言，默认为繁体中文
 游戏activity oncreate中调用
 /*
    * SGameLanguage.zh_TW 游戏为繁体语言时使用
    * SGameLanguage.en_US  游戏为英文语言时使用
    * */
    iStarpy.setGameLanguage(this, SGameLanguage.zh_TW);
    ```
	
* <h3 id="5">设置角色信息</h3> 


 ```
 在游戏获得角色信息的时候调用，每次登陆，切换账号等角色变化时调用（必须调用）
 
	/**
     * 在游戏获得角色信息的时候调用
     * roleId 角色id
     * roleName  角色名
     * rolelevel 角色等级
     * severCode 角色伺服器id
     * serverName 角色伺服器名称
     */
iStarpy.registerRoleInfo(this, "roleid_1", "roleName", "rolelevel", "s1001", "serverName");
```

* <h3 id="6">登录接口</h3>  

 ```
//登陆接口 ILoginCallBack为登录成功后的回调
iStarpy.login(MainActivity.this, new ILoginCallBack() {
        @Override
        public void onLogin(SLoginResponse sLoginResponse) {
            if (sLoginResponse != null){
                String uid = sLoginResponse.getUserId();
                String accessToken = sLoginResponse.getAccessToken();
                String timestamp = sLoginResponse.getTimestamp();

                PL.i("uid:" + uid);

            }
        }
    });
```

* <h3 id="7">充值接口</h3>    

 ```
/*
充值接口
SPayType SPayType.OTHERS为第三方储值，SPayType.GOOGLE为Google储值
cpOrderId cp订单号，请保持每次的值都是不会重复的
productId 充值的商品id
roleLevel 角色等级
customize 自定义透传字段（从服务端回调到cp）
注意：设置SPayType.OTHERS为第三方储值时不需要传cpOrderId，productId，customize，传了会被忽略掉
*/
iStarpy.pay(MainActivity.this, SPayType.GOOGLE, “cpOrderId”, "productId", "roleLevel", "customize");
```

* <h3 id="8">客服接口</h3>  

 ```
/**
 * 打开客服接口
 * level：游戏等级
 * vipLevel：vip等级，没有就传空字符""
 */
iStarpy.cs(MainActivity.this,"level","vipLevel");
```

* <h3 id="9">SDK客户端分享</h3>

 ```
//下面的参数请按照实际传值
    
    String shareUrl = "http://www.starb168.com/brmmd_201703171125.html";
      //分享回调
    ISdkCallBack iSdkCallBack = new ISdkCallBack() {
        @Override
        public void success() {
            PL.i("share  success");
        }

        @Override
        public void failure() {
            PL.i("share  failure");
        }
    };

    iStarpy.share(MainActivity.this,iSdkCallBack,shareUrl);
 ```
 
* <h3 id="10">SDK网页分享活动</h3>

 ```
 该功能按需求接入
/**
 * 打开一个活动页面接口
 * level：游戏等级
 * vipLevel：vip等级，没有就写""
 */  
 
 iStarpy.openWebview(MainActivity.this,"roleLevel","vipLevel");
 ```
 
* <h3 id="11">打开平台页面</h3>

 ```
/**
 * 打开一个SDK内置平台
 * level：游戏等级
 * vipLevel：vip等级，没有就写""
 */  
 
 iStarpy.openPlatform(MainActivity.this,"roleLevel","vipLevel");
 ```
 
* <h3 id="12">Google play game services接口</h3>

1. <h5 id="12.1">Google game services成就解锁</h5>
 
 ```
 public void unlockAchievement(String achievementID);
 如下
 /**
	 * 解锁成就
	 * 参数：
	 * 成就 id
	 */
	iStarpy.unlockAchievement("CgkIq8GizdAREAIQAA");
 ```
 
2. <h5 id="12.2">Google game services显示成就</h5>
 
 ```
 public void displayingAchievements();
 如下
  /**
     * 显示成就
     */
    iStarpy.displayingAchievements();
 ```
 
3. <h5 id="12.3">Google game services提交排行榜分数</h5>
 
 ```
 public void submitScore(String leaderboardID, long score);
  如下
  /**
     * 提交排行榜成绩
     *
     * 参数：
     *  排行榜id
     *  成绩分数
     */
    iStarpy.submitScore("CgkIq8GizdAREAIQHg",10l);
 ```
 
4. <h5 id="12.4">Google game services显示排行榜 </h5>

 ```
 public void displayLeaderboard(String leaderboardID);
 如下
  /**
     * 显示排行榜
     * 参数：
     *  排行榜id
     */
    iStarpy.displayLeaderboard("CgkIq8GizdAREAIQHg");
 ```
 
* <h3 id="13">Firebase接入</h3>
改功能是否需要接入请与对接童鞋沟通(一般是需要的)

1. <h5 id="13.1">首先，向您项目的根级 build.gradle 文件添加规则，以纳入 google-services 插件和 Google 的 Maven 代码库</h5>
 
 ```
buildscript {
  
    dependencies {
        // google-services   plugin
        classpath 'com.google.gms:google-services:3.1.1' 
    	}
}
allprojects {
   
    repositories {
        maven {
	        	// Google's Maven repository
	            url "https://maven.google.com"      
            }
     	}
}

 ```
 
2. <h5 id="13.2">在您的APP模块 Gradle 文件（通常是 app/build.gradle）中，在文件的最底部添加apply plugin 代码行，以启用 Gradle 插件</h5>
 
 ```
 // ADD THIS AT THE BOTTOM
	apply plugin: 'com.google.gms.google-services'
 ```
 
3. <h5 id="13.3">添加Firebase配置的google-services.json文件，将此文件复制到项目的模块module文件夹（通常是 app/）</h5>
	注意：google-services.json文件由我方对接人员提供










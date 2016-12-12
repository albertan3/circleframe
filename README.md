##Android Social Share Library

An Android library providing Social Share through intent feature (Support API 19+)

========
### Set up
There are two ways to import this library into your android project: you can either directly import SocialShareLib.aar file or import the whole authLib package (with source code) from this module

#### Import .aar file
* Download [SocialShareLib.aar](https://github.com/visa-innovation-sf/android-components/blob/dev/Libraries/SocialShareLib.aar)
* In Android Studio, choose File->New->New Module, then choose import new .JAR/.AAR package and select/import SocialShareLib.aar 
* In your app Gradle file, add following line to the dependencies
```gradle
compile project(':SocialShareLib')
```

#### Import SocialShareLib module with source code
* checkout this repo and locate SocialShareLib module
* In Android Studio, choose File->New->Import Module and select SocialShareLib folder to import
* In your app Gradle file, add following line to the dependencies
```gradle
compile project(':SocialShareLib')
```
========
### Sample App
An example of integration with this library can be found at SampleApp module in this repo

========
### Features
#### Sharing text/image from your app to multiple Social Platforms
This library supports sharing text/image content to social platforms with Android OS default chooser dialog or a predefined custom dialog. You can also supply custom social platforms to share.

========
### Initialize
You need to initialize an instance of ShareManager class and set a proper context to use this library. 
```Java
ShareManager shareManager = new ShareManager().setContext(Your Context);
```

=======
### Provide content you want to share 
You can provide text and image content you want to share to ShareManager.
#### Provide text content
You can provide title, content, and subject by doing:
```Java
new ShareManager().setContext(this)
                        .setContent("Your Text Content")
                        .setSubject("Your Text Subject")
                        .setTitle("Your Text Title")
                        ......
```
#### Provide image content
You need to provide the image Uri to share the image
```Java
new ShareManager().setContext(this)
                  ......
                  .setUri(Your Image Uri)
                  ......
```
=======
### Share your content
Sharing your content has never been this easy by doing the following:
```Java
new ShareManager().setContext(this)
                  ......
                  .share();
```
=======
### Customization
#### Use different chooser dialogs
You can choose to use Android OS default chooser dialog or our custom share dialog by calling useDefaultDialog(boolean b) method. 
By default, the library uses our custom dialog, you can choose to use OS default dialog by doing:
```Java
new ShareManager().setContext(this)
                  ......
                  .useDefaultDialog(true)
                  ......
```
You can choose use our custom dialog by doing:
```Java
new ShareManager().setContext(this)
                  ......
                  .useDefaultDialog(false)
                  ......
```
#### Provide different target apps to share
By default, the library will try to share your content to the following apps:
##### Twitter, Facebook, Gmail, Samsung Email, Google Message, Android Native Messager #####
You can choose a subset of above apps from ShareProvider interface and pass your custom list of share providers:
```Java
//Only share your content to Twitter, gmail and facebook
List<String> mShareProviders = new ArrayList<>();
mShareProviders.add(ShareProvider.TWITTER);
mShareProviders.add(ShareProvider.GMAIL);
mShareProviders.add(ShareProvider.FACEBOOK);
new ShareManager().setContext(this)
                  ......
                  .setShareProviders(mShareProviders)
                  .share();
```

You can also provide other apps (not defined in ShareProvider) or your own activities as sharing target. Simply add the related package names to your share providers list:
```Java
//share your content to your own activity and other apps
List<String> mShareProviders = new ArrayList<>();
......
mShareProviders.add("Your activity package name");
mShareProviders.add("other apps package name");
......
new ShareManager().setContext(this)
                  ......
                  .setShareProviders(mShareProviders)
                  .share();
```

=======
### Get Uri of your Drawable object
We also provide a utility class FileManager to help you get the Uri of your own Drawable. If your target Android API is 22+, you need to obtain Storage Permission before getting the Uri.
#### Use FileManager to get Uri
```Java
Bitmap bmp = ((BitmapDrawable) yourDrawable).getBitmap();
Uri uri = FileManager.getBitmapUri(this, bmp);
```
#### Obtain Storage Permission before getting Uri on Android 22+
You need either WRITE_EXTERNAL_STORAGE or READ_EXTERNAL_STORAGE permisson. For how to obtain runtime permission on Android 6+, please refer to [Requesting Permissions at Run Time](https://developer.android.com/training/permissions/requesting.html)


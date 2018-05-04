# RxMoreDemo_Kotlin
## MVP+Retrofit2+Rxjava2+RxLifecycle2.0<br/>

### Rxjava2为什么会造成内存泄漏？<br/>
一句话描述：使用RxJava发布一个 Subscribe（订阅）后，当页面被finish()，此时订阅逻辑还未完成，如果没有及时取消订阅，就会导致Activity/Fragment无法被回收，从而引发内存泄漏。
<br/>
### 当出现了内存泄漏怎么解决？<br/>
1. 使用trello公司提供的RxLifecycle来解决rxjava所造成的内存泄漏。<br/>
2. Rxlifecycle是通过绑定生命周期的方式，解决内存泄漏的问题。<br/>
### 开始使用<br/>
1. 首先要导入我们所需要的库 所使用的androidstudio版本是 3.0。<br/>
2. rxlifecycle Github地址 最新的版本在这里找 https://github.com/trello/RxLifecycle <br/>
3. rxjava Github地址 最新版本 https://github.com/ReactiveX/RxJava <br/>
4. retrofit GitHub 版本 https://github.com/square/retrofit <br/>
5. 导入库的结构如下所示：使用的rxjava版本与rxlifecycle版本必须要对应 1.0 配1.0的，2.0配2.0 
不能把1.0的版本配置到2.0里面去。<br/>

app->build<br/>
```Java
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'
apply plugin: 'kotlin-android-extensions'

androidExtensions {
    experimental = true
}

android {
    compileSdkVersion 27
    defaultConfig {
        applicationId "com.rx.rxmore"
        minSdkVersion 19
        targetSdkVersion 27
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'com.android.support:appcompat-v7:27.1.1'
    implementation 'com.android.support.constraint:constraint-layout:1.1.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'

    api "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    // Anko Commons
    api 'org.jetbrains.anko:anko-commons:0.10.1'
    // Support-v4 (only Anko Commons)
    api 'org.jetbrains.anko:anko-support-v4-commons:0.10.1'

    // RxKotlin and RxAndroid
    api 'io.reactivex.rxjava2:rxjava:2.1.13'
    api 'io.reactivex.rxjava2:rxandroid:2.0.2'
  

    // If you want to use Kotlin syntax
    api 'com.trello.rxlifecycle2:rxlifecycle-kotlin:2.2.1'
    // If you want pre-written Activities and Fragments you can subclass as providers
    api 'com.trello.rxlifecycle2:rxlifecycle-components:2.2.1'


    //Retrofit
    api "com.squareup.okhttp3:okhttp:3.10.0"
    api 'com.squareup.okhttp3:logging-interceptor:3.10.0'
    api 'com.squareup.retrofit2:retrofit:2.3.0'
    api 'com.squareup.retrofit2:converter-gson:2.3.0'
    api 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
}


```
还需要添加以下的配置<br/>
项目->build
```Java
buildscript {
    ext.kotlin_version = '1.2.41'
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.2'
        classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.41'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```
着手网络层 <br/>
首先要配置Reftrofit ：
```Kotlin
class RetrofitFactory private constructor(){

    companion object {
        val instance:RetrofitFactory by lazy { RetrofitFactory() }
    }

    private val retrofit:Retrofit
    private val interceptor: Interceptor

    init {
        interceptor = Interceptor {
            chain -> val request = chain.request()
                .newBuilder()
                .addHeader("Content_Type","application/json")
                .addHeader("charset","UTF-8")
                .addHeader("token","111")
                .build()
            chain.proceed(request)
        }

        //Retrofit实例化
        retrofit = Retrofit.Builder()
                .baseUrl(BaseConstant.SERVER_ADDRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(initClient())
                .build()
    }

    /*
        OKHttp创建
     */
    private fun initClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(initLogInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
    }

    /*
       日志拦截器
    */
    private fun initLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    /*
        具体服务实例化
     */
    fun <T> create(service:Class<T>):T{
        return retrofit.create(service)
    }
}
```
配置Rxjava2 公共装载 这里面compose就是用来管理
```Java
public class ObjectLoader {
    protected <T> Observable<T> observeat(Observable<T> observable,LifecycleProvider<ActivityEvent> lifecycleProvider){
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                //绑定生命周期
                .compose(lifecycleProvider.<T>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread()); //指定在主线程中
    }

    protected <T> Observable<T> observefg(Observable<T> observable, LifecycleProvider<FragmentEvent> lifecycleProvider){
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .compose(lifecycleProvider.<T>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread()); //指定在主线程中
    }
}

```

上面配置完成后方可以写代码MVP代码 <br/>
MVP的写法有很多种，我也是参考泓洋大神的文章来写的，可能稍微有些改动。 <br/>
谷歌官方的MVP感觉有点啰嗦所以取容易自己理解简单的方式来写。<br/>
由于很多数据都是共用的 所以必须要建立base类<br/>
Activity继承于RxAppCompatActivity<br/>
```Java
open class BaseActivity:RxAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
```
BaseMVP类
```Java
    abstract class BaseMvpActivity<T:BasePresenter<*>>:BaseActivity(),BaseView,View.OnClickListener {

    lateinit var mPresenter: T

    lateinit var progressLoading:ProgressLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        setContentView(getLayoutResID())
        initView()
        mPresenter = PMUtils.getT(this,0)!!
        //mPresenter.mView = this

        progressLoading = ProgressLoading.create(this)

    }


    abstract fun getLayoutResID():Int

    protected open fun initView() {

    }

    override fun showLoading() {
        progressLoading.showLoading()
    }

    override fun hideLoading() {
        progressLoading.hideLoading()
    }

    override fun onError(text: String) {
        toast(text)
    }

    override fun onClick(v: View?) {

    }

}



```
建立BasePresenter
```Java

open class BasePresenter<T:BaseView> {

    lateinit var mView:T

    lateinit var lifeProvider: LifecycleProvider<*>

    fun checkNetWork():Boolean{
        if (NetWorkUtils.isNetWorkAvailable(MainApplication.context)) {
            return true
        }
        mView.onError("网络不可用")
        return false
    }

}
```
建立BaseView层
```Java
interface BaseView{
    fun showLoading()
    fun hideLoading()
    fun onError(text:String)
}
```
以登录为Demo<br/>

LoginView层
```Java
interface UserView : BaseView {
    //获取成功
    fun onGetUserResult(user: User)
}
```
LoginPresenter
```Java
class UserPresenter:BasePresenter<UserView>() {

    private lateinit var userRepository: UserRepository

    fun getUser(phone: String, password: String, pushid: String){
        userRepository = UserRepository()

        if (!checkNetWork()) {
            return
        }
        mView.showLoading()

        userRepository.getUser(phone,password,pushid,lifeProvider).subscribe(object :BaseObserver<User>(mView){
            override fun onNext(t: User) {
                mView.onGetUserResult(t)
            }
        })


    }
}
```

LoginActivity
```Java
class LoginActivity :BaseMvpActivity<UserPresenter>(),UserView{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        mPresenter.mView = this
        mPresenter.lifeProvider = this
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_login
    }

    override fun initView(){
        mLogin.setOnClickListener{
            getUser()
        }
    }

    private fun getUser() {
        mPresenter.getUser(mPhoneEt!!.text.toString(), mPwdEt!!.text.toString(), "111")
    }


    override fun onGetUserResult(user: User) {
        if(user!=null) {
            startActivity<MainActivity>("User" to user)
        }
    }
}
```

以上代码只是一个简单的流程,具体过程看项目里面的代码。<br/>
谢谢你的Star...







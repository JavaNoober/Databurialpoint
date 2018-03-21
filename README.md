# Databurialpoint
android数据埋点实现
### 功能需求
一个项目实现之后，我们并不知道用户对某个部分的使用频率是对少，为了更好的来对项目各个功能的使用统计，我们需要做一些**数据埋点**的功能，也就是每当用户点击按钮的时候，都对这次点击进行保存处理，然后再之后统一上传到服务器，进行数据分析。
### 实现思路
#### 条件
假如，当前有两个方法进行数据埋点：登录和注册。
功能表的数据结构如下：**功能id、操作次数、操作人id**。  

    public class FunctionsTable {

    	//功能Id
    	private long functionId;
    	//操作次数
    	private int operateCounts;
    	//操作用户Id
    	private long operatorId;
    
    	public long getFunctionId() {
    		return functionId;
    	}
    
    	public void setFunctionId(long functionId) {
    		this.functionId = functionId;
    	}
    
    	public int getOperateCounts() {
    		return operateCounts;
    	}
    
    	public void setOperateCounts(int operateCounts) {
    		this.operateCounts = operateCounts;
    	}
    
    	public long getOperatorId() {
    		return operatorId;
    	}
    
    	public void setOperatorId(long operatorId) {
    		this.operatorId = operatorId;
    	}
    }
下载我们需要做的就是，每次用户点登陆和注册的时候，把数据库中的operateCount字段每次+1。

#### 分析
首先，最直接的实现方法也就是封装一个保存数据的数据库静态操作方法，然后将每次需要操作的地方传入functionId，来进行操作。  
这种方法看似可行，但是实际操作的时候会发现很可能需要修改原来的代码结构，同时，一旦function的数量到达一定量以后，这时候产品告诉我们，需求要改，还需要加入额外的操作，这时候一旦运气不好，比如增加一个方法参数，所有调用方法地方都得修改一遍。这时候就比较麻烦了。   
有没有什么更好的方法？肯定是有的。首先分析上述方案，它是一个单一重复的调用过程，唯一的区别就是传入的functionId，这是一个**单一却又重复**的操作，那很明显，aop思想来解决这种问题是最好的。  

### 具体实现
首先我们选用aop一个常用的类库：aspectj。因此这里我们通过字节码插桩的方式，修改编译之后的class来进行代码的自动生成,这样就不会对我们敲代码的逻辑产生任何影响。

#### 代码原逻辑

非常简单，就是两个点击事件，模拟一下登陆注册的点击。

    public class MainActivity extends AppCompatActivity {
    
    	@Override
    	protected void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setContentView(R.layout.activity_main);
    		Button btnLogin = findViewById(R.id.btn_login);
    		Button btnRegister = findViewById(R.id.btn_register);
    
    		btnLogin.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				Log.e("Statistics", "登陆");
    			}
    		});
    
    		btnRegister.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				Log.e("Statistics", "注册");
    			}
    		});
    	}
    }
    
#### aspectJ字节码插桩的实现

1.新建一个枚举类来定义需要埋点的功能:登陆、注册


    public enum Function {
    
    	LOGIN(1, "登陆"),
    	REGISTER(2, "注册");
    
    	int functionId;
    	String functionName;
    
    	Function(int functionId, String functionName) {
    		this.functionId = functionId;
    		this.functionName = functionName;
    	}
    
    	public String getFunctionName() {
    		return functionName;
    	}
    }
    
2.新建一个注解类来标记需要统计点击次数的方法：  
使用的时候只需要将需要统计的方法加上注解即可

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Statistics {
        Function function();
    }

3.Aspectj的简单实现：

        @Aspect
        public class StatisticsInstrumentation {

    	    public static final String TAG = "Statistics";
    
        	@Around("execution(@com.noob.databurialpoint.Statistics * *(..)) && @annotation(statistics)")
        	public void aroundJoinPoint(ProceedingJoinPoint joinPoint, Statistics statistics) throws Throwable {
        		calculate(statistics);
        		joinPoint.proceed();//执行原方法
        	}
    
            private void calculate(Statistics statistics){
            	if(statistics != null){
            		Log.e(TAG, "对" + statistics.function().getFunctionName() + "进行统计");
            		// select * from FunctionsTable where operatorId=statistics.getFunctionId()
            		//if(size > 0){
            		// int counts = operateCounts ++
            		// update FunctionsTable set operateCounts = counts
            		// }else {
            		// insert into FunctionsTable values (xxx, statistics.getFunctionId(), 1)
            		// }
            	}
            }
    }

代码解释：

    @Around("execution(@com.noob.databurialpoint.Statistics * *(..)) && @annotation(statistics)")
    
 * com.noob.databurialpoint.Statistics是Statistics注解的具体包名
 * @annotation(statistics) 代表执行方法中传入注解参数,才能再aroundJoinPoint方法里获取这个注解对象
 * calculate是点击统计的伪代码

#### 注解使用
     public class MainActivity extends AppCompatActivity {
    
    	@Override
    	protected void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		setContentView(R.layout.activity_main);
    		Button btnLogin = findViewById(R.id.btn_login);
    		Button btnRegister = findViewById(R.id.btn_register);
    
    		btnLogin.setOnClickListener(new View.OnClickListener() {
    			@Override
    			@Statistics(function = Function.LOGIN)
    			public void onClick(View v) {
    				Log.e("Statistics", "登陆");
    			}
    		});
    
    		btnRegister.setOnClickListener(new View.OnClickListener() {
    			@Override
    			@Statistics(function = Function.REGISTER)
    			public void onClick(View v) {
    				Log.e("Statistics", "注册");
    			}
    		});
    	}
    }
    
测试结果:


![](https://user-gold-cdn.xitu.io/2018/3/21/1624745b80eaea83?w=1449&h=861&f=gif&s=263250)


相信大家也都看到了，我们根本没有对之前的方法进行修改，唯一的区别就是在调用方法上添加一了一个
@Statistics注解，如果我们需要修改逻辑，也只需要修改一次aspectJ的实现类StatisticsInstrumentation即可，这样就开发的时候就非常方便。  
究其原因是因为aspectj修改了MainActivity.class类，修改后编译生成的class代码如下：


![](https://user-gold-cdn.xitu.io/2018/3/21/162474a34ccfd106?w=1272&h=556&f=png&s=62362)

不再只是一个简单的log打印，而是回去调用我们额外写的StatisticsInstrumentation中的方法.这就是aspectj的作用。

### 总结
aspectj是一个很好的aop框架，此处只是aspectj的一个简单使用示例，关于更深入的用法这里就不再介绍，大家可以去网上寻找相关代码。  

上述demo地址：[https://github.com/JavaNoober/Databurialpoint](https://user-gold-cdn.xitu.io/2018/3/21/162474e1657636b3)

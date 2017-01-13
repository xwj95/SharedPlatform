#实验平台说明

###一、实验前准备工作
  
0、电脑端生成随机实验序列文件（尽量采用`UTF-8`编码格式），这一步可以事先完成，但请注意好对应关系，建议统一存放在`task`目录下

1、电脑端和手机端连接`Wi-Fi`（尽量连接同一`Wi-Fi`，强烈建议不要连接校园网）

2、手机端`adb`无线连接

+ 2.1、有线连接电脑
+ 2.2、`adb tcpip 5555`
+ 2.3、`adb connect [ip address]`
+ 2.4、断开有线连接

3、打开电脑端实验平台

+ 3.1、在`IntellJ IDEA CE`中编译运行项目
+ 3.2、点击`连接网络`按钮，并确保之前的实验平台已经关闭（否则会报错`端口被占用`）
+ 3.3、输入用户名，可以输入中文，但请确保不会重名
+ 3.4、载入实验序列文件
+ 3.5、选择手机类型（默认选择`小V`）

4、打开手机端实验平台

+ 4.1、输入电脑端实验平台提供的`IP`地址，如果该地址不正确，`Windows`系统请输入`ipconfig`，`Mac OS`系统请输入`ifconfig en0`，以获得正确的`IP`地址
+ 4.2、点击`连接`按钮，如果实验平台还没有创建服务器，请点击`重试`按钮，并重新点击`连接`按钮
+ 4.3、在电脑端实验平台，选择手机端的`IP`地址

5、开始实验

+ 5.1、点击`开始实验`按钮，如果报错，请检查之前的步骤是否有遗漏
+ 5.2、点击`下一步`按钮正式开始实验
+ 5.3、检查手机端后台服务是否正常运行

###二、实验时注意事项

1、请务必点击开始实验按钮后才开始实验

2、请务必听到手机端的震动声后才开始实验

3、如遇到`adb`断开（触摸数据采集失败），或者电容数据采集失败的情况（会弹出黑色弹窗提示），请务必重新当前任务（实验平台会强制要求重试）

4、如遇到`socket`断开的情况（会弹出黑色弹窗提示），请重新开始实验，但需要使用新的用户名，已做过的实验可以跳过，但数据需要手动合并

5、如果想要确认手机端数据记录是否正常，请执行`adb shell ls -l sdcard/ExpData/[user name]/[task id]`或`adb shell ls -l data/log/dmd_log`

###三、实验后收集工作

1、关闭电脑端和手机端的实验平台

2、手机端`adb`有线连接

+ 2.1、`adb disconnect`
+ 2.2、有线连接电脑（确保传输速率）

3、获取数据

+ 3.1、`adb pull sdcard/ExpData/[user name]`
+ 3.2、将下载的数据移动到`data`目录下

4、解析数据

+ 4.1、切换到`parser`目录下
+ 4.2、`Windows`系统请执行`parseAll.bat [user directory]`（或者把目录拖动到`bat`文件上），`Mac OS`系统请执行`./parseAll.sh [user directory]`
+ 4.3、解析后的数据存放在用户目录下的`parse`目录下，请检查是否有遗漏数据的情况

5、观察数据

+ 5.1、打开`huaweiParser.exe`，载入解析后的数据
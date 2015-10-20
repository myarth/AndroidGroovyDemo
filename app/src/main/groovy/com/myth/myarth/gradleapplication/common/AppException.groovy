//package com.myth.myarth.gradleapplication.common
//
//import android.os.Environment
//import groovy.transform.CompileStatic
//import org.apache.http.HttpException
//
//import java.lang.Thread.UncaughtExceptionHandler
//
//
//@CompileStatic
//class AppException extends Exception implements UncaughtExceptionHandler {
//
//    private static final boolean Debug = false //是否保存错误日志
//
//    /**
//     * 定义异常类型
//     */
//    static final byte TYPE_NETWORK = 0x01
//    static final byte TYPE_SOCKET = 0x02
//    static final byte TYPE_HTTP_CODE = 0x03
//    static final byte TYPE_HTTP_ERROR = 0x04
//    static final byte TYPE_XML = 0x05
//    static final byte TYPE_IO = 0x06
//    static final byte TYPE_RUN = 0x07
//
//    private byte type
//    private int code
//
//    /**
//     * 系统默认的UncaughtException处理类
//     */
//    private UncaughtExceptionHandler mDefaultHandler
//
//    private AppException() {
//        this.mDefaultHandler = Thread.defaultUncaughtExceptionHandler
//    }
//
//    private AppException(byte type, int code, Exception ex) {
//        super(ex)
//        this.type = type
//        this.code = code
//        if (Debug) {
//            saveErrorLog(ex)
//        }
//    }
//
//    /**
//     * 保存异常日志
//     */
//    void saveErrorLog(Exception ex) {
//        try {
//            //判断是否挂载了SD卡
//            def logFilePath = ''
//            if (Environment.externalStorageState == Environment.MEDIA_MOUNTED) {
//                def savePath = "${Environment.externalStorageDirectory.absolutePath}/OSChina/Log/"
//                def file = new File(savePath)
//                if (!file.exists()) {
//                    file.mkdirs()
//                }
//                logFilePath = "${savePath}errorlog.txt"
//            }
//
//            //没有挂载SD卡，无法写文件
//            if (!logFilePath) {
//                return
//            }
//
//            def logFile = new File(logFilePath as String)
//            def logs = [
//                    "--------------------" + (new Date().toString()) + "---------------------",
//                    ex.stackTrace.join('\n')
//            ]
//            logFile.append("${logs.join('\n')}\n", Constants.UTF_8)
//        } catch (Exception e) {
//            e.printStackTrace()
//        }
//    }
//
//    static AppException http(int code) {
//        new AppException(TYPE_HTTP_CODE, code, null)
//    }
//
//    static AppException http(Exception ex) {
//        new AppException(TYPE_HTTP_ERROR, 0, ex)
//    }
//
//    static AppException socket(Exception ex) {
//        new AppException(TYPE_SOCKET, 0, ex)
//    }
//
//    static AppException io(Exception ex) {
//        if (ex instanceof UnknownHostException
//                || ex instanceof ConnectException) {
//            return new AppException(TYPE_NETWORK, 0, ex)
//        } else if (ex instanceof IOException) {
//            return new AppException(TYPE_IO, 0, ex)
//        }
//        run(ex)
//    }
//
//    static AppException xml(Exception e) {
//        new AppException(TYPE_XML, 0, e)
//    }
//
//    static AppException network(Exception e) {
//        if (e instanceof UnknownHostException || e instanceof ConnectException) {
//            return new AppException(TYPE_NETWORK, 0, e)
//        } else if (e instanceof HttpException) {
//            return http(e)
//        } else if (e instanceof SocketException) {
//            return socket(e)
//        }
//        http(e)
//    }
//
//    static AppException run(Exception e) {
//        new AppException(TYPE_RUN, 0, e)
//    }
//
//    /**
//     * 获取APP异常崩溃处理对象
//     */
//    static AppException getAppExceptionHandler() {
//        new AppException()
//    }
//
//    @Override
//    void uncaughtException(Thread thread, Throwable ex) {
//        if (!handleException(ex) && mDefaultHandler != null) {
//            mDefaultHandler.uncaughtException(thread, ex)
//        }
//
//    }
//
//    /**
//     * 自定义异常处理:收集错误信息&发送错误报告
//     *
//     * @return true:处理了该异常信息否则返回false
//     */
//    private boolean handleException(Throwable ex) {
//        if (ex == null) {
//            return false
//        }
//        println(ex.message)
//
////        def buf = ex.stackTrace.inject(new StringBuffer()) { buf, trace ->
////            buf.append("${trace}\n" as String)
////        }
////        println(buf)
////
////        def context = AppManager.instance.currentActivity()
////        if (!context) {
////            return false
////        }
//
////        // 显示异常信息 & 发送报告
////        final String crashReport = getCrashReport(context, ex)
////        new Thread() {
////            void run() {
////                Looper.prepare()
////                sendAppCrashReport(context, crashReport)
////                Looper.loop()
////            }
////        }.start()
//
//        true
//    }
//
//    /**
//     * 发送App异常崩溃报告
//     */
//    /*static void sendAppCrashReport(final Context cont, final String crashReport)
//	{
//		AlertDialog.Builder builder = new AlertDialog.Builder(cont)
//		builder.setIcon(android.R.drawable.ic_dialog_info)
//		builder.setTitle(R.string.app_error)
//		builder.setMessage(R.string.app_error_message)
//		builder.setPositiveButton(R.string.submit_report, new DialogInterface.OnClickListener() {
//			void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss()
//				//发送异常报告
//				Intent i = new Intent(Intent.ACTION_SEND)
//				//i.setType("text/plain") //模拟器
//				i.setType("message/rfc822")  //真机
//				i.putExtra(Intent.EXTRA_EMAIL, new String[]{"jxsmallmouse@163.com"})
//				i.putExtra(Intent.EXTRA_SUBJECT,"开源中国Android客户端 - 错误报告")
//				i.putExtra(Intent.EXTRA_TEXT,crashReport)
//				cont.startActivity(Intent.createChooser(i, "发送错误报告"))
//				//退出
//				AppManager.getAppManager().AppExit(cont)
//			}
//		})
//		builder.setNegativeButton(R.string.sure, new DialogInterface.OnClickListener() {
//			void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss()
//				//退出
//				AppManager.getAppManager().AppExit(cont)
//			}
//		})
//		builder.show()
//	}*/
//
//    /**
//     * 获取APP崩溃异常报告
//     */
//    /*private String getCrashReport(Context context, Throwable ex) {
//        PackageInfo pinfo = null
//        try {
//            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
//        } catch (NameNotFoundException e) {
//            e.printStackTrace(System.err)
//        }
//        if(pinfo == null) pinfo = new PackageInfo()
//        StringBuffer exceptionStr = new StringBuffer()
//        exceptionStr.append("Version: "+pinfo.versionName+"("+pinfo.versionCode+")\n")
//        exceptionStr.append("Android: "+android.os.Build.VERSION.RELEASE+"("+android.os.Build.MODEL+")\n")
//        exceptionStr.append("Exception: "+ex.getMessage()+"\n")
//        StackTraceElement[] elements = ex.getStackTrace()
//        for (int i = 0 i < elements.length i++) {
//            exceptionStr.append(elements[i].toString()+"\n")
//        }
//        return exceptionStr.toString()
//    }*/
//
//}

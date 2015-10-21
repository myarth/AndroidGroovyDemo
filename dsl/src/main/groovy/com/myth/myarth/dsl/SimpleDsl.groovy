package com.myth.myarth.dsl

import android.app.Activity
import android.content.Context
import groovy.transform.CompileStatic

@CompileStatic
class SimpleDsl {

    static void startActivity(Context self, Class<? extends Activity> activity) {
        self.startActivity(activity, {})
    }

    /**
     * 通过该扩展，为 T 类型添加可直接调用的方法。
     * 方法必须限定为 static, 因为最终方法会成为一个 Category method.
     * 实际使用时，需要确定 self 类型，否则会找不到该方法。
     *
     * @see /META-INF/services/org.codehaus.groovy.runtime.ExtensionModule
     *
     * extensionClasses 是扩展在实例上的方法
     * staticExtensionClasses 是扩展在类上的方法
     */
    static <T extends List> T simpleDsl(T self,
                                        @DelegatesTo(value = T, strategy = Closure.DELEGATE_FIRST)
                                                Closure closure = null) {
        build(self, closure) as T
    }

    private static Object build(object, Closure closure = null) {
        // clone(rehydrate) 是为了避免影响外部闭包原来的属性
        def clone = closure?.rehydrate(object, closure?.owner, closure?.thisObject)
        // clone 时将 delegate 设置为 object，因此 clone 优先使用 object 的方法和属性
        clone?.resolveStrategy = Closure.DELEGATE_FIRST
        clone?.call(object)
        object
    }

}

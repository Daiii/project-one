package cn.project.one.common.util;

public class ClassUtil {

    /**
     * 推断启动类
     * 
     * @return main.class
     */
    public static Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTraceElements = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("main class not found.");
        }
        return null;
    }
}

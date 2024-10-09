package top.yunmouren.electroncraft.Tools;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;


public class Log extends LogUtils {
    public static Logger logger = LogUtils.getLogger();
    public void info(String msg){
        logger.info(msg);
    }
    public void error(String msg){
        logger.error(msg);
    }
    public void warn(String s) {
        logger.warn(s);
    }
    public void debug(String line) {
        logger.debug(line);
    }
}

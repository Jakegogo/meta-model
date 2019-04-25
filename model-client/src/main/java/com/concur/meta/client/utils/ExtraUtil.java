package com.concur.meta.client.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * 对attribute处理的一个方法
 * @author huangshang
 * @version
 */
public class ExtraUtil {

    static final String SP = ";";
    static final String SSP = ":";
    static final String COMMA = ",";

    static final String R_SP = "#3A";
    static final String R_SSP = "#3B";
    static final String R_COMMA = "#3C";

    /**
     * 通过Map转换成String
     * @param attrs
     * @return
     */
    public static final String toString(Map<String, String> attrs) {
        StringBuilder sb = new StringBuilder();
        if (null != attrs && !attrs.isEmpty()) {
            sb.append(SP);
            Set<String> attrsKeys = new TreeSet<String>(attrs.keySet());
            for (String key : attrsKeys) {
                String val = attrs.get(key);
                if (isNotEmpty(val)) {
                    sb.append(encode(key)).append(SSP).append(encode(val)).append(SP);
                }
            }
        }
        return sb.toString();
    }


    /**
     * 通过字符串解析成attributes
     * @param str
     * @return
     */
    public static final Map<String, String> fromString(String str) {
        Map<String, String> attrs = new HashMap<String, String>();
        if (isNotBlank(str)) {
            String[] arr = str.split(SP);
            if (null != arr) {
                for (String kv : arr) {
                    if (isNotBlank(kv)) {
                        String[] ar = kv.split(SSP,2);
                        if (null != ar && ar.length == 2) {
                            String key = decode(ar[0]);
                            String val = decode(ar[1]);
                            if (isNotEmpty(val)) {
                                attrs.put(key, val);
                            }
                        }
                    }
                }
            }
        }
        return attrs;
    }

    /**
     * 字符串变成set
     * @param str
     * @return
     */
    public static Set<String> stringToSet(String str){
        Set<String> set = new HashSet<String>();
        if(isNotBlank(str)){
            String[] arr = str.split(COMMA);
            if(null != arr){
                for(String value : arr){
                    if(isNotEmpty(value.trim())){
                        set.add(value.trim());
                    }
                }
            }
        }
        return set;
    }

    /**
     * set变成字符串
     * @param set
     * @return
     */
    public static String setToString(Set<String> set){
        String str = null;
        if( null != set && !set.isEmpty() ){
            str = set.toString().replace("[", COMMA).replace("]", COMMA);

        }
        return str;
    }
    public static String encode(String val) {
        return replace(replace(replace(val, SP, R_SP), SSP, R_SSP), COMMA, R_COMMA);
    }

    public static String decode(String val) {
        return replace(replace(replace(val, R_SP, SP), R_SSP, SSP), R_COMMA, COMMA);
    }

    public static boolean isNotEmpty(String str) {
        return ((str != null) && (str.length() > 0));
    }

    public static boolean isNotBlank(String str) {
        int length;

        if ((str == null) || ((length = str.length()) == 0)) {
            return false;
        }

        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }
    /**
     * @param text 要扫描的字符串
     * @param repl 要搜索的子串
     * @param with 替换字符串
     *
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replace(String text, String repl, String with) {
        return replace(text, repl, with, -1);
    }
    /**
     * @param text 要扫描的字符串
     * @param repl 要搜索的子串
     * @param with 替换字符串
     * @param max maximum number of values to replace, or <code>-1</code> if no maximum
     *
     * @return 被替换后的字符串，如果原始字符串为<code>null</code>，则返回<code>null</code>
     */
    public static String replace(String text, String repl, String with, int max) {
        if ((text == null) || (repl == null) || (with == null) || (repl.length() == 0)
            || (max == 0)) {
            return text;
        }

        StringBuffer buf   = new StringBuffer(text.length());
        int          start = 0;
        int          end   = 0;

        while ((end = text.indexOf(repl, start)) != -1) {
            buf.append(text.substring(start, end)).append(with);
            start = end + repl.length();

            if (--max == 0) {
                break;
            }
        }

        buf.append(text.substring(start));
        return buf.toString();
    }

    /**
     * 将tempId:storeId1,storeId2 或者 tempId的string格式转换为map形式
     * @param str
     * @return
     */
    public static final Map<String, List<String>> stringToMap(String str) {
        Map<String, List<String>> attrs = new HashMap<String, List<String>>();
        if (isNotBlank(str)) {
            if(!str.contains(SSP)){
                attrs.put(str, null);
            }else{
                String[] arr = str.split(SP);
                if (null != arr && arr.length > 0) {
                    for (String kv : arr) {
                        if (isNotBlank(kv)) {
                            if(kv.contains(SSP)){
                                String[] ar = kv.split(SSP);
                                if (null != ar && ar.length == 2) {
                                    String key = decode(ar[0]);
                                    String val = decode(ar[1]);
                                    if(org.apache.commons.lang.StringUtils.isNotEmpty(key) && isNotEmpty(val)){
                                        String[] valStr = val.split(",");
                                        if(null != valStr && valStr.length > 0){
                                            List<String> value = new ArrayList<String>();
                                            value.addAll(Arrays.asList(valStr));
                                            attrs.put(key, value);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return attrs;
    }

    /**
     * 将Map<String,List<String>>转换成String格式
     * @param attrs
     * @return
     */
    public static final String mapToString(Map<String, List<String>> attrs) {
        StringBuilder sb = new StringBuilder();
        if (null != attrs && !attrs.isEmpty()) {
            sb.append(SP);
            for (Map.Entry<String, List<String>> en : attrs.entrySet()) {
                String key = en.getKey();
                List<String> val = en.getValue();
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(val)) {
                    String str = val.toString().trim().replaceAll(" ", "");
                    String newStr = str.substring(1, str.length()-1);
                    sb.append(encode(key)).append(SSP).append(encode(newStr)).append(SP);
                }else{
                    sb.append(encode(key)).append(SP);
                    break;
                }
            }
        }
        return sb.toString();
    }

}

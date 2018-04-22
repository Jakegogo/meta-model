package com.concur.meta.client.api.query;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 聚合参数
 *
 * @author yongfu.cyf
 * @create 2017-06-29 下午5:05
 **/
public class Aggregate implements Serializable {

    private static final long serialVersionUID = -6651176371431323613L;
    /**
     * 聚合key(属性名)
     */
    private String[] keys;

    /**
     * 聚合函数类型
     */
    private AggregateType type;

    public Aggregate(AggregateType type, String[] keys) {
        this.type = type;
        this.keys = keys;
    }

    public AggregateType getType() {
        return type;
    }

    public void setType(AggregateType type) {
        this.type = type;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    /**
     * 获取sql语句段
     * @return
     */
    public String getSqlPart() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length;i++) {
            sb.append(type.getName()).append('(');
            sb.append(keys[i]);
            if (i < keys.length - 1) {
                sb.append(',');
            }
            sb.append(") ").append(" as ")
                .append(type.getName()).append("_").append(keys[i]);
        }
        return sb.toString();
    }

    /**
     * 获取sql语句段
     * 使用主键代替*
     * @return
     */
    public String getSqlPart(String primaryKey) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length;i++) {
            sb.append(type.getName()).append('(');
            String key = keys[i];
            if ("*".equals(keys[i])) {
                key = primaryKey;
            }
            sb.append(key);
            if (i < keys.length - 1) {
                sb.append(',');
            }
            sb.append(") ").append(" as ")
                .append(type.getName()).append("_").append(key);
        }
        return sb.toString();
    }

    public String[] getResultKeys() {
        String[] resultKeys = new String[keys.length];
        for (int i = 0; i < keys.length;i++) {
            resultKeys[i] = new StringBuilder()
                .append(type.getName()).append("_").append(keys[i]).toString();
        }
        return resultKeys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (!(o instanceof Aggregate)) { return false; }

        Aggregate aggregate = (Aggregate)o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(keys, aggregate.keys)) { return false; }
        return type == aggregate.type;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(keys);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}

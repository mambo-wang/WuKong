package com.wukong.consumer.config;

import com.wukong.common.utils.DateTimeTool;

import java.lang.reflect.Field;
import java.util.*;

public class RestTool<T> {

    /**
     * 将数据对象根据@Column注解生成map，key为数据库中字段名称，值为实际数值
     * @param data 数据集
     * @param clazz 类
     * @return 数据集
     */
    public List<Map<String, String>> convert(List<T> data, Class<T> clazz) {

        List<Map<String,String>> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for(T t : data){
            Map<String, String> stringMap = new HashMap<>();
            for (Field field : fields) {
                if(field.getName().equalsIgnoreCase("serialVersionUID")){
                    continue;
                }
                Column column = field.getAnnotation(Column.class);
                String dbAttrName = column.name();
                field.setAccessible(true);
                try {
                    if(Objects.nonNull(field.get(t))){
                        if(field.getType() == Date.class){
                            stringMap.put(dbAttrName, DateTimeTool.formatDate((Date) field.get(t)));
                        } else {
                            stringMap.put(dbAttrName, String.valueOf(field.get(t)));
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            list.add(stringMap);
        }

        return list;
    }
}

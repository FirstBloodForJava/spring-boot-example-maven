package com.oycm.codegen;

import org.jooq.DataType;
import org.jooq.SQLDialect;
import org.jooq.codegen.JavaGenerator;
import org.jooq.impl.DefaultDataType;
import org.jooq.impl.SQLDataType;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;


/**
 * @author ouyangcm
 * create 2024/12/12 12:22
 */
public class CustomJavaGenerator extends JavaGenerator {

    public static final DataType<Date> ORIGIN_DATE = new DefaultDataType<>(null, Date.class, "date");
    static {
        try {
            // 新增不支持，替换修改的实现自定义功能
            Field typesByName = DefaultDataType.class.getDeclaredField("TYPES_BY_NAME");
            Field typesByType = DefaultDataType.class.getDeclaredField("TYPES_BY_TYPE");
            Field typesBySqlDatatype = DefaultDataType.class.getDeclaredField("TYPES_BY_SQL_DATATYPE");

            typesByName.setAccessible(true);
            typesByType.setAccessible(true);
            typesBySqlDatatype.setAccessible(true);


            try {
                Map<String, DataType<?>>[] nameMap = (Map<String, DataType<?>>[]) typesByName.get(DefaultDataType.class);
                Map<Class<?>, DataType<?>>[] typeMap = (Map<Class<?>, DataType<?>>[]) typesByType.get(DefaultDataType.class);
                Map<Map<DataType<?>, DataType<?>>[], DataType<?>>[] sqlDataMap = (Map<Map<DataType<?>, DataType<?>>[], DataType<?>>[]) typesBySqlDatatype.get(DefaultDataType.class);
                if (nameMap != null && nameMap.length > 1) {
                    // 替换date中的类型，生成的代码有问题
                    /*DataType<?> date = nameMap[1].get("DATE");
                    Field uType = DefaultDataType.class.getDeclaredField("uType");
                    uType.setAccessible(true);
                    uType.set(date, Date.class);*/
                    nameMap[1].remove("DATE");

                }
                if (typeMap != null && typeMap.length > 1) {
                    //typeMap[1].remove("DATE");
                }
                if (sqlDataMap != null && sqlDataMap.length > 1) {
                    //sqlDataMap[1].remove("DATE");
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

    }

    // 使用这个类型，自动生成代码无法识别
    public static final DataType<Date> SYD_DATE = new DefaultDataType<>(SQLDialect.DEFAULT, ORIGIN_DATE, "sysdate");
    // java.sql.Date 换成了java.util.Date 但是生成代码过程的类有问题，不能扁你
    public static final DataType<Date> DATE = new DefaultDataType<>(SQLDialect.DEFAULT, ORIGIN_DATE, "date");

    public static final DataType<String> NVARCHAR2 = new DefaultDataType<>(SQLDialect.DEFAULT, SQLDataType.NVARCHAR, "nvarchar2");
    public static final DataType<String> VARCHAR2 = new DefaultDataType<>(SQLDialect.DEFAULT, SQLDataType.NVARCHAR, "varchar2");
    public static final DataType<Integer> NUMBER = new DefaultDataType<>(SQLDialect.DEFAULT, Integer.class, "number");
}

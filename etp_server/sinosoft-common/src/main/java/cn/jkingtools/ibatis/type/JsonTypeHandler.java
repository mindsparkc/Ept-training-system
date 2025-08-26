package cn.jkingtools.ibatis.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用以处理 MySQL 中的 json 类型的字段转换为 bean
 *
 * <B>
 * 使用本类的说明
 * 1. 在 resultMap 中的配置
 * <result property="rule"    column="rule" typeHandler="cn.jkingtools.ibatis.type.JsonTypeHandler"   />
 *
 * 2. 在 update 或 insert 中的配置
 * <if test="rule != null">#{rule, typeHandler=cn.jkingtools.ibatis.type.JsonTypeHandler},</if>
 *
 * @author wanglijie
 * @param <T>
 */
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {
    private final static Log log = LogFactory.getLog(JsonTypeHandler.class);

    private static ObjectMapper objectMapper;
    private Class<T> type;
    static {
        objectMapper = new ObjectMapper();
    }

    public JsonTypeHandler(Class<T> type) {
        if (log.isTraceEnabled()) {
            log.trace("JsonTypeHandler(" + type + ")");
        }

        if (type == null) {
            throw new IllegalArgumentException("在 JsonTypeHandler 中，type 不能为 null！");
        }

        this.type = type;
    }

    private T parse(String json) {
        if (json == null || json.length() == 0) return null;

//        try {
//            return objectMapper.readValue(json, type);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
        return (T)JSON.parse(json);
    }

    private String toJsonString(Object obj) {
//        try {
//            return objectMapper.writeValueAsString(obj);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
        return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteClassName);
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, toJsonString(t));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return parse(resultSet.getString(s));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return parse(resultSet.getString(i));
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return parse(callableStatement.getNString(i));
    }
}

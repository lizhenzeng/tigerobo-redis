package com.tigerobo.redis.annotation;

import com.tigerobo.redis.operator.WrapClass;
import com.tigerobo.redis.utils.ReflectUtils;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class WrapAnnotation {
    public Annotation annotation;
    public String bindMethodName;
    public MetaAnnotation ma = new MetaAnnotation();


    WrapAnnotation(Annotation an, String bindMethodName) {
        ma.registerAnnotation(an).parser();
        this.annotation = an;
        this.bindMethodName = bindMethodName;
    }

    public String getId() {
        MetaAnnotation ma = new MetaAnnotation();
        ma.registerAnnotation(annotation);
        return ma.getId();
    }

    public void putAllArgs(Map<String, Object> map) {
        map.entrySet().forEach(val -> putArgs(val.getKey(), val.getValue()));
    }


    public void putArgs(String key, Object value) {
        ma.putMetaValue(key, value);
    }

    public Object[] getArgs(Method method, WrapClass wc) throws NotFoundException {
        Parameter[] paramters = method.getParameters();
        List<Object> args = new ArrayList();
        if (paramters != null && paramters.length > 0) {
            try {

                for (int i = 0; i < paramters.length; i++) {
                    Annotation parameterAnnotation = paramters[i].getAnnotation(Param.class);
                    if (parameterAnnotation != null && parameterAnnotation instanceof Param) {
                        Object arg = ma.getObject(((Param) parameterAnnotation).name());
                        if (arg != null) {
                            args.add(arg);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return args.toArray(new Object[args.size()]);
    }


    public static class Builder {
        public Annotation annotation;
        public String bindMethodName;

        public Builder setAnnotation(Annotation an) {
            this.annotation = an;
            return this;
        }

        public Builder setBindMethodName(String bindMethodName) {
            this.bindMethodName = bindMethodName;
            return this;
        }

        public WrapAnnotation build() {
            return new WrapAnnotation(annotation, bindMethodName);
        }
    }
}

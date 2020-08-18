package com.tigerobo.redis.operator;

import lombok.Data;
@Data
public class WrapClass {
    public Object annotation;
    public String bindMethodName;

    WrapClass(Object an,String bindMethodName){
        this.annotation = an;
        this.bindMethodName = bindMethodName;
    }
    public static class Builder{
        public Object value;
        public String bindMethodName;

        public WrapClass.Builder setClassObject(Object value){
            this.value = value;
            return this;
        }
        public WrapClass.Builder setBindMethodName(String bindMethodName){
            this.bindMethodName = bindMethodName;
            return this;
        }
        public WrapClass build(){
            return  new WrapClass(value,bindMethodName);
        }
    }
}

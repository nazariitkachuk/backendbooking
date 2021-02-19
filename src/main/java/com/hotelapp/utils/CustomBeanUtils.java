package com.hotelapp.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

public class CustomBeanUtils {



    public static void copyNotNullProperties(Object source, Object target,String ... args){


        List<String> nullFields= nullFieldsList(source);

        for(int i=0;i<args.length;i++){
            nullFields.add(args[i]);
        }

        BeanUtils.copyProperties(source,target,nullFields.toArray(new String[nullFields.size()]));
    }
    public static List<String> nullFieldsList(Object source)  {

        Field[] sourceFields = source.getClass().getDeclaredFields();

        List<String> arrayList=  new ArrayList<>();
        for (int i = 0; i < sourceFields.length; i++) {



            try {
                sourceFields[i].setAccessible(true);
                Object sourceFieldValue = null;
                sourceFieldValue = sourceFields[i].get(source);

                if (sourceFieldValue == null) {
                    arrayList.add(sourceFields[i].getName());
                }


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }



        }
        return arrayList;
    }
    
}

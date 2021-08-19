package cg.software.utils;


import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PrintJson {
    public static void booleanPrintJson(HttpServletResponse response,boolean flag){
        Map<String,Boolean> map=new HashMap<>();
        map.put("success",flag);
        ObjectMapper om=new ObjectMapper();
        try {
            String str=om.writeValueAsString(map);
            response.getWriter().print(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void ObjectPrintJson(HttpServletResponse response,Object obj){
        ObjectMapper om=new ObjectMapper();
        try {
            String json=om.writeValueAsString(obj);
            response.getWriter().print(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

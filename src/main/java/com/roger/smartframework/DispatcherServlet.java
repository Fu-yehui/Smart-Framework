package com.roger.smartframework;

import com.roger.smartframework.bean.Data;
import com.roger.smartframework.bean.Handler;
import com.roger.smartframework.bean.Param;
import com.roger.smartframework.bean.View;
import com.roger.smartframework.helper.BeanHelper;
import com.roger.smartframework.helper.ConfigHelper;
import com.roger.smartframework.helper.ControllerHelper;
import com.roger.smartframework.helper.DatabaseHelper;
import com.roger.smartframework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求转发器
 * @author roger
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet  {

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化相关Helper类
        HelperLoader.init();
        //获取 servletContext 对象（用于注册 Servlet）
        ServletContext servletContext=config.getServletContext();
        //注册处理JSP的Servlet
        ServletRegistration jspServlet=servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        //注册处理静态资源的默认Servlet
        ServletRegistration defaultServelt=servletContext.getServletRegistration("default");
        defaultServelt.addMapping(ConfigHelper.getAppAssetPath()+"*");


    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取请求方法与请求路径
        String requestMethod=req.getMethod().toLowerCase();
        String requestPath=req.getPathInfo();
        //获取Action处理器
        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if(handler != null){
            //获取Controller 类及其 Bean实例
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);
            //创建请求参数对象
            Map<String,Object> paramMap=new HashMap<String,Object>();
            /**
             * 防止将请求实体中的中文出现乱码
             */
            req.setCharacterEncoding("UTF-8");
            Enumeration<String> paramNames=req.getParameterNames();
            while(paramNames.hasMoreElements()){
                String paramName=paramNames.nextElement();
                String paramValue=req.getParameter(paramName);
                paramMap.put(paramName,paramValue);
            }

            String body= CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)){
                String[] params=StringUtil.splitString(body,"&");
                if(ArrayUtil.isNotEmpty(params)){
                    for(String param:params){
                        String[] array = StringUtil.splitString(param,"=");
                        if(ArrayUtil.isNotEmpty(array) && array.length==2){
                            String paramName=array[0];
                            String paramValue=array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param=new Param(paramMap);
            //调用Action方法
            Method actionMethod=handler.getActionMethod();
            Object result= ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            //处理Action方法返回值
            if(result instanceof View){
                //返回 JSP 数据
                View view=(View) result;
                String path=view.getPath();
                if(StringUtil.isNotEmpty(path)){
                    if(path.startsWith("/")){
                        resp.sendRedirect(req.getContextPath()+path);
                    }else{
                        Map<String,Object> model=view.getModel();
                        for(Map.Entry<String,Object> entry : model.entrySet()){
                            req.setAttribute(entry.getKey(),entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath()+path).forward(req, resp);
                    }
                }
            }else if(result instanceof Data){
                //返回 JSON 数据
                Data data=(Data)result;
                Object model=data.getModel();
                if(model != null){
                    resp.setContentType("application/json; charset=UTF-8");
                    PrintWriter writer=resp.getWriter();
                    String json=JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }


    @Override
    public void destroy() {
        super.destroy();
        DatabaseHelper.shutdown();
    }
}

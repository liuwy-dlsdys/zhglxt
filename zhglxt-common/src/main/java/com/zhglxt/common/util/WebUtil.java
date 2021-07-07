package com.zhglxt.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Description web工具类
 * @Author liuwy
 * @Date 2018/12/26
 */
public class WebUtil {
    /**
     * 将Request请求中的所有参数转换成Map对象
     *
     * @param requestParametersMap
     * @return result
     */
    public static Map paramsToMap(Map<String, String[]> requestParametersMap) {
        Iterator it = requestParametersMap.entrySet().iterator();
        Map result = new HashMap();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            String[] value = (String[]) entry.getValue();
            if (value.length == 1) {
                result.put(entry.getKey(), value[0].equalsIgnoreCase("'undefined'") ? null : value[0]);
            } else {
                result.put(entry.getKey(), value);
            }
        }
        return result;
    }

    /**
     * 分页参数处理
     * 针对于oracle分页
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> pagerManager(Map<String, Object> obj, int count) {
        if (StringUtils.isNotBlank(obj.get("page")) && StringUtils.isNotBlank(obj.get("rows"))) {
            int page = Integer.valueOf(obj.get("page").toString());
            int rows = Integer.valueOf(obj.get("rows").toString());
            int begin = (page - 1) * rows;// 0 10
            int end = page * rows;// 10 20
            int pages = count / rows + (count % rows > 0 ? 1 : 0);
            obj.put("begin", begin);
            obj.put("end", end);
            obj.put("pages", pages);
        }
        return obj;
    }

    /**
     * 分页参数处理
     * 针对于mysql分页
     * mysql分页原理 只需要知道从第几条数据开始，每页显示多少条 即可实现mysql的分页查询
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> pagerManager_MYSQL_bootstrap(Map<String, Object> obj) {
        if (StringUtils.isNotBlank(obj.get("limit")) && StringUtils.isNotBlank(obj.get("offset"))) {
            int limit = Integer.valueOf(obj.get("limit").toString());//页大小
            int offset = Integer.valueOf(obj.get("offset").toString());//开始下标
            obj.put("limit", limit);
            obj.put("offset", offset);
        }
        return obj;
    }

    /**
     * 分页参数处理
     * 针对于mysql分页
     * mysql分页原理 只需要知道从第几条数据开始，每页显示多少条 即可实现mysql的分页查询
     *
     * @param obj
     * @param totalRecords 总记录数
     * @return
     */
    public static Map<String, Object> pagerManager_MYSQL_jqGrid(Map<String, Object> obj, int totalRecords) {
        if (StringUtils.isNotBlank(obj.get("limit")) && StringUtils.isNotBlank(obj.get("page"))) {
            int limit = Integer.valueOf(obj.get("limit").toString());//页大小
            int page = Integer.valueOf(obj.get("page").toString());//当前页
            int totalPages = 0;
            if (totalRecords > 0) {
                totalPages = totalRecords / limit;
                if (totalRecords % limit > 0) {
                    totalPages++;
                }
            }
            int offset = (page - 1) * limit;//开始下标
            obj.put("limit", limit);
            obj.put("offset", offset);
            obj.put("totalPages", totalPages);//总页数
        }
        return obj;
    }

    /**
     * 分页参数处理
     * 针对于mysql分页
     * mysql分页原理 只需要知道从第几条数据开始，每页显示多少条 即可实现mysql的分页查询
     *
     * @param obj
     * @param totalRecords 总记录数
     * @return
     */
    public static Map<String, Object> pagerManager_MYSQL_dataGride(Map<String, Object> obj, int totalRecords) {
        if (StringUtils.isNotBlank(obj.get("rows")) && StringUtils.isNotBlank(obj.get("page"))) {
            int limit = Integer.valueOf(obj.get("rows").toString());//页大小
            int page = Integer.valueOf(obj.get("page").toString());//当前页
            int totalPages = 0;
            if (totalRecords > 0) {
                totalPages = totalRecords / limit;
                if (totalRecords % limit > 0) {
                    totalPages++;
                }
            }
            int offset = (page - 1) * limit;//开始下标
            obj.put("limit", limit);
            obj.put("offset", offset);
            obj.put("totalPages", totalPages);//总页数
        } else {
            obj.put("limit", totalRecords);
            obj.put("offset", 0);
        }
        return obj;
    }

    /**
     * 获取本应用所在服务器地址
     *
     * @throws UnknownHostException
     */
    public static String getServicerAddress() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        return localHost.getHostAddress();
    }
}

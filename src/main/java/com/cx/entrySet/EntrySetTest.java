package com.cx.entrySet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: CloudCx
 * Date: 2020/2/14
 * Time: 13:57
 * Description: No Description
 */
public class EntrySetTest {
    /**
     * list转换
     * @param oldList
     * @return
     */
    public static List<List<String>> mapToListOnList(List<Map<String, String>> oldList){
        List<List<String>> newList = new ArrayList();
        for (Map<String, String> map : oldList){
            List<String> oneList = new ArrayList();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                oneList.add(entry.getValue());
            }
            newList.add(oneList);
        }
        return newList;
    }

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
        List<Map<String,String>> oldList = new ArrayList<>();
        Map map1 = new HashMap();
        map1.put("codeName","测试1");
        map1.put("codeVlaue","测试1code");
        oldList.add(map1);
        Map map2 = new HashMap();
        map2.put("codeName","测试2");
        map2.put("codeVlaue","测试2code");
        oldList.add(map2);

        List<List<String>> newList = mapToListOnList(oldList);
        System.out.println(newList.toString());
    }
}

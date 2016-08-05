package main;

import Entities.Entity_Pctn;
import com.oracle.javafx.jmx.json.JSONFactory;
import com.oracle.javafx.jmx.json.JSONWriter;
import util.GraphGenerator;
import util.StockList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by FireAwayH on 12/07/2016.
 */
public class MainServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String path = request.getRequestURI();
        switch (path){
            case "/save":
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String query = request.getQueryString();
        String res = "";

        JSONWriter jw = JSONFactory.instance().makeWriter(response.getWriter());
        Map<String, Object> network = new HashMap<>();

        Map<String, Object> indegree = new HashMap<>();
        Map<String, Object> outdegree = new HashMap<>();
        List<Entity_Pctn> fullGraph = null;
        List stockList = null;

        jw.startObject();

        if(query != null) {
            try {
                String[] q = query.split("=");
                String method = q[0];
                String type = q[1];
                res = "true";

                if(method.equalsIgnoreCase("pctn")) {
                    fullGraph = GraphGenerator.getDependencyNetwork("Entity_Pctn");
                    switch (type) {
                        case "all":
                            jw.startArray("stocks");

                            stockList = GraphGenerator.getStock("Entity_Stocksinpctn");
                            stockList.forEach(o -> {
                                try {
                                    jw.arrayValue(o);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            jw.endArray();

                            jw.startArray("allStocks");

                            Arrays.asList(StockList.stocks).forEach(o -> {
                                try {
                                    jw.arrayValue(o);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            jw.endArray();
                            break;
                    }

//                    indegree.forEach((k,v) -> {
//                        try {
//                            jw.objectValue(k, v);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    });
                }else if(method.equalsIgnoreCase("pcpg")){
                    fullGraph = GraphGenerator.getDependencyNetwork("Entity_Pcpg");
                    switch (type) {
                        case "all":
                            jw.startArray("stocks");

                            stockList = GraphGenerator.getStock("Entity_Stocksinpcpg");
                            stockList.forEach(o -> {
                                try {
                                    jw.arrayValue(o);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            jw.endArray();

                            jw.startArray("allStocks");

                            Arrays.asList(StockList.stocks).forEach(o -> {
                                try {
                                    jw.arrayValue(o);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            jw.endArray();
                            break;
                    }

                }
            }catch (Exception e){
                res = e.getMessage();
                e.printStackTrace();
                network.put("nodata", "null");
            }
        }

        jw.startArray("edges");

        // 保证网络中的股票都存在于列表中
        List<Entity_Pctn> r = (List<Entity_Pctn>)removeNInStkLst(fullGraph, stockList);

        r.stream().forEach(e -> {
            try {
                String x = e.getTargetX();
                String y = e.getTargetY();
                String z = e.getSource();
                double d = e.getDependency();

                increaseWeight(indegree, x);
                increaseWeight(indegree, y);
                increaseWeight(outdegree, z);

                network.put("sourceID", z);
                network.put("targetID", x);
                network.put("width", d);
                jw.writeObject(network);

                network.put("sourceID", z);
                network.put("targetID", y);
                network.put("width", d);
                jw.writeObject(network);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        jw.endArray();

        jw.startObject("indegree");
        Iterator in = indegree.entrySet().iterator();
        while(in.hasNext()) {
            Map.Entry key = (Map.Entry)in.next();
            jw.objectValue((String)key.getKey(), key.getValue());
        }
        jw.endObject();

        jw.startObject("outdegree");
        Iterator out = outdegree.entrySet().iterator();
        while(out.hasNext()) {
            Map.Entry key = (Map.Entry)out.next();
            jw.objectValue((String)key.getKey(), key.getValue());
        }
        jw.endObject();

//        // Set JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF8 -Dsun.jnu.encoding=UTF8" in catalina.sh line 249
        jw.objectValue("result", res);
        jw.endObject();
        jw.flush();
        jw.close();
    }

    private void increaseWeight(Map<String, Object> map, String key) {
        if(map.containsKey(key)){
            int newValue = Integer.parseInt(map.get(key).toString()) + 1;
            map.replace(key, newValue);
        }else{
            map.put(key, 1);
        }
    }

    /* Remove items from removeFrom where the item's properties are not in whatToRemove
     * You must override items inside removeFrom equals whatToRemove
     */
    private List<?> removeNInStkLst(List<?> removeFrom, List<?> whatToRemove) {
        return removeFrom.stream().filter(e -> e.equals(whatToRemove)).collect(Collectors.toList());
    }

    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }
}

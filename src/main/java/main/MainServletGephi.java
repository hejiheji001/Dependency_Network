package main;

import util.Gephi.GraphToGexf;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by FireAwayH on 12/08/2016.
 */
public class MainServletGephi extends HttpServlet  {

    public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/gexf+xml");
        response.setCharacterEncoding("UTF-8");
        String query = request.getQueryString();

        Map<String, String> params = splitQuery(query);

        if(query != null) {
            try {
                String graph = params.get("graph");// pctn OR pcpg
                String type = params.get("stocks");// all OR some: [stockArray]
                String mode = params.get("mode");// in OR out

//                if(graph.equalsIgnoreCase("pctn")) {
//                    switch (type) {
//                        case "all":
                Writer w = response.getWriter();
                synchronized (this) {
                GraphToGexf g = new GraphToGexf(mode);

                    g.setMode(mode);
                    g.setGraphType(graph);
                    String gexf = g.getGraph();
//                            break;
//                    }
//                }else if(graph.equalsIgnoreCase("pcpg")){
//                    switch (type) {
//                        case "all":
//                            GraphToGexf g = new GraphToGexf(mode);
//                            g.setGraphType(graph);
//                            g.getGraph(w);
//                            break;
//                    }
//                }
                    w.write(gexf);
                    w.flush();
                    w.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

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
}

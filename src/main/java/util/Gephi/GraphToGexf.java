package util.Gephi;

import org.gephi.appearance.api.AppearanceController;
import org.gephi.appearance.api.AppearanceModel;
import org.gephi.appearance.api.Function;
import org.gephi.appearance.plugin.RankingElementColorTransformer;
import org.gephi.appearance.plugin.RankingLabelSizeTransformer;
import org.gephi.appearance.plugin.RankingNodeSizeTransformer;
import org.gephi.graph.api.Column;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.database.drivers.PostgreSQLDriver;
import org.gephi.io.database.drivers.SQLDriver;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.CharacterExporter;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.importer.plugin.database.EdgeListDatabaseImpl;
import org.gephi.io.importer.plugin.database.ImporterEdgeList;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

import java.awt.*;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

/**
 * Created by FireAwayH on 12/08/2016.
 */
public class GraphToGexf{

    private String mode = "in";
    private String DBNAME = "FTSE350";
    private String HOST = "localhost";
    private int PORT = 5432;
    private String USERNAME = "postgres";
    private String PASSWORD = "postgres";
    private SQLDriver SQLDRIVER = new PostgreSQLDriver();
    private String graphType = "pctn";
    private String NODE_QUERY;
    private String EDGE_QUERY;
    private AppearanceModel.GraphFunction NODE_SIZE_MODE = AppearanceModel.GraphFunction.NODE_INDEGREE;

    public GraphToGexf(){}

    public GraphToGexf(String DBNAME, String HOST, int PORT, String USERNAME, String PASSWORD, SQLDriver SQLDRIVER, String NODE_QUERY, String EDGE_QUERY, String NODESIZEMODE) {
        this.DBNAME = DBNAME;
        this.HOST = HOST;
        this.PORT = PORT;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.SQLDRIVER = SQLDRIVER;
        this.NODE_QUERY = NODE_QUERY;
        this.EDGE_QUERY = EDGE_QUERY;
        if(NODESIZEMODE.equalsIgnoreCase("out")){
            this.NODE_SIZE_MODE = AppearanceModel.GraphFunction.NODE_OUTDEGREE;
        }else if(NODESIZEMODE.equalsIgnoreCase("in")){
            this.NODE_SIZE_MODE = AppearanceModel.GraphFunction.NODE_INDEGREE;
        }else{
            this.NODE_SIZE_MODE = AppearanceModel.GraphFunction.NODE_DEGREE;
        }
    }

    public GraphToGexf(String modename) {
        setMode(modename);
        if(this.mode.equalsIgnoreCase("out")){
            this.NODE_SIZE_MODE = AppearanceModel.GraphFunction.NODE_OUTDEGREE;
        }else if(this.mode.equalsIgnoreCase("in")){
            this.NODE_SIZE_MODE = AppearanceModel.GraphFunction.NODE_INDEGREE;
        }else{
            this.NODE_SIZE_MODE = AppearanceModel.GraphFunction.NODE_DEGREE;
        }
    }

    public String getMode() {return mode;}

    public void setMode(String mode) {this.mode = mode;}

    public String getGraphType() {return graphType;}

    public void setGraphType(String graphType) {this.graphType = graphType;}

    public String getDBNAME() {
        return DBNAME;
    }

    public void setDBNAME(String DBNAME) {
        this.DBNAME = DBNAME;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public int getPORT() {
        return PORT;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public SQLDriver getSQLDRIVER() {
        return SQLDRIVER;
    }

    public void setSQLDRIVER(SQLDriver SQLDRIVER) {
        this.SQLDRIVER = SQLDRIVER;
    }

    public String getNODE_QUERY() {
        NODE_QUERY = "select name as id from stocksin" + this.graphType;
        return NODE_QUERY;
    }

    public void setNODE_QUERY(String NODE_QUERY) {
        this.NODE_QUERY = NODE_QUERY;
    }

    public String getEDGE_QUERY() {
        switch (this.graphType){
            case "pctn":
                EDGE_QUERY = "select source, target_x as target, dependency as weight from pctn where dependency > 0 UNION ALL select source, target_y as target, dependency as weight from pctn where dependency > 0";
                break;
            case "pcpg":
                EDGE_QUERY = "select source, target, dependency as weight from pcpg where dependency > 0";
                break;
        }
        return EDGE_QUERY;
    }

    public void setEDGE_QUERY(String EDGE_QUERY) {
        this.EDGE_QUERY = EDGE_QUERY;
    }

    public String getGraph() throws Exception {
        Class.forName("org.postgresql.Driver");
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace ws = pc.getCurrentWorkspace();

        ImportController ic = Lookup.getDefault().lookup(ImportController.class);
        EdgeListDatabaseImpl db = new EdgeListDatabaseImpl();
        db.setDBName(DBNAME);
        db.setHost(HOST);
        db.setPort(PORT);
        db.setUsername(USERNAME);
        db.setPasswd(PASSWORD);
        db.setSQLDriver(SQLDRIVER);
        db.setNodeQuery(getNODE_QUERY());
        db.setEdgeQuery(getEDGE_QUERY());

        System.out.println(getNODE_QUERY());
        System.out.println(getEDGE_QUERY());

        Container c = ic.importDatabase(db, new ImporterEdgeList());

        ic.process(c, new DefaultProcessor(), ws);

        GraphModel gm = Lookup.getDefault().lookup(GraphController.class).getGraphModel(ws);

        AutoLayout al = new AutoLayout(5, TimeUnit.SECONDS);
        al.setGraphModel(gm);

        ForceAtlasLayout force = new ForceAtlasLayout(null);

        al.setGraphModel(gm);

        Graph graph = gm.getDirectedGraph();
        AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout.createDynamicProperty("forceAtlas.adjustSizes.name", Boolean.TRUE, 0.1f);
        AutoLayout.DynamicProperty repulsionProperty = AutoLayout.createDynamicProperty("forceAtlas.repulsionStrength.name", 1000d, 0f);
        AutoLayout.DynamicProperty attractionProperty = AutoLayout.createDynamicProperty("forceAtlas.attractionStrength.name", 5d, 0f);
        AutoLayout.DynamicProperty maximumDisplacementProperty = AutoLayout.createDynamicProperty("forceAtlas.maxDisplacement.name", 100d, 0f);
        al.addLayout(force, 1.0f, new AutoLayout.DynamicProperty[]{
                adjustBySizeProperty, repulsionProperty, attractionProperty, maximumDisplacementProperty
        });

        AppearanceController appearanceController = Lookup.getDefault().lookup(AppearanceController.class);
        AppearanceModel appearanceModel = appearanceController.getModel();

        // Node Color
        Function nodeDegreeRanking = appearanceModel.getNodeFunction(graph, this.NODE_SIZE_MODE, RankingElementColorTransformer.class);
        RankingElementColorTransformer nodeDegreeTransformer = nodeDegreeRanking.getTransformer();
        nodeDegreeTransformer.setColors(new Color[]{new Color(0xFF1F22), new Color(0x2C7BB6), new Color(0x55FF00), new Color(0xFF00EA), new Color(0x00FFDF), new Color(0xFFE300)});
        nodeDegreeTransformer.setColorPositions(new float[]{0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f});
        appearanceController.transform(nodeDegreeRanking);

        // Edge Color
        Function edgeDegreeRanking = appearanceModel.getEdgeFunction(graph, AppearanceModel.GraphFunction.EDGE_WEIGHT, RankingElementColorTransformer.class);
        RankingElementColorTransformer edgeDegreeTransformer = edgeDegreeRanking.getTransformer();
        edgeDegreeTransformer.setColors(new Color[]{new Color(0xFF1F22), new Color(0x2C7BB6), new Color(0x55FF00), new Color(0xFF00EA), new Color(0x00FFDF), new Color(0xFFE300)});
        edgeDegreeTransformer.setColorPositions(new float[]{0f, 0.2f, 0.4f, 0.6f, 0.8f, 1f});
        appearanceController.transform(edgeDegreeRanking);

        GraphDistance distance = new GraphDistance();
        distance.setDirected(true);
        distance.execute(gm);

        Column centralityColumn = gm.getNodeTable().getColumn(GraphDistance.BETWEENNESS);

        // Node Size
        Function centralityRanking = appearanceModel.getNodeFunction(graph, centralityColumn, RankingNodeSizeTransformer.class);
        RankingNodeSizeTransformer centralityTransformer = centralityRanking.getTransformer();
        centralityTransformer.setMinSize(5);
        centralityTransformer.setMaxSize(50);
        appearanceController.transform(centralityRanking);

        // Label Size
        Function centralityRanking2 = appearanceModel.getNodeFunction(graph, centralityColumn, RankingLabelSizeTransformer.class);
        RankingLabelSizeTransformer labelSizeTransformer = centralityRanking2.getTransformer();
        labelSizeTransformer.setMinSize(10);
        labelSizeTransformer.setMaxSize(40);
        appearanceController.transform(centralityRanking2);

        al.execute();

        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
        GraphExporter exporter = (GraphExporter) ec.getExporter("gexf");
        Writer stringWriter = new StringWriter();
        ec.exportWriter(stringWriter,(CharacterExporter)exporter);
//        ExportController ec = Lookup.getDefault().lookup(ExportController.class);
//        try {
//            ec.exportFile(new File("test_in.gexf"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return;
//        }
        return stringWriter.toString();
    }

    public static void main(String[] args) {
//        GraphToGexf f = new GraphToGexf();
//        StringWriter s = new StringWriter();
//        try {
//            f.getGraph(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

//    @Override
//    public void run() {
//        try {
//            getGraph();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

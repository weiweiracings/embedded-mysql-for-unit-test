package indv.ghostwei.embedded.db;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.distribution.Version;

import static com.wix.mysql.ScriptResolver.classPathScripts;

public class EmbeddedDbManager {
    private static EmbeddedDbManager managerInstance;

    private EmbeddedMysql embeddedMysql;

    private String schema;

    private String initScript;

    private EmbeddedDbManager() {
    }

    public synchronized static EmbeddedDbManager getInstance(){
        if (managerInstance == null) {
            managerInstance = new EmbeddedDbManager();
        }
        return managerInstance;
    }

    /**
     * 初始化嵌入式数据库
     */
    public void init(String schema, String scriptPath) {
        EmbeddedMysql.Builder builder = EmbeddedMysql.anEmbeddedMysql(Version.v5_6_31);
        builder.addSchema(schema, classPathScripts(scriptPath));
        this.schema = schema;
        this.initScript = scriptPath;
        embeddedMysql = builder.start();
    }

    /**
     * 在启动的嵌入库上运行数据脚本，脚本为classpath路径，支持通配符*
     * @param scriptPath
     */
    public void runScript(String scriptPath){
        if(embeddedMysql == null){
            throw new RuntimeException("db not init");
        }
        embeddedMysql.executeScripts(schema, classPathScripts(scriptPath));
    }

    public void reload(){
        if(embeddedMysql == null){
            throw new RuntimeException("db not init");
        }
        embeddedMysql.reloadSchema(schema, classPathScripts(initScript));
    }
}

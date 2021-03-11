package com.aratek.framework.gen.util;

import com.aratek.framework.core.exception.AraBaseException;
import com.aratek.framework.core.util.DateUtil;
import com.aratek.framework.core.util.StringUtil;
import com.aratek.framework.gen.model.ColumnResult;
import com.aratek.framework.gen.model.TableResult;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月19日 下午11:40:24
 */
public class GenUtils {

    private static Configuration GEN_CONFIG = null;
    private static final Object INIT_LOCK = new Object();
    private static volatile boolean INIT_FLAG = false;

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("template/Entity.java.vm");
//		templates.add("template/Dao.java.vm");
//		templates.add("template/Dao.xml.vm");
//        templates.add("template/DaoImpl.java.vm");

//        templates.add("template/Service.java.vm");
//        templates.add("template/ServiceImpl.java.vm");
//        templates.add("template/Controller.java.vm");
//        templates.add("template/list.html.vm");
//        templates.add("template/list.js.vm");

//		templates.add("template/list2.js.vm");
        return templates;
    }

    /**
     * 生成代码
     */
    public static void generatorCode(TableResult tableEntity, ZipOutputStream zip) throws AraBaseException {
        Configuration config = getConfig();

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        //封装模板数据
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("objectName", tableEntity.getObjectName());
        map.put("pathName", tableEntity.getObjectName().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("package", config.getString("package"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtil.format(new Date(), DateUtil.DATE_PATTERN));
        VelocityContext context = new VelocityContext(map);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                String filename = getFileName(template, tableEntity.getClassName(), config.getString("package"));
                String data = sw.toString();
                //添加到zip
                zip.putNextEntry(new ZipEntry(filename));
                IOUtils.write(data, zip, "UTF-8");
                IOUtils.closeQuietly(sw);
            } catch (IOException e) {
                throw new AraBaseException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }

    public static TableResult getTableResult(Map<String, String> table, List<Map<String, Object>> columns) throws AraBaseException {
        Configuration config = getConfig();
        TableResult tableEntity = new TableResult();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setObjectName(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnResult> columsList = new ArrayList<ColumnResult>();
        for (Map<String, Object> column : columns) {
            ColumnResult columnEntity = new ColumnResult();
            columnEntity.setColumnName((String) column.get("columnName"));
            columnEntity.setDataType((String) column.get("dataType"));
            columnEntity.setComments((String) column.get("columnComment"));
            columnEntity.setExtra((String) column.get("extra"));
            columnEntity.setNullable((String) column.get("nullable"));
            if (column.get("mLength") != null) {
                columnEntity.setLength(((BigInteger) column.get("mLength")).intValue());
            }
            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(StringUtil.toUpperCaseFirstOne(attrName));
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            //是否主键
            if ("PRI".equalsIgnoreCase((String) column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
                columnEntity.setColumnKey((String) column.get("columnKey"));
            }
            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

//        tableEntity.getPk().setBusinessType("fid");
//        for (ColumnResult columnResult : tableEntity.getColumns()){
//            setBusinessType(columnResult);
//        }

        return tableEntity;
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
//		return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
        return columnName.toLowerCase();
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        String[] names = tableName.split("_");
        if (names.length > 2) {
            StringBuilder name = new StringBuilder();
            for (int i = 2; i < names.length; i++) {
                name.append(StringUtil.toUpperCaseFirstOne(names[i].toLowerCase()));
            }
            return name.toString();
        }
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return WordUtils.capitalizeFully(tableName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() throws AraBaseException {
        try {
            if (!INIT_FLAG) {
                synchronized (INIT_LOCK) {
                    if (!INIT_FLAG) {
                        GEN_CONFIG = new PropertiesConfiguration("generator.properties");
                        INIT_FLAG = true;
                    }
                }
            }
            return GEN_CONFIG;
        } catch (ConfigurationException e) {
            throw new AraBaseException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("Entity.java.vm")) {
            return packagePath + "entity" + File.separator + className + "Entity.java";
        }

        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (template.contains("DaoImpl.java.vm")) {
            return packagePath + "dao" + File.separator + "impl" + File.separator + className + "DaoImpl.java";
        }

        if (template.contains("Dao.xml.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.xml";
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "services" + File.separator + "interfaces" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "services" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + "manager" + File.separator + "Mgr" + className + "Controller.java";
        }

        if (template.contains("list.html.vm")) {
            return "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator + "page"
                    + File.separator + "generator" + File.separator + className.toLowerCase() + ".html";
        }

        if (template.contains("list.js.vm")) {
            return "main" + File.separator + "webapp" + File.separator + "assets" + File.separator + "js" + File.separator + "generator" + File.separator + className.toLowerCase() + ".js";
        }

        if (template.contains("list2.js.vm")) {
            return "main" + File.separator + "webapp" + File.separator + "js" + File.separator + "generator" + File.separator + className.toLowerCase() + ".js2";
        }

        if (template.contains("menu.sql.vm")) {
            return className.toLowerCase() + "_menu.sql";
        }

        throw new RuntimeException("filename is null");
    }
}

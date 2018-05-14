package com.lsq.jersey.generate;


import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: EntityMyBatis
 * @Description: 自动生成MyBatis的实体类、实体映射XML文件、Mapper
 */
public class EntityMyBatis {

    private final String type_char = "char";

    private final String type_date = "date";

    private final String type_timestamp = "timestamp";

    private final String type_int = "int";

    private final String type_bigint = "bigint";

    private final String type_text = "text";

    private final String type_bit = "bit";

    private final String type_decimal = "decimal";

    private final String type_blob = "blob";

    private final String moduleName = "goldfinance_db";

    // 生成类后缀，如果不需要置为空串
    private final String suffix = "";

    // 是否需要使用request
    private final boolean needRequest = true;

    // 对应模块名称（根据自己模块做相应调整!!!务必修改^_^）
    // private final String moduleName = "paycenter_db"; //
    // 对应模块名称（根据自己模块做相应调整!!!务必修改^_^）
    private final String bean_path = "D:/personalspace/jersey-framework/src/main/java/com/lsq/jersey/dao/po";

    private final String mapper_path = "D:/personalspace/jersey-framework/src/main/java/com/lsq/jersey/dao";

    private final String service_path = "D:/personalspace/jersey-framework/src/main/java/com/lsq/jersey/service";

    private final String xml_path = "D:/personalspace/jersey-framework/src/main/java/com/lsq/jersey/dao/sql";

    private final String request_path = "D:/personalspace/jersey-framework/src/main/java/com/lsq/jersey/api/request";

    private final String bean_package = "com.lsq.jersey.dao.po";

    private final String mapper_package = "com.lsq.jersey.dao";
    
    private final String service_package = "com.lsq.jersey.service";

    private final String request_package = "com.lsq.jersey.api.request";

    private final String driverName = "com.mysql.jdbc.Driver";

    private final String root = "root";// user

    private final String pass = "1qaz@WSX";// password

    private final String url = "jdbc:mysql://10.59.79.37:3306/" + moduleName
            + "?characterEncoding=utf8";

    private String tableName = null;

    private String beanName = null;

    private String mapperName = null;

    private Connection conn = null;

    private PreparedStatement pstate = null;

    private void init() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        conn = DriverManager.getConnection(url, root, pass);
    }

    /**
     * @Title: getTables @Description: 获取所有的表 @return @throws
     *         SQLException @throws
     */
    private List<String> getTables() throws SQLException {
        List<String> tables = new ArrayList<String>();
        pstate = conn.prepareStatement("show tables");
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString(1);
            // if ( tableName.toLowerCase().startsWith("yy_") ) {
            tables.add(tableName);
            // }
        }
        return tables;
    }

    private void processTable(String table) {
        StringBuffer sb = new StringBuffer(table.length());
        String tableNew = table.toLowerCase();
        String[] tables = tableNew.split("_");
        String temp = null;
        if (tables.length > 1) {
            for (int i = 1; i < tables.length; i++) {
                temp = tables[i].trim();
                sb.append(temp.substring(0, 1).toUpperCase())
                        .append(temp.substring(1));
            }
        } else {
            for (int i = 0; i < tables.length; i++) {
                temp = tables[i].trim();
                sb.append(temp.substring(0, 1).toUpperCase())
                        .append(temp.substring(1));
            }
        }
        beanName = sb.toString() + suffix;
        mapperName = beanName + "Mapper";
    }

    private String processType(String type) {
        if (type.indexOf(type_char) > -1) {
            return "String";
        } else if (type.indexOf(type_bigint) > -1) {
            return "Long";
        } else if (type.indexOf(type_int) > -1) {
            return "Integer";
        } else if (type.indexOf(type_date) > -1) {
            return "java.util.Date";
        } else if (type.indexOf(type_text) > -1) {
            return "String";
        } else if (type.indexOf(type_timestamp) > -1) {
            return "java.util.Date";
        } else if (type.indexOf(type_bit) > -1) {
            return "Boolean";
        } else if (type.indexOf(type_decimal) > -1) {
            return "java.math.BigDecimal";
        } else if (type.indexOf(type_blob) > -1) {
            return "byte[]";
        }
        return null;
    }

    private String processField(String field) {
        StringBuffer sb = new StringBuffer(field.length());
        String[] fields = field.split("_");
        String temp = null;
        sb.append(fields[0].substring(0, 1))
                .append(fields[0].substring(1, 2).toLowerCase())
                .append(fields[0].substring(2));
        for (int i = 1; i < fields.length; i++) {
            temp = fields[i].trim();
            sb.append(temp.substring(0, 1).toUpperCase())
                    .append(temp.substring(1));
        }

        return sb.toString();
    }

    /**
     * 将实体类名首字母改为小写
     * 
     * @param beanName
     * @return
     */
    private String processResultMapId(String beanName) {
        return beanName;
    }

    /**
     * 构建类上面的注释
     * 
     * @param bw
     * @param text
     * @return
     * @throws IOException
     */
    private BufferedWriter buildClassComment(BufferedWriter bw, String text)
            throws IOException {
        bw.newLine();
        bw.newLine();
        bw.write("/**");
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" * " + text);
        bw.newLine();
        bw.write(" * ");
        bw.newLine();
        bw.write(" **/");
        return bw;
    }

    /**
     * 构建方法上面的注释
     * 
     * @param bw
     * @param text
     * @return
     * @throws IOException
     */
    private BufferedWriter buildMethodComment(BufferedWriter bw, String text)
            throws IOException {
        bw.newLine();
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * ");
        bw.newLine();
        bw.write("\t * " + text);
        bw.newLine();
        bw.write("\t * ");
        bw.newLine();
        bw.write("\t **/");
        return bw;
    }

    /**
     * 生成实体类
     * 
     * @param columns
     * @param types
     * @param comments
     * @throws IOException
     */
    private void buildEntityBean(List<String> columns, List<String> types,
            List<String> comments, String tableComment, String table)
            throws IOException {
        File folder = new File(bean_path);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File beanFile = new File(bean_path, beanName + ".java");
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(beanFile)));
        bw.write("package " + bean_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import lombok.Data;");
        bw.newLine();
        // bw.write("import javax.persistence.*;");
        bw = buildClassComment(bw, tableComment);
        bw.newLine();
        // bw.write("@Entity");
        // bw.newLine();
        bw.write("@Data");
        bw.newLine();
        // bw.write("@Table(name = " + "\"" + table + "\"" + ")");
        // bw.newLine();
        bw.write("public class " + beanName + " {");
        bw.newLine();
        bw.newLine();
        int size = columns.size();
        bw.write("    /** " + comments.get(0) + " **/");
        bw.newLine();
        // bw.write("\t@Column(name = " + "\"" + columns.get(0) + "\"" + ")");
        // bw.newLine();
        // bw.write("\t@Id");
        // bw.newLine();
        bw.write("    private " + processType(types.get(0)) + " "
                + processField(columns.get(0)) + ";");
        bw.newLine();
        bw.newLine();
        for (int i = 1; i < size; i++) {
            bw.write("    /** " + comments.get(i) + " **/");
            bw.newLine();
            // bw.write("\t@Column(name = " + "\"" + columns.get(i) + "\"" +
            // ")");
            // bw.newLine();
            bw.write("    private " + processType(types.get(i)) + " "
                    + processField(columns.get(i)) + ";");
            bw.newLine();
            bw.newLine();
        }
        bw.newLine();
//        // 生成get 和 set方法 String tempField = null;
//        String _tempField = null;
//        String tempType = null;
//        String tempField = null;
//        for (int i = 0; i < size; i++) {
//            tempType = processType(types.get(i));
//            _tempField = processField(columns.get(i));
//            tempField = _tempField.substring(0, 1).toUpperCase()
//                    + _tempField.substring(1);
//            bw.newLine();
//            bw.write("\tpublic void set" + tempField + "(" + tempType + " "
//                    + _tempField + "){");
//            bw.newLine();
//            bw.write("\t\tthis." + _tempField + "=" + _tempField + ";");
//            bw.newLine();
//            bw.write("\t}");
//            bw.newLine();
//            bw.newLine();
//            bw.write("\tpublic " + tempType + " get" + tempField + "(){");
//            bw.newLine();
//            bw.write("\t\treturn this." + _tempField + ";");
//            bw.newLine();
//            bw.write("\t}");
//            bw.newLine();
//        }
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    }

    /**
     * 生成请求参数类
     *
     * @param columns
     * @param types
     * @param comments
     * @throws IOException
     */
    private void buildRequestBean(List<String> columns, List<String> types,
                                 List<String> comments, String tableComment, String table)
            throws IOException {
        File folder = new File(request_path);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File beanFile = new File(request_path, beanName + "Request.java");
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(beanFile)));
        bw.write("package " + request_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import lombok.Data;");
        bw.newLine();
        // bw.write("import javax.persistence.*;");
        bw = buildClassComment(bw, tableComment);
        bw.newLine();
        // bw.write("@Entity");
        // bw.newLine();
        bw.write("@Data");
        bw.newLine();
        // bw.write("@Table(name = " + "\"" + table + "\"" + ")");
        // bw.newLine();
        bw.write("public class " + beanName + "Request {");
        bw.newLine();
        bw.newLine();
        int size = columns.size();
        bw.write("    /** " + comments.get(0) + " **/");
        bw.newLine();
        // bw.write("\t@Column(name = " + "\"" + columns.get(0) + "\"" + ")");
        // bw.newLine();
        // bw.write("\t@Id");
        // bw.newLine();
        bw.write("    private " + processType(types.get(0)) + " "
                + processField(columns.get(0)) + ";");
        bw.newLine();
        bw.newLine();
        for (int i = 1; i < size; i++) {
            bw.write("    /** " + comments.get(i) + " **/");
            bw.newLine();
            // bw.write("\t@Column(name = " + "\"" + columns.get(i) + "\"" +
            // ")");
            // bw.newLine();
            bw.write("    private " + (processType(types.get(i)).equals("java.util.Date")? "String" : processType(types.get(i))) + " "
                    + processField(columns.get(i)) + ";");
            bw.newLine();
            bw.newLine();
        }
        bw.newLine();
//        // 生成get 和 set方法 String tempField = null;
//        String _tempField = null;
//        String tempType = null;
//        String tempField = null;
//        for (int i = 0; i < size; i++) {
//            tempType = processType(types.get(i));
//            _tempField = processField(columns.get(i));
//            tempField = _tempField.substring(0, 1).toUpperCase()
//                    + _tempField.substring(1);
//            bw.newLine();
//            bw.write("\tpublic void set" + tempField + "(" + tempType + " "
//                    + _tempField + "){");
//            bw.newLine();
//            bw.write("\t\tthis." + _tempField + "=" + _tempField + ";");
//            bw.newLine();
//            bw.write("\t}");
//            bw.newLine();
//            bw.newLine();
//            bw.write("\tpublic " + tempType + " get" + tempField + "(){");
//            bw.newLine();
//            bw.write("\t\treturn this." + _tempField + ";");
//            bw.newLine();
//            bw.write("\t}");
//            bw.newLine();
//        }
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    }

    /**
     * 构建Mapper文件
     * 
     * @throws IOException
     */
    private void buildMapper(List<String> columns, List<String> types,
            List<String> comments) throws IOException {
        File folder = new File(mapper_path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File mapperFile = new File(mapper_path, beanName + "Dao.java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + mapper_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        if (needRequest) {
            bw.newLine();
            bw.write("import " + request_package + "." + beanName + "Request;");
        }
        bw.newLine();
        bw.write("import java.util.List;");
        bw.newLine();
        // bw.write("import tk.mybatis.mapper.common.Mapper;");
        bw = buildClassComment(bw, mapperName + "数据库操作接口类");
        bw.newLine();
        bw.newLine();
        // bw.write("    public interface " + mapperName + " extends " +
        // mapper_extends+ "<" + beanName + "> {");
        bw.write("public interface " + beanName + "Dao" + " {"); //TODO--
        bw.newLine();
        bw.newLine();
        String poname =  beanName.substring(0, 1).toLowerCase()+ beanName.substring(1);
        bw.write("    /********************************* 自动生成器代码start ******************************************/");bw.newLine();
        bw.write("    /** 主键查询entity **/"); bw.newLine();
        bw.write("    public " + beanName + " selectByPrimaryKey("+processType(types.get(0))+" "+columns.get(0) + ");"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 过滤查询entity **/");bw.newLine();
        if (needRequest) {
            bw.write("    public List<" + beanName + "> selectFilter(" + beanName + "Request " + poname + ");"); //TODO--
        } else {
            bw.write("    public List<" + beanName + "> selectFilter(" + beanName + " " + poname + ");"); //TODO--
        }
        bw.newLine();
        bw.newLine();
        bw.write("    /** 主键删除entity **/");bw.newLine();
        bw.write("    public int deleteByPrimaryKey("+processType(types.get(0))+" "+columns.get(0) + ");"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 添加entity 返回记录条数 **/");bw.newLine();
        bw.write("    public int " +"insert("+beanName+" " +poname + ");"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 添加entity 返回主键Id **/");bw.newLine();
        if (needRequest) {
            bw.write("    public Long " + "insertGetId(" + beanName + "Request " + poname + ");"); //TODO--
        } else {
            bw.write("    public Long " + "insertGetId(" + beanName + " " + poname + ");"); //TODO--
        }
        bw.newLine();
        bw.newLine();
        bw.write("    /** 添加entity（匹配有值的字段） **/");bw.newLine();
        if (needRequest) {
            bw.write("    public int " + "insertSelective(" + beanName + "Request " + poname + ");"); //TODO--
        } else {
            bw.write("    public int " + "insertSelective(" + beanName + " " + poname + ");"); //TODO--
        }
        bw.newLine();
        bw.newLine();
        bw.write("    /** 修 改entity（匹配有值的字段） **/");bw.newLine();
        bw.write("    public int " +"updateByPrimaryKeySelective("+beanName+" "+poname + ");"); //TODO--updateByPrimaryKeySelective
        bw.newLine();
        bw.newLine();
        bw.write("    /** 修 改entity全修改 **/");bw.newLine();
        bw.write("    public int " +"updateByPrimaryKey("+beanName+" "+poname + ");"); //TODO--updateByPrimaryKeySelective
        bw.newLine();
        bw.newLine();
        bw.write("    /** 批量插入或修改 **/");bw.newLine();
        bw.write("    public Integer " +"batchInsertOrUpdate(List<" + beanName + ">" +" list);"); //TODO--updateByPrimaryKeySelective
        bw.newLine();
        bw.write("    /********************************* 自动生成器代码end ********************************************/");bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    }

    /**
     * 构建实体类映射XML文件
     * 
     * @param columns
     * @param types
     * @param comments
     * @throws IOException
     */
    private void buildMapperXml(List<String> columns, List<String> types,
            List<String> comments) throws IOException {
        File folder = new File(xml_path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File mapperXmlFile = new File(xml_path, mapperName + ".xml");
        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(mapperXmlFile)));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        bw.newLine();
        bw.write(
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" ");
        bw.newLine();
        bw.write("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        bw.newLine();
        bw.write("<mapper namespace=\"" + mapper_package + "." + beanName+"Dao"
                + "\">");
        bw.newLine();
        bw.newLine();

        bw.write("\t<!--实体映射-->");
        bw.newLine();
        bw.write("\t<resultMap id=\"BaseResultMap\" type=\"" + bean_package
                + "." + beanName + "\">");
        bw.newLine();
        bw.write("\t\t<!--" + comments.get(0) + "-->");
        bw.newLine();
        bw.write("\t\t<id property=\"" + this.processField(columns.get(0))
                + "\" column=\"" + columns.get(0) + "\" />");
        bw.newLine();
        int size = columns.size();
        for (int i = 1; i < size; i++) {
            bw.write("\t\t<!--" + comments.get(i) + "-->");
            bw.newLine();
            bw.write("\t\t<result property=\""
                    + this.processField(columns.get(i)) + "\" column=\""
                    + columns.get(i) + "\" />");
            bw.newLine();
        }
        bw.write("\t</resultMap>");

        bw.newLine();
        bw.newLine();
        bw.newLine();

        // 下面开始写SqlMapper中的方法 // this.outputSqlMapperMethod(bw, columns, types);
        buildSQL(bw, columns, types);
        bw.write("</mapper>");
        bw.flush();
        bw.close();
    }

    private void buildSQL(BufferedWriter bw, List<String> columns,
            List<String> types) throws IOException {
        int size = columns.size();
        // 通用结果列
        bw.write("\t<!-- 通用查询结果列-->");
        bw.newLine();
        bw.write("\t<sql id=\"Base_Column_List\">");
        bw.newLine();

        bw.write("");
        for (int i = 0; i < size; i++) {
            bw.write("\t" + columns.get(i));
            if (i != size - 1) {
                bw.write(",");
            }
        }

        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();

        // 查询（根据主键ID查询）
        bw.write("\t<!-- 查询（根据主键ID查询） -->");
        bw.newLine();
        bw.write("\t<select id=\"selectByPrimaryKey\" resultType=\""
                + bean_package + "." + beanName
                + "\" parameterType=\"java.lang." + processType(types.get(0))
                + "\">");
        bw.newLine();
        bw.write("\t\t SELECT");
        bw.newLine();
        bw.write("\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t FROM " + tableName);
        bw.newLine();
        bw.write("\t\t WHERE " + columns.get(0) + " = #{"
                + processField(columns.get(0)) + "}");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完

        // --------------- select方法（匹配有值的字段）
        bw.write("\t<!-- 添加 （匹配有值的字段）-->");
        bw.newLine();
        if (needRequest) {
            bw.write("\t<select id=\"selectFilter\" resultType=\""
                    + bean_package + "." + beanName
                    + "\" parameterType=\""
                    + request_package + "." + beanName + "Request\">");
        } else {
            bw.write("\t<select id=\"selectFilter\" resultType=\""
                    + bean_package + "." + beanName
                    + "\" parameterType=\""
                    + bean_package + "." + beanName + "\">");
        }
        bw.newLine();
        bw.write("\t\t SELECT");
        bw.newLine();
        bw.write("\t\t <include refid=\"Base_Column_List\" />");
        bw.newLine();
        bw.write("\t\t FROM " + tableName);
        bw.newLine();

        bw.write("\t\t WHERE 1 = 1 ");
        bw.newLine();

        String tempField = null;
        for (int i = 0; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t and " + columns.get(i) + " = #{"
                    + tempField + "}");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
        // 查询完

        // 删除（根据主键ID删除） TODO-
        bw.write("\t<!--删除：根据主键ID删除-->");
        bw.newLine();
        bw.write(
                "\t<delete id=\"deleteByPrimaryKey\" parameterType=\"java.lang."
                        + processType(types.get(0)) + "\">");
        bw.newLine();
        bw.write("\t\t DELETE FROM " + tableName);
        bw.newLine();
        bw.write("\t\t WHERE " + columns.get(0) + " = #{"
                + processField(columns.get(0)) + "}");
        bw.newLine();
        bw.write("\t</delete>");
        bw.newLine();
        bw.newLine();
        // 删除完

        // 添加insert方法
        bw.write("\t<!-- 添加 -->");
        bw.newLine();
        bw.write("\t<insert id=\"insert\" parameterType=\"" + bean_package + "."
                + beanName + "\">");
        bw.newLine();
        bw.write("\t\t INSERT INTO " + tableName);
        bw.newLine();
        bw.write(" \t\t(");
        for (int i = 0; i < size; i++) {
            bw.write(columns.get(i));
            if (i != size - 1) {
                bw.write(",");
            }
        }
        bw.write(") ");
        bw.newLine();
        bw.write("\t\t VALUES ");
        bw.newLine();
        bw.write(" \t\t(");
        for (int i = 0; i < size; i++) {
            bw.write("#{" + processField(columns.get(i)) + "}");
            if (i != size - 1) {
                bw.write(",");
            }
        }
        bw.write(") ");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
        // 添加insert完
     // --------------- insert方法（匹配有值的字段） 返回主键
        bw.write("\t<!-- 添加 （匹配有值的字段）-->");
        bw.newLine();
        if (needRequest) {
            bw.write("\t<insert id=\"insertGetId\" useGeneratedKeys=\"true\" keyProperty=\"id\" parameterType=\""
                    + request_package + "." + beanName + "Request\">");
        } else {
            bw.write("\t<insert id=\"insertGetId\" useGeneratedKeys=\"true\" keyProperty=\"id\" parameterType=\""
                    + bean_package + "." + beanName + "\">");
        }
        bw.newLine();
        bw.write("\t\t INSERT INTO " + tableName);
        bw.newLine();
        bw.write(
                "\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();

        tempField = null;
        for (int i = 0; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t " + columns.get(i) + ",");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.newLine();
        bw.write("\t\t </trim>");
        bw.newLine();

        bw.write(
                "\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();

        tempField = null;
        for (int i = 0; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + "!=null\">");
            bw.newLine();
            bw.write("\t\t\t\t #{" + tempField + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.write("\t\t </trim>");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
        // --------------- 完毕
        // --------------- insert方法（匹配有值的字段）
        bw.write("\t<!-- 添加 （匹配有值的字段）-->");
        bw.newLine();
        if (needRequest) {
            bw.write("\t<insert id=\"insertSelective\" parameterType=\""
                    + request_package + "." + beanName + "Request\">");
        } else {
            bw.write("\t<insert id=\"insertSelective\" parameterType=\""
                    + bean_package + "." + beanName + "\">");
        }
        bw.newLine();
        bw.write("\t\t INSERT INTO " + tableName);
        bw.newLine();
        bw.write(
                "\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();

        tempField = null;
        for (int i = 0; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t " + columns.get(i) + ",");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.newLine();
        bw.write("\t\t </trim>");
        bw.newLine();

        bw.write(
                "\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
        bw.newLine();

        tempField = null;
        for (int i = 0; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + "!=null\">");
            bw.newLine();
            bw.write("\t\t\t\t #{" + tempField + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.write("\t\t </trim>");
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
        // --------------- 完毕

        // 修改update方法
        bw.write("\t<!-- 修 改-->");
        bw.newLine();
        bw.write("\t<update id=\"updateByPrimaryKeySelective\" parameterType=\""
                + bean_package + "." + beanName + "\">");
        bw.newLine();
        bw.write("\t\t UPDATE " + tableName);
        bw.newLine();
        bw.write(" \t\t <set> ");
        bw.newLine();

        tempField = null;
        for (int i = 1; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
            bw.newLine();
            bw.write("\t\t\t\t " + columns.get(i) + " = #{" + tempField + "},");
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }

        bw.newLine();
        bw.write(" \t\t </set>");
        bw.newLine();
        bw.write("\t\t WHERE " + columns.get(0) + " = #{"
                + processField(columns.get(0)) + "}");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();
        bw.newLine();
        // update方法完毕
        // ----- 修改（匹配有值的字段）
        bw.write("\t<!-- 修 改-->");
        bw.newLine();
        bw.write("\t<update id=\"updateByPrimaryKey\" parameterType=\""
                + bean_package + "." + beanName + "\">");
        bw.newLine();
        bw.write("\t\t UPDATE " + tableName);
        bw.newLine();
        bw.write("\t\t SET ");

        bw.newLine();
        tempField = null;
        for (int i = 1; i < size; i++) {
            tempField = processField(columns.get(i));
            bw.write("\t\t\t " + columns.get(i) + " = #{" + tempField + "}");
            if (i != size - 1) {
                bw.write(",");
            }
            bw.newLine();
        }

        bw.write("\t\t WHERE " + columns.get(0) + " = #{"
                + processField(columns.get(0)) + "}");
        bw.newLine();
        bw.write("\t</update>");
        bw.newLine();
        bw.newLine();

        // 添加insert方法
        bw.write("\t<!-- 添加 -->");
        bw.newLine();
        bw.write("\t<insert id=\"batchInsertOrUpdate\" parameterType=\"java.util.List\">");
        bw.newLine();
        bw.write("\t\t INSERT INTO " + tableName +" (<include refid=\"Base_Column_List\" />) ");
        bw.newLine();
        bw.write("\t\t VALUES ");
        bw.newLine();
        bw.write("\t\t <foreach collection=\"list\" item=\"item\" index=\"index\" separator=\",\"> ");
        bw.newLine();
        bw.write("\t\t\t (");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                bw.write("\t\t\t ");
            }
            bw.write("#{item." + processField(columns.get(i)) + "}");
            if (i != size - 1) {
                bw.write(",");
                bw.newLine();
            }
        }
        bw.write(") ");
        bw.newLine();
        bw.write("\t\t </foreach>");
        bw.newLine();
        bw.write("\t\t ON DUPLICATE KEY UPDATE ");
        bw.newLine();
        for (int i = 1; i < size; i++) {
            bw.write("\t\t " + columns.get(i) + " = VALUES(" + columns.get(i) + ")");
            if (i != size - 1) {
                bw.write(",");
                bw.newLine();
            }
        }
        bw.newLine();
        bw.write("\t</insert>");
        bw.newLine();
        bw.newLine();
        // 添加insert完
    }

    /**
     * 获取所有的数据库表注释
     * 
     * @return
     * @throws SQLException
     */
    private Map<String, String> getTableComment() throws SQLException {
        Map<String, String> maps = new HashMap<String, String>();
        pstate = conn.prepareStatement("show table status");
        ResultSet results = pstate.executeQuery();
        while (results.next()) {
            String tableName = results.getString("NAME");
            String comment = results.getString("COMMENT");
            maps.put(tableName, comment);
        }
        return maps;
    }


    
    private void buildServiceImpl(List<String> columns, List<String> types, List<String> comments) throws Exception {

        File folder = new File(service_path+"/impl");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File mapperFile = new File(service_path+"/impl", beanName + "ServiceImpl.java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + service_package + ".impl;");
        bw.newLine();
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        bw.newLine();
        bw.write("import " + mapper_package + "." + beanName + "Dao;");
        bw.newLine();
        bw.write("import " + service_package + "." + beanName + "Service;");
        if (needRequest) {
            bw.newLine();
            bw.write("import " + request_package + "." + beanName + "Request;");
        }
        bw.newLine();
        bw.write("import org.springframework.beans.factory.annotation.Autowired;");
        bw.newLine();
        bw.write("import org.springframework.stereotype.Service;");
        bw.newLine();
        bw.write("import java.util.List;");
        bw.newLine();
        // bw.write("import tk.mybatis.mapper.common.Mapper;");
        bw = buildClassComment(bw, mapperName + "基本业务实现类");
        bw.newLine();
        bw.newLine();
        // bw.write("    public interface " + mapperName + " extends " +
        // mapper_extends+ "<" + beanName + "> {");
        bw.write("@Service");
        bw.newLine();
        bw.write("public class " + beanName + "ServiceImpl implements "  + beanName + "Service {"); //TODO--
        bw.newLine();
        bw.newLine();
        String poname =  beanName.substring(0, 1).toLowerCase()+ beanName.substring(1);
        bw.write("    /********************************* 自动生成器代码start ******************************************/");
        bw.newLine();
        bw.write("    @Autowired"); bw.newLine();
        bw.write("    private "+beanName+"Dao "+poname+"Dao;");
        bw.newLine();
        bw.newLine();
        bw.write("    /** 主键查询entity **/"); bw.newLine();
        bw.write("    public " + beanName + " selectByPrimaryKey("+processType(types.get(0))+" "+columns.get(0) + ") {"); //TODO--
        bw.newLine();
        bw.write("        return "+poname+"Dao.selectByPrimaryKey("+columns.get(0)+");");
        bw.newLine();
        bw.write("    }"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 过滤查询entity **/");bw.newLine();
        if (needRequest) {
            bw.write("    public List<" + beanName + "> selectFilter(" + beanName + "Request " + poname + ") {"); //TODO--
        } else {
            bw.write("    public List<" + beanName + "> selectFilter(" + beanName + " " + poname + ") {"); //TODO--
        }
        bw.newLine();
        bw.write("        return "+poname+"Dao.selectFilter("+poname+");");
        bw.newLine();
        bw.write("    }"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 主键删除entity **/");bw.newLine();
        bw.write("    public int deleteByPrimaryKey("+processType(types.get(0))+" "+columns.get(0) + ") {"); //TODO--
        bw.newLine();
        bw.write("        return "+poname+"Dao.deleteByPrimaryKey("+columns.get(0)+");");
        bw.newLine();
        bw.write("    }"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 添加entity 返回记录条数 **/");bw.newLine();
        bw.write("    public int " +"insert("+beanName+" " +poname + ") {"); //TODO--
        bw.newLine();
        bw.write("        return "+poname+"Dao.insert("+poname+");");
        bw.newLine();
        bw.write("    }"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 添加entity 返回主键Id **/");bw.newLine();
        if (needRequest) {
            bw.write("    public Long " + "insertGetId(" + beanName + "Request " + poname + ") {"); //TODO--
        } else {
            bw.write("    public Long " + "insertGetId(" + beanName + " " + poname + ") {"); //TODO--
        }
        bw.newLine();
        bw.write("        "+poname+"Dao.insertGetId("+poname+");");
        bw.newLine();
        String cc = columns.get(0).substring(0, 1).toUpperCase()+ columns.get(0).substring(1);
        bw.write("        return "+poname+".get"+cc+"();");
        bw.newLine();
        bw.write("    }"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 添加entity（匹配有值的字段） **/");bw.newLine();
        if (needRequest) {
            bw.write("    public int " + "insertSelective(" + beanName + "Request " + poname + ") {"); //TODO--
        } else {
            bw.write("    public int " + "insertSelective(" + beanName + " " + poname + ") {"); //TODO--
        }
        bw.newLine();
        bw.write("        return "+poname+"Dao.insertSelective("+poname+");");
        bw.newLine();
        bw.write("    }"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 修 改entity（匹配有值的字段） **/");bw.newLine();
        bw.write("    public int " +"updateByPrimaryKeySelective("+beanName+" "+poname + ") {"); //TODO--updateByPrimaryKeySelective
        bw.newLine();
        bw.write("        return "+poname+"Dao.updateByPrimaryKeySelective("+poname+");");
        bw.newLine();
        bw.write("    }"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 修 改entity全修改 **/");bw.newLine();
        bw.write("    public int " +"updateByPrimaryKey("+beanName+" "+poname + ") {"); //TODO--updateByPrimaryKeySelective
        bw.newLine();
        bw.write("        return "+poname+"Dao.updateByPrimaryKey("+poname+");");
        bw.newLine();
        bw.write("    }"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 批量插入或修改 **/");bw.newLine();
        bw.write("    public Integer " +"batchInsertOrUpdate(List<" + beanName + ">" +" list) {"); //TODO--updateByPrimaryKeySelective
        bw.newLine();
        bw.write("        return " + poname +"Dao.batchInsertOrUpdate(list);");
        bw.newLine();
        bw.write("    }"); //TODO--
        bw.newLine();
        bw.write("    /********************************* 自动生成器代码end ********************************************/");bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    
        
    }

    private void buildService(List<String> columns, List<String> types, List<String> comments) throws Exception {

        File folder = new File(service_path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File mapperFile = new File(service_path, beanName + "Service.java");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(mapperFile), "utf-8"));
        bw.write("package " + service_package + ";");
        bw.newLine();
        bw.newLine();
        bw.write("import " + bean_package + "." + beanName + ";");
        if (needRequest) {
            bw.newLine();
            bw.write("import " + request_package + "." + beanName + "Request;");
        }
        bw.newLine();
        bw.write("import java.util.List;");
        bw.newLine();
        // bw.write("import tk.mybatis.mapper.common.Mapper;");
        bw = buildClassComment(bw, mapperName + "基本业务接口类");
        bw.newLine();
        bw.newLine();
        // bw.write("    public interface " + mapperName + " extends " +
        // mapper_extends+ "<" + beanName + "> {");
        bw.write("public interface " + beanName + "Service" + " {"); //TODO--
        bw.newLine();
        bw.newLine();
        String poname =  beanName.substring(0, 1).toLowerCase()+ beanName.substring(1);
        bw.write("    /********************************* 自动生成器代码start ******************************************/");bw.newLine();
        bw.write("    /** 主键查询entity **/"); bw.newLine();
        bw.write("    public " + beanName + " selectByPrimaryKey("+processType(types.get(0))+" "+columns.get(0) + ");"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 过滤查询entity **/");bw.newLine();
        if (needRequest) {
            bw.write("    public List<" + beanName + "> selectFilter(" + beanName + "Request " + poname + ");"); //TODO--
        } else {
            bw.write("    public List<" + beanName + "> selectFilter(" + beanName + " " + poname + ");"); //TODO--
        }
        bw.newLine();
        bw.newLine();
        bw.write("    /** 主键删除entity **/");bw.newLine();
        bw.write("    public int deleteByPrimaryKey("+processType(types.get(0))+" "+columns.get(0) + ");"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 添加entity 返回记录条数 **/");bw.newLine();
        bw.write("    public int " +"insert("+beanName+" " +poname + ");"); //TODO--
        bw.newLine();
        bw.newLine();
        bw.write("    /** 添加entity 返回主键Id **/");bw.newLine();
        if (needRequest) {
            bw.write("    public Long " + "insertGetId(" + beanName + "Request " + poname + ");"); //TODO--
        } else {
            bw.write("    public Long " + "insertGetId(" + beanName + " " + poname + ");"); //TODO--
        }
        bw.newLine();
        bw.newLine();
        bw.write("    /** 添加entity（匹配有值的字段） **/");bw.newLine();
        if (needRequest) {
            bw.write("    public int " + "insertSelective(" + beanName + "Request " + poname + ");"); //TODO--
        } else {
            bw.write("    public int " + "insertSelective(" + beanName + " " + poname + ");"); //TODO--
        }
        bw.newLine();
        bw.newLine();
        bw.write("    /** 修 改entity（匹配有值的字段） **/");bw.newLine();
        bw.write("    public int " +"updateByPrimaryKeySelective("+beanName+" "+poname + ");"); //TODO--updateByPrimaryKeySelective
        bw.newLine();
        bw.newLine();
        bw.write("    /** 修 改entity全修改 **/");bw.newLine();
        bw.write("    public int " +"updateByPrimaryKey("+beanName+" "+poname + ");"); //TODO--updateByPrimaryKeySelective
        bw.newLine();
        bw.newLine();
        bw.write("    /** 批量插入或修改修改 **/");bw.newLine();
        bw.write("    public Integer " +"batchInsertOrUpdate(List<" + beanName + ">" +" list);"); //TODO--updateByPrimaryKeySelective
        bw.newLine();
        bw.write("    /********************************* 自动生成器代码end ********************************************/");bw.newLine();
        bw.newLine();
        bw.write("}");
        bw.newLine();
        bw.flush();
        bw.close();
    
        
    }


    public void generate(List<String> tablename)
            throws ClassNotFoundException, SQLException, Exception {
        init();
        String prefix = "show full fields from ";
        List<String> columns = null;
        List<String> types = null;
        List<String> comments = null;
        List<String> tables = getTables();
        if (tables.containsAll(tablename)) {
            tables.clear();
            tables.addAll(tablename);
        }
        Map<String, String> tableComments = getTableComment();
        for (String table : tables) {
            columns = new ArrayList<String>();
            types = new ArrayList<String>();
            comments = new ArrayList<String>();
            pstate = conn.prepareStatement(prefix + table);
            ResultSet results = pstate.executeQuery();
            while (results.next()) {
                columns.add(results.getString("FIELD"));
                types.add(results.getString("TYPE"));
                comments.add(results.getString("COMMENT"));
            }
            tableName = table;
            processTable(table);
            // this.outputBaseBean();
            String tableComment = tableComments.get(tableName);
            if (needRequest) {
                buildRequestBean(columns, types, comments, tableComment, table);
            }
            buildEntityBean(columns, types, comments, tableComment, table);
            buildMapper(columns, types, comments);
            buildService(columns, types, comments);
            buildServiceImpl(columns, types, comments);
            buildMapperXml(columns, types, comments);
        }
        pstate.close();
        conn.close();
    }

    public static void main(String[] args) throws Exception {
        try {
            List<String> tables = new ArrayList<String>();
            //tables.add("t_club_budget");
            /*tables.add("t_commission_operate_log");
            tables.add("t_commission_provision");
            tables.add("t_commission");
            tables.add("t_settlements");
            tables.add("t_settlement_items_xf");
            tables.add("t_settlement_items_esf");
            tables.add("t_settlement_orders_xf");
            tables.add("t_settlement_orders_esf");
            tables.add("t_settlement_order_balance");
            tables.add("t_audit_chain");
            tables.add("t_club_budget");*/
            //tables.add("t_settlements");
            //tables.add("t_settlement_items_xf");
            //tables.add("t_settlement_items_esf");
           // tables.add("t_club_budget");
            //tables.add("t_commission");
            tables.add("t_test_");
            //tables.add("t_commission");
            //tables.add("t_commission_provision");
            //tables.add("t_club_budget");
            new EntityMyBatis().generate(tables);
            // 自动打开生成文件的目录
            //Runtime.getRuntime().exec("cmd /c start explorer D:tmp/");
            System.out.println("success");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

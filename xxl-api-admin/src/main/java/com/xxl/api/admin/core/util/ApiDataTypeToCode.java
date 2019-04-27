package com.xxl.api.admin.core.util;

import com.xxl.api.admin.core.model.XxlApiDataType;
import com.xxl.api.admin.core.model.XxlApiDataTypeField;
import com.xxl.api.admin.core.util.tool.DateTool;

import java.util.*;


/**
 * Created by xuxueli on 17/6/19.
 */
public class ApiDataTypeToCode {

    public static void main(String[] args) {

        // base data-type
        XxlApiDataType StringType = new XxlApiDataType();
        StringType.setName("String");
        StringType.setAbout("字符串类型");

        // field
        List<XxlApiDataTypeField> fieldList = new ArrayList<XxlApiDataTypeField>();

        XxlApiDataTypeField field1 = new XxlApiDataTypeField();
        field1.setFieldName("name");
        field1.setFieldAbout("姓名");
        field1.setFieldType(0);
        field1.setFieldDatatype(StringType);
        fieldList.add(field1);

        XxlApiDataTypeField field2 = new XxlApiDataTypeField();
        field2.setFieldName("otherNames");
        field2.setFieldAbout("其他别名");
        field2.setFieldType(1);
        field2.setFieldDatatype(StringType);
        fieldList.add(field2);

        // dto
        XxlApiDataType apiDataTypeDTO = new XxlApiDataType();
        apiDataTypeDTO.setName("User");
        apiDataTypeDTO.setAbout("用户信息");
        apiDataTypeDTO.setFieldList(fieldList);

        String content = parseDataTypeToCode(apiDataTypeDTO);
        System.out.println(content);

    }

    /**
     * 根据DataType生成代码
     *
     * @param apiDataTypeDTO
     * @return
     */
    public static String parseDataTypeToCode(XxlApiDataType apiDataTypeDTO) {
        StringBuffer sb = new StringBuffer();

        // package
        sb.append("\r\n");

        // import
        Set<String> importSet = new HashSet<String>();
        if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                String fieldTypeImportItem = field.getFieldDatatype().getName();

                if (fieldTypeImportItem!=null && fieldTypeImportItem.equalsIgnoreCase("date")) {
                    String importItem = "import java.util.Date;";
                    importSet.add(importItem);
                    sb.append(importItem + "\r\n");
                }
                if (field.getFieldType() == 1) {
                    String importItem = "import java.util.List;";
                    importSet.add(importItem);
                    sb.append(importItem + "\r\n");
                }
            }
            sb.append("\r\n");
        }

        // 类注释
        sb.append("/**\r\n");
        sb.append("*\t" + apiDataTypeDTO.getAbout()+ "\r\n");
        sb.append("*\r\n");
        sb.append("*\tCreated by XXL-API on "+ DateTool.formatDate(new Date()) +".\r\n");
        sb.append("*/ \r\n");

        // 实体部分
        sb.append("public class " + upFirst(apiDataTypeDTO.getName())  + "Response {");
        sb.append("\r\n\r\n");

        // field
        if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                String fieldTypeItem = matchJavaType(field.getFieldDatatype().getName());
                String fieldNameItem = field.getFieldName();
                if (field.getFieldType() == 1) {
                    fieldTypeItem = "List<"+ fieldTypeItem +">";
                }
                sb.append("\tprivate " + fieldTypeItem + " " + fieldNameItem + ";\r\n");

            }
            sb.append("\r\n");
        }

        // get set
        if (apiDataTypeDTO.getFieldList()!=null && apiDataTypeDTO.getFieldList().size()>0) {
            for (XxlApiDataTypeField field: apiDataTypeDTO.getFieldList()) {
                String fieldTypeItem = matchJavaType(field.getFieldDatatype().getName());
                String fieldNameItem = field.getFieldName();
                String fieldNameItemUpFirst = upFirst(field.getFieldName());

                if (field.getFieldType() == 1) {
                    fieldTypeItem = "List<"+ fieldTypeItem +">";
                }

                sb.append("\tpublic void set" + fieldNameItemUpFirst + "(" + fieldTypeItem + " " + fieldNameItem + "){\r\n");
                sb.append("\t\tthis." + fieldNameItem + "=" + fieldNameItem + ";\r\n");
                sb.append("\t}\r\n");
                sb.append("\tpublic " + fieldTypeItem + " get" + fieldNameItemUpFirst + "(){\r\n");
                sb.append("\t\treturn this." + fieldNameItem + ";\r\n");
                sb.append("\t}\r\n");

            }
            sb.append("\r\n");
        }

        sb.append("}\r\n");

        return sb.toString();
    }


    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    private static String upFirst(String str) {

        char[] ch = str.toCharArray();
        if(ch[0] >= 'a' && ch[0] <= 'z'){
            ch[0] = (char)(ch[0] - 32);
        }

        return new String(ch);
    }

    /**
     * 匹配数据类型
     *
     * @param paramDataType
     * @return
     */
    private static String matchJavaType(String paramDataType) {

        if(paramDataType.equalsIgnoreCase("string")){
            return "String";
        } else if(paramDataType.equalsIgnoreCase("boolean")){
            return "boolean";
        } else if(paramDataType.equalsIgnoreCase("short")){
            return "short";
        } else if(paramDataType.equalsIgnoreCase("int")){
            return "int";
        } else if(paramDataType.equalsIgnoreCase("long")){
            return "long";
        } else if(paramDataType.equalsIgnoreCase("float")){
            return "float";
        } else if(paramDataType.equalsIgnoreCase("double")){
            return "double";
        } else if(paramDataType.equalsIgnoreCase("date") || paramDataType.equalsIgnoreCase("datetime")) {
            return "Date";
        } else if(paramDataType.equalsIgnoreCase("byte")){
            return "byte";
        }

        return paramDataType;
    }


}

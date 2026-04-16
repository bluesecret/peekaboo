package io.wangk.peekaboo.server.admin.handler;


import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

/**
 * @author bijie
 * @since 2024/4/12
 */
public class SexConverter implements Converter<String> {



    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public String convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
      if ("男".equals(cellData.getStringValue())){
          return "0";
      }else if ("女".equals(cellData.getStringValue())){
          return "1";
      }
      return null;
    }


//    @Override
//    public WriteCellData<?> convertToExcelData(String value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
//        return null;
//    }

}

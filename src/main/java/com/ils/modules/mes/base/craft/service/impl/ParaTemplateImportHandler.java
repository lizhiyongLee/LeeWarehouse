package com.ils.modules.mes.base.craft.service.impl;

import com.ils.common.exception.ILSBootException;
import com.ils.modules.mes.base.craft.entity.ParaTemplateDetail;
import com.ils.modules.mes.base.craft.entity.ParaTemplateHead;
import com.ils.modules.mes.base.craft.entity.Parameter;
import com.ils.modules.mes.base.craft.service.ParaTemplateHeadService;
import com.ils.modules.mes.base.craft.service.ParameterService;
import com.ils.modules.mes.base.craft.vo.ParameterTemplateVO;
import com.ils.modules.mes.enums.FieldTypeEnum;
import com.ils.modules.system.service.AbstractExcelHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * 参数模板导入类
 *
 * @author Anna.
 * @date 2021/10/18 11:46
 */
@Service
@Slf4j
public class ParaTemplateImportHandler extends AbstractExcelHandler {
    private static final String DETAIL = "detail";
    private final String IGNORE_PROPERTY = "id,createBy,createTime,updateBy,updateTime,deleted,tenantId";
    private final String NUM_PARA_TEMPLATE_STANDARD_TYPE = ">,<,=,>=,<=,区间,允差";
    private final String SWITCH_PARA_TEMPLATE_STANDARD_TYPE = "开启,关闭";
    @Autowired
    private ParaTemplateHeadService paraTemplateHeadService;
    @Autowired
    private ParameterService parameterService;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void importExcel(List<Map<String, Object>> dataList) {
        String[] ignoreProperty = IGNORE_PROPERTY.split(",");
        //主表
        Class<ParaTemplateHead> paraTemplateHeadClass = ParaTemplateHead.class;
        //关联表
        Class<ParaTemplateDetail> paraTemplateDetailClass = ParaTemplateDetail.class;

        List<Parameter> parameterList = parameterService.list();

        //获取属性列表
        Map<String, String> headFieldNameTypeMap = this.getFieldNameList(paraTemplateHeadClass);
        Map<String, String> detailFieldNameTypeMap = this.getFieldNameList(paraTemplateDetailClass);

        Map<String, ParameterTemplateVO> parameterTemplateVOMap = new HashMap<>(16);
        Set<String> keyNameSet = new HashSet<>();
        for (Map<String, Object> data : dataList) {
            keyNameSet.add((String) data.get("paraTempName"));
        }
        //根据唯一属性建立获取主表
        keyNameSet.forEach(keyName -> {
            ParameterTemplateVO parameterTemplateVO = new ParameterTemplateVO();
            List<ParaTemplateDetail> paraTemplateDetailList = new ArrayList<>();
            parameterTemplateVO.setParaTemplateDetailList(paraTemplateDetailList);
            parameterTemplateVOMap.put(keyName, parameterTemplateVO);
        });
        //根据数据为主表实体赋值，为关联表建立实体并且赋值
        initData(dataList, paraTemplateHeadClass, paraTemplateDetailClass, parameterList, headFieldNameTypeMap, detailFieldNameTypeMap, parameterTemplateVOMap);

        //保存
        Collection<ParameterTemplateVO> values = parameterTemplateVOMap.values();
        values.forEach(parameterTemplateVO -> paraTemplateHeadService.saveParaTemplateHead(parameterTemplateVO));
    }

    /**
     * 赋值数据
     *
     * @param dataList
     * @param paraTemplateHeadClass
     * @param paraTemplateDetailClass
     * @param parameterList
     * @param headFieldNameMap
     * @param detailFieldNameMap
     * @param headMap
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws ParseException
     */
    private void initData(List<Map<String, Object>> dataList, Class<ParaTemplateHead> paraTemplateHeadClass, Class<ParaTemplateDetail> paraTemplateDetailClass, List<Parameter> parameterList, Map<String, String> headFieldNameMap, Map<String, String> detailFieldNameMap, Map<String, ParameterTemplateVO> headMap) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
        for (Map<String, Object> data : dataList) {
            //检查
            boolean check = true;
            ParameterTemplateVO parameterTemplateVO = headMap.get(data.get("paraTempName"));
            ParaTemplateDetail paraTemplateDetail = new ParaTemplateDetail();
            for (String s : data.keySet()) {
                if (headFieldNameMap.containsKey(s)) {
                    this.invokeData(paraTemplateHeadClass, s, parameterTemplateVO, (String) data.get(s), headFieldNameMap.get(s));
                }
                if (detailFieldNameMap.containsKey(s)) {
                    if (s.contains("paraStandard")) {
                        if (NUM_PARA_TEMPLATE_STANDARD_TYPE.contains((String)data.get(s))) {
                            String[] split = NUM_PARA_TEMPLATE_STANDARD_TYPE.split(",");
                            for (int i = 0; i < split.length; i++) {
                                if (split[i].equals(data.get(s))) {
                                    paraTemplateDetail.setParaStandard(i + 1 + "");
                                    check = false;
                                }
                            }
                        }
                        if (SWITCH_PARA_TEMPLATE_STANDARD_TYPE.contains((String)data.get(s))) {
                            String[] split = SWITCH_PARA_TEMPLATE_STANDARD_TYPE.split(",");
                            for (int i = 0; i < split.length; i++) {
                                if (split[i].equals(data.get(s))) {
                                    paraTemplateDetail.setParaStandard(i + 1 + "");
                                    check = false;
                                }
                            }
                        }
                    } else if (s.contains("paraName")) {
                        for (Parameter parameter : parameterList) {
                            if (parameter.getParaName().equals(data.get(s))) {
                                paraTemplateDetail.setParaId(parameter.getId());
                                paraTemplateDetail.setParaName((String) data.get(s));
                                check = false;
                            }
                        }
                    } else {
                        this.invokeData(paraTemplateDetailClass, s, paraTemplateDetail, (String) data.get(s), detailFieldNameMap.get(s));
                    }
                }
            }
            if (check) {
                throw new ILSBootException("参数模板导入数据错误，错误参数名称:" + parameterTemplateVO.getParaTempName());
            }
            parameterTemplateVO.getParaTemplateDetailList().add(paraTemplateDetail);
        }
    }

    /**
     * 根据类获取属性
     *
     * @param clazz
     * @return
     */
    private Map<String, String> getFieldNameList(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, String> fieldNameTypeMap = new HashMap<>(16);
        for (Field field : fields) {
            fieldNameTypeMap.put(field.getName(), field.getType().getName());
        }
        return fieldNameTypeMap;
    }

    /**
     * 赋值函数
     *
     * @param clazz  当前类
     * @param field  赋值的属性
     * @param object 被赋值的对象
     * @param data   数据来源
     * @param type   属性的类型
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void invokeData(Class<?> clazz, String field, Object object, String data, String type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ParseException {
        boolean check = false;
        Method method = null;
        if (type.contains(FieldTypeEnum.STRING.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), String.class);
            method.invoke(object, data);
            check = true;
        }
        if (type.contains(FieldTypeEnum.BIG_DECIMAL.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), BigDecimal.class);
            BigDecimal newData = new BigDecimal(data);
            method.invoke(object, newData);
            check = true;
        }
        if (type.contains(FieldTypeEnum.INTEGER.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), Integer.class);
            Integer newData = new Integer(data);
            method.invoke(object, newData);
            check = true;
        }
        if (type.contains(FieldTypeEnum.LONG.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), Long.class);
            Long newData = new Long(data);
            method.invoke(object, newData);
            check = true;
        }
        if (type.contains(FieldTypeEnum.DATE.getValue())) {
            method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(field), Date.class);
            Date date = DateUtils.parseDate(data, "yyyy-MM-dd HH:mm:ss");
            method.invoke(object, date);
            check = true;
        }
        if (!check) {
            throw new NoSuchMethodException();
        }
    }
}

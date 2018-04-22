package com.concur.meta.client.conversion;

import java.lang.reflect.Modifier;
import java.util.Set;

import com.concur.meta.client.conversion.conveters.ConverterPackageInfo;
import com.concur.meta.client.utils.ClassPathScanHandler;

/**
 * 转换器注册
 *
 * @author yongfu.cyf
 * @create 2017-07-27 下午3:30
 **/
public class ConverterRegistry {

    protected ConverterService converterService;

    public ConverterRegistry(ConverterService converterService) {
        this.converterService = converterService;
    }

    private void registerClass(Set<Class<? extends Converter>> classes) {
        for (Class<? extends Converter> clazz : classes) {
            if (Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }
            try {
                Converter converter = clazz.newInstance();

                this.converterService.registerConverter(converter);
                if (converter instanceof ConverterAdapter) {
                    ((ConverterAdapter)converter)
                        .setConverterService(converterService)
                        .postRegister();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() throws Exception {
        // 注册
        String scanPath = ConverterPackageInfo.class.getPackage().getName();
        ClassPathScanHandler convertorScan = new ClassPathScanHandler(scanPath);
        Set<Class<? extends Converter>> converterClass = convertorScan.getAllSubClassesByParent(Converter.class);
        this.registerClass(converterClass);
    }
}

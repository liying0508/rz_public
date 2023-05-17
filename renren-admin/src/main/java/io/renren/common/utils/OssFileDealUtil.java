package io.renren.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.renren.modules.financing.dao.FDirectFinancingDao;
import io.renren.modules.financing.vo.FileListVO;
import io.renren.modules.financing.vo.FileVO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OssFileDealUtil {
    @Autowired
    private FDirectFinancingDao fDirectFinancingDao;

    @Autowired
    private static FDirectFinancingDao staticFDirectFinancingDao;

    @PostConstruct
    public void init() {
        staticFDirectFinancingDao = this.fDirectFinancingDao;
    }

    public static Map<String, List<String>> clearSurplusInOSSServer() {
        List<String> allFilesList = staticFDirectFinancingDao.getAllFilesInDataBase();
        List<String> allUrlList = new ArrayList<>();
        int i;
        for (i = 0; i < allFilesList.size(); i++) {
            String files = allFilesList.get(i);
            if (files != null) {
                List<FileListVO> fileListVOs = JSON.parseObject(files,new TypeReference<List<FileListVO>>(){});
                for (FileListVO fileListVO : fileListVOs) {
                    String fileStr = fileListVO.getFileStr();
                    List<FileVO> fileVOs = JSON.parseObject(fileStr,new TypeReference<List<FileVO>>(){});
                    for (FileVO fileVO : fileVOs)
                        allUrlList.add(fileVO.getUrl());
                }
            }
        }
        for (i = 0; i < allUrlList.size(); i++) {
            String substring = StringUtils.substring(allUrlList.get(i), 1);
            allUrlList.set(i, substring);
        }
        Map<String, List<String>> urlMap = new HashMap<>();
        for (String s : allUrlList) {
            String documentName = StringUtils.substring(s, 57, 65);
            List<String> list = new ArrayList<>();
            list.add(s);
            if (urlMap.containsKey(documentName)) {
                ((List<String>)urlMap.get(documentName)).addAll(list);
                continue;
            }
            urlMap.put(documentName, list);
        }
        return urlMap;
    }
}


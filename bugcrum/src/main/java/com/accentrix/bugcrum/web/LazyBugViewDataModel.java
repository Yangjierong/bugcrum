package com.accentrix.bugcrum.web;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.accentrix.bugcrum.domain.bugzilla.Bug;
import com.accentrix.bugcrum.service.BugService;

@Configurable(preConstruction = true)
public class LazyBugViewDataModel extends LazyDataModel<Bug> {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(LazyBugViewDataModel.class);
    @Autowired
    private BugService bugService;

    private Integer productId;
    private List<Integer> selectedComponentIds;
    private List<String> selectedBugStatus;
    private String searchDesc;

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setSelectedComponentIds(List<Integer> selectedComponentIds) {
        this.selectedComponentIds = selectedComponentIds;
    }

    public void setSelectedBugStatus(List<String> selectedBugStatus) {
        this.selectedBugStatus = selectedBugStatus;
    }

    public void setSearchDesc(String searchDesc) {
        this.searchDesc = searchDesc;
    }

    public LazyBugViewDataModel(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus) {
        this.productId = productId;
        this.selectedComponentIds = selectedComponentIds;
        this.selectedBugStatus = selectedBugStatus;

    }

    @Override
    public Bug getRowData(String rowKey) {
        log.debug("getRowData {}", rowKey);
        return bugService.findOne(Integer.parseInt(rowKey));
    }

    @Override
    public Object getRowKey(Bug bug) {
        log.debug("getRowKey");
        return bug.getId();
    }

    @Override
    public List<Bug> load(int first, int pageSize, String sortField, SortOrder sortOrder,
            Map<String, Object> filters) {
        if (log.isDebugEnabled()) {
            log.debug("load, first:{}, pageSize:{}", first, pageSize);
            log.debug("sortField:{}, sortOrder:{}", sortField, sortOrder);
        }

        // log.info("selectedComponentIds: {}", selectedComponentIds);

        if (selectedComponentIds == null) {
            log.info("selectedComponentIds is null");
        } else {
            for (Object cid : selectedComponentIds) {
                log.info("class: {}, v: {}", cid.getClass(), cid);
            }
        }

        List<Bug> bugs = bugService.findAllBySearchCriteria(productId, selectedComponentIds,
                selectedBugStatus, searchDesc, first, pageSize, sortField, sortOrder.toString());
        long count = bugService.count(productId, selectedComponentIds, selectedBugStatus,
                searchDesc);

        this.setRowCount((int) count);

        if (log.isDebugEnabled()) {
            log.debug("bugs size: {}", bugs.size());
            log.debug("count: {}", count);
        }

        return bugs;

    }

}

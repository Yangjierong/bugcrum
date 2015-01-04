package com.accentrix.bugcrum.repository.custom;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.accentrix.bugcrum.domain.bugzilla.Bug;
import com.accentrix.bugcrum.domain.bugzilla.QBug;
import com.accentrix.bugcrum.repository.BugRepositoryCustom;
import com.accentrix.nttca.dcms.common.repository.JpaDslQuery;

public class BugRepositoryImpl extends JpaDslQuery<Bug, QBug> implements BugRepositoryCustom {

    @Override
    public List<Bug> find(Integer bugId, Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus) {
        eq($.id, bugId);
        eq($.product.id, productId);
        in($.component.id, selectedComponentIds);
        in($.status, selectedBugStatus);

        return list();
    }

    @Override
    public List<Bug> findAll(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc, long offset, long limit,
            String sortField, String sortOrder) {
        searchCrit(productId, selectedComponentIds, selectedBugStatus, searchDesc);

        if (StringUtils.isBlank(sortField)) {
            sortField = "priority";
        }

        if (!"UNSORTED".equals(sortOrder)) {
            Direction dirction = Direction.ASC;
            if ("DESCENDING".equals(sortOrder)) {
                dirction = Direction.DESC;
            }

            Sort sort = new Sort(dirction, sortField);

            addSort(sort);
        }

        return list(offset, limit);
    }

    @Override
    public long count(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc) {
        searchCrit(productId, selectedComponentIds, selectedBugStatus, searchDesc);
        return count();
    }

    @Override
    public Double sumEstimatedTime(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc) {
        searchCrit(productId, selectedComponentIds, selectedBugStatus, searchDesc);

        return sum($.estimatedTime);
    }

    private void searchCrit(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc) {
        eq($.product.id, productId);
        in($.component.id, selectedComponentIds);
        in($.status, selectedBugStatus);
        contains($.shortDesc, searchDesc);
    }
}

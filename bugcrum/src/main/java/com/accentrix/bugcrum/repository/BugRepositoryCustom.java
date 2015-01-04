package com.accentrix.bugcrum.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.accentrix.bugcrum.domain.bugzilla.Bug;

@Repository
public interface BugRepositoryCustom {

    List<Bug> find(Integer bugId, Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus);

    List<Bug> findAll(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc, long offset, long limit,
            String sortField, String sortOrder);

    long count(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc);

    Double sumEstimatedTime(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc);
}

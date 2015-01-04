/*
 * $Id: BugServiceImpl.java 100 2014-12-30 10:23:53Z jierong $
 * 
 * Copyright (c) 2001-2008 Accentrix Company Limited. All Rights Reserved.
 * 
 * Accentrix Company Limited. ("Accentrix") retains copyright on all text, source
 * and binary code contained in this software and documentation. Accentrix grants
 * Licensee a limited license to use this software, provided that this copyright
 * notice and license appear on all copies of the software. The software source
 * code is provided for reference purposes only and may not be copied, modified 
 * or distributed.
 * 
 * THIS SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS," WITHOUT ANY WARRANTY OF
 * ANY KIND UNLESS A SEPARATE WARRANTIES IS PURCHASED FROM ACCENTRIX AND REMAINS
 * VALID.  ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. ACCENTRIX SHALL NOT BE LIABLE
 * FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING OR MODIFYING THE
 * SOFTWARE OR ITS DERIVATIVES.
 * 
 * IN NO EVENT WILL ACCENTRIX BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR
 * FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE
 * USE OF OR INABILITY TO USE SOFTWARE, EVEN IF ACCENTRIX HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 */
package com.accentrix.bugcrum.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accentrix.bugcrum.domain.FieldId;
import com.accentrix.bugcrum.domain.bugzilla.Bug;
import com.accentrix.bugcrum.domain.bugzilla.Component;
import com.accentrix.bugcrum.dto.BugActivityDto;
import com.accentrix.bugcrum.repository.BugRepository;
import com.accentrix.bugcrum.service.BugService;

@Service
@Transactional
public class BugServiceImpl implements BugService {

    @Autowired
    private BugRepository bugRepository;

    @Override
    public Bug save(Bug entity) {
        return bugRepository.save(entity);
    }

    @Override
    public List<Bug> save(List<Bug> entities) {
        return bugRepository.save(entities);
    }

    @Override
    public Bug findOne(Integer id) {
        return bugRepository.findOne(id);
    }

    @Override
    public boolean exists(Integer id) {
        return bugRepository.exists(id);
    }

    @Override
    public List<Bug> findAll() {
        return bugRepository.findAll();
    }

    @Override
    public List<Bug> findAll(List<Integer> ids) {
        return bugRepository.findAll(ids);
    }

    @Override
    public long count() {
        return bugRepository.count();
    }

    @Override
    public void delete(Integer id) {
        bugRepository.delete(id);
    }

    @Override
    public void delete(Bug entity) {
        bugRepository.delete(entity);
    }

    @Override
    public void delete(List<Bug> entities) {
        bugRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        bugRepository.deleteAll();
    }

    @Override
    public List<Bug> findAllByComponentAndStatus(Component component, String status) {
        return bugRepository.findAllByComponentAndStatus(component, status);
    }

    @Override
    public List<Bug> findAllBySearchCriteria(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc, long offset, long limit,
            String sortField, String sortOrder) {
        if (selectedComponentIds == null || selectedComponentIds.isEmpty()) {
            return Collections.emptyList();
        }

        if (selectedBugStatus == null || selectedBugStatus.isEmpty()) {
            return Collections.emptyList();
        }

        return bugRepository.findAll(productId, selectedComponentIds, selectedBugStatus,
                searchDesc, offset, limit, sortField, sortOrder);
    }

    @Override
    public List<BugActivityDto> findBugsWorkdHoursByFilter(Date startDate, Date endDate,
            List<Integer> bugIds) {
        return bugRepository.findBugsWorkdHoursByFilter(startDate, endDate, bugIds);
    }

    @Override
    public long count(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc) {
        if (selectedComponentIds == null || selectedComponentIds.isEmpty()) {
            return 0;
        }

        if (selectedBugStatus == null || selectedBugStatus.isEmpty()) {
            return 0;
        }

        return bugRepository.count(productId, selectedComponentIds, selectedBugStatus, searchDesc);
    }

    @Override
    public BugActivityDto getLastRemainingTime(Bug bug, Date time) {
        List<BugActivityDto> list = bugRepository.getPassedActivityByFieldId(bug.getId(),
                FieldId.REMAINING_TIME, time);
        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public BugActivityDto getFirstRemainingTime(Bug bug, Date time) {
        List<BugActivityDto> list = bugRepository.getComingActivityByFieldId(bug.getId(),
                FieldId.REMAINING_TIME, time);
        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }

    @Override
    public Double sumEstimatedTime(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc) {
        if (selectedComponentIds == null || selectedComponentIds.isEmpty()) {
            return 0d;
        }

        if (selectedBugStatus == null || selectedBugStatus.isEmpty()) {
            return 0d;
        }

        return bugRepository.sumEstimatedTime(productId, selectedComponentIds, selectedBugStatus,
                searchDesc);
    }

    @Override
    public boolean isClosedBefor(Bug bug, Date time) {
        List<BugActivityDto> list = bugRepository.getPassedActivityByFieldId(bug.getId(),
                FieldId.BUG_STATUS, time);
        if (list.isEmpty()) {
            return false;
        }

        return "CLOSED".equals(list.get(0).getAddedValue());
    }

}

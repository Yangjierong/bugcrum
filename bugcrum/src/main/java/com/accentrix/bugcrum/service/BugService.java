/*
 * $Id: BugService.java 100 2014-12-30 10:23:53Z jierong $
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
package com.accentrix.bugcrum.service;

import java.util.Date;
import java.util.List;

import com.accentrix.bugcrum.domain.bugzilla.Bug;
import com.accentrix.bugcrum.domain.bugzilla.Component;
import com.accentrix.bugcrum.dto.BugActivityDto;

public interface BugService {

    Bug save(Bug entity);

    List<Bug> save(List<Bug> entities);

    Bug findOne(Integer id);

    boolean exists(Integer id);

    List<Bug> findAll();

    List<Bug> findAll(List<Integer> ids);

    long count();

    void delete(Integer id);

    void delete(Bug entity);

    void delete(List<Bug> entities);

    void deleteAll();

    List<Bug> findAllByComponentAndStatus(Component component, String status);

    List<Bug> findAllBySearchCriteria(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc, long offset, long limit,
            String sortField, String sortOrder);

    long count(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc);

    List<BugActivityDto> findBugsWorkdHoursByFilter(Date startDate, Date endDate,
            List<Integer> bugIds);

    /**
     * get last RemainingTime before <code>time</code>
     * 
     * @param bug
     * @param time
     * @return
     */
    BugActivityDto getLastRemainingTime(Bug bug, Date time);

    /**
     * get first RemainingTime after <code>time</code>
     * 
     * @param bug
     * @param time
     * @return
     */
    BugActivityDto getFirstRemainingTime(Bug bug, Date time);

    Double sumEstimatedTime(Integer productId, List<Integer> selectedComponentIds,
            List<String> selectedBugStatus, String searchDesc);

    boolean isClosedBefor(Bug bug, Date time);

}

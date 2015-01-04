/*
 * $Id: BugRepository.java 100 2014-12-30 10:23:53Z jierong $
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
package com.accentrix.bugcrum.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.accentrix.bugcrum.domain.FieldId;
import com.accentrix.bugcrum.domain.bugzilla.Bug;
import com.accentrix.bugcrum.domain.bugzilla.Component;
import com.accentrix.bugcrum.dto.BugActivityDto;

@Repository
public interface BugRepository extends JpaRepository<Bug, Integer>, BugRepositoryCustom {

    @Query("FROM Bug WHERE component = ?1 AND status = ?2")
    List<Bug> findAllByComponentAndStatus(Component component, String status);

    @Query("SELECT new com.accentrix.bugcrum.dto.BugActivityDto(ba.bug.id, ba.when, ba.fieldId, ba.added) FROM BugActivity ba WHERE ba.when BETWEEN ?1 AND ?2 AND ba.bug.id IN (?3) AND ba.fieldId = "
            + FieldId.REMAINING_TIME + " AND ba.bug.status <> 'CLOSE' ORDER BY ba.when")
    List<BugActivityDto> findBugsWorkdHoursByFilter(Date startDate, Date endDate,
            List<Integer> bugIds);

    @Query("SELECT new com.accentrix.bugcrum.dto.BugActivityDto(ba.bug.id, ba.when, ba.fieldId, ba.added) FROM BugActivity ba WHERE ba.when BETWEEN ?1 AND ?2 AND ba.bug.id IN (?3) AND ba.fieldId = "
            + FieldId.REMAINING_TIME
            + " AND ba.bug.assignedTo.id = ?4 AND ba.bug.status <> 'CLOSE' ORDER BY ba.when")
    List<BugActivityDto> findBugsWorkdHoursByFilter(Date startDate, Date endDate,
            List<Integer> bugIds, Integer userId);

    // SELECT * FROM bugs_activity WHERE bug_id =6184 and fieldid=41 and
    // bug_when < '2014-12-04 23:17:08.0' order by bug_when desc
    @Query("SELECT new com.accentrix.bugcrum.dto.BugActivityDto(ba.bug.id, ba.when, ba.fieldId, ba.added) FROM BugActivity ba "
            + "WHERE ba.bug.id = ?1 AND ba.fieldId = ?2 AND ba.when < ?3 ORDER BY ba.when desc")
    List<BugActivityDto> getPassedActivityByFieldId(Integer bugId, int fieldId, Date time);

    @Query("SELECT new com.accentrix.bugcrum.dto.BugActivityDto(ba.bug.id, ba.when, ba.fieldId, ba.added) FROM BugActivity ba "
            + "WHERE ba.bug.id = ?1 AND ba.fieldId = ?2 AND ba.when > ?3 ORDER BY ba.when asc")
    List<BugActivityDto> getComingActivityByFieldId(Integer bugId, int fieldId, Date time);
}

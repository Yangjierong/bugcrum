/*
 * $Id: SprintServiceImpl.java 94 2014-12-29 07:06:15Z jierong $
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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accentrix.bugcrum.domain.scrum.Sprint;
import com.accentrix.bugcrum.domain.scrum.SprintBug;
import com.accentrix.bugcrum.repository.SprintBugRepository;
import com.accentrix.bugcrum.repository.SprintRepository;
import com.accentrix.bugcrum.service.SprintService;

@Service
@Transactional
public class SprintServiceImpl implements SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private SprintBugRepository sprintBugRepository;

    @Override
    public Sprint save(Sprint entity) {
        return sprintRepository.save(entity);
    }

    @Override
    public List<Sprint> save(List<Sprint> entities) {
        return sprintRepository.save(entities);
    }

    @Override
    public Sprint findOne(Integer id) {
        return sprintRepository.findOne(id);
    }

    @Override
    public boolean exists(Integer id) {
        return sprintRepository.exists(id);
    }

    @Override
    public List<Sprint> findAll() {
        return sprintRepository.findAll();
    }

    @Override
    public List<Sprint> findAll(List<Integer> ids) {
        return sprintRepository.findAll(ids);
    }

    @Override
    public long count() {
        return sprintRepository.count();
    }

    @Override
    public void delete(Integer id) {
        List<SprintBug> sprintBugs = sprintBugRepository.findBySprintId(id);
        for (SprintBug sprintBug : sprintBugs) {
            sprintBugRepository.delete(sprintBug);
        }
        sprintRepository.delete(id);
    }

    @Override
    public void delete(Sprint entity) {
        delete(entity.getId());
    }

    @Override
    public void delete(List<Sprint> entities) {
        sprintRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        sprintRepository.deleteAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sprint> findByProductId(Integer productId) {
        return sprintRepository.findByProductIdOrderByStartDateDesc(productId);
    }

}

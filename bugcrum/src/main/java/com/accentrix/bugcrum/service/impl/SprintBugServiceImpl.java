/*
 * $Id: SprintBugServiceImpl.java 62 2014-12-11 10:32:35Z joe $
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

import com.accentrix.bugcrum.domain.scrum.SprintBug;
import com.accentrix.bugcrum.repository.SprintBugRepository;
import com.accentrix.bugcrum.service.SprintBugService;

@Service
@Transactional
public class SprintBugServiceImpl implements SprintBugService {

    @Autowired
    private SprintBugRepository sprintBugRepository;

    @Override
    public SprintBug save(SprintBug entity) {
        return sprintBugRepository.save(entity);
    }

    @Override
    public List<SprintBug> save(List<SprintBug> entities) {
        return sprintBugRepository.save(entities);
    }

    @Override
    public SprintBug findOne(Integer id) {
        return sprintBugRepository.findOne(id);
    }

    @Override
    public boolean exists(Integer id) {
        return sprintBugRepository.exists(id);
    }

    @Override
    public List<SprintBug> findAll() {
        return sprintBugRepository.findAll();
    }

    @Override
    public List<SprintBug> findAll(List<Integer> ids) {
        return sprintBugRepository.findAll(ids);
    }

    @Override
    public long count() {
        return sprintBugRepository.count();
    }

    @Override
    public void delete(Integer id) {
        sprintBugRepository.delete(id);
    }

    @Override
    public void delete(SprintBug entity) {
        sprintBugRepository.delete(entity);
    }

    @Override
    public void delete(List<SprintBug> entities) {
        sprintBugRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        sprintBugRepository.deleteAll();
    }

    @Override
    public List<SprintBug> findBySprintId(Integer sprintId) {
        return sprintBugRepository.findBySprintId(sprintId);
    }

    @Override
    public List<SprintBug> findBy(Integer sprintId, Integer userId) {
        return sprintBugRepository.findBy(sprintId, userId);
    }

    @Override
    public SprintBug findBySprintIdAndBugId(Integer sprintId, Integer bugId) {
        return sprintBugRepository.findBySprintIdAndBugId(sprintId, bugId);
    }

}

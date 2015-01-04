/*
 * $Id: ClassificationServiceImpl.java 15 2014-12-04 07:03:57Z jierong $
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

import com.accentrix.bugcrum.domain.bugzilla.Classification;
import com.accentrix.bugcrum.repository.ClassificationRepository;
import com.accentrix.bugcrum.service.ClassificationService;

@Service
@Transactional
public class ClassificationServiceImpl implements ClassificationService {

    @Autowired
    private ClassificationRepository classificationRepository;

    @Override
    public Classification save(Classification entity) {
        return classificationRepository.save(entity);
    }

    @Override
    public List<Classification> save(List<Classification> entities) {
        return classificationRepository.save(entities);
    }

    @Override
    public Classification findOne(Integer id) {
        return classificationRepository.findOne(id);
    }

    @Override
    public boolean exists(Integer id) {
        return classificationRepository.exists(id);
    }

    @Override
    public List<Classification> findAll() {
        return classificationRepository.findAllOrderBySortkey();
    }

    @Override
    public List<Classification> findAll(List<Integer> ids) {
        return classificationRepository.findAll(ids);
    }

    @Override
    public long count() {
        return classificationRepository.count();
    }

    @Override
    public void delete(Integer id) {
        classificationRepository.delete(id);
    }

    @Override
    public void delete(Classification entity) {
        classificationRepository.delete(entity);
    }

    @Override
    public void delete(List<Classification> entities) {
        classificationRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        classificationRepository.deleteAll();
    }

}

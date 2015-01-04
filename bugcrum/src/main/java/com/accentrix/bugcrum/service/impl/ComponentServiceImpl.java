/*
 * $Id: ComponentServiceImpl.java 7 2014-12-03 07:12:49Z jierong $
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

import com.accentrix.bugcrum.domain.bugzilla.Component;
import com.accentrix.bugcrum.domain.bugzilla.Product;
import com.accentrix.bugcrum.repository.ComponentRepository;
import com.accentrix.bugcrum.service.ComponentService;

@Service
@Transactional
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private ComponentRepository componentRepository;

    @Override
    public Component save(Component entity) {
        return componentRepository.save(entity);
    }

    @Override
    public List<Component> save(List<Component> entities) {
        return componentRepository.save(entities);
    }

    @Override
    public Component findOne(Integer id) {
        return componentRepository.findOne(id);
    }

    @Override
    public boolean exists(Integer id) {
        return componentRepository.exists(id);
    }

    @Override
    public List<Component> findAll() {
        return componentRepository.findAll();
    }

    @Override
    public List<Component> findAll(List<Integer> ids) {
        return componentRepository.findAll(ids);
    }

    @Override
    public long count() {
        return componentRepository.count();
    }

    @Override
    public void delete(Integer id) {
        componentRepository.delete(id);
    }

    @Override
    public void delete(Component entity) {
        componentRepository.delete(entity);
    }

    @Override
    public void delete(List<Component> entities) {
        componentRepository.delete(entities);
    }

    @Override
    public void deleteAll() {
        componentRepository.deleteAll();
    }

    @Override
    public List<Component> findAllByProduct(Product product) {
        return componentRepository.findAllByProduct(product);
    }

    @Override
    public Component findByProductAndName(Product product, String name) {
        return componentRepository.findByProductAndName(product, name);
    }

}

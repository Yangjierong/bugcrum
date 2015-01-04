/*
 * $Id: BugServiceTest.java 88 2014-12-22 03:39:34Z jierong $
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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.accentrix.bugcrum.SpringTxTestCase;
import com.accentrix.bugcrum.domain.bugzilla.Bug;
import com.accentrix.bugcrum.domain.bugzilla.Component;
import com.accentrix.bugcrum.domain.bugzilla.Product;

public class BugServiceTest extends SpringTxTestCase {
    private static final Logger log = LoggerFactory.getLogger(BugServiceTest.class);

    @Autowired
    private BugService bugService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private ProductService productService;

    @Test
    public void findAllByComponent() {

        Product product = productService.findByName("NTT Professional Service");

        if (product == null) {
            log.info("product not found");
            return;
        }

        Component Component = componentService.findByProductAndName(product, "Asset Management");

        if (Component == null) {
            log.info("Component not found");
            return;
        }

        List<Bug> list = bugService.findAllByComponentAndStatus(Component, "UNCONFIRMED");
        for (Bug bug : list) {
            log.info("{}: {}", bug.getStatus(), bug.getShortDesc());
        }
    }

    @Test
    public void findAllBySearchCriteria() {

        List<Bug> list = bugService.findAllBySearchCriteria(60, null, null, null, 1, 5, "priority",
                "DESCENDING");
        for (Bug bug : list) {
            log.info("{}: {}", bug.getStatus(), bug.getShortDesc());
        }
    }

    @Test
    public void sum() {

        List<Integer> selectedComponentIds = Arrays.asList(590);
        List<String> selectedBugStatus = Arrays.asList("UNCONFIRMED");

        Double time = bugService
                .sumEstimatedTime(60, selectedComponentIds, selectedBugStatus, null);
        log.info("time: {}", time);
    }
}

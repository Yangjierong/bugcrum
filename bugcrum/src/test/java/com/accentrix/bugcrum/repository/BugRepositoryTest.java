/*
 * $Id: BugRepositoryTest.java 52 2014-12-09 07:55:29Z jierong $
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.accentrix.bugcrum.SpringTxTestCase;
import com.accentrix.bugcrum.dto.BugActivityDto;

public class BugRepositoryTest extends SpringTxTestCase {
    private static final Logger log = LoggerFactory.getLogger(BugRepositoryTest.class);

    @Autowired
    private BugRepository bugRepository;

    @Test
    public void findBugsWorkdHoursByFilterTest() {

        List<BugActivityDto> list = bugRepository.findBugsWorkdHoursByFilter(new Date(),
                new Date(), Arrays.asList(6184));

        log.info("size: {}", list.size());

        for (BugActivityDto bugActivityDto : list) {
            log.debug("FieldId: {}", bugActivityDto.getFieldId());
            log.debug("BugId: {}", bugActivityDto.getBugId());
            log.debug("When: {}", bugActivityDto.getWhen());
            log.debug("value: {}", bugActivityDto.getAddedValue());
        }

    }
}
